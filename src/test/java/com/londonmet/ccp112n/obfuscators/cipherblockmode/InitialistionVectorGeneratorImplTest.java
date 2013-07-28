package com.londonmet.ccp112n.obfuscators.cipherblockmode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.londonmet.ccp112n.obfuscators.utils.BitMatchUtils;

public class InitialistionVectorGeneratorImplTest {

	private final InitialistionVectorGenerator generator = new InitialistionVectorGeneratorImpl(32);
	
	@Test
	public void testGetIV() {
		final byte[] iv = generator.getIV();
		assertEquals(32, iv.length);
		final byte[] iv2 = generator.getIV();
		final double percentageBitMatches = BitMatchUtils.percentageBitMatches(iv, iv2);
		assertTrue(percentageBitMatches > 40 && percentageBitMatches < 60);
	}

	@Test
	public void testGetLengthInBytes() {
		assertEquals(32, generator.getLengthInBytes());
	}

}
