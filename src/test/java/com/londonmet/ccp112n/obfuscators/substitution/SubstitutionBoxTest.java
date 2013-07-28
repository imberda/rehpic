package com.londonmet.ccp112n.obfuscators.substitution;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;

import org.junit.Test;

import com.londonmet.ccp112n.obfuscators.utils.BitUtils;

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
