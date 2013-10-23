package uk.co.infinityapps.rehpic.substitution;

import uk.co.infinityapps.rehpic.utils.BitUtils;

import java.util.BitSet;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public class KeyBasedInvolutionSubstitutionBox implements KeyBasedSubstitutionBox {
	
   /* Columns                                           00      01      02      03      04      05      06      07      08      09      10      11      12      13      14      15  */ 	                                               
	public static final String[] ROW0 = new String[]{ "1000", "1110", "1101", "1011", "1001", "0110", "0101", "1100", "0000", "0100", "1111", "0011", "0111", "0010", "0001", "1010"};
	public static final String[] ROW1 = new String[]{ "0001", "0000", "0111", "1011", "1100", "1110", "1010", "0010", "1001", "1000", "0110", "0011", "0100", "1111", "0101", "1101"};
	public static final String[] ROW2 = new String[]{ "0110", "1011", "0100", "0111", "0010", "1101", "0000", "0011", "1100", "1111", "1110", "0001", "1000", "0101", "1010", "1001"};
	public static final String[] ROW3 = new String[]{ "1111", "1100", "1010", "0101", "1001", "0011", "0111", "0110", "1101", "0100", "0010", "1110", "0001", "1000", "1011", "0000"};	
	public static final String[] ROW4 = new String[]{ "0100", "0011", "1001", "0001", "0000", "0111", "1101", "0101", "1010", "0010", "1000", "1110", "1111", "0110", "1011", "1100"};
	
	public static final String[][] SBOX = new String[][]{ ROW0, ROW1, ROW2, ROW3, ROW4};
	
	public byte[] subtitute(final byte[] blockData, final byte[] subKey){
		
		final BitSet blockDataBitSet = BitSet.valueOf(blockData);
		final BitSet subKeyBitSet = BitSet.valueOf(subKey);
		final int outputNumBits = blockData.length * 8;
		final BitSet outputBitSet = new BitSet(outputNumBits);
		
		for(int i = 0 ; i < outputNumBits ; i = i + 4){
			final int rowIndex = getRowIndex(subKeyBitSet, i);
			final int columnIndex = getColumnIndex(blockDataBitSet, i);
			
			final String substitutionValue = SBOX[rowIndex][columnIndex];
			this.setsubstitutionValue(outputBitSet, i, substitutionValue);
		}
		
		return BitUtils.bitSetToByteArray(outputBitSet, blockData.length);
	}
	
	
	/**
	 * 
	 * @param outputBitSet
	 * @param offset
	 * @param substitutionValue
	 */
	void setsubstitutionValue(final BitSet outputBitSet, int offset, final String substitutionValue){
		final String reversedSubstitutionValue = new StringBuilder(substitutionValue).reverse().toString();
		for(final char aChar : reversedSubstitutionValue.toCharArray()){
			outputBitSet.set(offset++, getValue(aChar));
		}
	}
	
	/**
	 * Used to determine the column to use within the SBOX
	 * 
	 * Therefore values between must be within the range 0-15 as there are sixteen columns within the SBOX
	 * 
	 * The value is determined calculating the binary value of the  four bit sequence. Therefore "0000" would
	 * equate to 0, "1111" would equate to 15, "1010" would equate to 10 etc etc.
	 * 
	 * @param bitSet containing the block of data to process
	 * @param offset the starting bitindex within the BitSet into when processing the s-box column index value 
	 * @return the SBOX column index value
	 */
	int getColumnIndex(final BitSet bitSet, int offset) {
		final StringBuilder sb = new StringBuilder();
		sb.append(getValue(bitSet.get(offset++)));
		sb.append(getValue(bitSet.get(offset++)));
		sb.append(getValue(bitSet.get(offset++)));
		sb.append(getValue(bitSet.get(offset)));
		return Integer.valueOf(sb.reverse().toString(), 2);
	}
	/**
	 * Used to determine the row to use within the SBOX
	 * 
	 * Therefore values between must be within the range 0-4 as there are five rows within the SBOX
	 * 
	 * The value is determined calculating the value of the four bit sequence, using single digit decimal
	 * addition. Therefore "0000" would equate to 0, "1111" would equate to 4, "1010" would equate to 2 etc.
	 * 
	 * @param bitSet containing the block of data to process
	 * @param offset the starting bitindex within the BitSet into when processing the s-box row index value 
	 * @return the SBOX row index value
	 */
	int getRowIndex(final BitSet bitSet, int offset) {
		int rowId = 0;
		rowId += getValue(bitSet.get(offset++));
		rowId += getValue(bitSet.get(offset++));
		rowId += getValue(bitSet.get(offset++));
		rowId += getValue(bitSet.get(offset++));
		return rowId;
	}
	
	
	/**
	 * 
	 * @param bitFlag a boolean value representing if a bit is "on" (true) or "off" (false)
	 * @return the integer value for the bit (either 1 or 0)
	 */
	private int getValue(final boolean bitFlag){
		return bitFlag ? 1 : 0;
	}
	
	/**
	 * 
	 * @param aChar representing a bit (either '1' or '0')
	 * @return the boolean value for the bit ("on" (true) or "off" (false))
	 */
	private boolean getValue(final char aChar){
		assert aChar == '1' || aChar == '0';
		return aChar == '1';
	}

}
