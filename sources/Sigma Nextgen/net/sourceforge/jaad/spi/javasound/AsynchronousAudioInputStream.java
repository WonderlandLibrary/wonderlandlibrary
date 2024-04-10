package net.sourceforge.jaad.spi.javasound;

import net.sourceforge.jaad.spi.javasound.CircularBuffer.Trigger;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

abstract class AsynchronousAudioInputStream extends AudioInputStream implements Trigger {

	private byte[] singleByte;
	protected final CircularBuffer buffer;

	AsynchronousAudioInputStream(InputStream in, AudioFormat format, long length) throws IOException {
		super(in, format, length);
		buffer = new CircularBuffer(this);
	}

	@Override
	public int read() throws IOException {
		int i = -1;
		if(singleByte==null) singleByte = new byte[1];
		if(buffer.read(singleByte, 0, 1)==-1) i = -1;
		else i = singleByte[0]&0xFF;
		return i;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return buffer.read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return buffer.read(b, off, len);
	}

	@Override
	public long skip(long len) throws IOException {
		int l = (int) len;
		final byte[] b = new byte[l];
		while(l>0) {
			l -= buffer.read(b, 0, l);
		}
		return len;
	}

	@Override
	public int available() throws IOException {
		return buffer.availableRead();
	}

	@Override
	public void close() throws IOException {
		buffer.close();
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void mark(int limit) {
	}

	@Override
	public void reset() throws IOException {
		throw new IOException("mark not supported");
	}
}
