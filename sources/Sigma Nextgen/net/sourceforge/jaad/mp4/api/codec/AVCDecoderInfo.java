package net.sourceforge.jaad.mp4.api.codec;

import net.sourceforge.jaad.mp4.api.DecoderInfo;
import net.sourceforge.jaad.mp4.boxes.impl.sampleentries.codec.AVCSpecificBox;
import net.sourceforge.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class AVCDecoderInfo extends DecoderInfo {

	private AVCSpecificBox box;

	public AVCDecoderInfo(CodecSpecificBox box) {
		this.box = (AVCSpecificBox) box;
	}

	public int getConfigurationVersion() {
		return box.getConfigurationVersion();
	}

	public int getProfile() {
		return box.getProfile();
	}

	public byte getProfileCompatibility() {
		return box.getProfileCompatibility();
	}

	public int getLevel() {
		return box.getLevel();
	}

	public int getLengthSize() {
		return box.getLengthSize();
	}

	public byte[][] getSequenceParameterSetNALUnits() {
		return box.getSequenceParameterSetNALUnits();
	}

	public byte[][] getPictureParameterSetNALUnits() {
		return box.getPictureParameterSetNALUnits();
	}
}
