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

package uk.co.infinityapps.rehpic.key.schedule;

import uk.co.infinityapps.rehpic.utils.BitUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Given a secret key as an input, this class generates series of sub-keys (or round-keys)
 * given a specified number of rounds (also given as input).
 * <p>
 *  Each sub-key is generated in two steps:
 *  <p>
 *  1. XOR together
 *  <br>
 *  &nbsp;&nbsp; a) The current sub-key
 *     <br>
 *  &nbsp;&nbsp; b) The previous sub-key
 *  <br>   
 *  2. Compute a hash using SHA256 of the resulting value
 *  <p>
 *  The general formula for generating a sub-key is:
 *  <p>
 *  SUBK# = Hash(XOR(PSK, CSK))
 *  <p>
 *  Where: 
 *  <ul>
 *  	<li> PSK=Previous Sub Key
 *		<li> CSK=Current Sub Key
 *   	<li> Hash=SHA256 Hash Function
 *		<li> IV=Initialisation Vector
 *		<li> XOR=Exclusive OR operation
 *		<li> SUBK#=Is a numbers subkey   
 *	</ul>
 *  The following diagram shows the process of encrypting several sub-keys:
 *  <p>
 *  SUBK1 = Hash(XOR(IV, SK))
 *  <br>
 *  SUBK2 = Hash(XOR(SK, SUBK1))
 *  <br>
 *  SUBK3 = Hash(XOR(SUBK1, SUBK2))
 *  <br>
 *  SUBK4 = Hash(XOR(SUBK2, SUBK3))
 *  <br>
 *  ....and so on....
 * 
 * @author imberda
 * @version 1.0
 */
public class HashChainingSubKeyGenerator implements SubKeyGenerator {
	
	private static final String HASHING_ALGORITHM = "SHA-256";
	
	private final MessageDigest messageDigest;
	private final byte[] initialisationVector; 
	private final int blockSizeInBytes;
	
	public HashChainingSubKeyGenerator(final int blockSizeInBytes) throws NoSuchAlgorithmException {
		this.messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);
		this.initialisationVector = messageDigest.digest(this.getClass().getName().getBytes());
		this.blockSizeInBytes = blockSizeInBytes;
	}
	
	public byte[][] generateSubKeys(final byte[] secretKey, final int numberOfRounds){
		assert secretKey.length == blockSizeInBytes && numberOfRounds > 0;
		
		final byte[][] subKeys = new byte[numberOfRounds][blockSizeInBytes];
		byte[] currentValue = secretKey;
		byte[] previousValue = initialisationVector;
		
		for(int i = 0 ; i < numberOfRounds ; i++){
			subKeys[i] = getSubKey(currentValue, previousValue);
			previousValue = currentValue;
			currentValue = subKeys[i];
		}
		
		return subKeys;
	}
	
	private byte[] getSubKey(final byte[] currentValue, final byte[] previousValue){
		final BitSet currentValueBitSet = BitSet.valueOf(currentValue);
		currentValueBitSet.xor(BitSet.valueOf(previousValue));
		final byte[] digestBytes = messageDigest.digest(BitUtils.bitSetToByteArray(currentValueBitSet, currentValue.length));
		return getBlockSizeNumOfBytes(digestBytes);
	}
	
	private byte[] getBlockSizeNumOfBytes(final byte[] bytes){
		return Arrays.copyOfRange(bytes, 0, blockSizeInBytes);
	}

}
