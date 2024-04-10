package net.sourceforge.jaad.mp4.boxes.impl.meta;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

public class RequirementBox extends FullBox {

	private String requirement;

	public RequirementBox() {
		super("Requirement Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		requirement = in.readString((int) getLeft(in));
	}

	public String getRequirement() {
		return requirement;
	}
}
