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

import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.kuehne.wsg50.Acknowledge;
import cn.kuehne.wsg50.BugException;
import cn.kuehne.wsg50.Command;
import cn.kuehne.wsg50.Packet;
import cn.kuehne.wsg50.PacketBuilder;
import cn.kuehne.wsg50.PacketID;
import cn.kuehne.wsg50.Parameter;

public class AbstractPacket implements Packet {
	private final PacketID id;

	protected AbstractPacket(PacketID pID) {
		if (pID == null) {
			throw new IllegalArgumentException("id is null");
		}
		id = pID;
	}

	public ArrayList<Method> findInMethods() {
		final ArrayList<Method> methods = new ArrayList<Method>();
		for (final Method m : getClass().getMethods()) {
			final In in = m.getAnnotation(In.class);
			if (in != null) {
				final int index = in.value();
				if (index < 0) {
					throw new BugException("negative In.value " + index + " for " + m);
				}
				if (m.getParameterTypes().length != 1) {
					throw new BugException("In method shoul require exactly one parameter " + m);
				}
				while (index >= methods.size()) {
					methods.add(null);
				}
				final Method old = methods.get(index);
				if (null != old) {
					throw new BugException("duplicate In.value " + index + " for " + m + " and " + old);
				}
				methods.set(index, m);
			}
		}
		return methods;
	}

	public ArrayList<Method> findOutMethods() {
		final ArrayList<Method> methods = new ArrayList<Method>();
		for (final Method m : getClass().getMethods()) {
			final Out out = m.getAnnotation(Out.class);
			if (out != null) {
				final int index = out.value();
				if (index < 0) {
					throw new BugException("negative Out.value " + index + " for " + m);
				}
				if (m.getParameterTypes().length != 0) {
					throw new BugException("Out method shouln'd require parameters " + m);
				}
				while (index >= methods.size()) {
					methods.add(null);
				}
				final Method old = methods.get(index);
				if (null != old) {
					throw new BugException("duplicate Out.value " + index + " for " + m + " and " + old);
				}
				methods.set(index, m);
			}
		}
		return methods;
	}

	public final PacketID getId() {
		return id;
	}

	@Override
	public final byte getPacketID() {
		return id.getId();
	}

	private ReflectedParameter[] param;

	@Override
	public Parameter[] getParameters() {
		if (param == null) {
			final ArrayList<Method> in = findInMethods();
			final ArrayList<Method> out = findOutMethods();
			if(in.size() != out.size()){
				throw new BugException("@Out:" + out.size() + " != @In:"+in.size());
			}

			param = new ReflectedParameter[in.size()];
			for (int i = 0; i < param.length; i++) {
				param[i] = new ReflectedParameter(in.get(i), this, out.get(i));
			}
		}
		return param.clone();
	}

	@Override
	public void setPayload(final byte[] payload) {
		if (payload.length == 0) {
			return;
		}
		final InputArray in = new InputArray(payload);
		setPayload(in);
	}

	public void setPayload(final InputArray in) {
		final ArrayList<Method> methods = findInMethods();

		for (Method m : methods) {
			final Class<?> type = m.getParameterTypes()[0];
			final Object value = in.read(type);
			try {
				m.invoke(this, value);
			} catch (Exception e) {
				throw new BugException("" + m, e);
			}
		}

		// TODO check end of INPUT
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getId());
		if (this instanceof Command) {
			builder.append("Command");
		}
		if (this instanceof Acknowledge) {
			builder.append("Acknowledge");
		}
		toStringValues(builder);

		return builder.toString();
	}

	void toStringValues(StringBuilder builder) {
		for (final Method method : findOutMethods()) {
			builder.append(' ');
			builder.append(method.getName().substring(3));
			builder.append(':');
			try {
				builder.append(method.invoke(this));
			} catch (Exception e) {
				builder.append("BUG");
			}
		}
	}

	@Override
	public void writePayload(final PacketBuilder builder) {
		final ArrayList<Method> methods = findOutMethods();

		for (Method m : methods) {
			final Object value;
			try {
				value = m.invoke(this);
			} catch (Exception e) {
				throw new BugException("" + m, e);
			}
			builder.append(value);
		}
	}
}
