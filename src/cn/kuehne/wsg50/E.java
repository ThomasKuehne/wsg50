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

import java.io.Serializable;

public enum E implements Serializable {
	ACCESS_DENIED((short) 16), ALREADY_OPEN((short) 17), ALREADY_RUNNING((short) 4), AXIS_BLOCKED((short) 29), CHECKSUM_ERROR(
			(short) 11), CMD_ABORTED((short) 19), CMD_FAILED((short) 18), CMD_FORMAT_ERROR((short) 15), CMD_PENDING(
			(short) 26), CMD_UNKNOWN((short) 14), FEATURNOT_SUPPORTED((short) 5), FILEXISTS((short) 30), INCONSISTENT_DATA(
			(short) 6), INDEX_OUT_OF_BOUNDS((short) 25), INSUFFICIENT_RESOURCES((short) 10), INVALID_HANDLE((short) 20), INVALID_PARAMETER(
			(short) 24), IO_ERROR((short) 23), NO_PARAM_EXPECTED((short) 12), NO_SENSOR((short) 2), NOT_AVAILABLE((short) 1), NOT_ENOUGH_PARAMS(
			(short) 13), NOT_FOUND((short) 21), NOT_INITIALIZED((short) 3), NOT_OPEN((short) 22), OVERRUN((short) 27), RANGERROR(
			(short) 28), READ_ERROR((short) 8), SUCCESS((short) 0), TIMEOUT((short) 7), WRITERROR((short) 9);

	public static E lookup(short id) {
		for (final E e : values()) {
			if (e.getCode() == id) {
				return e;
			}
		}
		return null;
	}

	private final short code;

	private E(short c) {
		code = c;
	}

	public short getCode() {
		return code;
	}
}
