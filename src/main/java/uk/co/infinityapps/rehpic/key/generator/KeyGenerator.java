package uk.co.infinityapps.rehpic.key.generator;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface KeyGenerator {
	
	public byte[] generateKey(final byte[] keyIdentifier, final byte[] password);

}
