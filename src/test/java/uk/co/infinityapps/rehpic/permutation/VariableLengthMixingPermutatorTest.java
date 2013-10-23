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

import static org.junit.Assert.*;

import static org.hamcrest.core.IsEqual.*;
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
