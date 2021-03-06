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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.Test;

import uk.co.infinityapps.rehpic.engine.CryptoEngine;
import uk.co.infinityapps.rehpic.utils.BitMatchUtils;

public class TimingTests {
	
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	
	public static final int ITERATIONS = 10; 
	public static final int DATA_LENGTH_IN_BYTES = 1024000; // 1 Mb
	
	@Test
	public void testEncryptWithDES() throws Exception {
		final byte[] plainText = getRandomData(DATA_LENGTH_IN_BYTES);
		SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		final long[] times = new long[ITERATIONS];
		
		
		for(int i = 0 ; i < ITERATIONS ; i++){
			final long startTime = System.currentTimeMillis();
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);	
			cipher.doFinal(plainText);
			final long endTime = System.currentTimeMillis();
			times[i] = endTime - startTime;
		}
		
		System.out.println("DES Avg time: " + getAverage(times));
		System.out.println("DES Bit Matches: " + BitMatchUtils.percentageBitMatches(plainText, Arrays.copyOfRange(cipher.doFinal(plainText), 0, plainText.length)));
	}
	
	@Test
	public void testEncryptWithAES() throws Exception {
		final byte[] plainText = getRandomData(DATA_LENGTH_IN_BYTES);
		SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		final long[] times = new long[ITERATIONS];
		
		
		for(int i = 0 ; i < ITERATIONS ; i++){
			final long startTime = System.currentTimeMillis();
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);	
			cipher.doFinal(plainText);
			final long endTime = System.currentTimeMillis();
			times[i] = endTime - startTime;
		}
		
		System.out.println("AES Avg time: " + getAverage(times));
		System.out.println("AES Bit Matches: " + BitMatchUtils.percentageBitMatches(plainText, Arrays.copyOfRange(cipher.doFinal(plainText), 0, plainText.length)));
	}
	
	@Test
	public void testEncryptWithObfuscatorsAlgo() throws Exception {
		final byte[] plainText = getRandomData(DATA_LENGTH_IN_BYTES);
		CryptoEngine cryptoEngine = new CryptoEngine(32, 1);
		final long[] times = new long[ITERATIONS];
		final byte[] secretKey = cryptoEngine.createSecretKey("THIS IS MY KEY".getBytes(), "THIS IS MY KEY".getBytes());
		
		for(int i = 0 ; i < ITERATIONS ; i++){
			final long startTime = System.currentTimeMillis();
			cryptoEngine.encrypt(plainText, secretKey);
			final long endTime = System.currentTimeMillis();
			times[i] = endTime - startTime;
		}
		
		System.out.println("Obfuscators Avg time: " + getAverage(times));
		System.out.println("Obfuscators Bit Matches: " +BitMatchUtils.percentageBitMatches(plainText, Arrays.copyOfRange(cryptoEngine.encrypt(plainText, secretKey), 32, 32 + plainText.length)));
	}
	
	private static byte[] getRandomData(final int lengthInBytes){
		byte[] bytes = new byte[lengthInBytes];
		RANDOM.nextBytes(bytes);
		return bytes;
	}
	
	private long getAverage(final long[] times){
		long sum = 0;
		for(int i = 0 ; i < times.length ; i++){
			sum += times[i];
		}
		return sum / times.length;
	}

}
