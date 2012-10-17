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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cn.kuehne.wsg50.helper.AbstractPacket;
import cn.kuehne.wsg50.helper.In;
import cn.kuehne.wsg50.helper.InputArray;
import cn.kuehne.wsg50.helper.Out;
import cn.kuehne.wsg50.helper.OutputArray;

public class Wsg50CoderTests {

	public static class Example1 implements Packet {
		byte[] payload = new byte[] { 1, 2, 3 };

		@Override
		public byte getPacketID() {
			return 1;
		}

		@Override
		public byte[] getPayload() {
			return new byte[0];
		}

		@Override
		public void setPayload(final byte[] p) {
			payload = p.clone();
		}
	}

	public static class Example2 extends AbstractPacket {
		public Example2() {
			super((byte) 1);
		}
	}

	public static class Example3 extends AbstractPacket implements Command {
		private int v;

		public Example3() {
			super((byte) 1);
		}

		@Out(0)
		public int get() {
			return v;
		}

		@In(0)
		public void set(int i) {
			v = i;
		}
	}

	@Test
	public void testExample1In() {
		final byte[] input = new byte[] { (byte) 0xAA, (byte) 0xAA,
				(byte) 0xAA, 0x01, 0x00, 0x00, (byte) 0xE8, 0x10 };
		final InputArray in = new InputArray(input);
		final PacketCoder wsg50 = new PacketCoder();
		// Example1 e1 = new Example1();

		final Packet result = wsg50.readDebug(in, Example1.class, true);

		assertTrue(result instanceof Example1);
		Example1 e2 = (Example1) result;

		assertArrayEquals(new byte[] {}, e2.payload);
	}

	@Test
	public void testExample1Out() {
		final byte[] expected = new byte[] { (byte) 0xAA, (byte) 0xAA,
				(byte) 0xAA, 0x01, 0x00, 0x00, (byte) 0xE8, 0x10 };
		final OutputArray out = new OutputArray();
		final Packet packet = new Example1();
		final PacketCoder wsg50 = new PacketCoder();

		wsg50.write(out, packet);

		final byte[] result = out.getBytes();
		assertArrayEquals(expected, result);
	}

	@Test
	public void testExample2In() {
		final byte[] input = new byte[] { (byte) 0xAA, (byte) 0xAA,
				(byte) 0xAA, 0x01, 0x00, 0x00, (byte) 0xE8, 0x10 };
		final InputArray in = new InputArray(input);
		final PacketCoder wsg50 = new PacketCoder();
		Example2 e1 = new Example2();

		final Packet result = wsg50.readDebug(in, Example2.class, true);

		assertTrue(result instanceof Example2);
	}

	@Test
	public void testExample2Out() {
		final byte[] expected = new byte[] { (byte) 0xAA, (byte) 0xAA,
				(byte) 0xAA, 0x01, 0x00, 0x00, (byte) 0xE8, 0x10 };
		final OutputArray out = new OutputArray();
		final Example2 e2 = new Example2();
		final PacketCoder wsg50 = new PacketCoder();

		wsg50.write(out, e2);

		final byte[] result = out.getBytes();
		assertArrayEquals(expected, result);
	}

	@Test
	public void testExample3In() {
		final byte[] input = new byte[] { (byte) 0xAA, (byte) 0xAA,
				(byte) 0xAA, 0x01, 0x04, 0x00, 0x12, 0x34, 0x56, (byte) 0x78,
				0, 0 };
		final InputArray in = new InputArray(input);
		final PacketCoder wsg50 = new PacketCoder();

		final Example3 result = wsg50.readDebug(in, Example3.class, false);
		assertEquals(0x78563412, result.get());
	}
}
