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

package uk.co.infinityapps.rehpic.permutation;

import uk.co.infinityapps.rehpic.utils.BitUtils;

public class VariableLengthMixingPermutator implements MixingPermutator  {

    private static final int BITS_IN_BYTE = 8;

    private static final int[] SINGLE_BYTE_MAPPED_BIT_POSITIONS = new int[]{7,5,3,2,6,1,4,0};

    @Override
    public byte[] mix(byte[] inputData) {
        final int[] mappedBitPositions = this.getBitOffsets(inputData.length);
        final byte[] outputData = new byte[inputData.length];
        for(int position = 0 ; position < mappedBitPositions.length ; position++){
            final int bitValue = BitUtils.getBit(inputData, position);
            BitUtils.setBit(outputData, mappedBitPositions[position], bitValue);
        }
        return outputData;
    }

    @Override
    public byte[] unmix(byte[] inputData) {
        final int[] mappedBitPositions = this.getBitOffsets(inputData.length);
        final byte[] outputData = new byte[inputData.length];
        for(int position = 0 ; position < mappedBitPositions.length ; position++){
            final int bitValue = BitUtils.getBit(inputData, mappedBitPositions[position]);
            BitUtils.setBit(outputData, position, bitValue);
        }
        return outputData;
    }

    private int[] getBitOffsets(final int numberOfBytes){
        final int numberOfBits = numberOfBytes * BITS_IN_BYTE;
        final int[] mappedBitPositions = new int[numberOfBits];
        for(int bitPosition = 0; bitPosition < numberOfBits; bitPosition++){
            final int index = bitPosition % BITS_IN_BYTE;
            final int offset = SINGLE_BYTE_MAPPED_BIT_POSITIONS[index];
            final int currentByte = (bitPosition / BITS_IN_BYTE) + 1;
            mappedBitPositions[bitPosition] = ((offset * numberOfBytes) + currentByte) % (numberOfBits);
        }
        return mappedBitPositions;
    }
}
