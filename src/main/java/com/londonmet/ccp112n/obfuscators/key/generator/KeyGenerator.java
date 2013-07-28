package com.londonmet.ccp112n.obfuscators.key.generator;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public interface KeyGenerator {
	
	public byte[] generateKey(final byte[] keyIdentifier, final byte[] password);

}
