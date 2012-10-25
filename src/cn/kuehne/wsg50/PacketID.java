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

import cn.kuehne.wsg50.packets.*;

public enum PacketID {
	/** */
	Loop((byte) 0x06, LoopCommand.class, LoopAcknowledge.class),
	/** */
	DisconnectAnnouncement((byte) 0x07, DisconnectAnnouncementCommand.class, DisconnectAnnouncementAcknowledge.class),

	/** */
	Homing((byte) 0x20, HomingCommand.class, HomingAcknowledge.class),
	/** */
	PrePositionFingers((byte) 0x21, PrePositionFingersCommand.class, PrePositionFingersAcknowledge.class),
	/** */
	Stop((byte) 0x22, StopCommand.class, StopAcknowledge.class),
	/** */
	FastStop((byte) 0x23, FastStopCommand.class, FastStopAcknowledge.class),
	/** */
	AcknowledgeFault((byte) 0x24, AcknowledgeFaultCommand.class, AcknowledgeFaultAcknowledge.class),
	/** */
	GraspPart((byte) 0x25, GraspPartCommand.class, GraspPartAcknowledge.class),
	/** */
	ReleasePart((byte) 0x26, ReleasePartCommand.class, ReleasePartAcknowledge.class),

	/** */
	SetAcceleration((byte) 0x30, SetAccelerationCommand.class, SetAccelerationAcknowledge.class),
	/** */
	GetAcceleration((byte) 0x31, GetAccelerationCommand.class, GetAccelerationAcknowledge.class),
	/** */
	SetForceLimit((byte) 0x32, SetForceLimitCommand.class, SetForceLimitAcknowledge.class),
	/** */
	GetForceLimit((byte) 0x33, GetForceLimitCommand.class, GetForceLimitAcknowledge.class),
	/** */
	SetSoftLimits((byte) 0x34, SetSoftLimitsCommand.class, SetSoftLimitsAcknowledge.class),
	/** */
	GetSoftLimits((byte) 0x35, GetSoftLimitsCommand.class, GetSoftLimitsAcknowledge.class),
	/** */
	ClearSoftLimits((byte) 0x36, ClearSoftLimitsCommand.class, ClearSoftLimitsAcknowledge.class),
	/** */
	OverdriveMode((byte) 0x37, OverdriveModeCommand.class, OverdriveModeAcknowledge.class),
	/** */
	TareForceSensor((byte) 0x38, TareForceSensorCommand.class, TareForceSensorAcknowledge.class),

	/** */
	GetSystemState((byte) 0x40, GetSystemStateCommand.class, GetSystemStateAcknowledge.class),
	/** */
	GetGraspingState((byte) 0x41, GetGraspingStateCommand.class, GetGraspingStateAcknowledge.class),
	/** */
	GetGraspingStatistics((byte) 0x42, GetGraspingStatisticsCommand.class, GetGraspingStatisticsAcknowledge.class),
	/** */
	GetOpeningWidth((byte) 0x43, GetOpeningWidthCommand.class, GetOpeningWidthAcknowledge.class),
	/** */
	GetSpeed((byte) 0x44, GetSpeedCommand.class, GetSpeedAcknowledge.class),
	/** */
	GetForce((byte) 0x45, GetForceCommand.class, GetForceAcknowledge.class),
	/** */
	GetTemperature((byte) 0x46, GetTemperatureCommand.class, GetTemperatureAcknowledge.class),
	
	/** */
	GetSystemInformation((byte) 0x50, GetSystemInformationCommand.class,GetSystemInformationAcknowledge.class),
	/** */
	SetDeviceTag((byte) 0x51, SetDeviceTagCommand.class,SetDeviceTagAcknowledge.class),
	/** */
	GetDeviceTag((byte) 0x52, GetDeviceTagCommand.class, GetDeviceTagAcknowledge.class),
	/** */
	GetSystemLimits((byte) 0x53, GetSystemLimitsCommand.class, GetSystemLimitsAcknowledge.class),

	/** */
	GetFingerInfo((byte) 0x60, GetFingerInfoCommand.class, GetFingerInfoAcknowledge.class),
	/** */
	GetFingerFlags((byte) 0x61, GetFingerFlagsCommand.class, GetFingerFlagsAcknowledge.class),
	/** */
	FingerPowerControl((byte) 0x62, FingerPowerControlCommand.class, FingerPowerControlAcknowledge.class),
	/** */
	GetFingerData((byte) 0x63, GetFingerDataCommand.class, GetFingerDataAcknowledge.class);

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
