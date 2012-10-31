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

import cn.kuehne.wsg50.Packet;
import cn.kuehne.wsg50.Parameter;

// TODO add guards
public class ReflectedParameter implements Parameter {
	private final Method get;
	private final Packet packet;
	private final Method set;

	ReflectedParameter(final Method setter, final Packet packet, final Method getter) {
		if (getter == null) {
			throw new IllegalArgumentException("'getter' is null");
		}
		if (setter == null) {
			throw new IllegalArgumentException("'setter' is null");
		}
		if (packet == null) {
			throw new IllegalArgumentException("'packet' is null");
		}
		if(0 < getter.getParameterTypes().length){
			throw new IllegalArgumentException("'getter' requires parameters");
		}
		if(1 != setter.getParameterTypes().length){
			throw new IllegalArgumentException("'setter' has bad parameter count: "+setter.getParameterTypes().length);
		}
		if(!getter.getReturnType().equals(setter.getParameterTypes()[0])){
			throw new IllegalArgumentException("'setter' and 'getter' type mismatch");
		}
		set = setter;
		get = getter;
		this.packet = packet;
	}

	@Override
	public String getName() {
		return set.getName().substring(3);
	}

	@Override
	public Class<?> getType() {
		return get.getReturnType();
	}

	@Override
	public Object getValue() throws Exception {
		return get.invoke(packet);
	}

	@Override
	public void setValue(final Object newValue) throws Exception {
		set.invoke(packet, newValue);
	}
}
