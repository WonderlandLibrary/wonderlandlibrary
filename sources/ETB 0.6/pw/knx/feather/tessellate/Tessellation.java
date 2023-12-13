package pw.knx.feather.tessellate;

import java.awt.Color;

/**
 * A standard abstract interface for an OpenGL Tessellator.
 * This abstraction supports purely vertices, texture, and color.
 * The voids in this interface return the Tessellator object for easy method chaining
 *
 * @author KNOXDEV
 * @since 8/9/2016 01:46
 */
public interface Tessellation {


	/*
	 * Setters - Setting and adding values to this tessellator
	 */

	/**
	 * @param color The color to associate the upcoming vertex data with
	 *              NOTE: Must be in ABGR format
	 * @return The original Tessellator Object
	 */
	Tessellation setColor(int color);

	/**
	 * @param color The color to associate the upcoming vertex data with
	 * @return The original Tessellator Object
	 */
	default Tessellation setColor(Color color) {
		return setColor(new Color(255,255,255));
	}

	/**
	 * Set the texture coordinates to associate the upcoming vertex data with.
	 *
	 * @param u The x starting coordinate
	 * @param v The y starting coordinate
	 * @return The original Tessellator Object
	 */
	Tessellation setTexture(float u, float v);

	/**
	 * Enters a vertex of the shape to be rendered.
	 * All data fed to the Tessellator relies on the vertex data,
	 * as it is the only information absolutely necessary to render a shape.
	 *
	 * @param x The x coordinate of this vertex
	 * @param y The y coordinate of this vertex
	 * @param z The z coordinate of this vertex
	 * @return The original Tessellator Object
	 */
	Tessellation addVertex(float x, float y, float z);


	/*
	 * Render Commands - These provide fine-tuned control over the
	 * three-stage render process, if needed
	 */

	/**
	 * The first stage of rendering.
	 * <p>
	 * Binds (finalizes) the current rendering data stored in the buffer for drawing.
	 * This must be executed before you perform a rendering pass.
	 *
	 * @return The original Tessellator Object
	 */
	Tessellation bind();

	/**
	 * The second stage of rendering.
	 * <p>
	 * Performs a rendering pass with the data bound to the buffer.
	 * If the data is not bound first, this method will fail.
	 * Otherwise, you can render the data for as many passes as you please.
	 *
	 * @param mode The OpenGL mode to render the data with
	 * @return The original Tessellator Object
	 */
	Tessellation pass(int mode);

	/**
	 * The third and final stage of rendering.
	 * <p>
	 * Clears up the buffer and resets the Tessellator so it can
	 * be used again with new data. Passes can no longer be made
	 * after this method is executed.
	 *
	 * @return The original Tessellator Object
	 */
	Tessellation reset();

	/**
	 * Resets the buffer without clearing the data.
	 * This will allow you to stop using the Tessellator for a bit
	 * while still preserving its current contents. This means you
	 * can unbind this Tessellator, continue to add vertices, and
	 * reuse it later with the old data + the new information.
	 *
	 * @return The original Tessellator Object
	 */
	Tessellation unbind();

	/**
	 * Performs all three rendering stages in one method.
	 * This method cannot be run more than once without entering new data,
	 * due to the fact it resets the buffer.
	 *
	 * @param mode The OpenGL mode to render the data with
	 * @return The original Tessellator Object
	 */
	default Tessellation draw(int mode) {
		return this.bind().pass(mode).reset();
	}


	/*
	 * Static Constructors - allows intuitive initialization of a Tessellator to fit any purpose
	 */

	/**
	 * Creates an <i>immutable</i> Tessellator that will not grow past its initial capacity
	 *
	 * @param size the initial (and final) capacity of this Tessellator
	 * @return the requested Basic Tessellator
	 */
	static Tessellation createBasic(int size) {
		return new BasicTess(size);
	}

	/**
	 * Creates a growing Tessellator that will increase in capacity as its limit is reached
	 *
	 * @param size the initial capacity of this Tessellator
	 * @param ratio   The target ratio, between 0 and 1.0, that this Tessellator must hit before it grows.
	 * @param factor  The factor of which this Tessellator will grow when it hits the ratio.
	 * @return the requested Basic Tessellator
	 */
	static Tessellation createExpanding(int size, float ratio, float factor) {
		return new ExpandingTess(size, ratio, factor);
	}
}