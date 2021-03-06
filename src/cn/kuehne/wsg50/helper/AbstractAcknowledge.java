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

import cn.kuehne.wsg50.Acknowledge;
import cn.kuehne.wsg50.E;
import cn.kuehne.wsg50.PacketBuilder;
import cn.kuehne.wsg50.PacketID;

public class AbstractAcknowledge extends AbstractPacket implements Acknowledge {
	private short statusCode;

	protected AbstractAcknowledge(PacketID pID) {
		super(pID);
	}

	@Override
	public short getStatusCode() {
		return statusCode;
	}

	@Override
	public void setPayload(final byte[] payload) {
		if (payload.length == 0) {
			return;
		}
		final InputArray in = new InputArray(payload);
		final short s = in.readShort();
		setStatusCode(s);

		if (s == 0) {
			super.setPayload(in);
		} else {
			// TODO check end of INPUT
		}
	}

	@Override
	public void setStatusCode(short status) {
		statusCode = status;
	}

	@Override
	void toStringValues(final StringBuilder builder){
		final short status = getStatusCode();
		builder.append(' ');
		builder.append("Status:");
		E e = E.lookup(status);
		if(e == null){
			builder.append(status);
		}else{
			builder.append(e);
		}
		
		if (E.SUCCESS.getCode() == status) {
			super.toStringValues(builder);
		}
	}
	
	@Override
	public void writePayload(PacketBuilder builder) {
		builder.appendShort(getStatusCode());
		if (E.SUCCESS.getCode() == getStatusCode()) {
			super.writePayload(builder);
		}
	}
}
