package net.sourceforge.jaad.mp4.boxes.impl.meta;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

/**
 * This box is used in custom metadata tags (within the box-type '----'). It
 * contains the name of the custom tag, whose data is stored in the 'data'-box.
 *
 * @author in-somnia
 */
public class ITunesMetadataNameBox extends FullBox {

	private String metaName;

	public ITunesMetadataNameBox() {
		super("iTunes Metadata Name Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		metaName = in.readString((int) getLeft(in));
	}

	public String getMetaName() {
		return metaName;
	}
}
