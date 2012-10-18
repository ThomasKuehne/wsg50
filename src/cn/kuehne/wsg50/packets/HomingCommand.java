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
import cn.kuehne.wsg50.helper.AbstractCommand;
import cn.kuehne.wsg50.helper.In;
import cn.kuehne.wsg50.helper.Out;

public class HomingCommand extends AbstractCommand {

	public static enum Direction {
		/** */
		DEFAULT((byte) 0),
		/** */
		NEGATIVE((byte) 2),
		/** */
		POSITIVE((byte) 1);

		public static Direction lookup(final byte code) {
			for (final Direction d : values()) {
				if (d.getCode() == code) {
					return d;
				}
			}
			return null;
		}

		private final byte code;

		private Direction(byte c) {
			code = c;
		}

		public byte getCode() {
			return code;
		}
	}

	private Direction direction;

	public HomingCommand() {
		super(PacketID.Homing);

		setDirection(Direction.DEFAULT);
	}

	public Direction getDirection() {
		return direction;
	}

	@Out(0)
	public byte getDirectionRaw() {
		return direction.getCode();
	}

	public void setDirection(final Direction newDirection) {
		if (newDirection == null) {
			throw new IllegalArgumentException("new direction is null");
		}
		direction = newDirection;
	}

	@In(0)
	public void setDirectionRaw(final byte code) {
		setDirection(Direction.lookup(code));
	}
}