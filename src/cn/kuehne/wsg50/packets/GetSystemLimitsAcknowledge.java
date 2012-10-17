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

public class GetSystemLimitsAcknowledge extends AbstractAcknowledge {
	private float maxAcc;

	private float maxForce;

	private float maxSpeed;

	private float minAcc;

	private float minForce;

	private float minSpeed;

	private float ovrForce;

	private float stroke;

	public GetSystemLimitsAcknowledge() {
		super(PacketID.GetSystemLimits.getId());
	}

	@Out(4)
	public float getMaxAcc() {
		return maxAcc;
	}

	@Out(6)
	public float getMaxForce() {
		return maxForce;
	}

	@Out(2)
	public float getMaxSpeed() {
		return maxSpeed;
	}

	@Out(3)
	public float getMinAcc() {
		return minAcc;
	}

	@Out(5)
	public float getMinForce() {
		return minForce;
	}

	@Out(1)
	public float getMinSpeed() {
		return minSpeed;
	}

	@Out(7)
	public float getOvrForce() {
		return ovrForce;
	}

	@Out(0)
	public float getStroke() {
		return stroke;
	}

	@In(4)
	public void setMaxAcc(float maxAcc) {
		this.maxAcc = maxAcc;
	}
	@In(6)
	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}
	@In(2)
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	@In(3)
	public void setMinAcc(float minAcc) {
		this.minAcc = minAcc;
	}
	@In(5)
	public void setMinForce(float minForce) {
		this.minForce = minForce;
	}
	@In(1)
	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}
	@In(7)
	public void setOvrForce(float ovrForce) {
		this.ovrForce = ovrForce;
	}

	@In(0)
	public void setStroke(float stroke) {
		this.stroke = stroke;
	}

	@Override
	public String toString() {
		return PacketID.GetSystemLimits + " stroke:" + getStroke() + " speed:"
				+ getMinSpeed() + "-" + getMaxSpeed() + " acc:" + getMinAcc()
				+ "-" + getMaxAcc() + " force:" + getMinForce() + "-"
				+ getMaxForce() + "-" + getOvrForce();
	}
}
