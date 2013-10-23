package uk.co.infinityapps.rehpic.permutation;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleByteIteratingMixerTest {

	private final MixingPermutator mixingPermutator = new SimpleByteIteratingMixer();
	
	@Test
	public void testMix() {
		for(byte i = Byte.MIN_VALUE ; i < Byte.MAX_VALUE ; i++){
			final byte[] input = new byte[]{i, i};
			final byte[] output = mixingPermutator.mix(input);
			byte[] output2 = mixingPermutator.unmix(output);
			assertArrayEquals(input, output2);		}
	}

}
