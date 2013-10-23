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

package uk.co.infinityapps.rehpic.padding;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.Test;

public class PaddingServiceImplTest {
	
	private static final int BLOCK_SIZE_IN_BYTES = 4;
	
	private final PaddingService paddingService = new PaddingServiceImpl(BLOCK_SIZE_IN_BYTES);

	@Test
	public void testWhereInputSizeLessThanOneBlockInLength() {
		byte[] outputBytes = paddingService.pad(ByteBuffer.allocate(0).array());  
		assertEquals(BLOCK_SIZE_IN_BYTES, outputBytes.length);
		
		outputBytes = paddingService.pad(ByteBuffer.allocate(1).array());  
		assertEquals(BLOCK_SIZE_IN_BYTES, outputBytes.length);
		
		outputBytes = paddingService.pad(ByteBuffer.allocate(2).array());  
		assertEquals(BLOCK_SIZE_IN_BYTES, outputBytes.length);
		
		outputBytes = paddingService.pad(ByteBuffer.allocate(3).array());  
		assertEquals(BLOCK_SIZE_IN_BYTES, outputBytes.length);
	}
	
	@Test
	public void testWhereInputSizeIsExactLengthOfBlock() {
		byte[] outputBytes = paddingService.pad(ByteBuffer.allocate(BLOCK_SIZE_IN_BYTES).array());  
		assertEquals(BLOCK_SIZE_IN_BYTES + BLOCK_SIZE_IN_BYTES, outputBytes.length);
		
		outputBytes = paddingService.pad(ByteBuffer.allocate(BLOCK_SIZE_IN_BYTES * 2).array());  
		assertEquals(BLOCK_SIZE_IN_BYTES * 3, outputBytes.length);
	}

	@Test
	public void testWhereInputSizeIsGreaterThanOneBlockButNotExactMultiple() {
		byte[] outputBytes = paddingService.pad(ByteBuffer.allocate(BLOCK_SIZE_IN_BYTES + 1).array());  
		assertEquals(BLOCK_SIZE_IN_BYTES * 2, outputBytes.length);
		
		outputBytes = paddingService.pad(ByteBuffer.allocate(BLOCK_SIZE_IN_BYTES * 2 + 2).array());  
		assertEquals(BLOCK_SIZE_IN_BYTES * 3, outputBytes.length);
	}
	
	@Test
	public void testUnpadWhereMarkerIsLastByte(){
		final byte[] outputBytes = paddingService.unpad(new byte[]{1, 14, 127});
		assertEquals(2, outputBytes.length);
		assertEquals(1, outputBytes[0]);
		assertEquals(14, outputBytes[1]);
	}
	
	@Test
	public void testUnpadWhereMarkerIsNotLastByte(){
		final byte[] outputBytes = paddingService.unpad(new byte[]{1, 14, 127, 0, 0});
		assertEquals(2, outputBytes.length);
		assertEquals(1, outputBytes[0]);
		assertEquals(14, outputBytes[1]);
	}

}
