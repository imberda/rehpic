package com.londonmet.ccp112n.obfuscators.cipherblockmode;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public interface CipherBlockMode {
	
	byte[] encrypt(byte[] input, byte[][] subKeys);
	byte[] decrypt(byte[] input, byte[][] subKeys);

}
