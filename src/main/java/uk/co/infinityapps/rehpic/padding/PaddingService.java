package uk.co.infinityapps.rehpic.padding;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public interface PaddingService {
	
	public byte[] pad(final byte[] inputBytes);
	
	public byte[] unpad(final byte[] inputBytes);
	
	public int getNumberOfBlocks(final byte[] inputBytes);

}
