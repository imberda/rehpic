package com.londonmet.ccp112n.obfuscators.utils;

import java.util.BitSet;

public class BitMatchUtils {
	
	public static double percentageBitMatches(final byte[] data1, final byte[] data2){
		
		assert data1.length == data2.length;
		
		final BitSet data1BitSet = BitSet.valueOf(data1);
		final BitSet data2BitSet = BitSet.valueOf(data2);
		double bitMatches = 0;
		for(int j = 0 ; j < (data1.length * 8) ; j++){
			boolean data1Bit = data1BitSet.get(j);
			boolean data2Bit = data2BitSet.get(j);
			
			if(data1Bit == data2Bit){
				bitMatches++;
			}
		}
		
		double result = (100.00 / (data1.length * 8 )) * bitMatches;
		assert result <= 100 && result >= 0 : "result is: " + result;
		return result;
	}

}
