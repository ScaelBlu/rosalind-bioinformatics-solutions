package io.github.ScaelBlu;

import lombok.Getter;

import java.util.List;

@Getter
public enum AminoAcid {

    ALA("L-Alanine", "Ala", "A", List.of("GCU", "GCC", "GCA", "GCG")),
    ARG("L-Arginine", "Arg", "R", List.of("CGU", "CGC", "CGA", "CGG", "AGA", "AGG")),
    ASN("L-Asparagine", "Asn", "N", List.of("AAU", "AAC")),
    ASP("L-Aspartic acid", "Asp", "D", List.of("GAU", "GAC")),
    CYS("L-Cysteine", "Cys", "C", List.of("UGU", "UGC")),
    GLU("L-Glutamic acid", "Glu", "E", List.of("GAA", "GAG")),
    GLN("L-Glutamine", "Gln", "Q", List.of("CAA", "CAG")),
    GLY("Glycine", "Gly", "G", List.of("GGU", "GGC", "GGA", "GGG")),
    HIS("L-Histidine", "His", "H", List.of("CAU", "CAC")),
    ILE("L-Isoleucine", "Ile", "I", List.of("AUU", "AUC", "AUA" )),
    LEU("L-Leucine", "Leu", "L", List.of("UUA", "UUG", "CUU", "CUC", "CUA", "CUG")),
    LYS("L-Lysine", "Lys", "K", List.of("AAA", "AAG")),
    MET("L-Methionine", "Met", "M", List.of("AUG")),
    PHE("L-Phenylalanine", "Phe", "F", List.of("UUU", "UUC")),
    PRO("L-Proline", "Pro", "P", List.of("CCU", "CCC", "CCA", "CCG")),
    //PYL("L_Pyrrolysine", "Pyl", "O", List.of("UAG")), <-- procaryotes only!
    SEC("L-Selenocysteine", "Sec", "U", List.of("UGA")),
    SER("L-Serine", "Ser", "S", List.of("UCU", "UCC", "UCA", "UCG", "AGU", "AGC")),
    THR("L-Threonine", "Thr", "T", List.of("ACU", "ACC", "ACA", "ACG")),
    TRP("L-Tryptophan", "Trp", "W", List.of("UGG")),
    TYR("L-Tyrosine", "Tyr", "Y", List.of("UAU", "UAC")),
    VAL("L-Valine", "Val", "V", List.of("GUU", "GUC", "GUA", "GUG"));

    private final String name;

    private final String abbreviation;

    private final String letter;

    private final List<String> universalCodons;

    AminoAcid(String name, String abbreviation, String letter, List<String> universalCodons) {
        this.name = name;
        this.abbreviation=abbreviation;
        this.letter=letter;
        this.universalCodons = universalCodons;
    }

    public List<String> getUniversalCodons() {
        return List.copyOf(universalCodons);
    }
}
