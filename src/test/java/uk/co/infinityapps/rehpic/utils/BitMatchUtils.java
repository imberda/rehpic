/*
 * Copyright (c) 2013 Infinity Apps Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package uk.co.infinityapps.rehpic.utils;

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
