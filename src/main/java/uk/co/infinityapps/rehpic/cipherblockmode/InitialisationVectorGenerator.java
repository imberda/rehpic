package uk.co.infinityapps.rehpic.cipherblockmode;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface InitialisationVectorGenerator {
	
	public byte[] getIV();
	public int getLengthInBytes();
	
}
