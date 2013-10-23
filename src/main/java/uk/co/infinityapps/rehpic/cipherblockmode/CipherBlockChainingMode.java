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

package uk.co.infinityapps.rehpic.cipherblockmode;

import uk.co.infinityapps.rehpic.padding.PaddingService;
import uk.co.infinityapps.rehpic.round.RoundFunction;
import uk.co.infinityapps.rehpic.utils.BitUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

/**
 * A Block Cipher Mode Implementation. This implementation uses CBC (Cipher Block Chaining),
 * using a random initialisation vector for every encryption operation. Moreover it returns 
 * the IV (in clear text) prepended to the cipher text.
 * <p>
 * The decryption operation expects the IV to be at the start of the cipher text.
 * <p>
 * The encryption operation adds padding as required and conversely decryption 
 * operation removes padding. 
 * 
 * @author imberda
 * @version 1.0
 */
public class CipherBlockChainingMode implements CipherBlockMode {
	
	private final RoundFunction roundFunction;
	private final PaddingService paddingService;
	private final InitialisationVectorGenerator initialisationVectorGenerator;
	
	public CipherBlockChainingMode(final RoundFunction roundFunction, final PaddingService paddingService, final InitialisationVectorGenerator initialisationVectorGenerator) {
		this.roundFunction = roundFunction;
		this.paddingService = paddingService;
		this.initialisationVectorGenerator = initialisationVectorGenerator;
	}
	
	@Override
	public byte[] encrypt(byte[] input, byte[][] subKeys) {
		final byte[] initialisationVector = initialisationVectorGenerator.getIV();
		final byte[] output = this.encrypt(input, subKeys, initialisationVector);
		return ArrayUtils.addAll(initialisationVector, output);
	}

	@Override
	public byte[] decrypt(byte[] input, byte[][] subKeys) {
		final byte[] initialisationVector = getBlock(0, input);
		final byte[] inputWithoutIv = Arrays.copyOfRange(input, initialisationVector.length, input.length);
		return this.decrypt(inputWithoutIv, subKeys, initialisationVector); 
	}	
	
	private byte[] encrypt(byte[] input, byte[][] subKeys, final byte[] initialisationVector) {
		final byte[] paddedInput = paddingService.pad(input);
		final ByteBuffer outputBytes = ByteBuffer.allocate(paddedInput.length);
		byte[] cipherTextBlock = initialisationVector;
		for(int currentBlock = 0 ;  currentBlock < paddingService.getNumberOfBlocks(paddedInput) ; currentBlock ++){
			byte[] plainTextBlock = getBlock(currentBlock, paddedInput);	
			byte[] xorPlainTextBlock = xorCipherAndPlainText(cipherTextBlock, plainTextBlock);
			cipherTextBlock = roundFunction.encrypt(xorPlainTextBlock, subKeys);
			outputBytes.put(cipherTextBlock);
		}
		
		return outputBytes.array();
	}
	
	private byte[] decrypt(byte[] input, byte[][] subKeys, final byte[] initialisationVector) {
		final ByteBuffer outputBytes = ByteBuffer.allocate(input.length);
		byte[] previousCipherTextBlock = initialisationVector;
		for(int currentBlock = 0 ;  currentBlock < paddingService.getNumberOfBlocks(input) ; currentBlock ++){
			byte[] cipherTextBlock = getBlock(currentBlock, input);
			cipherTextBlock = roundFunction.decrypt(cipherTextBlock, subKeys);
			cipherTextBlock = xorCipherAndPlainText(cipherTextBlock, previousCipherTextBlock);
			outputBytes.put(cipherTextBlock);
			previousCipherTextBlock = getBlock(currentBlock, input);
		}
		
		return paddingService.unpad(outputBytes.array());
	}
	
	private byte[] xorCipherAndPlainText(byte[] cipherText, byte[] plainText){
		final BitSet cipherTextBitSet = BitSet.valueOf(cipherText);
		final BitSet plainTextBitSet = BitSet.valueOf(plainText);
		cipherTextBitSet.xor(plainTextBitSet);
		return BitUtils.bitSetToByteArray(cipherTextBitSet, cipherText.length);
	}
	
	private byte[] getBlock(final int blockNumber, byte[] data){
		final int bitsInBlock = data.length / paddingService.getNumberOfBlocks(data);
		final int startIndex = blockNumber * bitsInBlock;
		final int endIndex = startIndex + bitsInBlock;
		return Arrays.copyOfRange(data, startIndex, endIndex);
	}

}
