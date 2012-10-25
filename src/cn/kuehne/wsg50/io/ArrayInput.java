package cn.kuehne.wsg50.io;

import java.io.EOFException;

import cn.kuehne.wsg50.Input;

public class ArrayInput implements Input{
	private final byte[] data;
	private int next;
	
	public ArrayInput(final byte[] data){
		if(data == null){
			throw new IllegalArgumentException("data is null");
		}
		this.data = data.clone();
		next = 0;
	}
	
	public static void checkBufferBounds(final byte[] buffer, final int offset, final int length){
		if(buffer == null){
			throw new IllegalArgumentException("buffer is null");
		}
		
		if(offset < 0){
			throw new IllegalArgumentException("offset is negative");
		}
		
		if(buffer.length <= offset){
			throw new IllegalArgumentException("offset is excessive");
		}
		
		if(length < 0){
			throw new IllegalArgumentException("length is negative");	
		}
		
		if(buffer.length < offset+length){
			throw new IllegalArgumentException("buffer is too short");
		}		
	}
	
	@Override
	public void readBytes(final byte[] buffer, final int offset, final int length) throws IllegalArgumentException, EOFException {
		checkBufferBounds(buffer, offset, length);
		
		try{
			System.arraycopy(data, next, buffer, offset, length);
			next += length;
		}catch(final IndexOutOfBoundsException ioe){
			next = data.length;
			throw new EOFException();
		}
	}

	public int available(){
		return data.length - next;
	}
}
