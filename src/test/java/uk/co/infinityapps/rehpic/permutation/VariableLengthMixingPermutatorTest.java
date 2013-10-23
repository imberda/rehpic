package uk.co.infinityapps.rehpic.permutation;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.Is.*;

import org.junit.Test;

public class VariableLengthMixingPermutatorTest {

	private final MixingPermutator mixingPermutator = new VariableLengthMixingPermutator();

	@Test
	public void testMix() {
		for(byte i = Byte.MIN_VALUE ; i < Byte.MAX_VALUE ; i++){
			final byte[] input = new byte[]{i, i};
			final byte[] mixed = mixingPermutator.mix(input);
            final byte[] unmixed = mixingPermutator.unmix(mixed);
            assertThat(input, is(equalTo(unmixed)));
        }
	}
}
