package cn.kuehne.wsg50.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayOutputTests {
	
	@Test
	public void testEmpty(){
		final ArrayOutput output = new ArrayOutput();
		final byte[] result = output.toByteArray();
		assertArrayEquals(new byte[0], result);
	}
	
	@Test
	public void testWritePacket(){
		final ArrayOutput output = new ArrayOutput();
		output.writePacket(new byte[]{10});
		assertEquals(1, output.size());
		assertArrayEquals(new byte[]{10}, output.toByteArray());

		
		output.writePacket(new byte[]{20});
		assertEquals(2, output.size());
		assertArrayEquals(new byte[]{10, 20}, output.toByteArray());
		
		output.writePacket(new byte[]{30, 40, 50});
		assertEquals(5, output.size());
		assertArrayEquals(new byte[]{10, 20, 30, 40, 50}, output.toByteArray());
	}

	@Test
	public void testWrite(){
		final ArrayOutput output = new ArrayOutput();
		output.write((byte) 10);
		assertEquals(1, output.size());
		assertArrayEquals(new byte[]{10}, output.toByteArray());

		output.write((byte) 20);
		assertEquals(2, output.size());
		assertArrayEquals(new byte[]{10, 20}, output.toByteArray());
		
		output.write((byte) 30);
		output.write((byte) 40);
		output.write((byte) 50);
		assertEquals(5, output.size());
		assertArrayEquals(new byte[]{10, 20, 30, 40, 50}, output.toByteArray());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWriteNullPacket(){
		final ArrayOutput output = new ArrayOutput();
			
		output.writePacket(null);
	}
}
