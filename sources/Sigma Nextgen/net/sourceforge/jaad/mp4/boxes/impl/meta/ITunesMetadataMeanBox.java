package net.sourceforge.jaad.mp4.boxes.impl.meta;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

public class ITunesMetadataMeanBox extends FullBox {

	private String domain;

	public ITunesMetadataMeanBox() {
		super("iTunes Metadata Mean Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		domain = in.readString((int) getLeft(in));
	}

	public String getDomain() {
		return domain;
	}
}
