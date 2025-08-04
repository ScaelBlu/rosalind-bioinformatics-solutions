package io.github.ScaelBlu;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;

public class BioUtils {

    //Exercise 1: Counting DNA Nucleotides
    /**
     * Counts each nucleotide in the given DNA.
     * @param dna a DNA string.
     * @return a string with the count of A, C, G, and T respectively.
     */
    public static String countDnaNucleotides(String dna) {
        final String correctDna = dna.toUpperCase().strip();
        int aCount = 0;
        int cCount = 0;
        int gCount = 0;
        int tCount = 0;
        for(char nucleotide : correctDna.toCharArray()) {
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
        final String modDna = dna.toUpperCase().trim();
        final StringBuilder sb = new StringBuilder();
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
        String correctDna = dna.toUpperCase().strip();
        final char[] nucleotides = correctDna.toCharArray();
        final StringBuilder complementer = new StringBuilder();
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
            final long previouslyMatures = matures;
            matures = initialPopulation;
            initialPopulation += previouslyMatures * reproductionRate;
        }
        return initialPopulation;
    }

    //Exercise 5: Computing GC Content
    /**
     * Finds the highest GC content among the sequences of the given FASTA file.
     * @param fastaFile the path of the FASTA file
     * @return the highest GC percentage with its label
     */
    public static String findHighestGcContent(Path fastaFile) {
        String line;
        String currentLabel = null;
        StringBuilder currentSequence = new StringBuilder();
        GcContent highest = new GcContent(null, BigDecimal.ZERO);
        try (BufferedReader reader = Files.newBufferedReader(fastaFile)) {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(">")) {
                    if (currentLabel != null) {
                        highest = compareGcContent(currentLabel, currentSequence, highest);
                    }
                    currentLabel = line.substring(1);
                    currentSequence = new StringBuilder(line);
                } else {
                    currentSequence.append(line.toUpperCase().strip());
                }
            }
            highest = compareGcContent(currentLabel, currentSequence, highest);
        } catch (IOException ioe) {
            throw new IllegalStateException("Can not read file: ".concat(fastaFile.toString()), ioe);
        }
        return String.format("%s%n%s", highest.label(), highest.percentage());
    }

    /**
     * A container record for the sequence label and its GC percentage.
     * @param label the label of the sequence.
     * @param percentage the percentage of the sequence.
     */
    private record GcContent(String label, BigDecimal percentage) {}

    /**
     * Calculates the GC content of the current sequence and compares it to the highest.
     * @param currentLabel the label of the actual sequence.
     * @param currentSequence the sequence being processed.
     * @param highest the record holding the highest GC content found so far.
     * @return the record with higher GC content.
     */
    private static GcContent compareGcContent(String currentLabel, StringBuilder currentSequence, GcContent highest) {
        final BigDecimal nucleotideCount = new BigDecimal(currentSequence.length());
        final long gcCount = currentSequence.chars()
                .filter(nucleotide -> nucleotide == 'G' || nucleotide == 'C')
                .count();
        final BigDecimal actualGcContent = new BigDecimal(gcCount)
                .multiply(new BigDecimal(100))
                .divide(nucleotideCount, 6, RoundingMode.HALF_UP);
        if (actualGcContent.compareTo(highest.percentage()) > 0) {
            return new GcContent(currentLabel, actualGcContent);
        }
        return highest;
    }

}
