package net.sourceforge.jaad.mp4.boxes.impl;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.BoxTypes;
import net.sourceforge.jaad.mp4.boxes.FullBox;

/**
 * This box contains the degradation priority of each sample. The values are
 * stored in the table, one for each sample. Specifications derived from this
 * define the exact meaning and acceptable range of the priority field.
 * 
 * @author in-somnia
 */
public class DegradationPriorityBox extends FullBox {

	private int[] priorities;

	public DegradationPriorityBox() {
		super("Degradation Priority Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		//get number of samples from SampleSizeBox
		final int sampleCount = ((SampleSizeBox) parent.getChild(BoxTypes.SAMPLE_SIZE_BOX)).getSampleCount();

		priorities = new int[sampleCount];
		for(int i = 0; i<sampleCount; i++) {
			priorities[i] = (int) in.readBytes(2);
		}
	}

	/**
	 * The priority is integer specifying the degradation priority for each
	 * sample.
	 * @return the list of priorities
	 */
	public int[] getPriorities() {
		return priorities;
	}
}
