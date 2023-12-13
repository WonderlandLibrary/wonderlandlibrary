/*
 * Copyright (c) 2008, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name "TwelveMonkeys" nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package me.robbanrobbin.jigsaw.client.tools;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * This class contains methods for basic image manipulation and conversion.
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haku $
 * @version $Id: common/common-image/src/main/java/com/twelvemonkeys/image/ImageUtil.java#3 $
 */
public final class ImageUtil {
    // TODO: Split palette generation out, into ColorModel classes (?)
	
	public static BufferedImage createBuffered(int pWidth, int pHeight, int pType, int pTransparency,
			GraphicsConfiguration pConfiguration) {
		if (VM_SUPPORTS_ACCELERATION && pType == BI_TYPE_ANY) {
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			if (supportsAcceleration(env)) {
				return getConfiguration(pConfiguration).createCompatibleImage(pWidth, pHeight, pTransparency);
			}
		}

		return new BufferedImage(pWidth, pHeight, getImageType(pType, pTransparency));
	}

	private static GraphicsConfiguration getConfiguration(final GraphicsConfiguration pConfiguration) {
		return pConfiguration != null ? pConfiguration : DEFAULT_CONFIGURATION;
	}

	private static int getImageType(int pType, int pTransparency) {
		// TODO: Handle TYPE_CUSTOM?
		if (pType != BI_TYPE_ANY) {
			return pType;
		} else {
			switch (pTransparency) {
			case Transparency.OPAQUE:
				return BufferedImage.TYPE_INT_RGB;
			case Transparency.BITMASK:
			case Transparency.TRANSLUCENT:
				return BufferedImage.TYPE_INT_ARGB;
			default:
				throw new IllegalArgumentException("Unknown transparency type: " + pTransparency);
			}
		}
	}

    public final static int ROTATE_90_CCW = -90;
    public final static int ROTATE_90_CW = 90;
    public final static int ROTATE_180 = 180;

    public final static int FLIP_VERTICAL = -1;
    public final static int FLIP_HORIZONTAL = 1;

    /**
     * Alias for {@link ConvolveOp#EDGE_ZERO_FILL}.
     * @see #convolve(java.awt.image.BufferedImage, java.awt.image.Kernel, int)
     * @see #EDGE_REFLECT
     */
    public static final int EDGE_ZERO_FILL = ConvolveOp.EDGE_ZERO_FILL;

    /**
     * Alias for {@link ConvolveOp#EDGE_NO_OP}.
     * @see #convolve(java.awt.image.BufferedImage, java.awt.image.Kernel, int)
     * @see #EDGE_REFLECT
     */
    public static final int EDGE_NO_OP = ConvolveOp.EDGE_NO_OP;

    /**
     * Adds a border to the image while convolving. The border will reflect the
     * edges of the original image. This is usually a good default.
     * Note that while this mode typically provides better quality than the
     * standard modes {@code EDGE_ZERO_FILL} and {@code EDGE_NO_OP}, it does so
     * at the expense of higher memory consumption and considerable more computation.
     * @see #convolve(java.awt.image.BufferedImage, java.awt.image.Kernel, int)
     */
    public static final int EDGE_REFLECT = 2; // as JAI BORDER_REFLECT

    /**
     * Adds a border to the image while convolving. The border will wrap the
     * edges of the original image. This is usually the best choice for tiles.
     * Note that while this mode typically provides better quality than the
     * standard modes {@code EDGE_ZERO_FILL} and {@code EDGE_NO_OP}, it does so
     * at the expense of higher memory consumption and considerable more computation.
     * @see #convolve(java.awt.image.BufferedImage, java.awt.image.Kernel, int)
     * @see #EDGE_REFLECT
     */
    public static final int EDGE_WRAP = 3; // as JAI BORDER_WRAP

    /** Passed to the createXxx methods, to indicate that the type does not matter */
    private final static int BI_TYPE_ANY = -1;
    /*
    public final static int BI_TYPE_ANY_TRANSLUCENT = -1;
    public final static int BI_TYPE_ANY_BITMASK = -2;
    public final static int BI_TYPE_ANY_OPAQUE = -3;*/

    /** Tells wether this WM may support acceleration of some images */
    private static boolean VM_SUPPORTS_ACCELERATION = true;

    /** The sharpen matrix */
    private static final float[] SHARPEN_MATRIX = new float[] {
            0.0f, -0.3f, 0.0f,
            -0.3f, 2.2f, -0.3f,
            0.0f, -0.3f, 0.0f
    };

    /**
     * The sharpen kernel. Uses the following 3 by 3 matrix:
     * <TABLE border="1" cellspacing="0">
     * <TR><TD>0.0</TD><TD>-0.3</TD><TD>0.0</TD></TR>
     * <TR><TD>-0.3</TD><TD>2.2</TD><TD>-0.3</TD></TR>
     * <TR><TD>0.0</TD><TD>-0.3</TD><TD>0.0</TD></TR>
     * </TABLE>
     */
    private static final Kernel SHARPEN_KERNEL = new Kernel(3, 3, SHARPEN_MATRIX);

    /**
     * Component that can be used with the MediaTracker etc.
     */
    private static final Component NULL_COMPONENT = new Component() {};

    /** Our static image tracker */
    private static MediaTracker sTracker = new MediaTracker(NULL_COMPONENT);

    /** */
    protected static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();
    /** */
    protected static final Point LOCATION_UPPER_LEFT = new Point(0, 0);

    /** */
    private static final GraphicsConfiguration DEFAULT_CONFIGURATION = getDefaultGraphicsConfiguration();

    private static GraphicsConfiguration getDefaultGraphicsConfiguration() {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            if (!env.isHeadlessInstance()) {
                return env.getDefaultScreenDevice().getDefaultConfiguration();
            }
        }
        catch (LinkageError e) {
            // Means we are not in a 1.4+ VM, so skip testing for headless again
            VM_SUPPORTS_ACCELERATION = false;
        }
        return null;
    }
    /**
     * Tests if the given {@code GraphicsEnvironment} supports accelleration
     *
     * @param pEnv the environment
     * @return {@code true} if the {@code GraphicsEnvironment} supports
     * acceleration
     */
    private static boolean supportsAcceleration(GraphicsEnvironment pEnv) {
        try {
            // Acceleration only supported in non-headless environments, on 1.4+ VMs
            return /*VM_SUPPORTS_ACCELERATION &&*/ !pEnv.isHeadlessInstance();
        }
        catch (LinkageError ignore) {
            // Means we are not in a 1.4+ VM, so skip testing for headless again
            VM_SUPPORTS_ACCELERATION = false;
        }

        // If the invocation fails, assume no accelleration is possible
        return false;
}
    
}