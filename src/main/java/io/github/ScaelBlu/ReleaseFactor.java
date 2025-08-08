package io.github.ScaelBlu;

import java.util.Optional;

/**
 * Different release factors bind to the stop codons of the mRNA. They do not carry any amino acid like the tRNA's but
 * terminates the translation and help to release the newly synthesised polypeptide.
 */
public class ReleaseFactor implements TranslationUnit {

    private final String codon;

    public ReleaseFactor(String codon) {
        this.codon = codon;
    }

    @Override
    public Optional<AminoAcid> getAminoAcid() {
        return Optional.empty();
    }

    @Override
    public String getCodon() {
        return codon;
    }

    @Override
    public String getSymbol() {
        return "*";
    }
}
