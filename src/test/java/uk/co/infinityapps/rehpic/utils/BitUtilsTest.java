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
