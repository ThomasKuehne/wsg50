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
import cn.kuehne.wsg50.helper.AbstractAcknowledge;
import cn.kuehne.wsg50.helper.In;
import cn.kuehne.wsg50.helper.Out;

public class GetSystemInformationAcknowledge extends AbstractAcknowledge {
	private short firmwareVersion;
	private byte gripperType;
	private byte hardwareRevision;
	private int serialNumber;

	public GetSystemInformationAcknowledge() {
		super(PacketID.GetSystemInformation);
	}

	@Out(2)
	public short getFirmwareVersion() {
		return firmwareVersion;
	}

	@Out(0)
	public byte getGripperType() {
		return gripperType;
	}

	@Out(1)
	public byte getHardwareRevision() {
		return hardwareRevision;
	}

	@Out(3)
	public int getSerialNumber() {
		return serialNumber;
	}

	@In(2)
	public void setFirmwareVersion(final short version) {
		firmwareVersion = version;
	}

	@In(0)
	public void setGripperType(final byte newGripperType) {
		gripperType = newGripperType;
	}

	@In(1)
	public void setHardwareRevision(final byte newHardwareRevision) {
		hardwareRevision = newHardwareRevision;
	}

	@In(3)
	public void setSerialNumber(final int nr) {
		serialNumber = nr;
	}
}
