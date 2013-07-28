package com.londonmet.ccp112n.obfuscators.round;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public interface RoundFunction {

	public byte[] encrypt(byte[] blockData, final byte[][] subKeys);
	public byte[] decrypt(byte[] blockData, final byte[][] subKeys);
}
