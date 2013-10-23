/*
 * Copyright (c) 2013 Infinity Apps Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uk.co.infinityapps.rehpic.permutation;

import uk.co.infinityapps.rehpic.utils.BitUtils;

/**
 * 
 * 
 * @author imberda
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
