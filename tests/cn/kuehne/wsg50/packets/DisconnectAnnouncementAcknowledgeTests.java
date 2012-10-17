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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cn.kuehne.wsg50.Packet;
import cn.kuehne.wsg50.PacketCoder;
import cn.kuehne.wsg50.PacketID;
import cn.kuehne.wsg50.helper.InputArray;
import cn.kuehne.wsg50.helper.OutputArray;

public class DisconnectAnnouncementAcknowledgeTests {

	@Test
	public void testRoundtrip() {
		final Packet c = PacketID.DisconnectAnnouncement.getAcknowledge();
		assertNotNull(c);

		final PacketCoder wsg50 = new PacketCoder();

		final OutputArray output = new OutputArray();
		wsg50.write(output, c);

		final InputArray input = new InputArray(output.getBytes());
		final DisconnectAnnouncementAcknowledge result = wsg50.readDebug(input,
				DisconnectAnnouncementAcknowledge.class, true);

		assertNotNull(result);
	}
}
