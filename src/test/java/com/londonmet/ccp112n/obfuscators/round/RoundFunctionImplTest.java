package com.londonmet.ccp112n.obfuscators.round;

import org.junit.Test;

import com.londonmet.ccp112n.obfuscators.key.generator.HashChainingBitShiftingKeyGenerator;
import com.londonmet.ccp112n.obfuscators.key.generator.KeyGenerator;
import com.londonmet.ccp112n.obfuscators.key.schedule.HashChainingSubKeyGenerator;
import com.londonmet.ccp112n.obfuscators.key.schedule.SubKeyGenerator;
import com.londonmet.ccp112n.obfuscators.permutation.KeyBasedBitShiftPermutator;
import com.londonmet.ccp112n.obfuscators.permutation.KeyBasedPermutator;
import com.londonmet.ccp112n.obfuscators.permutation.MixingPermutator;
import com.londonmet.ccp112n.obfuscators.permutation.SimpleByteIteratingMixer;
import com.londonmet.ccp112n.obfuscators.substitution.KeyBasedInvolutionSubstitutionBox;
import com.londonmet.ccp112n.obfuscators.substitution.KeyBasedSubstitutionBox;
import com.londonmet.ccp112n.obfuscators.utils.BitMatchUtils;

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
