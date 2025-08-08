package io.github.ScaelBlu;

/**
 * The constants of this enum determines the pairing rules between the codons and amino acids in different genetic codes.
 */
@SuppressWarnings({"unused"})
public enum GeneticCodeType {

    UNIVERSAL {
        @Override
        public boolean translate(String codon, AminoAcid aa) {
            if (codon.equals("UGA") && aa.equals(AminoAcid.SEC)) return false;
            return aa.getUniversalCodons().contains(codon);
        }
    },
    MITOCHONDRIAL {
        @Override
        public boolean translate(String codon, AminoAcid aa) {
            return switch(codon) {
                case "AGA", "AGG" -> false;
                case "UGA" -> aa.equals(AminoAcid.TRP);
                case "AUA" -> aa.equals(AminoAcid.MET);
                default -> aa.getUniversalCodons().contains(codon);
            };
        }
    },
    SECIS {
        @Override
        public boolean translate(String codon, AminoAcid aa) {
            if (codon.equals("UGA") && aa.equals(AminoAcid.SEC)) return true;
            return aa.getUniversalCodons().contains(codon);
        }
    };

    public abstract boolean translate(String codon, AminoAcid aa);
}
