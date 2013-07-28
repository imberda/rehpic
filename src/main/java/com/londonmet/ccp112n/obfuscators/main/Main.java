package com.londonmet.ccp112n.obfuscators.main;

import static org.junit.Assert.assertArrayEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.londonmet.ccp112n.obfuscators.engine.CryptoEngine;
import com.londonmet.ccp112n.obfuscators.utils.BitUtils;

/**
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 * 
 * Simple command line runnable program to demonstrate the usage of the CryptoEngine.
 * 
 * This example uses 32 bit block sizes and 4 rounds. Each iteration of the for-loop
 * randomly generates a new secret key seed and a new plain text byte array, of 
 * variable length (between 1 byte and 1 kilobyte).
 * 
 * Each iteration then performs an encryption and decryption operation which verifies 
 * that the plain text recovered from the decryption operation is the same as the 
 * plain text submitted to the encryption operation. Therefore it tests the symmetric 
 * relationship between the two operations.
 *
 */
public class Main {
	
	private static final int NUMBER_OF_ROUNDS = 4;
	private static final int BLOCK_SIZE_IN_BYTES = 32;
	private static final int MAX_DATA_LENGTH_IN_BYTES = 512; // 0.5 kilobytes
	
	private static final Random random = new Random();
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		final CryptoEngine cryptoEngine = new CryptoEngine(BLOCK_SIZE_IN_BYTES, NUMBER_OF_ROUNDS); 
		
		for(int i = 0 ; i < Integer.MAX_VALUE ; i++){
			final byte[] secretKey = cryptoEngine.createSecretKey(getVariableLengthRandomData(), "user1".getBytes());
			final byte[] plainText = getVariableLengthRandomData();
			final byte[] cipherText = cryptoEngine.encrypt(plainText, secretKey);
			final byte[] recoveredPlainText = cryptoEngine.decrypt(cipherText, secretKey);
			
			System.out.println("Plain text:           " + BitUtils.getHexBytes(plainText));
			System.out.println("Cipher text:          " + BitUtils.getHexBytes(cipherText));
			System.out.println("Recovered plain text: " + BitUtils.getHexBytes(recoveredPlainText));
			
			assertArrayEquals(plainText,recoveredPlainText);
			System.out.println("Crypto operation # " + i + " was successful");
		}
	}
	
	private static byte[] getVariableLengthRandomData(){
		final int numberOfBytes = random.nextInt(MAX_DATA_LENGTH_IN_BYTES) + 1;
		byte[] bytes = new byte[numberOfBytes];
		random.nextBytes(bytes);
		return bytes;
	}
}
