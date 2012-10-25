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

package cn.kuehne.wsg50.io;

import java.io.ByteArrayOutputStream;

import cn.kuehne.wsg50.BugException;
import cn.kuehne.wsg50.Output;

public class ArrayOutput implements Output {
	private final ByteArrayOutputStream buffer;
	
	public ArrayOutput(){
		buffer = new ByteArrayOutputStream();
	}

	@Override
	public void writePacket(byte[] packet) {
		if(packet == null){
			throw new IllegalArgumentException("packet is null");
		}
		try {
			buffer.write(packet);
		} catch (Exception e) {
			throw new BugException("this should never happen", e);
		}
	}

	public void write(byte b) {
		try {
			buffer.write(b);
		} catch (Exception e) {
			throw new BugException("this should never happen", e);
		}
	}
	
	public byte[] toByteArray(){
		return buffer.toByteArray();
	}
	
	public int size(){
		return buffer.size();
	}
}
