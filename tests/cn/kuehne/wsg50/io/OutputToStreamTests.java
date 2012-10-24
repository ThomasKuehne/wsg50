package cn.kuehne.wsg50.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

public class OutputToStreamTests {

	@Test(expected = IllegalArgumentException.class)
	public void testNull(){
		new OutputToStream(null);
	}
	
	@Test
	public void testEmpty(){
		ByteArrayOutputStream store = new ByteArrayOutputStream();
		new OutputToStream(store);
		final byte[] result = store.toByteArray();
		assertArrayEquals(new byte[0], result);
	}
	
	@Test
	public void testWritePacket(){
		ByteArrayOutputStream store = new ByteArrayOutputStream();
		OutputToStream output = new OutputToStream(store);
		
		output.writePacket(new byte[]{10});
		assertEquals(1, store.size());
		assertArrayEquals(new byte[]{10}, store.toByteArray());

		
		output.writePacket(new byte[]{20});
		assertEquals(2, store.size());
		assertArrayEquals(new byte[]{10, 20}, store.toByteArray());
		
		output.writePacket(new byte[]{30, 40, 50});
		assertEquals(5, store.size());
		assertArrayEquals(new byte[]{10, 20, 30, 40, 50}, store.toByteArray());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWriteNullPacket(){
		ByteArrayOutputStream store = new ByteArrayOutputStream();
		OutputToStream output = new OutputToStream(store);
		
		output.writePacket(null);
	}

}
