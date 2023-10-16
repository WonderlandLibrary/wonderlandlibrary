package pw.knx.feather.tessellate;

import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * A standard implementation of the Tessellator interface.
 * <p>
 * The most noteworthy point to make about this Tessellator is that it *will not grow*.
 * Its size is final from the moment it's instantiated. If you attempt to add more data
 * than it can hold, it will throw an error. To create an automatically resizing
 * Tessellator, use the ExpandingTess implementation, also found in this package.
 * <p>
 * The voids in this interface return the Tessellator object for easy method chaining.
 *
 * @author KNOXDEV
 * @since 8/9/2016 03:00
 */
public class BasicTess implements Tessellation {

	/**
	 * Tracks the current index in the total data buffer that we're on
	 */
	int index;

	/**
	 * The raw array of integers that stores vertex information
	 */
	int[] raw;

	/**
	 * A byte buffer mainly utilized as a vehicle for transferring the raw data to OpenGL upon binding
	 */
	ByteBuffer buffer;

	/**
	 * A float buffer view of the main byte buffer
	 */
	FloatBuffer fBuffer;

	/**
	 * A integer buffer view of the main byte buffer
	 */
	IntBuffer iBuffer;

	/**
	 * An integer storing our main color data for this vertex
	 */
	private int colors;

	/**
	 * Floats storing our texture coordinate data for this vertex
	 */
	private float texU, texV;

	/**
	 * Booleans tracking whether this pass will require color or texture to be pushed as well
	 */
	private boolean color, texture;

	/**
	 * Constructs a Basic Tessellator. See Class Documentation for more information.
	 *
	 * @param capacity The total capacity, in whole verticies, that this Tessellator will be able to render at once.
	 */
	BasicTess(int capacity) {
		/** Why times 6? Because 6 is how much space (in integers) that each vertex
		 * takes up in the buffer, since each vertex stores color and texture as well. */
		capacity *= 6;
		this.raw = new int[capacity];
		this.buffer = ByteBuffer.allocateDirect(capacity * 4).order(ByteOrder.nativeOrder()); // 4 bytes in an integer!
		this.fBuffer = this.buffer.asFloatBuffer();
		this.iBuffer = this.buffer.asIntBuffer();
	}

	/**
	 * @param color The color to associate the upcoming vertex data with
	 * @return The original Tessellator Object
	 */
	public Tessellation setColor(int color) {
		this.color = true;
		this.colors = color;
		return this;
	}

	/**
	 * Set the texture coordinates to associate the upcoming vertex data with.
	 *
	 * @param u The x starting coordinate
	 * @param v The y starting coordinate
	 * @return The original Tessellator Object
	 */
	public Tessellation setTexture(float u, float v) {
		this.texture = true;
		this.texU = u;
		this.texV = v;
		return this;
	}

	/**
	 * Enters a vertex of the shape to be rendered.
	 * All data fed to the Tessellator revolves around the vertex data,
	 * as it is the only information absolutely necessary to render a shape.
	 *
	 * @param x The x coordinate of this vertex
	 * @param y The y coordinate of this vertex
	 * @param z The z coordinate of this vertex
	 * @return The original Tessellator Object
	 */
	public Tessellation addVertex(float x, float y, float z) {
		final int dex = this.index * 6;
		this.raw[dex] = Float.floatToRawIntBits(x);
		this.raw[dex + 1] = Float.floatToRawIntBits(y);
		this.raw[dex + 2] = Float.floatToRawIntBits(z);
		this.raw[dex + 3] = this.colors;
		this.raw[dex + 4] = Float.floatToRawIntBits(this.texU);
		this.raw[dex + 5] = Float.floatToRawIntBits(this.texV);
		this.index++;
		return this;
	}

	/**
	 * The first stage of rendering.
	 * Binds (finalizes) the current rendering data stored in the buffer for drawing.
	 * This must be executed before you perform a rendering pass.
	 *
	 * @return The original Tessellator Object
	 */
	public Tessellation bind() {
		final int dex = this.index * 6;
		this.iBuffer.put(this.raw, 0, dex);
		this.buffer.position(0);
		this.buffer.limit(dex * 4);
		if (this.color) {                           // if we've entered color data, push color data down the pipeline.
			this.buffer.position(12);
			GL11.glColorPointer(4, true, 24, this.buffer);
		}
		if (this.texture) {                         // if we've entered texture data, push texture data down the pipeline.
			this.fBuffer.position(4);
			GL11.glTexCoordPointer(2, 24, this.fBuffer);
		}
		this.fBuffer.position(0);
		GL11.glVertexPointer(3, 24, this.fBuffer); // pushing bare minimum vertex data.
		return this;
	}

	/**
	 * The second stage of rendering.
	 * Performs a rendering pass with the data bound to the buffer.
	 * If the data is not bound first, this method will fail.
	 * Otherwise, you can render the data for as many passes as you please.
	 * <p>
	 * This particular implementation uses VertexArrayObjects.
	 *
	 * @param mode The OpenGL mode to render the data with
	 * @return The original Tessellator Object
	 */
	public Tessellation pass(int mode) {
		GL11.glDrawArrays(mode, 0, this.index);
		return this;
	}

	/**
	 * Resets the buffer without clearing the data.
	 * This will allow you to stop using the Tessellator for a bit
	 * while still preserving its contents, you'll be able to continue
	 * to add vertices, rebind it later, and render it again.
	 *
	 * @return The original Tessellator Object
	 */
	public Tessellation unbind() {
		this.iBuffer.position(0);
		return this;
	}

	/**
	 * The third and final stage of rendering.
	 * Clears up the buffer and resets the Tessellator so it can
	 * be used again with new data. Passes can no longer be made
	 * after this method is executed.
	 *
	 * @return The original Tessellator Object
	 */
	public Tessellation reset() {
		this.iBuffer.clear();
		this.index = 0;
		this.color = false;
		this.texture = false;
		return this;
	}
}
