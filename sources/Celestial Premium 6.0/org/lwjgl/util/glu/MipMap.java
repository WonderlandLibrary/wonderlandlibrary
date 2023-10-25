/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.PixelStoreState;
import org.lwjgl.util.glu.Util;

public class MipMap
extends Util {
    public static int gluBuild2DMipmaps(int target, int components, int width, int height, int format, int type, ByteBuffer data) {
        ByteBuffer image;
        int h;
        if (width < 1 || height < 1) {
            return 100901;
        }
        int bpp = MipMap.bytesPerPixel(format, type);
        if (bpp == 0) {
            return 100900;
        }
        int maxSize = MipMap.glGetIntegerv(3379);
        int w = MipMap.nearestPower(width);
        if (w > maxSize) {
            w = maxSize;
        }
        if ((h = MipMap.nearestPower(height)) > maxSize) {
            h = maxSize;
        }
        PixelStoreState pss = new PixelStoreState();
        GL11.glPixelStorei(3330, 0);
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3331, 0);
        GL11.glPixelStorei(3332, 0);
        int retVal = 0;
        boolean done = false;
        if (w != width || h != height) {
            image = BufferUtils.createByteBuffer((w + 4) * h * bpp);
            int error = MipMap.gluScaleImage(format, width, height, type, data, w, h, type, image);
            if (error != 0) {
                retVal = error;
                done = true;
            }
            GL11.glPixelStorei(3314, 0);
            GL11.glPixelStorei(3317, 1);
            GL11.glPixelStorei(3315, 0);
            GL11.glPixelStorei(3316, 0);
        } else {
            image = data;
        }
        ByteBuffer bufferA = null;
        ByteBuffer bufferB = null;
        int level = 0;
        while (!done) {
            int newH;
            if (image != data) {
                GL11.glPixelStorei(3314, 0);
                GL11.glPixelStorei(3317, 1);
                GL11.glPixelStorei(3315, 0);
                GL11.glPixelStorei(3316, 0);
            }
            GL11.glTexImage2D(target, level, components, w, h, 0, format, type, image);
            if (w == 1 && h == 1) break;
            int newW = w < 2 ? 1 : w >> 1;
            int n = newH = h < 2 ? 1 : h >> 1;
            ByteBuffer newImage = bufferA == null ? (bufferA = BufferUtils.createByteBuffer((newW + 4) * newH * bpp)) : (bufferB == null ? (bufferB = BufferUtils.createByteBuffer((newW + 4) * newH * bpp)) : bufferB);
            int error = MipMap.gluScaleImage(format, w, h, type, image, newW, newH, type, newImage);
            if (error != 0) {
                retVal = error;
                done = true;
            }
            image = newImage;
            if (bufferB != null) {
                bufferB = bufferA;
            }
            w = newW;
            h = newH;
            ++level;
        }
        pss.save();
        return retVal;
    }

    public static int gluScaleImage(int format, int widthIn, int heightIn, int typein, ByteBuffer dataIn, int widthOut, int heightOut, int typeOut, ByteBuffer dataOut) {
        int j;
        int i;
        int k;
        int sizeout;
        int sizein;
        int components = MipMap.compPerPix(format);
        if (components == -1) {
            return 100900;
        }
        float[] tempIn = new float[widthIn * heightIn * components];
        float[] tempOut = new float[widthOut * heightOut * components];
        switch (typein) {
            case 5121: {
                sizein = 1;
                break;
            }
            case 5126: {
                sizein = 4;
                break;
            }
            default: {
                return 1280;
            }
        }
        switch (typeOut) {
            case 5121: {
                sizeout = 1;
                break;
            }
            case 5126: {
                sizeout = 4;
                break;
            }
            default: {
                return 1280;
            }
        }
        PixelStoreState pss = new PixelStoreState();
        int rowlen = pss.unpackRowLength > 0 ? pss.unpackRowLength : widthIn;
        int rowstride = sizein >= pss.unpackAlignment ? components * rowlen : pss.unpackAlignment / sizein * MipMap.ceil(components * rowlen * sizein, pss.unpackAlignment);
        switch (typein) {
            case 5121: {
                k = 0;
                dataIn.rewind();
                for (i = 0; i < heightIn; ++i) {
                    int ubptr = i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components;
                    for (j = 0; j < widthIn * components; ++j) {
                        tempIn[k++] = dataIn.get(ubptr++) & 0xFF;
                    }
                }
                break;
            }
            case 5126: {
                k = 0;
                dataIn.rewind();
                for (i = 0; i < heightIn; ++i) {
                    int fptr = 4 * (i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components);
                    for (j = 0; j < widthIn * components; ++j) {
                        tempIn[k++] = dataIn.getFloat(fptr);
                        fptr += 4;
                    }
                }
                break;
            }
            default: {
                return 100900;
            }
        }
        float sx = (float)widthIn / (float)widthOut;
        float sy = (float)heightIn / (float)heightOut;
        float[] c = new float[components];
        for (int iy = 0; iy < heightOut; ++iy) {
            for (int ix = 0; ix < widthOut; ++ix) {
                int src;
                int ic;
                int x0 = (int)((float)ix * sx);
                int x1 = (int)((float)(ix + 1) * sx);
                int y0 = (int)((float)iy * sy);
                int y1 = (int)((float)(iy + 1) * sy);
                int readPix = 0;
                for (ic = 0; ic < components; ++ic) {
                    c[ic] = 0.0f;
                }
                for (int ix0 = x0; ix0 < x1; ++ix0) {
                    for (int iy0 = y0; iy0 < y1; ++iy0) {
                        src = (iy0 * widthIn + ix0) * components;
                        for (int ic2 = 0; ic2 < components; ++ic2) {
                            int n = ic2;
                            c[n] = c[n] + tempIn[src + ic2];
                        }
                        ++readPix;
                    }
                }
                int dst = (iy * widthOut + ix) * components;
                if (readPix == 0) {
                    src = (y0 * widthIn + x0) * components;
                    for (ic = 0; ic < components; ++ic) {
                        tempOut[dst++] = tempIn[src + ic];
                    }
                    continue;
                }
                for (k = 0; k < components; ++k) {
                    tempOut[dst++] = c[k] / (float)readPix;
                }
            }
        }
        rowlen = pss.packRowLength > 0 ? pss.packRowLength : widthOut;
        rowstride = sizeout >= pss.packAlignment ? components * rowlen : pss.packAlignment / sizeout * MipMap.ceil(components * rowlen * sizeout, pss.packAlignment);
        switch (typeOut) {
            case 5121: {
                k = 0;
                for (i = 0; i < heightOut; ++i) {
                    int ubptr = i * rowstride + pss.packSkipRows * rowstride + pss.packSkipPixels * components;
                    for (j = 0; j < widthOut * components; ++j) {
                        dataOut.put(ubptr++, (byte)tempOut[k++]);
                    }
                }
                break;
            }
            case 5126: {
                k = 0;
                for (i = 0; i < heightOut; ++i) {
                    int fptr = 4 * (i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components);
                    for (j = 0; j < widthOut * components; ++j) {
                        dataOut.putFloat(fptr, tempOut[k++]);
                        fptr += 4;
                    }
                }
                break;
            }
            default: {
                return 100900;
            }
        }
        return 0;
    }
}

