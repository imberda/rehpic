package uk.co.infinityapps.rehpic.cipherblockmode;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface InitialistionVectorGenerator {
	
	public byte[] getIV();
	public int getLengthInBytes();
	
}
