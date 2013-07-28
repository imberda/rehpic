package com.londonmet.ccp112n.obfuscators.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

import org.apache.commons.lang3.StringUtils;

/*
 ****************************************************************************************************************
 *                                                                                                              *
 * NOTE:                                                                                                        * 
 *    Methods setBit, getBit, rotateLeft, printBytes & byteToBits should be considered as library code in       *
 *    re-use as opposed to code written by this group. The method implementations were obtained from:           *
 *                                                                                                              *
 *    http://www.herongyang.com                                                                                 *
 *                                                                                                              *        
 ****************************************************************************************************************
 */

public class BitUtils {

	/**
	 * This method makes up for a quirk in the standard java.util.BitSet class toByteArray()
	 * method, whereby high-end zero value bytes are discarded. This has the effect on returning
	 * a byte array whose size is < the number of bits in the BitSet.
	 * 
	 * This method works around this by adding (or padding) with zeros where nessecary.
	 * 
	 * @param bitSet The input bitset whose underlying data should be returned a a byte array
	 * @param numBytes The desired length in bytes of the byte array returned
	 * @return a byte array of the desired length
	 */
	public static byte[] bitSetToByteArray(final BitSet bitSet, final int numBytes) {
		return Arrays.copyOf(bitSet.toByteArray(), numBytes);
	}
	
	public static void setBit(byte[] data, int pos, int val) {
		int posByte = pos / 8;
		int posBit = pos % 8;
		byte oldByte = data[posByte];
		oldByte = (byte) (((0xFF7F >> posBit) & oldByte) & 0x00FF);
		byte newByte = (byte) ((val << (8 - (posBit + 1))) | oldByte);
		data[posByte] = newByte;
	}

	public static int getBit(byte[] data, int pos) {
		int posByte = pos / 8;
		int posBit = pos % 8;
		byte valByte = data[posByte];
		int valInt = valByte >> (8 - (posBit + 1)) & 0x0001;
		return valInt;
	}

	public static byte[] rotateLeft(byte[] bytes, int step) {
		int length = bytes.length * 8;
		int numOfBytes = (length - 1) / 8 + 1;
		byte[] out = new byte[numOfBytes];
		for (int i = 0; i < length; i++) {
			int val = getBit(bytes, (i + step) % length);
			setBit(out, i, val);
		}
		return out;
	}
	
	public static void printBytes(byte[] data, String name) {
		System.out.println("");
		System.out.println(name + ":");
		System.out.println(getBytes(data));
		System.out.println();
	}
	
	public static String getByte(byte data) {
		return getBytes(new byte[]{data});
	}
	
	public static String getBytes(byte[] data) {
		final StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < data.length; i++) {
			if(i != 0){
				sb.append(" ");
			}
			sb.append(byteToBits(data[i]));
		}
		return sb.toString();
	}
	
	public static byte[] getBytes(final String str){
		final BitSet bitSet = new BitSet();
		int i = 0;
		for(String aByteStr : StringUtils.split(str, ' ')){
			final String reversedByteStr = StringUtils.reverse(aByteStr);
			for(char aChar : reversedByteStr.toCharArray()){
				bitSet.set(i++, aChar == '1');
			}
		}
		return bitSetToByteArray(bitSet, i / 8);
	}
	
	public static String getHexBytes(byte[] data) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			final String byteAsString = byteToBits(data[i]);
			if(i != 0){
				sb.append(":");
			}
			sb.append(Integer.toHexString(Integer.valueOf(byteAsString, 2)));
		}
		return sb.toString();
	}

	private static String byteToBits(byte b) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 8; i++)
			buf.append((int) (b >> (8 - (i + 1)) & 0x0001));
		return buf.toString();
	}
	
	/**
	 * Takes in a byte array and reverse the bit ordering in the 
	 * returned byte array.
	 * 
	 * 
	 * @param data a block of data as a byte array
	 */
	public static byte[] reverseBits(byte[] data){
		final int numBits = data.length * 8;
		final BitSet originalBitSet = BitSet.valueOf(data);
		final BitSet reversedBitSet = new BitSet(numBits);
		for(int i = 0 ; i < numBits ; i++){
			reversedBitSet.set(i, originalBitSet.get(numBits - 1 - i));
		}
		return BitUtils.bitSetToByteArray(reversedBitSet, data.length);
	}
}
