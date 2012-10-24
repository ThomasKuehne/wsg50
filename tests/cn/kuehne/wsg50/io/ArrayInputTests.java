package cn.kuehne.wsg50.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.EOFException;

import org.junit.Test;

import cn.kuehne.wsg50.io.ArrayInput;
public class ArrayInputTests {
	@Test
	public void testAvailable(){
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		assertEquals(org.length, input.available());
	}
	
	@Test
	public void testSingleRead() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		byte[] buffer = new byte[1];
		input.readBytes(buffer, 0, buffer.length);
		assertEquals(org.length-1, input.available());
		assertEquals(org[0], buffer[0]);
	}
	
	@Test
	public void testMaxRead() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		byte[] buffer = new byte[org.length];
		input.readBytes(buffer, 0, buffer.length);
		assertEquals(0, input.available());
		assertArrayEquals(org, buffer);
	}
	
	@Test
	public void testMultiRead() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		byte[] buffer = new byte[1];
		
		input.readBytes(buffer, 0, buffer.length);
		assertEquals(org.length-1, input.available());
		assertEquals(org[0], buffer[0]);
		
		input.readBytes(buffer, 0, buffer.length);
		assertEquals(org.length-2, input.available());
		assertEquals(org[1], buffer[0]);
	}
	
	@Test
	public void testCloneData() throws Exception{
		byte[] master = new byte[]{1, 2, 3};
		byte[] org = master.clone();
		ArrayInput input = new ArrayInput(org);
		org[0] = 4;
		byte[] buffer = new byte[org.length];
		input.readBytes(buffer, 0, buffer.length);
		assertEquals(0, input.available());
		assertArrayEquals(master, buffer);
	}
	
	@Test(expected = EOFException.class)
	public void testEof() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		byte[] buffer = new byte[org.length+1];
		input.readBytes(buffer, 0, buffer.length);
		assertEquals(0, input.available());
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testSourceNull(){
		new ArrayInput(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBufferNull() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		input.readBytes(null, 0, 3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOffsetNegative() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		input.readBytes(new byte[3], -1, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOffsetExcessive() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		input.readBytes(new byte[3], 3, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLengthExcessive() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		input.readBytes(new byte[3], 0, 4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLengthNegative() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		input.readBytes(new byte[3], 0, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSumExcessive() throws Exception{
		byte[] org = new byte[]{1, 2, 3};
		ArrayInput input = new ArrayInput(org);
		input.readBytes(new byte[3], 2, 2);
	}
}
