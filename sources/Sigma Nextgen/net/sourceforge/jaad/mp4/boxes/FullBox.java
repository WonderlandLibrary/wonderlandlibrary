package net.sourceforge.jaad.mp4.boxes;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;

public class FullBox extends BoxImpl {

	protected int version, flags;

	public FullBox(String name) {
		super(name);
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		version = in.read();
		flags = (int) in.readBytes(3);
	}
}
