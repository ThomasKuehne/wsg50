/*
 * Copyright (c) 2012, Thomas Kuehne <thomas@kuehne.cn>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cn.kuehne.wsg50.helper;

import cn.kuehne.wsg50.BugException;
import cn.kuehne.wsg50.PacketBuilder;

public class PacketBuilderImpl implements PacketBuilder {
	private static final int BUFFER_BLOCK_SIZE = 256;
	private static final int HEADER_SIZE = 3 + 1 + 2;
	private byte[] bytes;
	private int nextByteIndex;

	public PacketBuilderImpl(final byte id) {
		bytes = new byte[BUFFER_BLOCK_SIZE];
		nextByteIndex = 0;

		init(id);
	}

	@Override
	public void append(final Object o) {
		if (o == null) {
			throw new IllegalArgumentException("argument is null");
		}

		final Class<?> c = o.getClass();

		if (Byte.class.isAssignableFrom(c)) {
			appendByte((Byte) o);
		} else if (Short.class.isAssignableFrom(c)) {
			appendShort((Short) o);
		} else if (Integer.class.isAssignableFrom(c)) {
			appendInt((Integer) o);
		} else if (Float.class.isAssignableFrom(c)) {
			appendFloat((Float) o);
		} else if (CharSequence.class.isAssignableFrom(c)) {
			appendText((CharSequence) o);
		} else if (byte[].class.isAssignableFrom(c)) {
			appendBytes((byte[]) o);
		} else {
			throw new BugException("not handled: " + c + " " + o);
		}
	}

	@Override
	public final void appendByte(final byte value) {
		ensureCapacity();
		bytes[nextByteIndex++] = value;
	}

	@Override
	public final void appendBytes(final byte[] values) {
		for (final byte value : values) {
			appendByte(value);
		}
	}

	@Override
	public void appendFloat(final float f) {
		int i = Float.floatToRawIntBits(f);
		appendInt(i);
	}

	@Override
	public final void appendInt(final int value) {
		appendByte((byte) (0xFF & value));
		appendByte((byte) (0xFF & (value >> 8)));
		appendByte((byte) (0xFF & (value >> 16)));
		appendByte((byte) (0xFF & (value >> 24)));
	}

	@Override
	public final void appendShort(final short value) {
		appendByte((byte) (0xFF & value));
		appendByte((byte) (0xFF & (value >> 8)));
	}

	@Override
	public void appendText(final CharSequence cs) {
		if (cs == null) {
			throw new IllegalArgumentException("sequence is null");
		}
		for (int index = 0; index < cs.length(); index++) {
			final char c = cs.charAt(index);
			final byte b = (byte) (0xFF & c);
			if ((0xFFFF & c) != (0xFF & b)) {
				throw new IllegalArgumentException("illegal character \"" + c + "\" ("
						+ Integer.toHexString(0xFFFF & c) + ") at position " + index);
			}

			appendByte((byte) (0xFF & b));
		}
	}

	private short calculateCRC() {
		final CRC16 crc = new CRC16();
		for (int i = 0; i < nextByteIndex; i++) {
			crc.update(bytes[i]);
		}
		return crc.getValue();
	}

	private final void ensureCapacity() {
		while (nextByteIndex >= bytes.length) {
			final byte[] tmp = new byte[bytes.length + BUFFER_BLOCK_SIZE];
			System.arraycopy(bytes, 0, tmp, 0, bytes.length);
			bytes = tmp;
		}
	}

	public byte[] getEncoded() {
		updatePayloadSize();
		final short crc = calculateCRC();
		appendShort(crc);

		final byte[] encoded = new byte[nextByteIndex];
		System.arraycopy(bytes, 0, encoded, 0, encoded.length);

		nextByteIndex -= 2; // "remove" CRC value

		return encoded;
	}

	public byte[] getPayload() {
		final byte[] payload = new byte[nextByteIndex - HEADER_SIZE];
		System.arraycopy(bytes, HEADER_SIZE, payload, 0, payload.length);
		return payload;
	}

	private void init(final byte id) {
		// praeamble
		appendByte((byte) 0xAA);
		appendByte((byte) 0xAA);
		appendByte((byte) 0xAA);

		// dummy id
		appendByte(id);

		// dummy size
		appendShort((short) 0);
	}

	private void updatePayloadSize() {
		final int size = nextByteIndex - 3 - 1 - 2;
		if (size > 0xFFFF) {
			throw new IllegalArgumentException("excessive payload size: " + size);
		}
		if (size < 0) {
			throw new BugException("negative payload size: " + size);
		}
		bytes[4] = (byte) (0xFF & size);
		bytes[5] = (byte) (0xFF & (size >> 8));
	}
}
