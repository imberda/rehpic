package com.londonmet.ccp112n.obfuscators.cipherblockmode;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

import org.apache.commons.lang3.ArrayUtils;

import com.londonmet.ccp112n.obfuscators.padding.PaddingService;
import com.londonmet.ccp112n.obfuscators.round.RoundFunction;
import com.londonmet.ccp112n.obfuscators.utils.BitUtils;

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
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public class CipherBlockChainingMode implements CipherBlockMode {
	
	private final RoundFunction roundFunction;
	private final PaddingService paddingService;
	private final InitialistionVectorGenerator initialistionVectorGenerator;
	
	public CipherBlockChainingMode(final RoundFunction roundFunction, final PaddingService paddingService, final InitialistionVectorGenerator initialistionVectorGenerator) {
		this.roundFunction = roundFunction;
		this.paddingService = paddingService;
		this.initialistionVectorGenerator = initialistionVectorGenerator;
	}
	
	@Override
	public byte[] encrypt(byte[] input, byte[][] subKeys) {
		final byte[] initialisationVector = initialistionVectorGenerator.getIV();
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
