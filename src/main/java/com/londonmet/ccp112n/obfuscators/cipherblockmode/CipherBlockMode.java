package com.londonmet.ccp112n.obfuscators.cipherblockmode;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface CipherBlockMode {
	
	byte[] encrypt(byte[] input, byte[][] subKeys);
	byte[] decrypt(byte[] input, byte[][] subKeys);

}
