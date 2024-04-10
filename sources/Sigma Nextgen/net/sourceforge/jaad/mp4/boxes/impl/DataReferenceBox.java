package net.sourceforge.jaad.mp4.boxes.impl;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

/**
 * The data reference object contains a table of data references (normally URLs)
 * that declare the location(s) of the media data used within the presentation.
 * The data reference index in the sample description ties entries in this table
 * to the samples in the track. A track may be split over several sources in
 * this way.
 * The data entry is either a DataEntryUrnBox or a DataEntryUrlBox.
 * 
 * @author in-somnia
 */
public class DataReferenceBox extends FullBox {

	public DataReferenceBox() {
		super("Data Reference Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		final int entryCount = (int) in.readBytes(4);

		readChildren(in, entryCount); //DataEntryUrlBox, DataEntryUrnBox
	}
}
