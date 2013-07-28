package com.londonmet.ccp112n.obfuscators.padding;

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
