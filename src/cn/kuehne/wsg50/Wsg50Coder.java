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
package cn.kuehne.wsg50;

import cn.kuehne.wsg50.helper.InputHelper;
import cn.kuehne.wsg50.helper.OutputHelper;

public class Wsg50Coder {

	public Packet read(final Input input, boolean isCommand, boolean validateCRC) {
		return read(input, isCommand, null, validateCRC);
	}

	private Packet read(final Input input, final boolean isCommand, Packet packet, final boolean validateCRC) {
		if (input == null) {
			throw new IllegalArgumentException("output is null");
		}

		final InputHelper in = new InputHelper(input);
		in.markPacketStart();
		for (int i = 0; i < 3; i++) {
			final byte tmpByte = in.readByte();
			if (tmpByte != (byte) 0xAA) {
				throw new IllegalArgumentException("bad praeamble byte " + i + ": 0x"
						+ Integer.toHexString(0xFF & tmpByte));
			}
		}
		final byte id = in.readByte();
		final int size = 0xFFFF & in.readShort();
		final byte[] payload = new byte[size];
		for (int i = 0; i < payload.length; i++) {
			payload[i] = in.readByte();
		}

		final short crc = in.readShort();

		if (validateCRC) {
			if (0 != in.getCRC()) {
				throw new IllegalArgumentException("crc failed, expected 0 got "
						+ Integer.toHexString(0xFFFF & in.getCRC()));
			}
		} else if (crc != 0) {
			throw new IllegalArgumentException("expected 0x0000 crc data got " + Integer.toHexString(0xFFFF & crc));
		}

		if (packet == null) {
			PacketID pID = PacketID.lookup(id);
			if (pID == null) {
				throw new BugException("unknown packet ID: " + Integer.toHexString(0xFF & id));
			}
			if (isCommand) {
				packet = pID.getCommand();
			} else {
				packet = pID.getAcknowledge();
			}
		}
		System.out.println();
		packet.setPayload(payload);

		return packet;
	}

	public Packet read(final Input input, Packet packet, boolean validateCRC) {
		return read(input, false, packet, validateCRC);
	}

	public void write(final Output output, final Packet packet) {
		if (output == null) {
			throw new IllegalArgumentException("output is null");
		}
		if (packet == null) {
			throw new IllegalArgumentException("packet is null");
		}

		final byte id = packet.getPacketID();
		final byte[] payload = packet.getPayload();
		if (payload != null && payload.length > 0xFFFF) {
			throw new IllegalArgumentException("excessive payload length: " + payload.length);
		}

		final OutputHelper out = new OutputHelper(output);

		// praeamble
		out.appendByte((byte) 0xAA);
		out.appendByte((byte) 0xAA);
		out.appendByte((byte) 0xAA);

		// id
		out.writeByte(id);

		// payload
		if (payload == null) {
			out.appendShort((short) 0);
		} else {
			out.appendShort((short) payload.length);
			out.appendBytes(payload);
		}

		out.appendShort(out.getCRC());
	}
}
