package net.sourceforge.jaad.mp4.boxes.impl;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

public class DataEntryUrlBox extends FullBox {

	private boolean inFile;
	private String location;

	public DataEntryUrlBox() {
		super("Data Entry Url Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		inFile = (flags&1)==1;
		if(!inFile) location = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
	}

	public boolean isInFile() {
		return inFile;
	}

	public String getLocation() {
		return location;
	}
}
