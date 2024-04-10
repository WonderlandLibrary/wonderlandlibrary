package net.sourceforge.jaad.mp4.boxes.impl.samplegroupentries;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;

public class VisualSampleGroupEntry extends SampleGroupDescriptionEntry {

	public VisualSampleGroupEntry() {
		super("Video Sample Group Entry");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
	}

}
