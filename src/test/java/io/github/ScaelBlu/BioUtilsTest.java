package io.github.ScaelBlu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(InputFileResolver.class)
class BioUtilsTest {

    //Exercise 1: Counting DNA Nucleotides
    @Test
    void testCountDnaNucleotides(@InputFile("/01-nucleotide-counting.txt") BufferedReader dna) {
        assertEquals("198 220 196 212", BioUtils.countDnaNucleotides(dna));
    }

    //Exercise 2: Transcribing DNA into RNA
    @Test
    void testTranscribeDnaToRna(@InputFile("/02-transcribe-dna-to-rna.txt") BufferedReader dna,
                                @InputFile("/02-expected.txt") String rna) {
        assertEquals(rna, BioUtils.transcribeDnaToRna(dna));
    }

    //Exercise 3: Complementing a Strand of DNA
    @Test
    void testReverseComplementingDna(@InputFile("/03-reverse-complementing-dna.txt") BufferedReader dna,
                                @InputFile("/03-expected.txt") String complementer) {
        assertEquals(complementer, BioUtils.reverseComplementerOf(dna));
    }

    //Exercise 4: Rabbits and Recurrence Relations
    @ParameterizedTest(name = "Expects {3} population with {2} reproduction rate after {1} month(s)")
    @CsvFileSource(
            resources = "/04-recurrent-population-growth.txt",
            delimiterString = " "
    )
    void testCalculatingPopulationAfterMonths(int months, int reproductionRate, long expected) {
        assertEquals(expected, BioUtils.calculatePopulationAfterMonths(months, reproductionRate));
    }

    //Exercise 5: Computing GC Content
    @Test
    void testGCCountCalculation(@InputFile("/05-expected.txt") String expectedOutput,
                                @InputFile("/05-rosalind-sample1.fasta") BufferedReader input) throws IOException {
            assertEquals(expectedOutput, BioUtils.findHighestGcContent((input)));
    }

    //Exercise 6: Counting Point Mutations
    @Test
    void testPointMutationCounter(@InputFile("/06-sequences-to-compare.txt") BufferedReader strands) throws IOException {
        assertEquals(470, BioUtils.countPointMutations(strands));
    }

    //Exercise 7: Mendel's First Law
    @ParameterizedTest
    @CsvSource({
            "2, 2, 2, 0.78333",
            "19, 21, 26, 0.69674"
    })
    void testCalculateDominantPhenotypeProbability(int AA, int Aa, int aa, BigDecimal expected) {
        assertEquals(expected, BioUtils.calculateDominantPhenotypeProbability(AA, Aa, aa, 5));
    }

}