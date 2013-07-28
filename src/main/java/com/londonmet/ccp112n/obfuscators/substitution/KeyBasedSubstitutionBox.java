package com.londonmet.ccp112n.obfuscators.substitution;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public interface KeyBasedSubstitutionBox {
	
	public byte[] subtitute(final byte[] blockData, final byte[] subKey);

}
