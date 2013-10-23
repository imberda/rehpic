# rehpic

Rehpic (which is an anagram of Cipher) is a symmetric key cipher. It is not meant to be used commercially (or even seriously). It was originally created as part of a coursework submission for an MSc Computer Science module in Cryptography.

The hope is that other students tasked with a similar assignment may find this work as a useful starting point.

# Quick Start
```java
import uk.co.infinityapps.rehpic.engine.CryptoEngine;
import uk.co.infinityapps.rehpic.utils.BitUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;

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
		byte[] bytes = new byte[numberOfBytes];
		random.nextBytes(bytes);
		return bytes;
	}
}
```
