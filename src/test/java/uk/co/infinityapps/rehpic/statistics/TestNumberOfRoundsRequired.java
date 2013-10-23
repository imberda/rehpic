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
package uk.co.infinityapps.rehpic.statistics;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import uk.co.infinityapps.rehpic.engine.CryptoEngine;
import uk.co.infinityapps.rehpic.utils.BitMatchUtils;

public class TestNumberOfRoundsRequired {
	
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	
	public static final int MAX_ROUNDS = 8; 
	public static final int MAX_DATA_SIZE_IN_BYTES = 4096;
	public static final int MAX_ITERATIONS = 1000;
	
	@Test
	public void testEncryptWithBespoke() throws Exception {
		for(int i = 1 ; i <= MAX_ROUNDS ; i++){
			for(int j = 1 ; j < MAX_DATA_SIZE_IN_BYTES ; j *= 2) {
				double bitsMatch = 0;
				for(int k = 1 ; k <= MAX_ITERATIONS ; k++) {
					final byte[] plainText = getRandomData(j);
					final CryptoEngine cryptoEngine = new CryptoEngine(32, i);
					final byte[] secretKey = cryptoEngine.createSecretKey("THIS IS MY KEY".getBytes(), "THIS IS MY KEY".getBytes());
					byte[] ciphertext = cryptoEngine.encrypt(plainText, secretKey);
					bitsMatch += BitMatchUtils.percentageBitMatches(plainText, Arrays.copyOfRange(ciphertext, 32, 32 + plainText.length));
				}
				double bitsMatchAvg =  bitsMatch  / MAX_ITERATIONS;
				System.out.println("Rounds: " + i + " datasize: " + j + " matches: " + bitsMatchAvg);
			}

		}
	}
	
	private static byte[] getRandomData(final int lengthInBytes){
		byte[] bytes = new byte[lengthInBytes];
		RANDOM.nextBytes(bytes);
		return bytes;
	}
}
