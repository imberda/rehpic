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
package uk.co.infinityapps.rehpic.round;

import org.junit.Test;

import uk.co.infinityapps.rehpic.key.generator.HashChainingBitShiftingKeyGenerator;
import uk.co.infinityapps.rehpic.key.generator.KeyGenerator;
import uk.co.infinityapps.rehpic.key.schedule.HashChainingSubKeyGenerator;
import uk.co.infinityapps.rehpic.key.schedule.SubKeyGenerator;
import uk.co.infinityapps.rehpic.permutation.*;
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
		final MixingPermutator mixingPermutator = new VariableLengthMixingPermutator();
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
