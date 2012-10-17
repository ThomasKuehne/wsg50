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

import cn.kuehne.wsg50.packets.DisconnectAnnouncementAcknowledge;
import cn.kuehne.wsg50.packets.DisconnectAnnouncementCommand;
import cn.kuehne.wsg50.packets.HomingAcknowledge;
import cn.kuehne.wsg50.packets.HomingCommand;
import cn.kuehne.wsg50.packets.LoopAcknowledge;
import cn.kuehne.wsg50.packets.LoopCommand;
import cn.kuehne.wsg50.packets.PrePositionFingersCommand;

public enum PacketID {
	Loop((byte) 0x06, LoopCommand.class, LoopAcknowledge.class),
	DisconnectAnnouncement((byte) 0x07, DisconnectAnnouncementCommand.class, DisconnectAnnouncementAcknowledge.class),
	Homing((byte) 0x20, HomingCommand.class, HomingAcknowledge.class),
	PrePositionFingers((byte) 0x21, PrePositionFingersCommand.class, null), ;

	public static PacketID lookup(byte id) {
		for (final PacketID pID : values()) {
			if (pID.getId() == id) {
				return pID;
			}
		}
		return null;
	}

	private final Class<? extends Acknowledge> acknowledge;
	private final Class<? extends Command> command;

	private final byte id;

	private PacketID(byte i, Class<? extends Command> c, Class<? extends Acknowledge> a) {
		id = i;
		command = c;
		acknowledge = a;
	}

	public Acknowledge getAcknowledge() {
		if (acknowledge == null) {
			throw new TodoException("TODO: implemend Acknowledge \"" + name() + "\"");
		}

		try {
			return acknowledge.newInstance();
		} catch (Exception e) {
			throw new BugException("BUG: failed to instanciate \"" + acknowledge.getName() + "\"", e);
		}
	}

	public Command getCommand() {
		if (command == null) {
			throw new TodoException("TODO: implemend Command \"" + name() + "\"");
		}

		try {
			return command.newInstance();
		} catch (Exception e) {
			throw new BugException("BUG: failed to instanciate \"" + command.getName() + "\"", e);
		}
	}

	/**
	 * ID byte of the packet.
	 */
	public byte getId() {
		return id;
	}
}
