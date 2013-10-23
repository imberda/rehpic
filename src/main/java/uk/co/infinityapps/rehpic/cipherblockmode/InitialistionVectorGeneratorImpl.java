package uk.co.infinityapps.rehpic.cipherblockmode;

import java.security.SecureRandom;

/**
 * 
 * 
 * @author imberda
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
