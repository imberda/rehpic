package uk.co.infinityapps.rehpic.engine;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import uk.co.infinityapps.rehpic.utils.BitMatchUtils;

public class CryptoEngineTest {
	
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final int DATA_LENGTH_IN_BYTES = 500; 
	
	private static final int TEST_ITERATIONS = 1000;
	
	private double sumPercentageBitMatch = 0;
	
	@Test
	public void testEngine() throws NoSuchAlgorithmException {
		final CryptoEngine cryptoEngine = new CryptoEngine(32, 16);
		
		for(int i = 0 ; i <= TEST_ITERATIONS ; i++){
			final byte[] secretKey = cryptoEngine.createSecretKey(getVariableLengthRandomData(), "user1".getBytes());
			final byte[] plainText = getVariableLengthRandomData();
			final byte[] cipherText = cryptoEngine.encrypt(plainText, secretKey);
			final byte[] recoveredPlainText = cryptoEngine.decrypt(cipherText, secretKey);
			
			assertArrayEquals(plainText, recoveredPlainText);

			final double percentage = BitMatchUtils.percentageBitMatches(plainText, Arrays.copyOfRange(cipherText, 32, 32 + plainText.length));
			sumPercentageBitMatch += percentage;
	
		}
		
		final double avgPercentageBitMatch = sumPercentageBitMatch / TEST_ITERATIONS;
		System.out.println("Avg percentage bit match = " + avgPercentageBitMatch);
		assertTrue("Avg percentage bit match = " + avgPercentageBitMatch, avgPercentageBitMatch >= 40 && avgPercentageBitMatch <= 60);
		
	}
	
	private static byte[] getVariableLengthRandomData(){
		byte[] bytes = new byte[DATA_LENGTH_IN_BYTES];
		RANDOM.nextBytes(bytes);
		return bytes;
	}

}
