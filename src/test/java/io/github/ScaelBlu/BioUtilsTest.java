package io.github.ScaelBlu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(InputStringResolver.class)
class BioUtilsTest {

    //Exercise 1: Counting DNA Nucleotides
    @Test
    void testCountDnaNucleotides(@InputFile("/01-nucleotide-counting.txt") String dna) {
        assertEquals("198 220 196 212", BioUtils.countDnaNucleotides(dna));
    }

    //Exercise 2: Transcribing DNA into RNA
    @Test
    void testTranscribeDnaToRna(@InputFile("/02-transcribe-dna-to-rna.txt") String dna,
                                @InputFile("/02-expected.txt") String rna) {
        assertEquals(rna, BioUtils.transcribeDnaToRna(dna));
    }

    //Exercise 3: Complementing a Strand of DNA
    @Test
    void testReverseComplementingDna(@InputFile("/03-reverse-complementing-dna.txt") String dna,
                                @InputFile("/03-expected.txt") String complementer) {
        assertEquals(complementer, BioUtils.reverseComplementerOf(dna));
    }
}