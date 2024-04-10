package net.sourceforge.jaad.mp4.boxes.impl.meta;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.BoxTypes;
import net.sourceforge.jaad.mp4.boxes.FullBox;

public class EncoderBox extends FullBox {

	private String data;

	public EncoderBox() {
		super("Encoder Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		if(parent.getType()==BoxTypes.ITUNES_META_LIST_BOX) readChildren(in);
		else {
			super.decode(in);
			data = in.readString((int) getLeft(in));
		}
	}

	public String getData() {
		return data;
	}
}
