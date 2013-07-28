package com.londonmet.ccp112n.obfuscators.cipherblockmode;

import java.security.SecureRandom;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public class InitialistionVectorGeneratorImpl implements InitialistionVectorGenerator {
	
	private final SecureRandom secureRandom;
	private final int lengthInBytes;
	
	public InitialistionVectorGeneratorImpl(final int lengthInBytes) {
		this.lengthInBytes = lengthInBytes;
		this.secureRandom = new SecureRandom();
	}
	
	public synchronized byte[] getIV(){
		final byte[] bytes = new byte[lengthInBytes];
		secureRandom.nextBytes(bytes);
		return bytes;
	}
	
	public int getLengthInBytes(){
		return lengthInBytes;
	}
}
