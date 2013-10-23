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
package uk.co.infinityapps.rehpic.substitution;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;

import org.junit.Test;

import uk.co.infinityapps.rehpic.utils.BitUtils;

public class SubstitutionBoxTest {
	
	
	private final KeyBasedInvolutionSubstitutionBox sbox = new KeyBasedInvolutionSubstitutionBox();

	@Test
	public void testSubtitute() {
		final String blockDataStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456";
		final byte[] plainTextBytes = blockDataStr.getBytes();
		BitUtils.printBytes(plainTextBytes, "plainTextBytes");
		
		final byte[] cipherTextBytes = sbox.subtitute(plainTextBytes, blockDataStr.getBytes());
		BitUtils.printBytes(cipherTextBytes, "cipherTextBytes");
		System.out.println("cipher text String: " + new String(cipherTextBytes));
		
		final byte[] recoveredPlainTextBytes = sbox.subtitute(cipherTextBytes, blockDataStr.getBytes());
		BitUtils.printBytes(recoveredPlainTextBytes, "recoveredPlainTextBytes");
		System.out.println(new String(recoveredPlainTextBytes));
	}
	
	@Test
	public void testGetColumnIndex(){
		// Creates a byte as 11010011 or rather two nibbles of 1101 (13) and 0011 (3) 
		final BitSet bitSet = new BitSet(8);
		bitSet.set(0, true);
		bitSet.set(1, true);
		bitSet.set(2, false);
		bitSet.set(3, false);
		bitSet.set(4, true);
		bitSet.set(6, false);
		bitSet.set(6, true);
		bitSet.set(7, true);
		
		assertEquals(3, sbox.getColumnIndex(bitSet, 0));
		assertEquals(13, sbox.getColumnIndex(bitSet, 4));
	}
	
	@Test
	public void testGetRowIndex(){
		// Creates a byte as 11010011 or rather two nibbles of 1101 (3) and 0011 (2)
		final BitSet bitSet = new BitSet(8);
		bitSet.set(0, true);
		bitSet.set(1, true);
		bitSet.set(2, false);
		bitSet.set(3, false);
		bitSet.set(4, true);
		bitSet.set(6, false);
		bitSet.set(6, true);
		bitSet.set(7, true);
		
		assertEquals(2, sbox.getRowIndex(bitSet, 0));
		assertEquals(3, sbox.getRowIndex(bitSet, 4));
	}
	
	@Test
	public void testSetSubstitutionValue(){
		final BitSet outputBitSet = new BitSet(8);
		final String substitutionValue1 = "0110";
		final String substitutionValue2 = "1100";
		sbox.setsubstitutionValue(outputBitSet, 0, substitutionValue1);
		sbox.setsubstitutionValue(outputBitSet, 4, substitutionValue2);
		byte[] outputBytes = BitUtils.bitSetToByteArray(outputBitSet, 1);
		
		assertEquals(1, outputBytes.length);
		assertEquals(0, BitUtils.getBit(outputBytes,7));
		assertEquals(1, BitUtils.getBit(outputBytes,6));
		assertEquals(1, BitUtils.getBit(outputBytes,5));
		assertEquals(0, BitUtils.getBit(outputBytes,4));
		assertEquals(0, BitUtils.getBit(outputBytes,3));
		assertEquals(0, BitUtils.getBit(outputBytes,2));
		assertEquals(1, BitUtils.getBit(outputBytes,1));
		assertEquals(1, BitUtils.getBit(outputBytes,0));
	}

}
