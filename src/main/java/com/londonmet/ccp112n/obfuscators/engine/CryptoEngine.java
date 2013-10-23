package com.londonmet.ccp112n.obfuscators.engine;

import com.londonmet.ccp112n.obfuscators.cipherblockmode.CipherBlockChainingMode;
import com.londonmet.ccp112n.obfuscators.cipherblockmode.CipherBlockMode;
import com.londonmet.ccp112n.obfuscators.cipherblockmode.InitialistionVectorGenerator;
import com.londonmet.ccp112n.obfuscators.cipherblockmode.InitialistionVectorGeneratorImpl;
import com.londonmet.ccp112n.obfuscators.key.generator.HashChainingBitShiftingKeyGenerator;
import com.londonmet.ccp112n.obfuscators.key.generator.KeyGenerator;
import com.londonmet.ccp112n.obfuscators.key.schedule.HashChainingSubKeyGenerator;
import com.londonmet.ccp112n.obfuscators.key.schedule.SubKeyGenerator;
import com.londonmet.ccp112n.obfuscators.padding.PaddingService;
import com.londonmet.ccp112n.obfuscators.padding.PaddingServiceImpl;
import com.londonmet.ccp112n.obfuscators.permutation.KeyBasedBitShiftPermutator;
import com.londonmet.ccp112n.obfuscators.permutation.KeyBasedPermutator;
import com.londonmet.ccp112n.obfuscators.permutation.MixingPermutator;
import com.londonmet.ccp112n.obfuscators.permutation.VariableLengthMixingPermutator;
import com.londonmet.ccp112n.obfuscators.round.RoundFunction;
import com.londonmet.ccp112n.obfuscators.round.RoundFunctionImpl;
import com.londonmet.ccp112n.obfuscators.substitution.KeyBasedInvolutionSubstitutionBox;
import com.londonmet.ccp112n.obfuscators.substitution.KeyBasedSubstitutionBox;

import java.security.NoSuchAlgorithmException;

/**
 * This class acts as the entry point into the project and is the class instantiated 
 * by clients to interact with the crypto operations.
 * <p>
 *  Provides a simple abstraction for clients, effectively encapsulating the underlying 
 *  complexity of the operations performed, and the components used to perform those 
 *  operations.
 * <p>   
 *  Clients are required to decide the size of the blocks processed and the number of rounds
 *  which will be used in the round function.
 *  <p>
 *  The CrypoEngine operates on and with byte arrays. Therefore the responsibility is the 
 *  client's for supplying data in this format. For example a client dealing with Strings
 *  would need to call aString.getBytes() in order to get a byte[] to pass into one of the 
 *  public methods in this class. Similarly all public methods return byte[] and the client
 *  is responsible for the conversion into the desired data type.
 *  
 * @author imberda
 * @version 1.0
 * 
 */
public class CryptoEngine {

	private final int blockSizeInBytes;
	private final int numberOfRounds;

	private MixingPermutator mixingPermutator;
	private KeyBasedPermutator keyBasedPermutator;
	private KeyGenerator keyGenerator;
	private SubKeyGenerator subKeyGenerator;
	private KeyBasedSubstitutionBox substitutionBox;
	private RoundFunction roundFunction;
	private PaddingService paddingService;
	private CipherBlockMode cipherBlockMode;
	private InitialistionVectorGenerator initialistionVectorGenerator;

	/**
	 *
	 * @param blockSizeInBytes The size of each block of plaintext/ciphertext processed at a time
	 * @param numberOfRounds The number of times the round function is envoked when encrypting/decrpying a block of data
	 * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available on the system executing this class
	 */
	public CryptoEngine(final int blockSizeInBytes, final int numberOfRounds) throws NoSuchAlgorithmException {
		this.blockSizeInBytes = blockSizeInBytes;
		this.numberOfRounds = numberOfRounds;
		this.mixingPermutator = new VariableLengthMixingPermutator();
		this.keyBasedPermutator = new KeyBasedBitShiftPermutator();
		this.keyGenerator = new HashChainingBitShiftingKeyGenerator(keyBasedPermutator);
		this.subKeyGenerator = new HashChainingSubKeyGenerator(blockSizeInBytes);
		this.substitutionBox = new KeyBasedInvolutionSubstitutionBox();
		this.roundFunction = new RoundFunctionImpl(keyBasedPermutator, substitutionBox, mixingPermutator);
		this.paddingService = new PaddingServiceImpl(blockSizeInBytes);
		this.initialistionVectorGenerator = new InitialistionVectorGeneratorImpl(blockSizeInBytes);
		this.cipherBlockMode = new CipherBlockChainingMode(roundFunction, paddingService, initialistionVectorGenerator);
	}

	/**
	 *
	 * @param keyIdentifier
     * @param password
	 * @return A byte array containing the secret key
	 */
	public byte[] createSecretKey(final byte[] keyIdentifier, final byte[] password){
		return this.keyGenerator.generateKey(keyIdentifier, password);
	}

	/**
	 * The encrypt function encrypts a plain text byte array of arbitrary using a secret key.
	 * <p>
	 * In doing so the plaintext is padded such that is is an exact multiple of the block size.
	 * <p>
	 * Moreover cipher block chaining is used (as the Mode of Operation) so two equal plain text
	 * blocks result in two unequal chipher text blocks.
	 * <p>
	 * The byte array returned is made up of:
	 * <p>
	 * &nbsp; a) The random initialisation vector used within the CBC operations (length = 1 block)
	 * <br>
	 * &nbsp; b) The padded cipher text
	 * <p>
	 * It is therefore expected that the output is bigger than the size of the plain text input
	 *
	 * Sub-keys (or round keys) are generated from the supplied secret key. The number generated is
	 * equal to the configured number of rounds within the cipher.
	 *
	 * @param plainText A byte array of arbitrary length representing plain text to be encrypted
	 * @param secretKey A byte array of arbitrary length representing the secret key used to generate subkey
	 * @return The cipher text version of padded plain text prepended with the initialisation vector used in CBC
	 */
	public byte[] encrypt(final byte[] plainText, final byte[] secretKey){
		final byte[][] subKeys = subKeyGenerator.generateSubKeys(secretKey, numberOfRounds);
		return cipherBlockMode.encrypt(plainText, subKeys);
	}


	/**
	 *
	 * The decrypt function decrypts cipher text into plain text. It is expected (and enforced) that the cihper text length (in bytes)
	 * is an exact multiple of the block size used within the cipher.
	 * <p>
	 * During decryption padding is removed (from the end of the cipher text) and the initialisation vector used by the Cipher Block
	 * Chaining (CBC) operations is removed (from the start of the cipher text). It is therefore expected that the output plain text
	 * from this method will be smaller in size as compared to the cipher text.
	 * <p>
	 * Sub-keys (or round keys) are generated from the supplied secret key. The number generated is
	 * equal to the configured number of rounds within the cipher.
	 *
	 * @param cipherText A byte array of representing cipher text to be encrypted. The length must be an exact multiple of the block size
	 * @param secretKey A byte array of arbitrary length representing the secret key used to generate subkeys
	 * @return plain text recovered from the cipher text
	 */
	public byte[] decrypt(final byte[] cipherText, final byte[] secretKey){
		assert cipherText != null && cipherText.length % blockSizeInBytes == 0;
		final byte[][] subKeys = subKeyGenerator.generateSubKeys(secretKey, numberOfRounds);
		return cipherBlockMode.decrypt(cipherText, subKeys);
	}

	/**
	 *
	 * @return The size of each block in bytes
	 */
	public int getBlockSizeInBytes() {
		return blockSizeInBytes;
	}

	/**
	 *
	 * @return The number of rounds
	 */
	public int getNumberOfRounds() {
		return numberOfRounds;
	}

	public MixingPermutator getMixingPermutator() {
		return mixingPermutator;
	}

	public KeyBasedPermutator getKeyBasedPermutator() {
		return keyBasedPermutator;
	}

	public KeyGenerator getKeyGenerator() {
		return keyGenerator;
	}

	public SubKeyGenerator getSubKeyGenerator() {
		return subKeyGenerator;
	}

	public KeyBasedSubstitutionBox getSubstitutionBox() {
		return substitutionBox;
	}

	public RoundFunction getRoundFunction() {
		return roundFunction;
	}

	public PaddingService getPaddingService() {
		return paddingService;
	}

	public CipherBlockMode getCipherBlockMode() {
		return cipherBlockMode;
	}

	public InitialistionVectorGenerator getInitialistionVectorGenerator() {
		return initialistionVectorGenerator;
	}

}
