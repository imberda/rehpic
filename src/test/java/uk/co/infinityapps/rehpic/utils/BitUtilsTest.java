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
package uk.co.infinityapps.rehpic.utils;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;

import org.junit.Test;

public class BitUtilsTest {

	@Test
	public void testBitSetToByteArray() {
		final BitSet bitSet = new BitSet(32);
		final byte[] output = BitUtils.bitSetToByteArray(bitSet, 32);
		final byte[] output2 = bitSet.toByteArray();
		assertEquals(32, output.length);
		
		/* If this assert fails then the BitSet.toByteArray() method impl has changed and BitUtils.bitSetToByteArray(..) is no longer required */
		assertEquals(0, output2.length);

	}

	@Test
	public void testGetHexBytes() {
		final byte[] bytes = new byte[]{20,127};
		final String hexStr = BitUtils.getHexBytes(bytes);
		assertEquals("14:7f", hexStr);
	}
	
	
	@Test
	public void testReverseBits(){
		byte byte1 = 15;
		byte byte2 = 76;
		byte[] bytes = new byte[]{byte1, byte2};

		final String originalBytesStr = BitUtils.getBytes(bytes);
		
		byte[] reverseBytes = BitUtils.reverseBits(bytes);
		final String reverseBytesStr = BitUtils.getBytes(reverseBytes);
		
		assertEquals(new StringBuilder(originalBytesStr).reverse().toString(), reverseBytesStr);
		
		final String reversedReversedStr = BitUtils.getBytes(BitUtils.reverseBits(reverseBytes));
		assertEquals(originalBytesStr, reversedReversedStr);
	}

}
