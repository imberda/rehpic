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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.infinityapps.rehpic.utils.BitUtils;

public class KeyBasedBitShiftPermutatorTest {
	
	private final KeyBasedBitShiftPermutator permutator = new KeyBasedBitShiftPermutator();

	@Test
	public void testShiftReversable() {
		byte aByte = 16;
		byte[] bytes = new byte[]{aByte};
		final byte[] shiftedBytes = permutator.shiftLeft(bytes, bytes);
		assertArrayEquals(bytes, permutator.shiftRight(shiftedBytes, bytes));
	}
	
	@Test
	public void testShiftLeftBy1() {
		final String originalStr = "00010000"; // 16 
		byte[] bytes = new byte[]{Byte.valueOf(originalStr, 2)};
		final byte[] shiftedBytes = permutator.shiftLeft(bytes, bytes);
		assertEquals("00100000",BitUtils.getBytes(shiftedBytes));
	}

	@Test
	public void testShiftLeftBy3() {
		final String originalStr = "01110000";  
		byte[] bytes = new byte[]{Byte.valueOf(originalStr, 2)};
		final byte[] shiftedBytes = permutator.shiftLeft(bytes, bytes);
		assertEquals("10000011",BitUtils.getBytes(shiftedBytes));
	}
	
	@Test
	public void testShiftRightBy1() {
		final String originalStr = "00010000"; // 16 
		byte[] bytes = new byte[]{Byte.valueOf(originalStr, 2)};
		final byte[] shiftedBytes = permutator.shiftRight(bytes, bytes);
		assertEquals("00001000",BitUtils.getBytes(shiftedBytes));
	}

	@Test
	public void testShiftRightBy3() {
		final String originalStr = "01110000";  
		byte[] bytes = new byte[]{Byte.valueOf(originalStr, 2)};
		final byte[] shiftedBytes = permutator.shiftRight(bytes, bytes);
		assertEquals("00001110",BitUtils.getBytes(shiftedBytes));
	}
}
