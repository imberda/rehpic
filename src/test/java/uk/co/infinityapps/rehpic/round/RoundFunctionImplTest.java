package uk.co.infinityapps.rehpic.round;

import org.junit.Test;

import uk.co.infinityapps.rehpic.key.generator.HashChainingBitShiftingKeyGenerator;
import uk.co.infinityapps.rehpic.key.generator.KeyGenerator;
import uk.co.infinityapps.rehpic.key.schedule.HashChainingSubKeyGenerator;
import uk.co.infinityapps.rehpic.key.schedule.SubKeyGenerator;
import uk.co.infinityapps.rehpic.permutation.KeyBasedBitShiftPermutator;
import uk.co.infinityapps.rehpic.permutation.KeyBasedPermutator;
import uk.co.infinityapps.rehpic.permutation.MixingPermutator;
import uk.co.infinityapps.rehpic.permutation.SimpleByteIteratingMixer;
import uk.co.infinityapps.rehpic.substitution.KeyBasedInvolutionSubstitutionBox;
import uk.co.infinityapps.rehpic.substitution.KeyBasedSubstitutionBox;
import uk.co.infinityapps.rehpic.utils.BitMatchUtils;

import static org.junit.Assert.*;

public class RoundFunctionImplTest {

	private static final int NUMBER_OF_ROUNDS = 16;
	
	private static final byte[] CLEAR_TEXT_BLOCK = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456".getBytes();
	
	@Test
	public void testFunctionIsReversable() throws Exception {
		final KeyBasedPermutator permutator = new KeyBasedBitShiftPermutator();
		final KeyBasedSubstitutionBox substitutionBox = new KeyBasedInvolutionSubstitutionBox();
		final MixingPermutator mixingPermutator = new SimpleByteIteratingMixer();
		final RoundFunction function = new RoundFunctionImpl(permutator, substitutionBox, mixingPermutator);
		final KeyGenerator keyGenerator = new HashChainingBitShiftingKeyGenerator(permutator);
		final SubKeyGenerator subKeyGenerator = new HashChainingSubKeyGenerator(32);
		
		final byte[] secretKey = keyGenerator.generateKey("identifier".getBytes(), "This is my initial password used to generate the secret key".getBytes());
		final byte[][] subKeys = subKeyGenerator.generateSubKeys(secretKey, NUMBER_OF_ROUNDS);
		
		byte[] output1 = function.encrypt(CLEAR_TEXT_BLOCK, subKeys);
		
		byte[] output2 = function.decrypt(output1, subKeys);
		
		assertArrayEquals(CLEAR_TEXT_BLOCK, output2);
		
		final double percentageBitMatch = BitMatchUtils.percentageBitMatches(output1, output2);
		System.out.println(percentageBitMatch);
		assertTrue(percentageBitMatch > 40 && percentageBitMatch < 60);
	}
	
}
