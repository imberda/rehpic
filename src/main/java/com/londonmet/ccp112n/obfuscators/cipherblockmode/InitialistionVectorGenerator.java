package com.londonmet.ccp112n.obfuscators.cipherblockmode;

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
