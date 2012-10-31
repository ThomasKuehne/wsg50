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

package cn.kuehne.wsg50.roboter2012;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.kuehne.wsg50.Acknowledge;
import cn.kuehne.wsg50.BugException;
import cn.kuehne.wsg50.E;
import cn.kuehne.wsg50.Input;
import cn.kuehne.wsg50.Output;
import cn.kuehne.wsg50.PacketCoder;
import cn.kuehne.wsg50.helper.InputFromStream;
import cn.kuehne.wsg50.helper.OutputToStream;

public class CommunicatorBridge implements Closeable {
	private final PacketCoder coder;
	private final List<CommandBridge> inFlight;

	private Input input;
	private Output output;
	private URI uri;

	public CommunicatorBridge() {
		coder = new PacketCoder();
		inFlight = new LinkedList<CommandBridge>();
		try {
			setURI("tcp://wsg50-00419251:1000");
		} catch (Exception e) {
			throw new BugException(e);
		}
	}

	@Override
	public final void close() {
		disconnect();
	}

	public void connect() throws IOException {
		if (uri == null) {
			throw new IllegalArgumentException("'uri' is null");
		}

		final String scheme = uri.getScheme();
		if ("tcp".equals(scheme)) {
			connectTCP(uri.getHost(), uri.getPort());
		} else {
			throw new UnsupportedOperationException("unhandled scheme: " + scheme);
		}
	}

	@SuppressWarnings("resource")
	private void connectTCP(final String host, int port) throws IOException {
		if (host == null) {
			throw new IllegalArgumentException("'host' is null");
		}
		if (port == -1) {
			port = 1000;
		}

		IOException last = null;

		for (final InetAddress address : InetAddress.getAllByName(host)) {
			Socket socket = null;
			Input in = null;
			Output out = null;
			try {
				socket = new Socket(address, port);
				out = new OutputToStream(socket.getOutputStream());
				in = new InputFromStream(socket.getInputStream());
			} catch (IOException ioe) {
				last = ioe;

				try {
					out.close();
				} catch (Exception e) {
				}
				try {
					socket.close();
				} catch (Exception e) {
				}
				continue;
			}

			setIO(in, out);
			return;
		}

		throw last;
	}

	public void disconnect() {
		try {
			output.close();
		} catch (Exception e) {
		}
		output = null;

		try {
			input.close();
		} catch (Exception e) {
		}
		input = null;

		try {
			inFlight.clear();
		} catch (Exception e) {
		}
	}

	public final boolean isConnected() {
		return (input != null) || (output != null);
	}

	public final Acknowledge readAcknowledge() {
		if (input == null) {
			throw new IllegalStateException("'input' is null");
		}

		final Acknowledge ack;
		synchronized (input) {
			ack = coder.readAcknowledge(input, true);
		}

		synchronized (inFlight) {
			Iterator<CommandBridge> i = inFlight.iterator();
			while (i.hasNext()) {
				CommandBridge cmd = i.next();
				if (ack.getPacketID() == cmd.getPacketID()) {
					cmd.addAcknowledge(ack);
					if (ack.getStatusCode() != E.CMD_PENDING.getCode()) {
						i.remove();
						break;
					}
				}
			}
		}
		return ack;
	}

	public final void sendCommand(final CommandBridge command) {
		if (command == null) {
			throw new IllegalArgumentException("'command' is null");
		}
		if (output == null) {
			throw new IllegalStateException("'output' is null");
		}

		synchronized (output) {
			synchronized (inFlight) {
				coder.write(output, command);
				command.outgoing();
				inFlight.add(command);
			}
		}
	}

	final void setIO(Input newInput, Output newOutput) {
		if (newInput == null) {
			throw new IllegalStateException("'newInput' is null");
		}
		if (newOutput == null) {
			throw new IllegalStateException("'newOutput' is null");
		}
		if (input != null) {
			throw new IllegalStateException("'input' is already open");
		}
		if (output != null) {
			throw new IllegalStateException("'output' is already open");
		}
		input = newInput;
		output = newOutput;
	}

	public final void setURI(final String newURI) throws URISyntaxException {
		if (newURI == null) {
			throw new IllegalArgumentException("'newURI' is null");
		}
		if (newURI.length() < 1) {
			throw new IllegalArgumentException("'newURI' is empty");
		}
		setURI(new URI(newURI));
	}

	public void setURI(final URI newURI) throws URISyntaxException {
		if (newURI == null) {
			throw new IllegalArgumentException("'newURI' is null");
		}
		if (input != null) {
			throw new IllegalStateException("'input' is already open");
		}
		uri = newURI;
	}
}
