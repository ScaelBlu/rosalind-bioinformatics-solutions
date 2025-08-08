package io.github.ScaelBlu;

import java.util.Optional;

/**
 * They are the carriers of the amino acids for the protein synthesis. Their anticodon loop can pair with the codons of
 * the mRNA, while the ribosome catalyzes the formation of the new peptide bond.
 */
public class TRna implements TranslationUnit {

    private final AminoAcid aminoAcid;
    private final String codon;

    public TRna(AminoAcid aminoAcid, String codon) {
        this.aminoAcid = aminoAcid;
        this.codon=codon;
    }

    @Override
    public Optional<AminoAcid> getAminoAcid() {
        return Optional.of(aminoAcid);
    }

    @Override
    public String getCodon() {
        return codon;
    }

    @Override
    public String getSymbol() {
        return aminoAcid.getLetter();
    }
}
