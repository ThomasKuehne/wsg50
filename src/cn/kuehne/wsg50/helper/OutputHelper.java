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
import cn.kuehne.wsg50.Output;

public class OutputHelper implements Output {
	private final CRC16 crc;
	private final Output out;

	public OutputHelper(final Output o) {
		if (o == null) {
			throw new IllegalArgumentException("output is null");
		}
		out = o;
		crc = new CRC16();
	}

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

	public void appendByte(final byte b) {
		writeByte(b);
	}

	public void appendBytes(final byte[] bytes) {
		for (final byte b : bytes) {
			appendByte(b);
		}
	}

	public void appendFloat(final float f) {
		int i = Float.floatToRawIntBits(f);
		appendInt(i);
	}

	public void appendInt(final int i) {
		writeByte((byte) (i & 0xFF));
		writeByte((byte) ((i >> 8) & 0xFF));
		writeByte((byte) ((i >> 16) & 0xFF));
		writeByte((byte) ((i >> 24) & 0xFF));
	}

	public void appendShort(final short s) {
		writeByte((byte) (s & 0xFF));
		writeByte((byte) ((s >> 8) & 0xFF));
	}

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

	public short getCRC() {
		return crc.getValue();
	}

	public Output getOutput() {
		return out;
	}

	@Override
	public void writeByte(byte b) {
		out.writeByte(b);
		crc.update(b);
	}
}