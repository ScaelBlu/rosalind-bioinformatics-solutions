package io.github.ScaelBlu;

import java.util.Arrays;
import java.util.Optional;

import static io.github.ScaelBlu.BioUtils.RIBONUCLEOTIDES;

/**
 * Represents the mobile carrier building blocks of the translation like tRNA's and release factors.
 */
public interface TranslationUnit {

    static TranslationUnit of(String codon, GeneticCodeType codeType) {
        for (char nucleotide : codon.toCharArray()) {
            if (Arrays.binarySearch(RIBONUCLEOTIDES, nucleotide) < 0) {
                throw new IllegalArgumentException("Invalid codon: %s.".formatted(codon));
            }
        }
        final Optional<AminoAcid> aminoAcid =
                Arrays.stream(AminoAcid.values())
                        .filter(aa -> codeType.translate(codon, aa))
                        .findFirst();

        return aminoAcid.isPresent() ? new TRna(aminoAcid.get(), codon) : new ReleaseFactor(codon);
    }

    Optional<AminoAcid> getAminoAcid();

    String getCodon();

    String getSymbol();
}
