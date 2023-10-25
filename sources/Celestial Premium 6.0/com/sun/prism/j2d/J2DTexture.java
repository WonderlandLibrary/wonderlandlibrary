/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseTexture;
import com.sun.prism.impl.ManagedResource;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.j2d.J2DTexturePool;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class J2DTexture
extends BaseTexture<J2DTexResource> {
    private final PixelSetter setter;

    static J2DTexture create(PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n, int n2) {
        PixelSetter<ByteBuffer> pixelSetter;
        int n3;
        switch (pixelFormat) {
            case BYTE_RGB: {
                n3 = 5;
                pixelSetter = ByteBgr.setter;
                break;
            }
            case BYTE_GRAY: {
                n3 = 10;
                pixelSetter = ByteGray.setter;
                break;
            }
            case INT_ARGB_PRE: 
            case BYTE_BGRA_PRE: {
                n3 = 3;
                pixelSetter = IntArgbPre.setter;
                break;
            }
            default: {
                throw new InternalError("Unrecognized PixelFormat (" + pixelFormat + ")!");
            }
        }
        J2DTexturePool j2DTexturePool = J2DTexturePool.instance;
        long l = J2DTexturePool.size(n, n2, n3);
        if (!j2DTexturePool.prepareForAllocation(l)) {
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(n, n2, n3);
        return new J2DTexture(bufferedImage, pixelFormat, pixelSetter, wrapMode);
    }

    J2DTexture(BufferedImage bufferedImage, PixelFormat pixelFormat, PixelSetter pixelSetter, Texture.WrapMode wrapMode) {
        super(new J2DTexResource(bufferedImage), pixelFormat, wrapMode, bufferedImage.getWidth(), bufferedImage.getHeight());
        this.setter = pixelSetter;
    }

    J2DTexture(J2DTexture j2DTexture, Texture.WrapMode wrapMode) {
        super(j2DTexture, wrapMode, false);
        this.setter = j2DTexture.setter;
    }

    @Override
    protected Texture createSharedTexture(Texture.WrapMode wrapMode) {
        return new J2DTexture(this, wrapMode);
    }

    BufferedImage getBufferedImage() {
        return (BufferedImage)((J2DTexResource)this.resource).getResource();
    }

    private static PixelGetter getGetter(PixelFormat pixelFormat) {
        switch (pixelFormat) {
            case BYTE_RGB: {
                return ByteRgb.getter;
            }
            case BYTE_GRAY: {
                return ByteGray.getter;
            }
            case INT_ARGB_PRE: {
                return IntArgbPre.getter;
            }
            case BYTE_BGRA_PRE: {
                return ByteBgraPre.getter;
            }
        }
        throw new InternalError("Unrecognized PixelFormat (" + pixelFormat + ")!");
    }

    private static Buffer getDstBuffer(BufferedImage bufferedImage) {
        if (bufferedImage.getType() == 3) {
            int[] arrn = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
            return IntBuffer.wrap(arrn);
        }
        byte[] arrby = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
        return ByteBuffer.wrap(arrby);
    }

    void updateFromBuffer(BufferedImage bufferedImage, Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        PixelGetter pixelGetter = J2DTexture.getGetter(pixelFormat);
        PixelConverter<Buffer, Buffer> pixelConverter = PixelUtils.getConverter(pixelGetter, this.setter);
        if (PrismSettings.debug) {
            System.out.println("src = [" + n3 + ", " + n4 + "] x [" + n5 + ", " + n6 + "], dst = [" + n + ", " + n2 + "]");
            System.out.println("bimg = " + bufferedImage);
            System.out.println("format = " + pixelFormat + ", buffer = " + buffer);
            System.out.println("getter = " + pixelGetter + ", setter = " + this.setter);
            System.out.println("converter = " + pixelConverter);
        }
        int n8 = bufferedImage.getWidth() * this.setter.getNumElements();
        int n9 = n2 * n8 + n * this.setter.getNumElements();
        if (pixelGetter instanceof IntPixelGetter) {
            n7 /= 4;
        }
        int n10 = buffer.position() + n4 * n7 + n3 * pixelGetter.getNumElements();
        pixelConverter.convert(buffer, n10, n7, J2DTexture.getDstBuffer(bufferedImage), n9, n8, n5, n6);
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        BufferedImage bufferedImage = this.getBufferedImage();
        buffer.position(0);
        this.updateFromBuffer(bufferedImage, buffer, pixelFormat, n, n2, n3, n4, n5, n6, n7);
    }

    @Override
    public void update(MediaFrame object, boolean bl) {
        Object object2;
        object.holdFrame();
        if (object.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
            object2 = object.convertToFormat(PixelFormat.INT_ARGB_PRE);
            object.releaseFrame();
            object = object2;
            if (null == object) {
                return;
            }
        }
        object2 = object.getBufferForPlane(0);
        BufferedImage bufferedImage = this.getBufferedImage();
        this.updateFromBuffer(bufferedImage, ((ByteBuffer)object2).asIntBuffer(), PixelFormat.INT_ARGB_PRE, 0, 0, 0, 0, object.getWidth(), object.getHeight(), object.strideForPlane(0));
        object.releaseFrame();
    }

    static class J2DTexResource
    extends ManagedResource<BufferedImage> {
        public J2DTexResource(BufferedImage bufferedImage) {
            super(bufferedImage, J2DTexturePool.instance);
        }

        @Override
        public void free() {
            ((BufferedImage)this.resource).flush();
        }
    }
}

