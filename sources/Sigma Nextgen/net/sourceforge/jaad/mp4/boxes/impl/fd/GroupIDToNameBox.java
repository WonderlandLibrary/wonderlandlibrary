package net.sourceforge.jaad.mp4.boxes.impl.fd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

public class GroupIDToNameBox extends FullBox {

	private final Map<Long, String> map;

	public GroupIDToNameBox() {
		super("Group ID To Name Box");
		map = new HashMap<Long, String>();
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		final int entryCount = (int) in.readBytes(2);
		long id;
		String name;
		for(int i = 0; i<entryCount; i++) {
			id = in.readBytes(4);
			name = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
			map.put(id, name);
		}
	}

	/**
	 * Returns the map that contains the ID-name-pairs for all groups.
	 *
	 * @return the ID to name map
	 */
	public Map<Long, String> getMap() {
		return map;
	}
}
