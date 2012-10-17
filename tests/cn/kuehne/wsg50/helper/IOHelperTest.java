package cn.kuehne.wsg50.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cn.kuehne.wsg50.Output;

public class IOHelperTest {
	@Test
	public void testByte() {
		byte a = (byte) 0x12;
		final OutputArray o = new OutputArray();
		o.append(a);

		final byte[] result = o.getBytes();
		assertArrayEquals(new byte[] { a }, result);

		final InputArray i = new InputArray(result);
		assertEquals(a, i.readByte());
	}

	@Test
	public void testBytes() {
		byte a = (byte) 0x12;
		byte b = (byte) 0x34;
		final OutputArray o = new OutputArray();
		o.append(a);
		o.append(b);

		final byte[] result = o.getBytes();
		assertArrayEquals(new byte[] { a, b }, result);

		final InputArray i = new InputArray(result);
		assertEquals(a, i.readByte());
		assertEquals(b, i.readByte());
	}

	@Test
	public void testFloat() {
		float a = 150.0f;
		final OutputArray o = new OutputArray();
		o.append(a);

		final byte[] result = o.getBytes();
		assertArrayEquals(new byte[] { 0x00, 0x00, 0x16, 0x43 }, result);

		final InputArray i = new InputArray(result);
		assertTrue(a == i.readFloat());
	}

	@Test
	public void testFloats() {
		float a = 150.0f;
		float b = -150.0f;
		final OutputArray o = new OutputArray();
		o.append(a);
		o.append(b);

		final byte[] result = o.getBytes();
		assertArrayEquals(new byte[] { 0x00, 0x00, 0x16, 0x43, 0x00, 0x00,
				0x16, (byte) 0xc3 }, result);

		final InputArray i = new InputArray(result);
		assertTrue(a == i.readFloat());
		assertTrue(b == i.readFloat());
	}

	@Test
	public void testInt() {
		int a = 0x01020304;
		final OutputArray o = new OutputArray();
		o.append(a);

		final byte[] result = o.getBytes();
		assertArrayEquals(new byte[] { 0x04, 0x03, 0x02, 0x01 }, result);

		final InputArray i = new InputArray(result);
		assertEquals(a, i.readInt());

	}

	@Test
	public void testInts() {
		int a = 0x01020304;
		int b = 0x05060708;
		final OutputArray o = new OutputArray();
		o.append(a);
		o.append(b);

		final byte[] result = o.getBytes();
		assertArrayEquals(new byte[] { 0x04, 0x03, 0x02, 0x01, 0x08, 0x07,
				0x06, 0x05 }, result);

		final InputArray i = new InputArray(result);
		assertEquals(a, i.readInt());
		assertEquals(b, i.readInt());
	}

	@Test
	public void testOutput() {
		final OutputArray array = new OutputArray();
		final Output out = array;
		out.writeByte((byte) 0x12);
		out.writeByte((byte) 0x34);

		final byte[] result = array.getBytes();
		assertArrayEquals(new byte[] { 0x12, 0x34 }, result);
	}

	@Test
	public void testShort() {
		final short a = (short) 0x1234;
		final OutputArray o = new OutputArray();
		o.append(a);

		final byte[] result = o.getBytes();
		assertArrayEquals(new byte[] { 0x34, 0x12 }, result);

		final InputArray i = new InputArray(result);
		assertEquals(a, i.readShort());
	}

	@Test
	public void testShorts() {
		short a = (short) 0x1234;
		short b = (short) 0x5678;
		final OutputArray o = new OutputArray();
		o.append(a);
		o.append(b);

		final byte[] result = o.getBytes();
		assertArrayEquals(new byte[] { 0x34, 0x12, 0x78, 0x56 }, result);

		final InputArray i = new InputArray(result);
		assertEquals(a, i.readShort());
		assertEquals(b, i.readShort());
	}
}
