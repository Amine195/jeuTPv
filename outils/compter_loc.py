"""Calcule une estimation des lignes de code par dossiers pour le projet Tetris.

Le script implémente un comptage minimal inspiré de cloc :
- seules les lignes non vides sont comptées ;
- les commentaires sur une ligne (`//`) et les blocs `/* ... */` sont ignorés.

Ce script est utilisé lorsque l'installation de cloc n'est pas possible dans l'environnement
(dépôts APT et pip inaccessibles).
"""
from __future__ import annotations

from dataclasses import dataclass, field
import re
from pathlib import Path
from statistics import mean
from typing import Dict, Iterable, List, Optional, Sequence, Set, Tuple


@dataclass(frozen=True)
class EntréeLOC:
    chemin: Path
    loc: int


@dataclass(frozen=True)
class ClasseStat:
    fichier: Path
    nom: str
    loc: int
    abstraite: bool
    est_interface: bool
    ligne_début: int
    ligne_fin: int


@dataclass(frozen=True)
class MéthodeStat:
    fichier: Path
    classe: str
    nom: str
    loc: int
    ligne_début: int
    ligne_fin: int


def compter_loc_fichier(fichier: Path) -> int:
    loc = 0
    in_block_comment = False
    with fichier.open("r", encoding="utf-8", errors="ignore") as handle:
        for ligne in handle:
            texte = ligne.strip()
            if in_block_comment:
                if "*/" in texte:
                    in_block_comment = False
                    texte = texte.split("*/", 1)[1].strip()
                    if not texte:
                        continue
                else:
                    continue
            if not texte or texte.startswith("//"):
                continue
            if "/*" in texte:
                avant, _, après = texte.partition("/*")
                if avant.strip():
                    loc += 1
                if "*/" in après:
                    après = après.split("*/", 1)[1]
                    if après.strip():
                        loc += 1
                else:
                    in_block_comment = True
                continue
            loc += 1
    return loc


def lignes_sans_commentaires(fichier: Path) -> List[str]:
    lignes: List[str] = []
    in_block_comment = False
    with fichier.open("r", encoding="utf-8", errors="ignore") as handle:
        for brut in handle:
            ligne = ""
            idx = 0
            longueur = len(brut)
            while idx < longueur:
                if in_block_comment:
                    fin = brut.find("*/", idx)
                    if fin == -1:
                        idx = longueur
                        break
                    idx = fin + 2
                    in_block_comment = False
                    continue
                début_bloc = brut.find("/*", idx)
                double_slash = brut.find("//", idx)
                if double_slash != -1 and (début_bloc == -1 or double_slash < début_bloc):
                    ligne += brut[idx:double_slash]
                    idx = longueur
                    break
                if début_bloc != -1:
                    ligne += brut[idx:début_bloc]
                    idx = début_bloc + 2
                    in_block_comment = True
                    continue
                ligne += brut[idx:]
                idx = longueur
            lignes.append(ligne.rstrip())
    return lignes


def extraire_classes(lignes: Sequence[str], fichier: Path) -> List[ClasseStat]:
    classes: List[ClasseStat] = []
    i = 0
    longueur = len(lignes)
    while i < longueur:
        ligne = lignes[i]
        if "class" not in ligne and "interface" not in ligne and "enum" not in ligne:
            i += 1
            continue
        signature = ligne
        début = i
        while "{" not in signature and i + 1 < longueur:
            i += 1
            signature += " " + lignes[i]
        if "{" not in signature or not re.search(r"\b(class|interface|enum)\b", signature):
            i += 1
            continue
        if re.search(r"\bnew\s+\w+\s*\(", signature):
            i += 1
            continue
        correspondance_nom = re.search(
            r"\b(class|interface|enum)\s+([A-Za-z_][A-Za-z0-9_]*)",
            signature,
        )
        if not correspondance_nom:
            i += 1
            continue
        nom = correspondance_nom.group(2)
        mot_clef = correspondance_nom.group(1)
        est_interface = mot_clef == "interface"
        abstraite = est_interface or bool(re.search(r"\babstract\s+class\b", signature))
        compte_accolades = signature.count("{") - signature.count("}")
        fin = i
        while compte_accolades > 0 and fin + 1 < longueur:
            fin += 1
            compte_accolades += lignes[fin].count("{") - lignes[fin].count("}")
        contenu = lignes[début : fin + 1]
        loc = sum(1 for contenu_ligne in contenu if contenu_ligne.strip())
        classes.append(
            ClasseStat(
                fichier=fichier,
                nom=nom,
                loc=loc,
                abstraite=abstraite,
                est_interface=est_interface,
                ligne_début=début + 1,
                ligne_fin=fin + 1,
            )
        )
        i = fin + 1
    return classes


def est_signature_méthode(signature: str) -> bool:
    signature_sans_annotations = re.sub(r"@\w+", "", signature)
    avant_parenthèse = signature_sans_annotations.split("(", 1)[0]
    mots_interdits = {
        "if",
        "for",
        "while",
        "switch",
        "catch",
        "do",
        "new",
        "return",
        "class",
        "synchronized",
    }
    tokens = re.split(r"\s+", avant_parenthèse.strip())
    if not tokens:
        return False
    if any(token in mots_interdits for token in tokens[-2:]):
        return False
    if "=" in avant_parenthèse:
        return False
    if " class " in signature_sans_annotations:
        return False
    if not re.search(r"\(", signature_sans_annotations):
        return False
    return True


def trouver_classe_pour_ligne(classes: Sequence[ClasseStat], ligne: int) -> Optional[ClasseStat]:
    candidates = [cls for cls in classes if cls.ligne_début <= ligne <= cls.ligne_fin]
    if not candidates:
        return None
    return min(candidates, key=lambda cls: cls.ligne_fin - cls.ligne_début)


def extraire_méthodes(
    lignes: Sequence[str], classes: Sequence[ClasseStat], fichier: Path
) -> List[MéthodeStat]:
    méthodes: List[MéthodeStat] = []
    i = 0
    longueur = len(lignes)
    while i < longueur:
        ligne = lignes[i]
        stripped = ligne.strip()
        if not stripped:
            i += 1
            continue
        signature_lignes: List[str] = []
        début = i
        while stripped.startswith("@"):
            signature_lignes.append(lignes[i])
            i += 1
            if i >= longueur:
                break
            stripped = lignes[i].strip()
        if i >= longueur:
            break
        signature_lignes.append(lignes[i])
        while "{" not in signature_lignes[-1] and i + 1 < longueur:
            i += 1
            signature_lignes.append(lignes[i])
        signature = " ".join(el.strip() for el in signature_lignes)
        if "{" not in signature:
            i += 1
            continue
        if not est_signature_méthode(signature):
            i = début + 1
            continue
        compte_accolades = sum(l.count("{") - l.count("}") for l in signature_lignes)
        fin = i
        while compte_accolades > 0 and fin + 1 < longueur:
            fin += 1
            compte_accolades += lignes[fin].count("{") - lignes[fin].count("}")
        loc = sum(1 for contenu_ligne in lignes[début : fin + 1] if contenu_ligne.strip())
        partie_avant_parenthèse = signature.split("(", 1)[0].strip()
        nom = partie_avant_parenthèse.split()[-1]
        classe = trouver_classe_pour_ligne(classes, début + 1)
        if classe is not None:
            méthodes.append(
                MéthodeStat(
                    fichier=fichier,
                    classe=classe.nom,
                    nom=nom,
                    loc=loc,
                    ligne_début=début + 1,
                    ligne_fin=fin + 1,
                )
            )
        i = fin + 1
    return méthodes


@dataclass(frozen=True)
class RésultatAnalyse:
    total_loc: int
    détails: List[EntréeLOC]
    classes: List[ClasseStat]
    méthodes: List[MéthodeStat]
    packages: List["PackageBrut"]


@dataclass
class PackageBrut:
    nom: str
    classes: List[ClasseStat] = field(default_factory=list)
    dépendances: Set[str] = field(default_factory=set)


@dataclass(frozen=True)
class PackageMetrics:
    nom: str
    cc: int
    ac: int
    ca: int
    ce: int
    abstractness: float
    instability: float
    distance: float
    volatility: int
    cyclic: bool


def extraire_nom_package(lignes: Sequence[str]) -> str:
    for ligne in lignes:
        stripped = ligne.strip()
        if stripped.startswith("package "):
            contenu = stripped[len("package ") :].strip()
            return contenu.rstrip(";")
        if stripped and not stripped.startswith("import"):
            break
    return "(default)"


def extraire_imports(lignes: Sequence[str]) -> Set[str]:
    imports: Set[str] = set()
    for ligne in lignes:
        stripped = ligne.strip()
        if not stripped.startswith("import "):
            continue
        contenu = stripped[len("import ") :].strip()
        if contenu.startswith("static "):
            contenu = contenu[len("static ") :].strip()
        if not contenu:
            continue
        contenu = contenu.rstrip(";")
        if contenu.endswith(".*"):
            paquet = contenu[: -2]
        else:
            if "." not in contenu:
                continue
            paquet = contenu.rsplit(".", 1)[0]
        if paquet:
            imports.add(paquet)
    return imports


def analyser_racines(racines: Sequence[Path]) -> RésultatAnalyse:
    total = 0
    détails: List[EntréeLOC] = []
    classes: List[ClasseStat] = []
    méthodes: List[MéthodeStat] = []
    packages_temp: Dict[str, PackageBrut] = {}
    fichiers = sorted(iterer_fichiers(racines))
    for fichier in fichiers:
        loc = compter_loc_fichier(fichier)
        total += loc
        détails.append(EntréeLOC(fichier, loc))
        lignes = lignes_sans_commentaires(fichier)
        classes_fichier = extraire_classes(lignes, fichier)
        classes.extend(classes_fichier)
        méthodes.extend(extraire_méthodes(lignes, classes_fichier, fichier))
        nom_package = extraire_nom_package(lignes)
        info = packages_temp.setdefault(nom_package, PackageBrut(nom_package))
        info.classes.extend(classes_fichier)
        info.dépendances.update(extraire_imports(lignes))
    packages = list(packages_temp.values())
    return RésultatAnalyse(
        total_loc=total,
        détails=détails,
        classes=classes,
        méthodes=méthodes,
        packages=packages,
    )


def iterer_fichiers(racines: Iterable[Path]) -> Iterable[Path]:
    for racine in racines:
        if racine.is_file() and racine.suffix == ".java":
            yield racine
        elif racine.is_dir():
            yield from racine.rglob("*.java")


def afficher_details_loc(titre: str, analyse: RésultatAnalyse, racine_projet: Path) -> None:
    print(titre)
    print("=" * len(titre))
    print(f"Total LOC : {analyse.total_loc}")
    for entrée in analyse.détails:
        chemin_rel = entrée.chemin.relative_to(racine_projet)
        print(f"  - {chemin_rel}: {entrée.loc}")
    print()


def afficher_metriques_structurales(titre: str, analyse: RésultatAnalyse) -> None:
    print(titre)
    print("-" * len(titre))
    nb_classes = len(analyse.classes)
    nb_méthodes = len(analyse.méthodes)
    if nb_classes:
        moyenne_classe = mean(cls.loc for cls in analyse.classes)
        nb_abstraites = sum(1 for cls in analyse.classes if cls.abstraite)
        print(f"Nombre de classes : {nb_classes}")
        print(f"  - Moyenne LOC par classe : {moyenne_classe:.1f}")
        print(f"  - Classes abstraites : {nb_abstraites} ({nb_abstraites / nb_classes:.1%})")
        print(f"  - Classes concrètes : {nb_classes - nb_abstraites}")
    else:
        print("Aucune classe détectée")
    if nb_méthodes:
        moyenne_méthode = mean(méthode.loc for méthode in analyse.méthodes)
        print(f"Nombre de méthodes : {nb_méthodes}")
        print(f"  - Moyenne LOC par méthode : {moyenne_méthode:.1f}")
    else:
        print("Aucune méthode détectée")
    plus_grande_classe = max(analyse.classes, key=lambda cls: cls.loc, default=None)
    plus_grande_méthode = max(analyse.méthodes, key=lambda méthode: méthode.loc, default=None)
    if plus_grande_classe is not None:
        print(
            "Classe la plus volumineuse : "
            f"{plus_grande_classe.nom} ({plus_grande_classe.loc} LOC)"
        )
    if plus_grande_méthode is not None:
        print(
            "Méthode la plus volumineuse : "
            f"{plus_grande_méthode.classe}.{plus_grande_méthode.nom} "
            f"({plus_grande_méthode.loc} LOC)"
        )
    print()


def _filtrer_dépendances(packages: Sequence[PackageBrut]) -> Dict[str, Set[str]]:
    noms = {pkg.nom for pkg in packages}
    dépendances_filtrées: Dict[str, Set[str]] = {}
    for pkg in packages:
        dépendances_filtrées[pkg.nom] = {
            dep for dep in pkg.dépendances if dep in noms and dep != pkg.nom
        }
    return dépendances_filtrées


def _détecter_paquets_cycliques(dépendances: Dict[str, Set[str]]) -> Set[str]:
    index = 0
    indices: Dict[str, int] = {}
    lowlink: Dict[str, int] = {}
    pile: List[str] = []
    sur_pile: Set[str] = set()
    cycliques: Set[str] = set()

    def strongconnect(nom: str) -> None:
        nonlocal index
        indices[nom] = index
        lowlink[nom] = index
        index += 1
        pile.append(nom)
        sur_pile.add(nom)

        for successeur in dépendances.get(nom, set()):
            if successeur not in indices:
                strongconnect(successeur)
                lowlink[nom] = min(lowlink[nom], lowlink[successeur])
            elif successeur in sur_pile:
                lowlink[nom] = min(lowlink[nom], indices[successeur])

        if lowlink[nom] == indices[nom]:
            composante: List[str] = []
            while True:
                sommet = pile.pop()
                sur_pile.remove(sommet)
                composante.append(sommet)
                if sommet == nom:
                    break
            if len(composante) > 1:
                cycliques.update(composante)
            else:
                successeurs = dépendances.get(nom, set())
                if nom in successeurs:
                    cycliques.add(nom)

    for nom in dépendances:
        if nom not in indices:
            strongconnect(nom)

    return cycliques


def calculer_metriques_packages(packages: Sequence[PackageBrut]) -> List[PackageMetrics]:
    dépendances = _filtrer_dépendances(packages)
    ca_temp: Dict[str, Set[str]] = {pkg.nom: set() for pkg in packages}
    for source, deps in dépendances.items():
        for destination in deps:
            ca_temp[destination].add(source)
    cycliques = _détecter_paquets_cycliques(dépendances)
    métriques: List[PackageMetrics] = []
    for pkg in packages:
        cc = sum(1 for cls in pkg.classes if not cls.abstraite)
        ac = sum(1 for cls in pkg.classes if cls.abstraite)
        total_classes = cc + ac
        abstractness = ac / total_classes if total_classes else 0.0
        ce = len(dépendances.get(pkg.nom, set()))
        ca = len(ca_temp.get(pkg.nom, set()))
        if ca + ce:
            instability = ce / (ca + ce)
        else:
            instability = 0.0
        distance = abs(abstractness + instability - 1)
        métriques.append(
            PackageMetrics(
                nom=pkg.nom,
                cc=cc,
                ac=ac,
                ca=ca,
                ce=ce,
                abstractness=abstractness,
                instability=instability,
                distance=distance,
                volatility=1,
                cyclic=pkg.nom in cycliques,
            )
        )
    return sorted(métriques, key=lambda metric: metric.nom)


def afficher_metriques_packages(titre: str, métriques: Sequence[PackageMetrics]) -> None:
    if not métriques:
        print(f"{titre}\n{'-' * len(titre)}")
        print("Aucun paquet détecté\n")
        return
    en_tête = (
        "{:<45} {:>3} {:>3} {:>3} {:>3} {:>6} {:>6} {:>6} {:>3} {:>6}".format(
            "Paquet", "CC", "AC", "Ca", "Ce", "A", "I", "D", "V", "Cyclic"
        )
    )
    print(titre)
    print("-" * len(titre))
    print(en_tête)
    print("-" * len(en_tête))
    for métrique in métriques:
        print(
            "{:<45} {:>3} {:>3} {:>3} {:>3} {:>6.2f} {:>6.2f} {:>6.2f} {:>3} {:>6}".format(
                métrique.nom,
                métrique.cc,
                métrique.ac,
                métrique.ca,
                métrique.ce,
                métrique.abstractness,
                métrique.instability,
                métrique.distance,
                métrique.volatility,
                "Oui" if métrique.cyclic else "Non",
            )
        )
    print()


if __name__ == "__main__":
    racine_projet = Path(__file__).resolve().parents[1]
    génériques = [
        racine_projet / "JeuGenerique",
        racine_projet / "MoteurGenerique",
        racine_projet / "InterfaceGenerique",
        racine_projet / "PersistScore",
        racine_projet / "Utils",
    ]
    spécifiques = [
        racine_projet / "MoteursSpecifiques" / "JeuTetris",
        racine_projet / "InterfacesSpecifiques" / "IUTetris",
        racine_projet / "Tetris.java",
    ]

    analyse_générique = analyser_racines(génériques)
    analyse_spécifique = analyser_racines(spécifiques)

    afficher_details_loc("Partie générique", analyse_générique, racine_projet)
    afficher_details_loc("Partie spécifique", analyse_spécifique, racine_projet)

    afficher_metriques_structurales(
        "Métriques structurelles (générique)", analyse_générique
    )
    afficher_metriques_structurales(
        "Métriques structurelles (spécifique)", analyse_spécifique
    )

    analyse_combinée = analyser_racines(génériques + spécifiques)
    afficher_metriques_packages(
        "Métriques de type JDepend (ensemble du projet)",
        calculer_metriques_packages(analyse_combinée.packages),
    )

