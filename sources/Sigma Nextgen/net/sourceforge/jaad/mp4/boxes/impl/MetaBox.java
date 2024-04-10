package net.sourceforge.jaad.mp4.boxes.impl;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

//needs to be defined, because readChildren() is not called by factory
/* TODO: this class shouldn't be needed. at least here, things become too
complicated. change this!!! */
public class MetaBox extends FullBox {

	public MetaBox() {
		super("Meta Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);
		readChildren(in);
	}
}
