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

import uk.co.infinityapps.rehpic.permutation.KeyBasedPermutator;
import uk.co.infinityapps.rehpic.permutation.MixingPermutator;
import uk.co.infinityapps.rehpic.substitution.KeyBasedSubstitutionBox;
import uk.co.infinityapps.rehpic.utils.BitUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.BitSet;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public class RoundFunctionImpl implements RoundFunction {
	
	private final KeyBasedPermutator permutator;
	private final KeyBasedSubstitutionBox substitutionBox;
	private final MixingPermutator mixingPermutator;
	
	public RoundFunctionImpl(final KeyBasedPermutator permutator, final KeyBasedSubstitutionBox substitutionBox, final MixingPermutator mixingPermutator) {
		this.permutator = permutator;
		this.substitutionBox = substitutionBox;
		this.mixingPermutator = mixingPermutator;
	}

	public byte[] encrypt(byte[] blockData, final byte[][] subKeys){
		for(byte[] subKey : subKeys){
			blockData = mixingPermutator.mix(blockData);
			blockData = permutator.shiftLeft(blockData, subKey);
			final BitSet blockBitSet = BitSet.valueOf(blockData);
			blockBitSet.xor(BitSet.valueOf(subKey));
			blockData = substitutionBox.subtitute(BitUtils.bitSetToByteArray(blockBitSet, blockData.length), subKey);
		}
		return blockData;
	}
	
	public byte[] decrypt(byte[] blockData, final byte[][] subKeys){
		final byte[][] reversedSubKeys = reverseSubKeysForDecryption(subKeys);
		
		for(byte[] subKey : reversedSubKeys){
			blockData = substitutionBox.subtitute(blockData, subKey);
			BitSet xorBlockBitSet = BitSet.valueOf(blockData);
			xorBlockBitSet.xor(BitSet.valueOf(subKey));
			blockData = permutator.shiftRight(BitUtils.bitSetToByteArray(xorBlockBitSet, blockData.length), subKey);
			blockData = mixingPermutator.unmix(blockData);
		}
		return blockData;
	}
	
	private byte[][] reverseSubKeysForDecryption(final byte[][] subKeys){
		final byte[][] reversedSubKeys = Arrays.copyOf(subKeys, subKeys.length); 
		ArrayUtils.reverse(reversedSubKeys);
		return reversedSubKeys;
	}

}
