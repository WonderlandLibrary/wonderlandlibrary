package com.masterof13fps.utils.render.slick.openal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.BufferUtils;
import com.masterof13fps.utils.render.slick.util.Log;

/**
 * An input stream that can extract ogg data. This class is a bit of an experiment with continuations
 * so uses thread where possibly not required. It's just a test to see if continuations make sense in 
 * some cases.
 *
 * @author kevin
 */
public class OggInputStream extends InputStream implements AudioInputStream {
	/** The conversion buffer size */
	private int convsize = 4096 * 4;
	/** The buffer used to read OGG file */
	private byte[] convbuffer = new byte[convsize]; // take 8k out of the data segment, not the stack
	/** The stream we're reading the OGG file from */
	private InputStream input;
	/** The audio information from the OGG header */
	/** True if we're at the end of the available data */
	private boolean endOfStream;
	
	/** Temporary scratch buffer */
	byte[] buffer;
	/** The number of bytes read */
	int bytes = 0;
	/** The true if we should be reading big endian */
	boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
	/** True if we're reached the end of the current bit stream */
	boolean endOfBitStream = true;
	/** True if we're initialise the OGG info block */
	boolean inited = false;
	
	/** The index into the byte array we currently read from */
	private int readIndex;
	/** The byte array store used to hold the data read from the ogg */
	private ByteBuffer pcmBuffer = BufferUtils.createByteBuffer(4096 * 500);
	/** The total number of bytes */
	private int total;
	
	/**
	 * Create a new stream to decode OGG data
	 * 
	 * @param input The input stream from which to read the OGG file
	 * @throws IOException Indicates a failure to read from the supplied stream
	 */
	public OggInputStream(InputStream input) throws IOException {
		this.input = input;
		total = input.available();
		
		init();
	}
	
	/**
	 * Get the number of bytes on the stream
	 * 
	 * @return The number of the bytes on the stream
	 */
	public int getLength() {
		return total;
	}
	
	/**
	 * @see AudioInputStream#getChannels()
	 */
	public int getChannels() {
		return -1;
	}
	
	/**
	 * @see AudioInputStream#getRate()
	 */
	public int getRate() {
		return -1;
	}
	
	/**
	 * Initialise the streams and thread involved in the streaming of OGG data
	 * 
	 * @throws IOException Indicates a failure to link up the streams
	 */
	private void init() throws IOException {
		initVorbis();
		readPCM();
	}
		
	/**
	 * @see InputStream#available()
	 */
	public int available() {
		return endOfStream ? 0 : 1;
	}
	
	/**
	 * Initialise the vorbis decoding
	 */
	private void initVorbis() {

	}
	
	/**
	 * Get a page and packet from that page
	 *
	 * @return True if there was a page available
	 */
	private boolean getPageAndPacket() {
		return true;
	}
	
	/**
	 * Decode the OGG file as shown in the jogg/jorbis examples
	 * 
	 * @throws IOException Indicates a failure to read from the supplied stream
	 */
	private void readPCM() throws IOException {
	}
	
	/**
	 * @see InputStream#read()
	 */
	public int read() throws IOException {
		if (readIndex >= pcmBuffer.position()) {
			pcmBuffer.clear();
			readPCM();
			readIndex = 0;
		}
		if (readIndex >= pcmBuffer.position()) {
			return -1;
		}

		int value = pcmBuffer.get(readIndex);
		if (value < 0) {
			value = 256 + value;
		}
		readIndex++;
		
		return value;
	}

	/**
	 * @see AudioInputStream#atEnd()
	 */
	public boolean atEnd() {
		return endOfStream && (readIndex >= pcmBuffer.position());
	}

	/**
	 * @see InputStream#read(byte[], int, int)
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		for (int i=0;i<len;i++) {
			try {
				int value = read();
				if (value >= 0) {
					b[i] = (byte) value;
				} else {
					if (i == 0) {						
						return -1;
					} else {
						return i;
					}
				}
			} catch (IOException e) {
				Log.error(e);
				return i;
			}
		}
		
		return len;
	}

	/**
	 * @see InputStream#read(byte[])
	 */
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	/**
	 * @see InputStream#close()
	 */
	public void close() throws IOException {
	}
}
