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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

import cn.kuehne.wsg50.helper.InputFromStream;
import cn.kuehne.wsg50.helper.OutputToStream;
import cn.kuehne.wsg50.packets.AcknowledgeFaultCommand;
import cn.kuehne.wsg50.packets.ClearSoftLimitsCommand;
import cn.kuehne.wsg50.packets.DisconnectAnnouncementCommand;
import cn.kuehne.wsg50.packets.FastStopCommand;
import cn.kuehne.wsg50.packets.FingerPowerControlCommand;
import cn.kuehne.wsg50.packets.GetAccelerationCommand;
import cn.kuehne.wsg50.packets.GetDeviceTagCommand;
import cn.kuehne.wsg50.packets.GetFingerDataCommand;
import cn.kuehne.wsg50.packets.GetFingerFlagsCommand;
import cn.kuehne.wsg50.packets.GetFingerInfoCommand;
import cn.kuehne.wsg50.packets.GetForceCommand;
import cn.kuehne.wsg50.packets.GetForceLimitCommand;
import cn.kuehne.wsg50.packets.GetGraspingStateCommand;
import cn.kuehne.wsg50.packets.GetGraspingStatisticsCommand;
import cn.kuehne.wsg50.packets.GetOpeningWidthCommand;
import cn.kuehne.wsg50.packets.GetSoftLimitsCommand;
import cn.kuehne.wsg50.packets.GetSpeedCommand;
import cn.kuehne.wsg50.packets.GetSystemInformationCommand;
import cn.kuehne.wsg50.packets.GetSystemLimitsCommand;
import cn.kuehne.wsg50.packets.GetSystemStateCommand;
import cn.kuehne.wsg50.packets.GetTemperatureCommand;
import cn.kuehne.wsg50.packets.GraspPartCommand;
import cn.kuehne.wsg50.packets.HomingCommand;
import cn.kuehne.wsg50.packets.LoopCommand;
import cn.kuehne.wsg50.packets.OverdriveModeCommand;
import cn.kuehne.wsg50.packets.PrePositionFingersCommand;
import cn.kuehne.wsg50.packets.ReleasePartCommand;
import cn.kuehne.wsg50.packets.SetAccelerationCommand;
import cn.kuehne.wsg50.packets.SetDeviceTagCommand;
import cn.kuehne.wsg50.packets.SetForceLimitCommand;
import cn.kuehne.wsg50.packets.SetSoftLimitsCommand;
import cn.kuehne.wsg50.packets.StopCommand;
import cn.kuehne.wsg50.packets.TareForceSensorCommand;

public class PacketTests {
	static class RoundtripTest {
		private final byte[] coded;
		private final Packet master;

		RoundtripTest(byte[] coded, Packet master) {
			this.coded = coded;
			this.master = master;

		}

		public void run() {
			assertTrue((master instanceof Command) ^ (master instanceof Acknowledge));

			final ByteArrayInputStream in = new ByteArrayInputStream(coded);
			final Input input = new InputFromStream(in);
			final PacketCoder coder = new PacketCoder();
			final Packet p;
			if (master instanceof Command) {
				p = coder.readCommand(input, true);
			} else {
				p = coder.readAcknowledge(input, true);
			}
			assertEquals("remaining input", 0, in.available());
			assertNotNull("result packet", p);
			assertEquals("class", master.getClass(), p.getClass());
			assertEquals("parameters", master, p);

			{
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				final Output output = new OutputToStream(out);
				coder.write(output, p);
				assertArrayEquals("write(self)", coded, out.toByteArray());
			}

			{
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				final Output output = new OutputToStream(out);
				coder.write(output, master);
				assertArrayEquals("write(master)", coded, out.toByteArray());
			}
		}
	}

	@Test
	public void testAcknowledgeFaultCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x24, 0x03, 0x00, 0x61, 0x63, 0x6B, (byte) 0xDC,
				(byte) 0xB9 };

		final Packet p = new AcknowledgeFaultCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testClearSoftLimitsCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x36, 0x00, 0x00, 0x17, (byte) 0x93 };

		final Packet p = new ClearSoftLimitsCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testDisconnectAnnouncementCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x35, 0x4C };

		final Packet p = new DisconnectAnnouncementCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testFastStopCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x23, 0x00, 0x00, (byte) 0xAC, 0x1C };

		final Packet p = new FastStopCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testFingerPowerControlCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x62, 0x02, 0x00, 0x03, 0x01, (byte) 0xD8,
				(byte) 0xE3 };

		final FingerPowerControlCommand p = new FingerPowerControlCommand();
		p.setFingerPowerControl(true);
		p.setIndex((byte) 3);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetAccelerationCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x31, 0x00, 0x00, 0x68, (byte) 0xA5 };

		final GetAccelerationCommand p = new GetAccelerationCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetDeviceTagCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x52, 0x00, 0x00, (byte) 0x9F, 0x52 };

		final Packet p = new GetDeviceTagCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetFingerDataCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x63, 0x01, 0x00, 0x11, 0x49, 0x1E };

		final GetFingerDataCommand p = new GetFingerDataCommand();
		p.setIndex((byte) 17);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetFingerFlagsCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x61, 0x01, 0x00, 0x0B, (byte) 0xA7, (byte) 0xA5 };

		final GetFingerFlagsCommand p = new GetFingerFlagsCommand();
		p.setIndex((byte) 11);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetFingerInfoCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x60, 0x01, 0x00, 0x0D, (byte) 0xA3, 0x50 };

		final GetFingerInfoCommand p = new GetFingerInfoCommand();
		p.setIndex((byte) 13);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetForceInfoCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x45, 0x03, 0x00, 0x00, 0x20, 0x4E, (byte) 0xDD,
				0x06 };

		final GetForceCommand p = new GetForceCommand();
		p.setFlags((byte) 0);
		p.setPeriod((short) 20000);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetForceLimitCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x33, 0x00, 0x00, 0x2C, 0x70 };

		final Packet p = new GetForceLimitCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetGraspingStateCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x41, 0x03, 0x00, 0x01, 0x0E, 0x4E, 0x15,
				(byte) 0xB9 };

		final GetGraspingStateCommand p = new GetGraspingStateCommand();
		p.setPeriod((short) 19982);
		p.setFlags((byte) 1);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetGraspingStatisticsCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x42, 0x01, 0x00, 0x01, 0x63, (byte) 0x99 };

		final GetGraspingStatisticsCommand p = new GetGraspingStatisticsCommand();
		p.setReset(true);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetOpeningWidthCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x43, 0x03, 0x00, 0x01, 0x34, 0x12, (byte) 0x85,
				(byte) 0xF3 };

		final GetOpeningWidthCommand p = new GetOpeningWidthCommand();
		p.setPeriod((short) 0x1234);
		p.setFlags((byte) 1);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetPacketID() {
		for (final PacketID pID : PacketID.values()) {
			final byte id = pID.getId();

			final Acknowledge a = pID.getAcknowledge();
			assertNotNull(pID.toString(), a);
			assertEquals(id, a.getPacketID());

			final Packet p = pID.getCommand();
			assertNotNull(pID.toString(), p);
			assertEquals(id, p.getPacketID());
		}
	}

	@Test
	public void testGetSoftLimitsCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x35, 0x00, 0x00, (byte) 0xF1, 0x2C };

		final GetSoftLimitsCommand p = new GetSoftLimitsCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetSpeedCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x44, 0x03, 0x00, 0x01, 0x02, 0x03, 0x5B,
				(byte) 0xE3 };

		final GetSpeedCommand p = new GetSpeedCommand();
		p.setPeriod((short) 0x0302);
		p.setFlags((byte) 1);

		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetSystemInformationCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x50, 0x00, 0x00, (byte) 0xDB, (byte) 0x87 };

		final GetSystemInformationCommand p = new GetSystemInformationCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetSystemLimitsCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x53, 0x00, 0x00, 0x3D, 0x38 };

		final GetSystemLimitsCommand p = new GetSystemLimitsCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetSystemStateCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x40, 0x03, 0x00, 0x03, 0x05, 0x04, (byte) 0x88,
				0x6A };

		final GetSystemStateCommand p = new GetSystemStateCommand();
		p.setFlags((byte) 3);
		p.setPeriod((short) 0x0405);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGetTemperatureCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x46, 0x00, 0x00, (byte) 0x86, (byte) 0xB7 };

		final GetTemperatureCommand p = new GetTemperatureCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testGraspPartCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x25, 0x08, 0x00, 0x00, 0x00, 0x40, 0x41, 0x00,
				0x00, 0x08, 0x42, (byte) 0xA7, (byte) 0xF1 };

		final GraspPartCommand p = new GraspPartCommand();
		p.setWidth(12f);
		p.setSpeed(34f);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testHomingCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x20, 0x01, 0x00, 0x02, (byte) 0xCD, (byte) 0xA3 };

		final HomingCommand p = new HomingCommand();
		p.setDirection(HomingCommand.Direction.NEGATIVE);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testLoopCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x06, 0x03, 0x00, 0x61, 0x62, 0x63, (byte) 0xCA,
				0x44 };

		final LoopCommand p = new LoopCommand();
		p.setLoopData(new byte[] { 'a', 'b', 'c' });
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testOverdriveModeCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x37, 0x01, 0x00, 0x01, 0x04, (byte) 0x8D };

		final OverdriveModeCommand p = new OverdriveModeCommand();
		p.setOverdrive(true);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testPrePositionFingersCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x21, 0x09, 0x00, 0x02, 0x00, 0x00, 0x08, 0x42,
				0x00, 0x00, 0x60, 0x42, (byte) 0x95, 0x56 };

		final PrePositionFingersCommand p = new PrePositionFingersCommand();
		p.setStopOnBlock(true);
		p.setRelative(false);
		p.setWidth(34f);
		p.setSpeed(56f);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testReleasePartCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x26, 0x08, 0x00, 0x00, 0x00, 0x08, 0x42, 0x00,
				0x00, 0x60, 0x42, 0x43, (byte) 0xE7 };

		final ReleasePartCommand p = new ReleasePartCommand();
		p.setWidth(34f);
		p.setSpeed(56f);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testSetAccelerationCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x30, 0x04, 0x00, (byte) 0x9A, (byte) 0x99,
				(byte) 0x99, 0x3F, (byte) 0xF7, (byte) 0xD7 };

		final SetAccelerationCommand p = new SetAccelerationCommand();
		p.setAcceleration(1.2f);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testSetDeviceTagCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x51, 0x04, 0x00, 0x61, 0x62, 0x63, 0x64,
				(byte) 0x87, (byte) 0x85 };

		final SetDeviceTagCommand p = new SetDeviceTagCommand();
		p.setDeviceTag("abcd");
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testSetForceLimitCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x32, 0x04, 0x00, (byte) 0x9A, (byte) 0x99,
				(byte) 0xD9, 0x3F, 0x45, 0x42 };

		final SetForceLimitCommand p = new SetForceLimitCommand();
		p.setForceLimit(1.7f);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testSetSoftLimitsCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x34, 0x08, 0x00, (byte) 0x9A, (byte) 0x99, 0x59,
				0x40, 0x33, 0x33, (byte) 0xB3, 0x40, (byte) 0x8C, 0x44 };

		final SetSoftLimitsCommand p = new SetSoftLimitsCommand();
		p.setSoftLimitMinus(3.4f);
		p.setSoftLimitPlus(5.6f);
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testStopCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x22, 0x00, 0x00, 0x0E, 0x76 };

		final StopCommand p = new StopCommand();
		new RoundtripTest(enc, p).run();
	}

	@Test
	public void testTareForceSensorCommand() {
		final byte[] enc = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x38, 0x00, 0x00, (byte) 0xEA, (byte) 0xCF };

		final TareForceSensorCommand p = new TareForceSensorCommand();
		new RoundtripTest(enc, p).run();
	}

}
