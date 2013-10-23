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
