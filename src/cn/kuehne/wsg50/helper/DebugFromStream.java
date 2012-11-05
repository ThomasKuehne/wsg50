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

import java.io.InputStream;
import java.io.PrintStream;

public class DebugFromStream extends InputFromStream {
	private final PrintStream debugOut;
	private boolean firstByte;
	private boolean firstPacket;

	public DebugFromStream(InputStream s, PrintStream debug) {
		super(s);
		if (debug == null) {
			throw new IllegalArgumentException("'debug' is null");
		}

		debugOut = debug;
		firstPacket = true;
	}

	@Override
	public void close() {
		super.close();
		if (!firstPacket) {
			debugOut.println("}");
		}
	}

	@Override
	public void markPacketStart() {
		if (firstPacket) {
			firstPacket = false;
		} else {
			debugOut.println("}");
		}
		debugOut.print("{");
		firstByte = true;
	}

	@Override
	public byte readByte() {
		final byte b = super.readByte();
		if (firstByte) {
			firstByte = false;
		} else {
			debugOut.print(", ");
		}
		if (b < 0) {
			debugOut.print("(byte)");
		}
		debugOut.printf("0x%02X", 0xFF & b);
		return b;
	}

}
