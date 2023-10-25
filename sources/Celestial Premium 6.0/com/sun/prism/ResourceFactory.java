/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.shape.ShapeRep;

public interface ResourceFactory
extends GraphicsResource {
    public boolean isDisposed();

    public boolean isDeviceReady();

    public TextureResourcePool getTextureResourcePool();

    public Texture createTexture(Image var1, Texture.Usage var2, Texture.WrapMode var3);

    public Texture createTexture(Image var1, Texture.Usage var2, Texture.WrapMode var3, boolean var4);

    public Texture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5);

    public Texture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5, boolean var6);

    public Texture createTexture(MediaFrame var1);

    public Texture getCachedTexture(Image var1, Texture.WrapMode var2);

    public Texture getCachedTexture(Image var1, Texture.WrapMode var2, boolean var3);

    public boolean isFormatSupported(PixelFormat var1);

    public boolean isWrapModeSupported(Texture.WrapMode var1);

    public int getMaximumTextureSize();

    public int getRTTWidth(int var1, Texture.WrapMode var2);

    public int getRTTHeight(int var1, Texture.WrapMode var2);

    public Texture createMaskTexture(int var1, int var2, Texture.WrapMode var3);

    public Texture createFloatTexture(int var1, int var2);

    public RTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3);

    public RTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3, boolean var4);

    public boolean isCompatibleTexture(Texture var1);

    public Presentable createPresentable(PresentableState var1);

    public ShapeRep createPathRep();

    public ShapeRep createRoundRectRep();

    public ShapeRep createEllipseRep();

    public ShapeRep createArcRep();

    public void addFactoryListener(ResourceFactoryListener var1);

    public void removeFactoryListener(ResourceFactoryListener var1);

    public void setRegionTexture(Texture var1);

    public Texture getRegionTexture();

    public void setGlyphTexture(Texture var1);

    public Texture getGlyphTexture();

    public boolean isSuperShaderAllowed();

    public PhongMaterial createPhongMaterial();

    public MeshView createMeshView(Mesh var1);

    public Mesh createMesh();
}

