package uk.co.infinityapps.rehpic.cipherblockmode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.co.infinityapps.rehpic.utils.BitMatchUtils;

public class InitialisationVectorGeneratorImplTest {

	private final InitialisationVectorGenerator generator = new InitialisationVectorGeneratorImpl(32);
	
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
