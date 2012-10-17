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
import cn.kuehne.wsg50.helper.PayloadHandlerAcknowledge;
import cn.kuehne.wsg50.helper.PayloadHandlerCommand;
import cn.kuehne.wsg50.helper.PayloadHandlerDebug;

public class PacketCoder {

	public synchronized void read(final Input input,
			final PayloadHandler payloadHandler, final boolean validateCRC) {
		if (input == null) {
			throw new IllegalArgumentException("output is null");
		}

		final InputHelper in = new InputHelper(input);
		in.markPacketStart();
		for (int i = 0; i < 3; i++) {
			final byte tmpByte = in.readByte();
			if (tmpByte != (byte) 0xAA) {
				throw new IllegalArgumentException("bad praeamble byte " + i
						+ ": 0x" + Integer.toHexString(0xFF & tmpByte));
			}
		}
		final byte id = in.readByte();
		final int size = 0xFFFF & in.readShort();
		final byte[] payload = new byte[size];
		for (int i = 0; i < payload.length; i++) {
			payload[i] = in.readByte();
		}

		final short crc = in.readShort();

		final boolean hasValidCRC;
		if (validateCRC) {
			hasValidCRC = (0 == in.getCRC());
		} else {
			hasValidCRC = (crc == 0);
		}

		payloadHandler.handlePayload(id, payload, hasValidCRC);
	}

	public Acknowledge readAcknowledge(final Input input,
			final boolean validateCRC) {
		final PayloadHandlerAcknowledge payloadHandler = new PayloadHandlerAcknowledge();
		read(input, payloadHandler, validateCRC);
		return payloadHandler.getLastAcknowledge();
	}

	public Command readCommand(final Input input, final boolean validateCRC) {
		final PayloadHandlerCommand payloadHandler = new PayloadHandlerCommand();
		read(input, payloadHandler, validateCRC);
		return payloadHandler.getLastCommand();
	}

	@SuppressWarnings("unchecked")
	public <T extends Packet> T readDebug(final Input input, Class<T> imp,
			final boolean validateCRC) {
		final PayloadHandlerDebug payloadHandler = new PayloadHandlerDebug(imp);
		read(input, payloadHandler, validateCRC);
		return (T) payloadHandler.getLastPacket();
	}

	public synchronized void write(final Output output, final Packet packet) {
		if (output == null) {
			throw new IllegalArgumentException("output is null");
		}
		if (packet == null) {
			throw new IllegalArgumentException("packet is null");
		}

		final byte id = packet.getPacketID();
		final byte[] payload = packet.getPayload();
		if (payload != null && payload.length > 0xFFFF) {
			throw new IllegalArgumentException("excessive payload length: "
					+ payload.length);
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
