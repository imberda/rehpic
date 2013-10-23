package com.londonmet.ccp112n.obfuscators.round;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface RoundFunction {

	public byte[] encrypt(byte[] blockData, final byte[][] subKeys);
	public byte[] decrypt(byte[] blockData, final byte[][] subKeys);
}
