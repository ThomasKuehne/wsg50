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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import cn.kuehne.wsg50.Acknowledge;
import cn.kuehne.wsg50.Command;
import cn.kuehne.wsg50.E;

public class CommandBridge{
	private final Command command;
	private int nextReplyIndex;
	private List<Acknowledge> replies;
	private Semaphore semaphore;

	public CommandBridge(final Command command) {
		if(command == null){
			throw new IllegalArgumentException("command is null");
		}
		this.command = command;
		semaphore = new Semaphore(0);
	}

	void addAcknowledge(Acknowledge acknowledge) {
		if (acknowledge == null) {
			throw new IllegalArgumentException("'acknowledge' is null");
		}
		if (acknowledge.getPacketID() != command.getPacketID()) {
			throw new IllegalArgumentException("'acknowledge' has bad packet ID: " + acknowledge.getPacketID()
					+ " instead of " + command.getPacketID());
		}
		replies.add(acknowledge);
		semaphore.release(1);
	}

	public final Command getCommand() {
		return command;
	}

	public final Acknowledge getLatestAcknowledge(){
		if (replies == null || replies.size() < 1){
			return null;
		}
		return replies.get(replies.size()-1);
	}

	public final boolean isError() {
		final Acknowledge last = getLatestAcknowledge();
		if(last != null){
			final short status = last.getStatusCode();
			return !(E.SUCCESS.getCode() == status || E.CMD_PENDING.getCode() == status);
		}
		return false;
	}

	public boolean isFinished() {
		final Acknowledge last = getLatestAcknowledge();
		if(last != null){
			final short status = last.getStatusCode();
			return E.SUCCESS.getCode() == status;
		}
		return false;
	}

	public final boolean isStarted() {
		return replies != null;
	}

	public final boolean isTouched() {
		return command != null;
	}

	void outgoing() {
		replies = new ArrayList<Acknowledge>();
	}

	@Override
	public String toString(){
		return command.toString();
	}

	public Acknowledge waitForNextAcknowledge() {
		while (true) {
			try {
				semaphore.acquire();
				return replies.get(nextReplyIndex++);
			} catch (InterruptedException e) {
				// noop
			}
		}
	}
}
