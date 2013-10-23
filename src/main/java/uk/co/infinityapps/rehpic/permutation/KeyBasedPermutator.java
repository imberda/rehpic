package uk.co.infinityapps.rehpic.permutation;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface KeyBasedPermutator {

	byte[] shiftLeft(final byte[] dataBlock, final byte[] key);
	byte[] shiftRight(final byte[] dataBlock, final byte[] key);
}
