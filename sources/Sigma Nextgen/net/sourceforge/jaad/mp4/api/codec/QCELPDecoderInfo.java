package net.sourceforge.jaad.mp4.api.codec;

import net.sourceforge.jaad.mp4.api.DecoderInfo;
import net.sourceforge.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;
import net.sourceforge.jaad.mp4.boxes.impl.sampleentries.codec.QCELPSpecificBox;

public class QCELPDecoderInfo extends DecoderInfo {

	private QCELPSpecificBox box;

	public QCELPDecoderInfo(CodecSpecificBox box) {
		this.box = (QCELPSpecificBox) box;
	}

	public int getDecoderVersion() {
		return box.getDecoderVersion();
	}

	public long getVendor() {
		return box.getVendor();
	}

	public int getFramesPerSample() {
		return box.getFramesPerSample();
	}
}
