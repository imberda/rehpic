package uk.co.infinityapps.rehpic.padding;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Padding implementation that uses Byte.MAX_VALUE as the "marker" to distinguish between padding and real data. It 
 * uses zeros for the padded bytes themselves.

 * @author imberda
 * @version 1.0
 *
 *
 */
public class PaddingServiceImpl implements PaddingService {

	private static final byte PAD_MARKER = Byte.MAX_VALUE;
	private static final byte PAD_VALUE = 0;
			
	private final int blocksizeInBytes;
	
	public PaddingServiceImpl(final int blocksizeInBytes){
		this.blocksizeInBytes = blocksizeInBytes;
	}
	
	
	/**
	 * Performs padding of inputBytes to ensure that the bytes returned are an exact multiple of
	 * the configured blocksizeInBytes. 
	 * <p>
	 * It is important to state that the size of the byte array output is ALWAYS longer than the size
	 * of inputBytes. That is because the padding marker is always suffixed to the array even if the 
	 * inputBytes are an exact multiple of blocksizeInBytes. This is required to allow for safe 
	 * unpadding.
	 * <p>
	 * Padding is achieved by appending a "marker" byte to the end of the inputBytes. The marker is 
	 * Byte.MAX_VALUE (127). Thereafter a number of padding bytes are added. The padding bytes are
	 * simple zero (0) values. The number of padding bytes is the number of bytes required to make:
	 * (inputBytes + marker byte + padding bytes) %  blocksizeInBytes = 0
	 * <p>
	 * The invariants are:
	 * <p>
	 * &nbsp; There is always 1 marker byte
	 * <br>
	 * &nbsp; There are 0 to blocksizeInBytes - 1 padding bytes
	 * 
	 * @param inputBytes
	 * @return padded byte array such that inputBytes is padded to exact multiple of blocksizeInBytes
	 */
	@Override
	public byte[] pad(final byte[] inputBytes) {
		final int inputSize = inputBytes.length;
		final int numPaddingBytes = getNumberOfPaddingBytes(inputSize);
		final ByteBuffer buffer = ByteBuffer.allocate(inputSize + numPaddingBytes).put(inputBytes).put(PAD_MARKER);
		
		for(int i = 1 ; i < numPaddingBytes ; i++){
			buffer.put(PAD_VALUE);
		}

		return buffer.array();
	}
	
	/**
	 * Method for removing padding from inputBytes. This is achieved by iterating 
	 * the bytes within inputBytes, starting at the last byte, looking for the first
	 * Occurrence of the market value (Byte.MAX). 
	 * 
	 * Once the position of the marker value is found the method returns the bytes 
	 * within inputBytes starting at the first byte up to (but not including) the
	 * marker byte.
	 * 
	 *  @param inputBytes padded array of bytes
	 *  @return unpadded array of bytes
	 *  
	 */
	@Override
	public byte[] unpad(byte[] inputBytes) {
		for(int i = 0 ; i < inputBytes.length ; i++){
			final int pos = inputBytes.length - i - 1;
			byte aByte = inputBytes[pos];
			if(PAD_MARKER == aByte){
				return Arrays.copyOfRange(inputBytes, 0, pos);
			}
		}
		return inputBytes;
	}
	

	/**
	 * Simple convenience method to calculate the number of blocks within the 
	 * input data bytes.
	 * 
	 * Note this method uses integer arithmetic, therefore if inputBytes is not
	 * a divisor of blocksizeInBytes the remainder will be lost.
	 * 
	 * @param inputBytes 
	 * @return The number of blocks within inputBytes
	 */
	@Override
	public int getNumberOfBlocks(byte[] inputBytes) {
		return inputBytes.length / blocksizeInBytes;
	}
	
	/**
	 * Calculates the number of padding bytes required in order to turn the input data bytes
	 * into an exact multiple of the block size
	 * 
	 * @param inputSizeInByte The number of bytes within the input data
	 * @return the number of padding bytes required to make input a multiple of the block size
	 */
	private int getNumberOfPaddingBytes(final int inputSizeInByte){
		final int remainder = inputSizeInByte % blocksizeInBytes;
		return blocksizeInBytes - remainder;
	}
	
}
