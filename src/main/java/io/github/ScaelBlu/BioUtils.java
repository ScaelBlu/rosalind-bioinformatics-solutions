package io.github.ScaelBlu;

public class BioUtils {

    //Exercise 1: Counting DNA Nucleotides
    /**
     * Counts each nucleotide in the given DNA.
     * @param dna a DNA string.
     * @return a string with the count of A, C, G, and T respectively.
     */
    public static String countDnaNucleotides(String dna) {
        dna = dna.toUpperCase().strip();
        int aCount = 0;
        int cCount = 0;
        int gCount = 0;
        int tCount = 0;
        for(char nucleotide : dna.toCharArray()) {
            switch (nucleotide) {
                case 'A' -> aCount++;
                case 'C' -> cCount++;
                case 'G' -> gCount++;
                case 'T' -> tCount++;
                default -> throw new IllegalArgumentException("There is no such nucleotide: ".concat(Character.toString(nucleotide)));
            }
        }
        return String.join(" ", Integer.toString(aCount), Integer.toString(cCount), Integer.toString(gCount), Integer.toString(tCount));
    }

    //Exercise 2: Transcribing DNA into RNA
    /**
     * Transcribes the given DNA into RNA (within one strand only!).
     * @param dna a DNA string
     * @return a transcribed RNA (changed T -> U)
     */
    public static String transcribeDnaToRna(String dna) {
        String modDna = dna.toUpperCase().trim();
        StringBuilder sb = new StringBuilder();
        for (char nucleotide : modDna.toCharArray()) {
            if (nucleotide == 'T') {
                sb.append('U');
            } else {
                sb.append(nucleotide);
            }
        }
        return sb.toString();
    }

    //Exercise 3: Complementing a Strand of DNA
    /**
     * Generates the reverse complementer of the given DNA strand.
     * @param dna a DNA string
     * @return the reverse complementer DNA strand
     */
    public static String reverseComplementerOf(String dna) {
        dna = dna.toUpperCase().strip();
        char[] nucleotides = dna.toCharArray();
        StringBuilder complementer = new StringBuilder();
        for (int i = nucleotides.length - 1; i >= 0; i--) {
            switch (nucleotides[i]) {
                case 'A' -> complementer.append('T');
                case 'C' -> complementer.append('G');
                case 'G' -> complementer.append('C');
                case 'T' -> complementer.append('A');
                default ->
                        throw new IllegalArgumentException("There is no such nucleotide: ".concat(Character.toString(nucleotides[i])));
            }
        }
        return complementer.toString();
    }

    //Excercise 4: Rabbits and Recurrence Relations
    /**
     * Calculates the population in pairs. The individuals are immortal and each pair becomes sexually mature after one
     * month. Every mature pair produces male and female offspring according to the reproduction rate. The population starts
     * with one immature pair.
     * @param months the growing period in months.
     * @param reproductionRate the produced immature offspring pairs.
     * @return the size of the population after the given months.
     */
    public static long calculatePopulationAfterMonths(int months, int reproductionRate) {
        long initialPopulation = 1;
        long matures = 0;
        for(int i = 1; i < months; i++) {
            long previouslyMatures = matures;
            matures = initialPopulation;
            initialPopulation += previouslyMatures * reproductionRate;
        }
        return initialPopulation;
    }
}
