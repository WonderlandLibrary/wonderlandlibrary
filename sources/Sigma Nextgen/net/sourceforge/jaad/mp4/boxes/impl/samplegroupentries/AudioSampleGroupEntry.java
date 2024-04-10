package net.sourceforge.jaad.mp4.boxes.impl.samplegroupentries;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;

public class AudioSampleGroupEntry extends SampleGroupDescriptionEntry {

	public AudioSampleGroupEntry() {
		super("Audio Sample Group Entry");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
	}
}
