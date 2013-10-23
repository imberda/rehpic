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

import java.security.SecureRandom;

/**
 * 
 * 
 * @author imberda
 * @version 1.0
 */
public class InitialisationVectorGeneratorImpl implements InitialisationVectorGenerator {
	
	private final SecureRandom secureRandom;
	private final int lengthInBytes;
	
	public InitialisationVectorGeneratorImpl(final int lengthInBytes) {
		this.lengthInBytes = lengthInBytes;
		this.secureRandom = new SecureRandom();
	}
	
	public synchronized byte[] getIV(){
		final byte[] bytes = new byte[lengthInBytes];
		secureRandom.nextBytes(bytes);
		return bytes;
	}
	
	public int getLengthInBytes(){
		return lengthInBytes;
	}
}
