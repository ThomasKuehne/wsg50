package cn.kuehne.wsg50.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CRC16Tests {

	@Test
	public void testDocumentedPackages() {
		final byte[][] input = new byte[][] { { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x01, 0x00, 0x00 },
				{ (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x01, 0x00, 0x00, (byte) 0xE8, 0x10 },
				{ (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x01, 0x02, 0x00, 0x12, 0x34 },
				{ (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x01, 0x02, 0x00, 0x12, 0x34, 0x6D, 0x66 },
				{ (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x20, 0x02, 0x00, 0x00, 0x00 },
				{ (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x20, 0x02, 0x00, 0x00, 0x00, (byte) 0xB3, (byte) 0xFD },
				{ (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x90, 0x02, 0x00, 0x0E, 0x00 },
				{ (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x90, 0x02, 0x00, 0x0E, 0x00, (byte) 0xFD, 0x02 }, };

		final short[] output = new short[] { 0x10E8, 0x0, 0x666D, 0x0, (short) 0xFDB3, 0x0, 0x02FD, 0x0 };

		assertEquals(input.length, output.length);

		for (int testIndex = 0; testIndex < output.length; testIndex++) {
			final CRC16 crc = new CRC16();
			final byte[] data = input[testIndex];
			for (final byte b : data) {
				crc.update(b);
			}
			assertEquals(output[testIndex], crc.getValue());
		}
	}

	@Test
	public void testInit() {
		final CRC16 crc = new CRC16();
		assertEquals((short) 0xFFFF, crc.getValue());
	}
}