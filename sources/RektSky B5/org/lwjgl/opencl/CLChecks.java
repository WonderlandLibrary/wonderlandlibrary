/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

final class CLChecks {
    private CLChecks() {
    }

    static int calculateBufferRectSize(PointerBuffer offset, PointerBuffer region, long row_pitch, long slice_pitch) {
        if (!LWJGLUtil.CHECKS) {
            return 0;
        }
        long x2 = offset.get(0);
        long y2 = offset.get(1);
        long z2 = offset.get(2);
        if (LWJGLUtil.DEBUG && (x2 < 0L || y2 < 0L || z2 < 0L)) {
            throw new IllegalArgumentException("Invalid cl_mem host offset: " + x2 + ", " + y2 + ", " + z2);
        }
        long w2 = region.get(0);
        long h2 = region.get(1);
        long d2 = region.get(2);
        if (LWJGLUtil.DEBUG && (w2 < 1L || h2 < 1L || d2 < 1L)) {
            throw new IllegalArgumentException("Invalid cl_mem rectangle region dimensions: " + w2 + " x " + h2 + " x " + d2);
        }
        if (row_pitch == 0L) {
            row_pitch = w2;
        } else if (LWJGLUtil.DEBUG && row_pitch < w2) {
            throw new IllegalArgumentException("Invalid host row pitch specified: " + row_pitch);
        }
        if (slice_pitch == 0L) {
            slice_pitch = row_pitch * h2;
        } else if (LWJGLUtil.DEBUG && slice_pitch < row_pitch * h2) {
            throw new IllegalArgumentException("Invalid host slice pitch specified: " + slice_pitch);
        }
        return (int)(z2 * slice_pitch + y2 * row_pitch + x2 + w2 * h2 * d2);
    }

    static int calculateImageSize(PointerBuffer region, long row_pitch, long slice_pitch) {
        if (!LWJGLUtil.CHECKS) {
            return 0;
        }
        long w2 = region.get(0);
        long h2 = region.get(1);
        long d2 = region.get(2);
        if (LWJGLUtil.DEBUG && (w2 < 1L || h2 < 1L || d2 < 1L)) {
            throw new IllegalArgumentException("Invalid cl_mem image region dimensions: " + w2 + " x " + h2 + " x " + d2);
        }
        if (row_pitch == 0L) {
            row_pitch = w2;
        } else if (LWJGLUtil.DEBUG && row_pitch < w2) {
            throw new IllegalArgumentException("Invalid row pitch specified: " + row_pitch);
        }
        if (slice_pitch == 0L) {
            slice_pitch = row_pitch * h2;
        } else if (LWJGLUtil.DEBUG && slice_pitch < row_pitch * h2) {
            throw new IllegalArgumentException("Invalid slice pitch specified: " + slice_pitch);
        }
        return (int)(slice_pitch * d2);
    }

    static int calculateImage2DSize(ByteBuffer format, long w2, long h2, long row_pitch) {
        if (!LWJGLUtil.CHECKS) {
            return 0;
        }
        if (LWJGLUtil.DEBUG && (w2 < 1L || h2 < 1L)) {
            throw new IllegalArgumentException("Invalid 2D image dimensions: " + w2 + " x " + h2);
        }
        int elementSize = CLChecks.getElementSize(format);
        if (row_pitch == 0L) {
            row_pitch = w2 * (long)elementSize;
        } else if (LWJGLUtil.DEBUG && (row_pitch < w2 * (long)elementSize || row_pitch % (long)elementSize != 0L)) {
            throw new IllegalArgumentException("Invalid image_row_pitch specified: " + row_pitch);
        }
        return (int)(row_pitch * h2);
    }

    static int calculateImage3DSize(ByteBuffer format, long w2, long h2, long d2, long row_pitch, long slice_pitch) {
        if (!LWJGLUtil.CHECKS) {
            return 0;
        }
        if (LWJGLUtil.DEBUG && (w2 < 1L || h2 < 1L || d2 < 2L)) {
            throw new IllegalArgumentException("Invalid 3D image dimensions: " + w2 + " x " + h2 + " x " + d2);
        }
        int elementSize = CLChecks.getElementSize(format);
        if (row_pitch == 0L) {
            row_pitch = w2 * (long)elementSize;
        } else if (LWJGLUtil.DEBUG && (row_pitch < w2 * (long)elementSize || row_pitch % (long)elementSize != 0L)) {
            throw new IllegalArgumentException("Invalid image_row_pitch specified: " + row_pitch);
        }
        if (slice_pitch == 0L) {
            slice_pitch = row_pitch * h2;
        } else if (LWJGLUtil.DEBUG && (row_pitch < row_pitch * h2 || slice_pitch % row_pitch != 0L)) {
            throw new IllegalArgumentException("Invalid image_slice_pitch specified: " + row_pitch);
        }
        return (int)(slice_pitch * d2);
    }

    private static int getElementSize(ByteBuffer format) {
        int channelOrder = format.getInt(format.position() + 0);
        int channelType = format.getInt(format.position() + 4);
        return CLChecks.getChannelCount(channelOrder) * CLChecks.getChannelSize(channelType);
    }

    private static int getChannelCount(int channelOrder) {
        switch (channelOrder) {
            case 4272: 
            case 4273: 
            case 4280: 
            case 4281: 
            case 4282: {
                return 1;
            }
            case 4274: 
            case 4275: 
            case 4283: {
                return 2;
            }
            case 4276: 
            case 4284: {
                return 3;
            }
            case 4277: 
            case 4278: 
            case 4279: {
                return 4;
            }
        }
        throw new IllegalArgumentException("Invalid cl_channel_order specified: " + LWJGLUtil.toHexString(channelOrder));
    }

    private static int getChannelSize(int channelType) {
        switch (channelType) {
            case 4304: 
            case 4306: 
            case 4311: 
            case 4314: {
                return 1;
            }
            case 4305: 
            case 4307: 
            case 4308: 
            case 4309: 
            case 4312: 
            case 4315: 
            case 4317: {
                return 2;
            }
            case 4310: 
            case 4313: 
            case 4316: 
            case 4318: {
                return 4;
            }
        }
        throw new IllegalArgumentException("Invalid cl_channel_type specified: " + LWJGLUtil.toHexString(channelType));
    }
}

