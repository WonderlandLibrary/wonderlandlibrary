package net.sourceforge.jaad.mp4.boxes.impl;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;
import net.sourceforge.jaad.mp4.od.Descriptor;

/**
 * The IPMP Control Box may contain IPMP descriptors which may be referenced by
 * any stream in the file.
 *
 * The IPMP ToolListDescriptor is defined in ISO/IEC 14496-1, which conveys the
 * list of IPMP tools required to access the media streams in an ISO Base Media
 * File or meta-box, and may include a list of alternate IPMP tools or
 * parametric descriptions of tools required to access the content.
 * 
 * The presence of IPMP Descriptor in this IPMPControlBox indicates that media
 * streams within the file or meta-box are protected by the IPMP Tool described
 * in the IPMP Descriptor. More than one IPMP Descriptors can be carried here,
 * if there are more than one IPMP Tools providing the global governance.
 *
 * @author in-somnia
 */
public class IPMPControlBox extends FullBox {

	private /*IPMPToolList*/Descriptor toolList;
	private /*IPMP*/Descriptor[] ipmpDescriptors;

	public IPMPControlBox() {
		super("IPMP Control Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);

		toolList = /*(IPMPToolListDescriptor)*/ Descriptor.createDescriptor(in);

		final int count = in.read();

		ipmpDescriptors = new Descriptor[count];
		for(int i = 0; i<count; i++) {
			ipmpDescriptors[i] = /*(IPMPDescriptor)*/ Descriptor.createDescriptor(in);
		}
	}

	/**
	 * The toollist is an IPMP ToolListDescriptor as defined in ISO/IEC 14496-1.
	 *
	 * @return the toollist
	 */
	public Descriptor getToolList() {
		return toolList;
	}

	/**
	 * The list of contained IPMP Descriptors.
	 *
	 * @return the IPMP descriptors
	 */
	public Descriptor[] getIPMPDescriptors() {
		return ipmpDescriptors;
	}
}
