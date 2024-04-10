package net.sourceforge.jaad.mp4.boxes.impl.oma;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

public class OMAContentObjectBox extends FullBox {

	private byte[] data;

	public OMAContentObjectBox() {
		super("OMA Content Object Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		final int len = (int) in.readBytes(4);
		data = new byte[len];
		in.readBytes(data);
	}

	/**
	 * Returns the data of this content object.
	 * 
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
}
