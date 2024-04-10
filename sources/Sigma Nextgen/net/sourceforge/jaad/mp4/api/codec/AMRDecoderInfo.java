package net.sourceforge.jaad.mp4.api.codec;

import net.sourceforge.jaad.mp4.api.DecoderInfo;
import net.sourceforge.jaad.mp4.boxes.impl.sampleentries.codec.AMRSpecificBox;
import net.sourceforge.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class AMRDecoderInfo extends DecoderInfo {

	private AMRSpecificBox box;

	public AMRDecoderInfo(CodecSpecificBox box) {
		this.box = (AMRSpecificBox) box;
	}

	public int getDecoderVersion() {
		return box.getDecoderVersion();
	}

	public long getVendor() {
		return box.getVendor();
	}

	public int getModeSet() {
		return box.getModeSet();
	}

	public int getModeChangePeriod() {
		return box.getModeChangePeriod();
	}

	public int getFramesPerSample() {
		return box.getFramesPerSample();
	}
}
