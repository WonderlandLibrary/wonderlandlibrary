package pw.knx.feather.tessellate;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * An automatically resizing implementation of the Tessellator interface.
 * <p>
 * This class directly extends the Basic Tessellator, and leaves most of its
 * behavior intact. The only difference is that this Tessellator grows in
 * capacity automatically should you continue to add vertices past its initial
 * size.
 *
 * @author KNOXDEV
 * @since 8/9/2016 03:00
 */
public class ExpandingTess extends BasicTess {

	/**
	 * The target ratio, between 0 and 1.0, that this Tessellator must hit before it
	 * grows.
	 */
	private final float ratio;

	/**
	 * The factor of which this Tessellator will grow when it hits the ratio.
	 */
	private final float factor;

	/**
	 * Constructs an Expanding Tessellator. See Class Documentation for more
	 * information.
	 *
	 * @param initial
	 *            The total initial capacity, in whole vertices.
	 * @param ratio
	 *            The target ratio, between 0 and 1.0, that this Tessellator must
	 *            hit before it grows.
	 * @param factor
	 *            The factor of which this Tessellator will grow when it hits the
	 *            ratio.
	 */
	ExpandingTess(int initial, float ratio, float factor) {
		super(initial);
		this.ratio = ratio;
		this.factor = factor;
	}

	/**
	 * Enters a vertex of the shape to be rendered. All data fed to the Tessellator
	 * revolves around the vertex data, as it is the only information absolutely
	 * necessary to render a shape.
	 * <p>
	 * If the Tessellator's capacity ratio is reached, it will increase in size
	 * before adding the vertices to the buffer.
	 *
	 * @param x
	 *            The x coordinate of this vertex
	 * @param y
	 *            The y coordinate of this vertex
	 * @param z
	 *            The z coordinate of this vertex
	 * @return The original Tessellator Object
	 */
	@Override
	public Tessellation addVertex(float x, float y, float z) {
		int capacity = raw.length;
		if (index * 6 >= capacity * ratio) { // if we've hit our capacity limit
			capacity *= factor; // raise our limit by the amount specified
			final int[] newBuffer = new int[capacity]; // allocate the new data
			System.arraycopy(raw, 0, newBuffer, 0, raw.length); // transfer the data from the old array to the new array
			raw = newBuffer; // replace the array
			buffer = ByteBuffer.allocateDirect(capacity * 4).order(ByteOrder.nativeOrder()); // allocate a new
																								// corresponding
																								// ByteBuffer
			iBuffer = buffer.asIntBuffer();
			fBuffer = buffer.asFloatBuffer();
		}
		return super.addVertex(x, y, z);
	}
}