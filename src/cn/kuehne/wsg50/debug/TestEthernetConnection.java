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

import java.net.Socket;

import cn.kuehne.wsg50.Packet;
import cn.kuehne.wsg50.PacketCoder;
import cn.kuehne.wsg50.helper.InputFromStream;
import cn.kuehne.wsg50.helper.OutputToStream;
import cn.kuehne.wsg50.packets.GetSystemLimitsCommand;

public class TestEthernetConnection {
	public static void main(String[] args) throws Exception {
		final String host = "wsg50-00419251";
		final int port = 1000;
		final Socket socket = new Socket(host, port);
		try {
			final OutputToStream out = new OutputToStream(socket.getOutputStream());
			try {
				final InputFromStream in = new InputFromStream(socket.getInputStream());
				try {
					final GetSystemLimitsCommand cmd = new GetSystemLimitsCommand();

					final PacketCoder coder = new PacketCoder();

					coder.write(out, cmd);

					Packet reply = coder.readAcknowledge(in, true);
					System.out.println(reply);
				} finally {
					try {
						in.close();
					} catch (Exception e) {
					}
				}
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		} finally {
			try {
				socket.shutdownInput();
			} catch (Exception e) {
			}
			try {
				socket.shutdownOutput();
			} catch (Exception e) {
			}
			try {
				socket.close();
			} catch (Exception e) {
			}
		}

	}
}
