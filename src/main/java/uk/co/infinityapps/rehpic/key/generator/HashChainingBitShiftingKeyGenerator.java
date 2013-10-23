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

package uk.co.infinityapps.rehpic.key.generator;

import uk.co.infinityapps.rehpic.permutation.KeyBasedPermutator;
import org.apache.commons.lang3.ArrayUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public class HashChainingBitShiftingKeyGenerator implements KeyGenerator {
	
	private static final String HASHING_ALGORITHM = "SHA-256";
	private static final int HASH_CHAIN_ITERATIONS = 1000;
	
	private final MessageDigest messageDigest;
	private final KeyBasedPermutator permutator;
	
	public enum ShiftDirection {
		LEFT, RIGHT
	}
	
	/**
	 * 
	 * @param permutator The Permutator implementation to use in conjunction with the key generator
	 * @throws NoSuchAlgorithmException Thrown in the SHA-256 hashing algorithm is not available on the system hosting the application
	 */
	public HashChainingBitShiftingKeyGenerator(final KeyBasedPermutator permutator) throws NoSuchAlgorithmException {
		this.messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);
		this.permutator = permutator;
	}
	
	@Override
	public synchronized byte[] generateKey(final byte[] keyIdentifier, final byte[] password) {
		assert keyIdentifier != null && password != null ;
		assert keyIdentifier.length > 0 && password.length > 0;
		
		final byte[] shiftedPasswordBytes = getShiftedBytes(password);
		
		byte[] secretKey = messageDigest.digest(ArrayUtils.addAll(shiftedPasswordBytes, keyIdentifier));
		for(int i = 0 ; i < HASH_CHAIN_ITERATIONS ; i++){
			secretKey = messageDigest.digest(ArrayUtils.addAll(secretKey, keyIdentifier));
		}
		
		return secretKey;
		
	}
	
	public ShiftDirection getShiftDirection(final byte[] bytes){
		byte lastByte = bytes[bytes.length - 1];
		if(Byte.valueOf(lastByte).intValue() > 0){
			return ShiftDirection.LEFT;
		} else {
			return ShiftDirection.RIGHT;
		}
	}
	
	public byte[] getShiftedBytes(byte[] bytes) {
		final ShiftDirection direction = getShiftDirection(bytes);
		if(direction.equals(ShiftDirection.LEFT)){
			return permutator.shiftLeft(bytes, bytes);
		} else {
			return permutator.shiftRight(bytes, bytes);
		}
	}

}
