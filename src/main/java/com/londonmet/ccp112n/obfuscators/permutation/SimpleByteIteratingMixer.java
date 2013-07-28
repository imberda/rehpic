package com.londonmet.ccp112n.obfuscators.permutation;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import com.londonmet.ccp112n.obfuscators.utils.BitUtils;

@Deprecated
public class SimpleByteIteratingMixer implements MixingPermutator {
	
	private static Map<Integer, Integer> MIX_MAP  = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> UNMIX_MAP  = new HashMap<Integer, Integer>();
	static {
		MIX_MAP.put(0, 7);
		MIX_MAP.put(1, 5);
		MIX_MAP.put(2, 3);
		MIX_MAP.put(3, 2);
		MIX_MAP.put(4, 6);
		MIX_MAP.put(5, 1);
		MIX_MAP.put(6, 4);
		MIX_MAP.put(7, 0);
		
		for(Map.Entry<Integer, Integer> entry : MIX_MAP.entrySet()){
			UNMIX_MAP.put(entry.getValue(), entry.getKey());
		}
	}
	
	public byte[] mix(byte[] inputData){
		return doMixing(inputData, MIX_MAP);
	}
	
	public byte[] unmix(byte[] inputData){
		return doMixing(inputData, UNMIX_MAP);
	}
	
	private byte[] doMixing(final byte[] inputData, final Map<Integer, Integer> mixingMap){
		final int inputBitLength = inputData.length * 8;
		final BitSet inputBitSet = BitSet.valueOf(inputData);
		final BitSet outputBitSet = new BitSet(inputBitLength);
		
		for(int byteOffset = 0 ; byteOffset < inputBitLength ; byteOffset += 8) {
			for(int bitOffset = 0 ; bitOffset < 8 ; bitOffset++) {
				final boolean bit = inputBitSet.get(byteOffset + bitOffset);
				final int newBitOffset = mixingMap.get(bitOffset);
				outputBitSet.set(byteOffset + newBitOffset , bit);
			}
		}
		return BitUtils.bitSetToByteArray(outputBitSet, inputData.length);
	}
}
