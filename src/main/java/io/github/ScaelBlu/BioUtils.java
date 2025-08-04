package io.github.ScaelBlu;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BioUtils {

    //Exercise 1: Counting DNA Nucleotides
    /**
     * Counts each nucleotide in the given DNA.
     * @param dna a DNA string reader.
     * @return a string with the count of A, C, G, and T respectively.
     */
    public static String countDnaNucleotides(BufferedReader dna) {
        final Map<Character, Long> nucleotides = dna.lines()
                .flatMap(line -> line.strip().toUpperCase().chars()
                        .peek(n -> {
                            if (n != 'A' && n != 'C' && n != 'G' && n != 'T') {
                                throw new IllegalArgumentException("Invalid nucleotide: ".concat(String.valueOf((char) n)));
                            }})
                        .mapToObj(i -> (char) i))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        return String.join(" ",
                nucleotides.get('A').toString(),
                nucleotides.get('C').toString(),
                nucleotides.get('G').toString(),
                nucleotides.get('T').toString());
    }

    //Exercise 2: Transcribing DNA into RNA
    /**
     * Transcribes the given DNA into RNA (within one strand only!).
     * @param dna a DNA string reader.
     * @return a transcribed RNA (changed T -> U).
     */
    public static String transcribeDnaToRna(BufferedReader dna) {
        return dna.lines()
                .map(line -> line.strip().toUpperCase().replace('T', 'U'))
                .collect(Collectors.joining());
    }

    //Exercise 3: Complementing a Strand of DNA
    /**
     * Generates the reverse complementer of the given DNA strand.
     * @param dna a DNA string reader.
     * @return the reverse complementer DNA strand.
     */
    public static String reverseComplementerOf(BufferedReader dna) {
        return dna.lines()
                .flatMap(line -> line.strip().toUpperCase().chars()
                        .mapToObj(c -> (char) c))
                .map(n ->
                            switch (n) {
                                case 'A' -> 'T';
                                case 'C' -> 'G';
                                case 'G' -> 'C';
                                case 'T' -> 'A';
                                default ->
                                        throw new IllegalArgumentException("Invalid nucleotide: ".concat(String.valueOf(n)));
                })
                .collect(Collector.of(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append,
                        StringBuilder::reverse
                ))
                .toString();
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
     * @param fasta the reader of the FASTA file
     * @return the highest GC percentage with its label
     */
    public static String findHighestGcContent(BufferedReader fasta) throws IOException {
        String line;
        String currentLabel = null;
        StringBuilder currentSequence = new StringBuilder();
        GcContent highest = new GcContent(null, BigDecimal.ZERO);
        while ((line = fasta.readLine()) != null) {
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
     * @param currentLabel the label of the current sequence.
     * @param currentSequence the sequence being processed.
     * @param highest the record holding the highest GC content found so far.
     * @return the record with higher GC content.
     */
    private static GcContent compareGcContent(String currentLabel, StringBuilder currentSequence, GcContent highest) {
        final BigDecimal nucleotideCount = new BigDecimal(currentSequence.length());
        final long gcCount = currentSequence.chars()
                .filter(nucleotide -> nucleotide == 'G' || nucleotide == 'C')
                .count();
        final BigDecimal currentGcContent = new BigDecimal(gcCount)
                .multiply(new BigDecimal(100))
                .divide(nucleotideCount, 6, RoundingMode.HALF_UP);
        if (currentGcContent.compareTo(highest.percentage()) > 0) {
            return new GcContent(currentLabel, currentGcContent);
        }
        return highest;
    }

}
