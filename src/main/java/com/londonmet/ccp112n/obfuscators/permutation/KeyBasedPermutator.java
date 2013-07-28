package com.londonmet.ccp112n.obfuscators.permutation;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public interface KeyBasedPermutator {

	byte[] shiftLeft(final byte[] dataBlock, final byte[] key);
	byte[] shiftRight(final byte[] dataBlock, final byte[] key);
}
