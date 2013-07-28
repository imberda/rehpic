package com.londonmet.ccp112n.obfuscators.permutation;

import static org.junit.Assert.*;

import org.junit.Test;

import com.londonmet.ccp112n.obfuscators.utils.BitUtils;

public class SimpleByteIteratingMixerTest {

	private final MixingPermutator  mixingPermutator = new SimpleByteIteratingMixer();
	
	@Test
	public void testMix() {
		for(byte i = Byte.MIN_VALUE ; i < Byte.MAX_VALUE ; i++){
			final byte[] input = new byte[]{i, i};
			final byte[] output = mixingPermutator.mix(input);
			byte[] output2 = mixingPermutator.unmix(output);
			assertArrayEquals(input, output2);		}
	}

}
