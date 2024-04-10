package net.sourceforge.jaad.mp4.boxes.impl.oma;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

public class OMAAccessUnitFormatBox extends FullBox {

	private boolean selectiveEncrypted;
	private int keyIndicatorLength, initialVectorLength;

	public OMAAccessUnitFormatBox() {
		super("OMA DRM Access Unit Format Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		//1 bit selective encryption, 7 bits reserved
		selectiveEncrypted = ((in.read()>>7)&1)==1;
		keyIndicatorLength = in.read(); //always zero?
		initialVectorLength = in.read();
	}

	public boolean isSelectiveEncrypted() {
		return selectiveEncrypted;
	}

	public int getKeyIndicatorLength() {
		return keyIndicatorLength;
	}

	public int getInitialVectorLength() {
		return initialVectorLength;
	}
}
