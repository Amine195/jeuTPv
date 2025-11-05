"""Calcule une estimation des lignes de code par dossiers pour le projet Tetris.

Le script implémente un comptage minimal inspiré de cloc :
- seules les lignes non vides sont comptées ;
- les commentaires sur une ligne (`//`) et les blocs `/* ... */` sont ignorés.

Ce script est utilisé lorsque l'installation de cloc n'est pas possible dans l'environnement
(dépôts APT et pip inaccessibles).
"""
from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path
from typing import Iterable, List, Sequence, Tuple


@dataclass(frozen=True)
class EntréeLOC:
    chemin: Path
    loc: int


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


def iterer_fichiers(racines: Iterable[Path]) -> Iterable[Path]:
    for racine in racines:
        if racine.is_file() and racine.suffix == ".java":
            yield racine
        elif racine.is_dir():
            yield from racine.rglob("*.java")


def compter_loc(racines: Sequence[Path]) -> Tuple[int, List[EntréeLOC]]:
    total = 0
    détails: List[EntréeLOC] = []
    for fichier in sorted(iterer_fichiers(racines)):
        loc = compter_loc_fichier(fichier)
        total += loc
        détails.append(EntréeLOC(fichier, loc))
    return total, détails


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

    total_générique, détails_génériques = compter_loc(génériques)
    total_spécifique, détails_spécifiques = compter_loc(spécifiques)

    def afficher(titre: str, total: int, détails: Sequence[EntréeLOC]) -> None:
        print(titre)
        print("=" * len(titre))
        print(f"Total LOC : {total}")
        for entrée in détails:
            chemin_rel = entrée.chemin.relative_to(racine_projet)
            print(f"  - {chemin_rel}: {entrée.loc}")
        print()

    afficher("Partie générique", total_générique, détails_génériques)
    afficher("Partie spécifique", total_spécifique, détails_spécifiques)
