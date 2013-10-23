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

package uk.co.infinityapps.rehpic.main;

import uk.co.infinityapps.rehpic.engine.CryptoEngine;
import uk.co.infinityapps.rehpic.utils.BitUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;

/**
 * 
 * @author imberda
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
			final byte[] secretKey = cryptoEngine.createSecretKey(getVariableLengthRandomData(), "secret".getBytes());
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
		final byte[] bytes = new byte[numberOfBytes];
		random.nextBytes(bytes);
		return bytes;
	}
}
