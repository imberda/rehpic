package com.londonmet.ccp112n.obfuscators.padding;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public interface PaddingService {
	
	public byte[] pad(final byte[] inputBytes);
	
	public byte[] unpad(final byte[] inputBytes);
	
	public int getNumberOfBlocks(final byte[] inputBytes);

}
