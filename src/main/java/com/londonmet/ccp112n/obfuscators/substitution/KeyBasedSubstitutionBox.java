package com.londonmet.ccp112n.obfuscators.substitution;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface KeyBasedSubstitutionBox {
	
	public byte[] subtitute(final byte[] blockData, final byte[] subKey);

}
