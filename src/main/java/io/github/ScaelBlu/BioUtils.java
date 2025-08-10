package io.github.ScaelBlu;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BioUtils {

    public static final Set<Character> RIBONUCLEOTIDES = Set.of('U', 'A', 'G', 'C');

    public static final Set<Character> D_RIBONUCLEOTIDES = Set.of('T', 'A', 'G', 'C');

    //Exercise 1: Counting DNA Nucleotides
    /**
     * Counts each nucleotide in the given DNA.
     * @param dna a DNA string reader.
     * @return a string with the count of A, C, G, and T respectively.
     */
    public static String countDnaNucleotides(BufferedReader dna) {
        final Map<Character, Long> nucleotides = dna.lines()
                .flatMap(line -> line.strip().toUpperCase().chars()
                        .filter(n -> {
                            if (!D_RIBONUCLEOTIDES.contains((char) n)) {
                                throw new IllegalArgumentException("Invalid nucleotide: ".concat(String.valueOf((char) n)));
                            }
                            return true;
                        })
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
     * Transcribes the given DNA sense strand into RNA (not a real transcription!).
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
     * @throws IOException if an I/O error occurs.
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

    //Exercise 6: Counting Point Mutations
    /**
     * Counts the Hamming distance (the number of point mutations) between two DNA strands.
     * @param strands reader of the two DNA strands to compare.
     * @return the number of point mutations.
     * @throws IOException if an I/O error occurs.
     */
    public static long countPointMutations(BufferedReader strands) throws IOException {
        long hammingDistance = 0;
        final char[] firstStrand = strands.readLine().strip().toUpperCase().toCharArray();
        final char[] secondStrand = strands.readLine().strip().toUpperCase().toCharArray();
        if (firstStrand.length != secondStrand.length) {
            throw new IllegalArgumentException("Lengths must be equal.");
        }
        for (int i = 0; i < firstStrand.length; i++) {
            if (!D_RIBONUCLEOTIDES.contains(firstStrand[i]) || !D_RIBONUCLEOTIDES.contains(secondStrand[i])) {
                throw new IllegalArgumentException("Invalid nucleotide at pos: %d".formatted(i));
            }
            if (firstStrand[i] != secondStrand[i]) {
                hammingDistance++;
            }
        }
        return hammingDistance;
    }

    //Exercise 7: Mendel's First Law
    /**
     * Calculates the probability of offspring having a dominant phenotype in a conceptual population where the numbers
     * of homozygous dominant, heterozygous, and homozygous recessive individuals are given. Every individual is able to
     * mate with any other. The phenotype is determined by a single gene and follows mendelian inheritance.
     * @param homoDominants the number of homozygous dominant individuals.
     * @param heterozygous the number of heterozygous individuals.
     * @param homoRecessives the number of homozygous recessive individuals.
     * @param scale the scale of the result (number of decimal places).
     * @return the probability of an offspring having a dominant phenotype.
     */
    public static BigDecimal calculateDominantPhenotypeProbability(long homoDominants, long heterozygous, long homoRecessives, int scale) {
        if (homoDominants < 0 || heterozygous < 0 || homoRecessives < 0 || scale < 0) {
            throw new IllegalArgumentException("Arguments must be non-negative integers!");
        }

        final long totalIndividuals = homoDominants + heterozygous + homoRecessives;
        if (totalIndividuals < 2) {
            return BigDecimal.ZERO;
        }

        final BigDecimal totalPairs =  new BigDecimal(MathUtils.combination(totalIndividuals, 2));

        final BigDecimal hetHetPairs = new BigDecimal(MathUtils.combination(heterozygous, 2));
        final BigDecimal recessivesFromHetHet = hetHetPairs.multiply(new BigDecimal("0.25"));

        final BigDecimal recHetPairs = BigDecimal.valueOf(homoRecessives).multiply(BigDecimal.valueOf(heterozygous));
        final BigDecimal recessivesFromRecHet = recHetPairs.multiply(new BigDecimal("0.5"));

        final BigDecimal recRecPairs = new BigDecimal(MathUtils.combination(homoRecessives, 2));

        final BigDecimal totalRecessiveOffsprings = recessivesFromHetHet
                .add(recessivesFromRecHet)
                .add(recRecPairs);
        final BigDecimal totalDominantOffsprings = totalPairs
                .subtract(totalRecessiveOffsprings);

        return totalDominantOffsprings.divide(totalPairs, scale, RoundingMode.HALF_UP);
    }

    //Exercise 8: Translating RNA into Protein
    /**
     * Translates an mRNA sequence into protein sequence.
     * @param mRna mRna string to translate.
     * @param frame the reading frame of translation. Determines the offset of the codon triplets.
     * @param codeType determines the rules of codon-amino acid assignment.
     * @param terminateAtStop translation stops at stop codons if true.
     * @return a translated protein string.
     */
    public static String mRnaTranslator(BufferedReader mRna, OpenReadingFrame frame,
                                        GeneticCodeType codeType, boolean terminateAtStop) {
        final Map<String, TranslationUnit> codonTable = getCodonTable(codeType);
        System.out.println(codonTable);
        final StringBuffer codonBuilder = new StringBuffer();
        return mRna.lines()
                .flatMapToInt(line -> line.strip().toUpperCase().chars())
                .filter(n -> {
                    if (!RIBONUCLEOTIDES.contains((char) n)) {
                        throw new IllegalArgumentException("Invalid nucleotide: %s.".formatted((char) n));
                    }
                    return true;
                })
                .skip(frame.getOffset())
                .mapToObj(n -> {
                    if (codonBuilder.length() < 3) {
                        codonBuilder.append((char) n);
                        return "";
                    }
                    final String codon = codonBuilder.toString();
                    codonBuilder.setLength(0);
                    codonBuilder.append((char) n);
                    return codon;
                })
                .filter(s -> !s.isEmpty())
                .map(codon -> codonTable.get(codon).getSymbol())
                .takeWhile(symbol -> !terminateAtStop || symbol.equals("*"))
                .collect(Collectors.joining());
    }

    /**
     * Generates the codon table for codon-amino acid translation.
     * @param codeType the type of the genetic code for the assignment rules.
     * @return a Map of the 64 possible codon.
     */
    public static Map<String, TranslationUnit> getCodonTable(GeneticCodeType codeType) {
        final Map<String, TranslationUnit> codonTable = new HashMap<>();
        for (char n1 : RIBONUCLEOTIDES) {
            for (char n2 : RIBONUCLEOTIDES) {
                for (char n3 : RIBONUCLEOTIDES) {
                    String codon = new String(new char[]{n1, n2, n3});
                    codonTable.put(codon, TranslationUnit.of(codon, codeType));
                }
            }
        }
        return codonTable;
    }

    //Exercise 9: Finding a Motif in DNA
    /**
     * Finds all occurrence of the given motif in the given DNA sequence.
     * @param dnaSequence a DNA sequence to examine.
     * @param motif a DNA motif to find.
     * @return a list with the number (not the index!) of the first character of the found motifs.
     */
    public static List<Integer> findAllMotifs(String dnaSequence, String motif) {
        final List<Integer> indices = new ArrayList<>();
        int index = -1;
        while (index != 0) {
            index = dnaSequence.indexOf(motif, index);
            indices.add(++index);
        }
        indices.removeLast();
        return indices;
    }

    /**
     * Finds all occurrence of a motif in a DNA sequence from file. The first line must contain the sequence.
     * @param input an input file with the sequence and the motif in separate lines.
     * @return a list with the number (not the index!) of the first character of the found motifs.
     * @throws IOException if an I/O error occurs.
     */
    public static List<Integer> findAllMotifs(BufferedReader input) throws IOException {
        final String sequence = input.readLine();
        final String motif = input.readLine();
        return findAllMotifs(sequence, motif);
    }

    //Exercise 10: Consensus and Profile
    /**
     * Generates the consensus sequence based on the same length sequences of a given FASTA file. It creates a position
     * matrix as well which appears in the output. The matrix contains the counts of a nucleotide in a position in the
     * sequences. If more consensus motifs are possible, any of them may be returned.
     * @param fasta the FASTA file with same length DNA sequences.
     * @return a possible consensus motif and the underlying position matrix.
     * @throws IOException if an I/O error occurs.
     */
    public static String createConsensusSequence(BufferedReader fasta) throws IOException {
        final Map<Character, int[]> posMatrix = new HashMap<>();
        String line;
        int expectedLength = -1;
        final StringBuilder sequence = new StringBuilder();
        while ((line = fasta.readLine()) != null) {
            if (line.startsWith(">")) {
                if (!sequence.isEmpty()) {
                    expectedLength = validateLength(expectedLength, sequence);
                    processSequence(sequence, posMatrix);
                }
                sequence.setLength(0);
            } else {
                sequence.append(line.strip());
            }
        }
        if (!sequence.isEmpty()) {
            expectedLength = validateLength(expectedLength, sequence);
            processSequence(sequence, posMatrix);
        }

        return processPositionMatrix(posMatrix, expectedLength);
    }

    /**
     * This method checks the length of a single FASTA sequence and compares it with the expected nucleotide count. The
     * FASTA file must contain same length sequences.
     * @param expectedLength the expected length of the sequences. It is equal to the length of the first sequence.
     * @param sequence the actual sequence to check.
     * @return the length of the first sequence to set it in the caller method.
     */
    private static int validateLength(int expectedLength, StringBuilder sequence) {
        if (expectedLength == -1) {
            return sequence.length();
        }
        if (sequence.length() != expectedLength) {
            throw new IllegalArgumentException("Sequence must be the same length.");
        }
        return expectedLength;
    }

    /**
     * Updates the position matrix by counting the nucleotides in each position.
     * @param sequence the actual DNA sequence to process.
     * @param posMatrix the position matrix with nucleotide keys to update.
     */
    private static void processSequence(StringBuilder sequence, Map<Character, int[]> posMatrix) {
        final char[] nucleotides = sequence.toString().toUpperCase().toCharArray();
        for (int i = 0; i < nucleotides.length; i++) {
            if (!D_RIBONUCLEOTIDES.contains(nucleotides[i])) {
                throw new IllegalArgumentException("Invalid nucleotide: %s".formatted(nucleotides[i]));
            }
            int[] positions = posMatrix.computeIfAbsent(nucleotides[i], _ -> new int[nucleotides.length]);
            positions[i]++;
        }
    }

    /**
     * Creates the correct output based on the position matrix.
     * @param matrix the matrix with the counts of nucleotides in each position.
     * @param rowLength the length of a row of the matrix (the count of positions).
     * @return a possible consensus sequence and the underlying position matrix.
     */
    private static String processPositionMatrix(Map<Character, int[]> matrix, int rowLength) {
        final StringBuilder result = new StringBuilder(createConsensusMotif(matrix, rowLength));
        matrix.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(nucPositions -> {
                    result.append(nucPositions.getKey()).append(":");
                    int[] positions = nucPositions.getValue();
                    for (int count : positions) {
                        result.append(" ").append(count);
                    }
                    result.append("\n");
                });
        return result.toString().strip();
    }

    /**
     * Generates a possible consensus sequence based on the highest nucleotide count in each position.
     * @param matrix the position matrix with the nucleotides ant their counts.
     * @param rowLength the length of a row of the matrix (the count of positions).
     * @return a possible consensus DNA string.
     */
    private static String createConsensusMotif(Map<Character,int[]> matrix, int rowLength) {
        final StringBuilder consensusMotif = new StringBuilder();
        for (int i=0; i<rowLength; i++) {
            Map.Entry<Character, int[]> highestOccurrence = null;
            for (Map.Entry<Character, int[]> e : matrix.entrySet()) {
                if (highestOccurrence == null || e.getValue()[i] > highestOccurrence.getValue()[i]) {
                    highestOccurrence = e;
                }
            }
            consensusMotif.append(highestOccurrence.getKey());
        }
        return consensusMotif.append("\n").toString();
    }
}
