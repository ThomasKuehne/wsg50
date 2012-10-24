package cn.kuehne.wsg50.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

import org.junit.Test;

import cn.kuehne.wsg50.io.InputFromStream;

public class InputFromStreamTests {

	@Test
	public void testSimple() throws EOFException{
		byte[] master = new byte[]{1, 2, 3};
		final ByteArrayInputStream source = new ByteArrayInputStream(master);
		final InputFromStream input = new InputFromStream(source);
		
		byte[] buffer = new byte[master.length];
		input.readBytes(buffer, 0, 1);
		assertEquals(master[0], buffer[0]);
		assertEquals(0, buffer[1]);
		
		input.readBytes(buffer, 1, 2);
		assertArrayEquals(master, buffer);
		
		input.close();
	}
	
	@Test
	public void testAll() throws EOFException{
		byte[] master = new byte[]{1, 2, 3};
		final ByteArrayInputStream source = new ByteArrayInputStream(master);
		final InputFromStream input = new InputFromStream(source);
		
		byte[] buffer = new byte[master.length];
		input.readBytes(buffer, 0, 3);
		assertArrayEquals(master, buffer);
	
		input.close();
	}
	
	@Test(expected = EOFException.class)
	public void testEOFAll() throws EOFException{
		byte[] master = new byte[]{1, 2, 3};
		final ByteArrayInputStream source = new ByteArrayInputStream(master);
		final InputFromStream input = new InputFromStream(source);
		
		byte[] buffer = new byte[master.length];
		input.readBytes(buffer, 0, 3);
		input.readBytes(buffer, 0, 1);
	}
	
	@Test(expected = EOFException.class)
	public void testEOFPartial() throws EOFException{
		byte[] master = new byte[]{1, 2, 3};
		final ByteArrayInputStream source = new ByteArrayInputStream(master);
		final InputFromStream input = new InputFromStream(source);
		
		byte[] buffer = new byte[master.length+1];
		input.readBytes(buffer, 0, buffer.length);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNull() throws EOFException{
		new InputFromStream(null);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testBufferNull() throws Exception{
		byte[] master = new byte[]{1, 2, 3};
		final InputFromStream input = new InputFromStream(new ByteArrayInputStream(master));
		
		input.readBytes(null, 0, 3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOffsetNegative() throws Exception{
		byte[] master = new byte[]{1, 2, 3};
		final InputFromStream input = new InputFromStream(new ByteArrayInputStream(master));
		
		input.readBytes(new byte[3], -1, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOffsetExcessive() throws Exception{
		byte[] master = new byte[]{1, 2, 3};
		final InputFromStream input = new InputFromStream(new ByteArrayInputStream(master));
		
		input.readBytes(new byte[3], 3, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLengthExcessive() throws Exception{
		byte[] master = new byte[]{1, 2, 3};
		final InputFromStream input = new InputFromStream(new ByteArrayInputStream(master));
		
		input.readBytes(new byte[3], 0, 4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLengthNegative() throws Exception{
		byte[] master = new byte[]{1, 2, 3};
		final InputFromStream input = new InputFromStream(new ByteArrayInputStream(master));
		
		input.readBytes(new byte[3], 0, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSumExcessive() throws Exception{
		byte[] master = new byte[]{1, 2, 3};
		final InputFromStream input = new InputFromStream(new ByteArrayInputStream(master));
		
		input.readBytes(new byte[3], 2, 2);
	}

}
