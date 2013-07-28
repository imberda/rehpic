package com.londonmet.ccp112n.obfuscators.key.schedule;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.junit.Test;

import com.londonmet.ccp112n.obfuscators.utils.BitMatchUtils;

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
