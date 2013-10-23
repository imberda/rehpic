package uk.co.infinityapps.rehpic.key.generator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import uk.co.infinityapps.rehpic.permutation.KeyBasedBitShiftPermutator;
import uk.co.infinityapps.rehpic.permutation.KeyBasedPermutator;
import uk.co.infinityapps.rehpic.utils.BitMatchUtils;

public class KeyGeneratorImplTest {
	
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final int NUM_ITERATIONS = 100000;

	@Test
	public void testGenerateKey() throws NoSuchAlgorithmException {
		final HashChainingBitShiftingKeyGenerator keyGenerator =  new HashChainingBitShiftingKeyGenerator(new KeyBasedBitShiftPermutator());


		double bitMatchPercentageRunningTotal = 0;
		
		for(int i = 0 ; i <= NUM_ITERATIONS ; i++){

			final String password = RandomStringUtils.randomAlphanumeric(5 + RANDOM.nextInt(15));

			final String password2 = password + RandomStringUtils.randomAlphanumeric(1);
			
			final byte[] key1 = keyGenerator.generateKey(password.getBytes(), password.getBytes());
			final byte[] key2 = keyGenerator.generateKey(password2.getBytes(), password2.getBytes());
			final byte[] key3 = keyGenerator.generateKey(password2.getBytes(), password2.getBytes());
			
			// Key generation must be predictable and repeatable given the same initial password
			assertArrayEquals("Expected key2 and key3 to match but they did not", key2, key3);
			
			bitMatchPercentageRunningTotal += BitMatchUtils.percentageBitMatches(key1, key2);
			
		}
		
		final double bitMatchPercentageGrandTotal = bitMatchPercentageRunningTotal / NUM_ITERATIONS;

		assertTrue("Expected the overall percentage of matched bits to be between 45-55%: " + bitMatchPercentageGrandTotal, bitMatchPercentageGrandTotal > 45 && bitMatchPercentageGrandTotal < 55);
		System.out.println("Overall percentage of matched bits was: " + bitMatchPercentageGrandTotal);
		
	}
	
	@Test
	public void testGetShiftedBytesLeft() throws NoSuchAlgorithmException {
		final byte[] bytes = new byte[]{2, 2};
		final KeyBasedPermutator permutator = mock(KeyBasedPermutator.class);
		final HashChainingBitShiftingKeyGenerator keyGenerator = new HashChainingBitShiftingKeyGenerator(permutator);
		keyGenerator.getShiftedBytes(bytes);
		verify(permutator).shiftLeft(bytes, bytes);
	}
	
	@Test
	public void testGetShiftedBytesRight() throws NoSuchAlgorithmException {
		final byte[] bytes = new byte[]{2, -1};
		final KeyBasedPermutator permutator = mock(KeyBasedPermutator.class);
		final HashChainingBitShiftingKeyGenerator keyGenerator = new HashChainingBitShiftingKeyGenerator(permutator);
		keyGenerator.getShiftedBytes(bytes);
		verify(permutator).shiftRight(bytes, bytes);
	}

}
