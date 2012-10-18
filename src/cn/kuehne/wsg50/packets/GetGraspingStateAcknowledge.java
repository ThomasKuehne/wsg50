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

public class GetGraspingStateAcknowledge extends AbstractAcknowledge {
	public static enum GraspingState {
		/** */
		Error((byte) 7),
		/** */
		Grasping((byte) 1),
		/** */
		Holding((byte) 4),
		/** */
		Idle((byte) 0),
		/** */
		NoPartFound((byte) 2),
		/** */
		PartLost((byte) 3),
		/** */
		Positioning((byte) 6),
		/** */
		Releasing((byte) 5);

		public static GraspingState lookup(final byte code) {
			for (final GraspingState s : values()) {
				if (s.getCode() == code) {
					return s;
				}
			}
			return null;
		}

		private final byte code;

		private GraspingState(final byte c) {
			code = c;
		}

		public byte getCode() {
			return code;
		}
	}

	private GraspingState state;

	public GetGraspingStateAcknowledge() {
		super(PacketID.GetGraspingState);
		setState(GraspingState.Idle);
	}

	public GraspingState getState() {
		return state;
	}

	@Out(0)
	public byte getStateRaw() {
		return state.getCode();
	}

	public void setState(final GraspingState newState) {
		if (newState == null) {
			throw new IllegalArgumentException("new state is null");
		}
		state = newState;
	}

	@In(0)
	public void setStateRaw(final byte newStateCode) {
		setState(GraspingState.lookup(newStateCode));
	}
}
