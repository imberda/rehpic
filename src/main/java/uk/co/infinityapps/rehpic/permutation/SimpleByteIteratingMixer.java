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

package uk.co.infinityapps.rehpic.permutation;

import uk.co.infinityapps.rehpic.utils.BitUtils;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

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
