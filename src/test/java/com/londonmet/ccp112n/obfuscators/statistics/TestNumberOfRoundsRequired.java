package com.londonmet.ccp112n.obfuscators.statistics;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import com.londonmet.ccp112n.obfuscators.engine.CryptoEngine;
import com.londonmet.ccp112n.obfuscators.utils.BitMatchUtils;

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
