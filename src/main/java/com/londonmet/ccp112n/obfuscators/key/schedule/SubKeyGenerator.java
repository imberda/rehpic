package com.londonmet.ccp112n.obfuscators.key.schedule;

/**
 * 
 * 
 * @author London Met, Module: CCP112N, Group: 'The Obfuscators', Year: Autumn 2011
 * @version 1.0
 */
public interface SubKeyGenerator {

	byte[][] generateSubKeys(final byte[] secretKey, final int numberOfRounds);
}
