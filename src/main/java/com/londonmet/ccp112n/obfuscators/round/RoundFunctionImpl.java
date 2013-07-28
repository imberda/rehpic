package com.londonmet.ccp112n.obfuscators.round;

import com.londonmet.ccp112n.obfuscators.permutation.KeyBasedPermutator;
import com.londonmet.ccp112n.obfuscators.permutation.MixingPermutator;
import com.londonmet.ccp112n.obfuscators.substitution.KeyBasedSubstitutionBox;
import com.londonmet.ccp112n.obfuscators.utils.BitUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.BitSet;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
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
