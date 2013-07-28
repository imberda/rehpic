package com.londonmet.ccp112n.obfuscators.permutation;

public interface MixingPermutator {
	
	public byte[] mix(byte[] inputData);
	public byte[] unmix(byte[] inputData);

}
