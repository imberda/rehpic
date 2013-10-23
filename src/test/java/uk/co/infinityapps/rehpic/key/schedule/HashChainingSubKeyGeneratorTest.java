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

package uk.co.infinityapps.rehpic.key.schedule;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.junit.Test;

import uk.co.infinityapps.rehpic.utils.BitMatchUtils;

public class HashChainingSubKeyGeneratorTest {
	
	private final Random random = new Random();

	@Test
	public void testGenerateSubKeysIsRepeatable() throws NoSuchAlgorithmException {
		final SubKeyGenerator subKeyGenerator = new HashChainingSubKeyGenerator(32);
		final byte[] sectretKey = new byte[32];
		random.nextBytes(sectretKey);
		
		final byte[][] subKeys = subKeyGenerator.generateSubKeys(sectretKey, 4);
		
		assertEquals(4, subKeys.length);
		assertEquals(256/8, subKeys[0].length);
		assertEquals(256/8, subKeys[1].length);
		assertEquals(256/8, subKeys[2].length);
		assertEquals(256/8, subKeys[3].length);
		
		final byte[][] subKeys2 = subKeyGenerator.generateSubKeys(sectretKey, 4);
		final byte[][] subKeys3 = subKeyGenerator.generateSubKeys(sectretKey, 4);
		
		assertArrayEquals(subKeys, subKeys2);
		assertArrayEquals(subKeys2, subKeys3);
	}
	
	@Test
	public void testSubKeysAreVeryDifferent() throws NoSuchAlgorithmException {
		final SubKeyGenerator subKeyGenerator = new HashChainingSubKeyGenerator(32);
		final int numOfKeys = 10000;
		final byte[] sectretKey = new byte[32];
		random.nextBytes(sectretKey);
		
		final byte[][] subKeys = subKeyGenerator.generateSubKeys(sectretKey, numOfKeys);
		double totlalPercentageBitsSame = 0;
		for(byte[] subKey : subKeys){
			for(byte[] otherSubKey : subKeys){
				if(subKey != otherSubKey){
					double percentageBitsSame = BitMatchUtils.percentageBitMatches(subKey, otherSubKey);
					totlalPercentageBitsSame = totlalPercentageBitsSame + percentageBitsSame;
				}
			}
		}
		
		System.out.println("Total Average: " + totlalPercentageBitsSame / (numOfKeys * (numOfKeys - 1)));
		
	}

}
