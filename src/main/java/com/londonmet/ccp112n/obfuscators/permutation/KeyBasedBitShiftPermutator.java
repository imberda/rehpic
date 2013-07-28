package com.londonmet.ccp112n.obfuscators.permutation;

import com.londonmet.ccp112n.obfuscators.utils.BitUtils;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public class KeyBasedBitShiftPermutator implements KeyBasedPermutator {

	/**
	 * Simple permutation function which performs a circular bit shift to the 
	 * left on a dataBlock.
	 * 
	 * The number of bits to shift by is determined by the number of "1" bits in
	 * the final byte of the key. 
	 * 
	 * @param dataBlock a block of data as a byte array
	 * @param key a byte array representing a key 
	 */
	@Override
	public byte[] shiftLeft(final byte[] dataBlock, final byte[] key) {
		final byte lastKeyByte = key[key.length - 1];
		final int shiftValue = getShiftValue(lastKeyByte);
		return BitUtils.rotateLeft(dataBlock, shiftValue);
	}
	
	/**
	 * Simple permutation function which performs a circular bit shift to the 
	 * right on a dataBlock.
	 * 
	 * The number of bits to shift by is determined by the number of "1" bits in
	 * the final byte of the key. 
	 * 
	 * @param dataBlock a block of data as a byte array
	 * @param key a byte array representing a key 
	 */
	public byte[] shiftRight(final byte[] dataBlock, final byte[] key) {
		final byte[] reversedDataBlock = BitUtils.reverseBits(dataBlock);
		return BitUtils.reverseBits(shiftLeft(reversedDataBlock, key));
	}
	
	/**
	 * 
	 * Method takes the last byte of the password entered by the user. It uses it to determine 
	 * the number of bits to shift the password by (to help create confusion).
	 * 
	 *  It achieves this by taking the sum of the bits within the byte set to 1.
	 *  
	 *  For example byte 01001010 has 3 bits set to one so the value returned would be 3.
	 * 
	 * @return int the number of bits by which to shift the password 
	 */
	public static int getShiftValue(final byte finalByte){
		return Integer.bitCount(finalByte);
	}

}
