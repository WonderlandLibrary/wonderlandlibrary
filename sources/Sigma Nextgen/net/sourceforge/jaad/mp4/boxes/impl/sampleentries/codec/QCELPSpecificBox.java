package net.sourceforge.jaad.mp4.boxes.impl.sampleentries.codec;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;

public class QCELPSpecificBox extends CodecSpecificBox {

	private int framesPerSample;

	public QCELPSpecificBox() {
		super("QCELP Specific Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		decodeCommon(in);
		
		framesPerSample = in.read();
	}

	public int getFramesPerSample() {
		return framesPerSample;
	}
}
