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
package cn.kuehne.wsg50.packets;

import cn.kuehne.wsg50.PacketID;
import cn.kuehne.wsg50.TodoException;
import cn.kuehne.wsg50.helper.AbstractCommand;
import cn.kuehne.wsg50.helper.In;
import cn.kuehne.wsg50.helper.Out;

public class PrePositionFingersCommand extends AbstractCommand {
	private byte flags;

	private float speed;

	private float width;

	public PrePositionFingersCommand() {
		super(PacketID.PrePositionFingers);
		setSpeed(0);
		setWidth(0);
	}

	@Out(0)
	public byte getFlags() {
		return flags;
	}

	@Out(2)
	public float getSpeed() {
		return speed;
	}

	@Out(1)
	public float getWidth() {
		return width;
	}

	public boolean isClamp() {
		throw new TodoException();
	}

	public boolean isRelative() {
		throw new TodoException();
	}

	public void setClamp(boolean b) {
		throw new TodoException();
	}

	@In(0)
	public void setFlags(byte newFlags) {
		flags = newFlags;
	}

	public void setRelative(boolean b) {
		throw new TodoException();
	}

	@In(2)
	public void setSpeed(float newSpeed) {
		speed = newSpeed;
	}

	@In(1)
	public void setWidth(float newWidth) {
		width = newWidth;
	}
}
