package uk.co.infinityapps.rehpic.key.schedule;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface SubKeyGenerator {

	byte[][] generateSubKeys(final byte[] secretKey, final int numberOfRounds);
}
