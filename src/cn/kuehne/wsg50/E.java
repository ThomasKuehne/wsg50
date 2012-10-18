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

public enum E {
	ACCESS_DENIED((byte) 16), ALREADY_OPEN((byte) 17), ALREADY_RUNNING((byte) 4), AXIS_BLOCKED((byte) 29), CHECKSUM_ERROR(
			(byte) 11), CMD_ABORTED((byte) 19), CMD_FAILED((byte) 18), CMD_FORMAT_ERROR((byte) 15), CMD_PENDING(
			(byte) 26), CMD_UNKNOWN((byte) 14), FEATURNOT_SUPPORTED((byte) 5), FILEXISTS((byte) 30), INCONSISTENT_DATA(
			(byte) 6), INDEX_OUT_OF_BOUNDS((byte) 25), INSUFFICIENT_RESOURCES((byte) 10), INVALID_HANDLE((byte) 20), INVALID_PARAMETER(
			(byte) 24), IO_ERROR((byte) 23), NO_PARAM_EXPECTED((byte) 12), NO_SENSOR((byte) 2), NOT_AVAILABLE((byte) 1), NOT_ENOUGH_PARAMS(
			(byte) 13), NOT_FOUND((byte) 21), NOT_INITIALIZED((byte) 3), NOT_OPEN((byte) 22), OVERRUN((byte) 27), RANGERROR(
			(byte) 28), READ_ERROR((byte) 8), SUCCESS((byte) 0), TIMEOUT((byte) 7), WRITERROR((byte) 9);

	public static E lookup(byte id) {
		for (final E e : values()) {
			if (e.getCode() == id) {
				return e;
			}
		}
		return null;
	}

	private final byte code;

	private E(byte c) {
		code = c;
	}

	public byte getCode() {
		return code;
	}
}
