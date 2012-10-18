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

import java.io.ByteArrayOutputStream;

import cn.kuehne.wsg50.BugException;
import cn.kuehne.wsg50.Input;

public class InputHelper implements Input {
	private final CRC16 crc;
	private final Input in;

	public InputHelper(Input i) {
		if (i == null) {
			throw new IllegalArgumentException("input is null");
		}
		in = i;
		crc = new CRC16();
	}

	public short getCRC() {
		return crc.getValue();
	}

	public Input getInput() {
		return in;
	}

	@Override
	public void markPacketStart() {
		in.markPacketStart();
	}

	public Object read(final Class<?> c) {
		if (c == null) {
			throw new IllegalArgumentException("argument is null");
		}

		if (Byte.class.isAssignableFrom(c) || byte.class.isAssignableFrom(c)) {
			return readByte();
		} else if (Short.class.isAssignableFrom(c) || short.class.isAssignableFrom(c)) {
			return readShort();
		} else if (Integer.class.isAssignableFrom(c) || int.class.isAssignableFrom(c)) {
			return readInt();
		} else if (Float.class.isAssignableFrom(c) || float.class.isAssignableFrom(c)) {
			return readFloat();
		} else if (byte[].class.isAssignableFrom(c)) {
			return readBytes();
		} else {
			throw new BugException("not handled: " + c);
		}
	}

	@Override
	public byte readByte() {
		final byte b = in.readByte();
		crc.update(b);
		return b;
	}

	public byte[] readBytes() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		while (true) {
			try {
				out.write(readByte());
			} catch (Exception e) {
				return out.toByteArray();
			}
		}
	}

	public float readFloat() {
		final int a = readInt();
		return Float.intBitsToFloat(a);
	}

	public int readInt() {
		final byte a = readByte();
		final byte b = readByte();
		final byte c = readByte();
		final byte d = readByte();

		return (0xFF000000 & (d << 24)) | (0xFF0000 & (c << 16)) | (0xFF00 & (b << 8)) | (0xFF & a);
	}

	public short readShort() {
		final byte a = readByte();
		final byte b = readByte();

		return (short) ((0xFF00 & (b << 8)) | (0xFF & a));
	}
}
