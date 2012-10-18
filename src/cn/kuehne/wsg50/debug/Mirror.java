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

package cn.kuehne.wsg50.debug;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cn.kuehne.wsg50.Acknowledge;
import cn.kuehne.wsg50.Command;
import cn.kuehne.wsg50.E;
import cn.kuehne.wsg50.PacketCoder;
import cn.kuehne.wsg50.PacketID;
import cn.kuehne.wsg50.PayloadHandler;
import cn.kuehne.wsg50.helper.InputFromStream;
import cn.kuehne.wsg50.helper.OutputToStream;
import cn.kuehne.wsg50.helper.PayloadHandlerCommand;
import cn.kuehne.wsg50.packets.GetGraspingStateAcknowledge;
import cn.kuehne.wsg50.packets.GetGraspingStateAcknowledge.GraspingState;
import cn.kuehne.wsg50.packets.GetOpeningWidthAcknowledge;
import cn.kuehne.wsg50.packets.GetSystemLimitsAcknowledge;
import cn.kuehne.wsg50.packets.GetSystemStateAcknowledge;

public class Mirror implements PayloadHandler, Runnable {
	private static class FeatureNotSupportedPacket implements Acknowledge {
		private short code = E.FEATURNOT_SUPPORTED.getCode();
		private byte id;

		public FeatureNotSupportedPacket(byte id) {
			this.id = id;
		}

		@Override
		public byte getPacketID() {
			return id;
		}

		@Override
		public byte[] getPayload() {
			return new byte[] { (byte) (code & 0xFF), (byte) ((code >> 8) & 0xFF) };
		}

		@Override
		public short getStatusCode() {
			return code;
		}

		@Override
		public void setPayload(byte[] p) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStatusCode(short status) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			return "! " + getClass().getSimpleName();
		}
	}

	public static void main(String[] args) throws IOException {
		new Mirror().run();
	}

	private Command command;
	private PayloadHandlerCommand commandHandler = new PayloadHandlerCommand();

	private byte lastRawId;

	private Acknowledge getAcknowledge(byte id, Command command) {

		if (command == null) {
			return new FeatureNotSupportedPacket(id);
		} else if (command.getPacketID() == PacketID.GetSystemLimits.getId()) {
			GetSystemLimitsAcknowledge systemLimits = new GetSystemLimitsAcknowledge();
			systemLimits.setMaxAcc(10);
			systemLimits.setMinAcc(1);

			systemLimits.setOvrForce(40);
			systemLimits.setMaxForce(30);
			systemLimits.setMinForce(11);
			systemLimits.setMaxSpeed(400);
			systemLimits.setMinSpeed(100);
			systemLimits.setStroke(10);
			systemLimits.setStatusCode(E.SUCCESS.getCode());
			return systemLimits;
		} else if (command.getPacketID() == PacketID.GetOpeningWidth.getId()) {
			final GetOpeningWidthAcknowledge back = new GetOpeningWidthAcknowledge();
			back.setWidth(10);
			return back;
		} else if (command.getPacketID() == PacketID.GetGraspingState.getId()) {
			final GetGraspingStateAcknowledge back = new GetGraspingStateAcknowledge();
			back.setState(GraspingState.Idle);
			return back;
		} else if (command.getPacketID() == PacketID.GetSystemState.getId()) {
			final GetSystemStateAcknowledge back = new GetSystemStateAcknowledge();
			return back;
		} else {
			return new FeatureNotSupportedPacket(id);
		}
	}

	@Override
	public void handlePayload(byte rawId, byte[] payload, boolean validCRC) {
		final PacketID id = PacketID.lookup(rawId);
		System.out.println(id + " (" + rawId + " 0x" + Integer.toHexString(0xFF & rawId) + ") "
				+ (validCRC ? "goodCRC" : "badCRC"));
		try {
			commandHandler.handlePayload(rawId, payload, validCRC);
			command = commandHandler.getLastCommand();
			System.out.println("\t" + command);
		} catch (Exception e) {
			command = null;
			System.out.println("\tIN " + e);
		}
		lastRawId = rawId;
	}

	@Override
	public void run() {
		final PacketCoder pc = new PacketCoder();
		int startPort = 1000;
		ServerSocket server = null;
		Exception lastE = null;
		for (int port = 0; port < 200; port++) {
			try {
				server = new ServerSocket(port + startPort);
				break;
			} catch (Exception e) {
				lastE = e;
			}
		}
		if (server == null) {
			lastE.printStackTrace();
			return;
		}

		try {
			System.out.println("listening on " + server.getLocalSocketAddress());

			while (true) {
				try {
					final Socket client = server.accept();
					try {
						final InputFromStream in = new InputFromStream(client.getInputStream());
						final OutputToStream out = new OutputToStream(client.getOutputStream());
						try {
							while (true) {
								pc.read(in, this, true);

								Acknowledge a = getAcknowledge(lastRawId, command);
								System.out.println("\t" + a);
								pc.write(out, a);
							}
						} finally {
							in.close();
							out.close();
						}
					} finally {
						try {
							client.shutdownInput();
						} catch (Exception dummy) {
						}
						try {
							client.shutdownOutput();
						} catch (Exception dummy) {
						}
						try {
							client.close();
						} catch (Exception dummy) {
						}
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		} finally {
			try {
				server.close();
			} catch (Exception dummy) {
			}
		}
	}

	public void send(byte id, E e) {

	}

}
