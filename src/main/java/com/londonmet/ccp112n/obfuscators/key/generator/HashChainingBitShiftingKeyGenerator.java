package com.londonmet.ccp112n.obfuscators.key.generator;

import com.londonmet.ccp112n.obfuscators.permutation.KeyBasedPermutator;
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
