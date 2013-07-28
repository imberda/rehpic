package com.londonmet.ccp112n.obfuscators.permutation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.londonmet.ccp112n.obfuscators.utils.BitUtils;

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
