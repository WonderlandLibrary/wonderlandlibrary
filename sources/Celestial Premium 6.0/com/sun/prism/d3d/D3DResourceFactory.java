/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.MultiTexture;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.Texture;
import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DFrameStats;
import com.sun.prism.d3d.D3DMesh;
import com.sun.prism.d3d.D3DMeshView;
import com.sun.prism.d3d.D3DPhongMaterial;
import com.sun.prism.d3d.D3DPipeline;
import com.sun.prism.d3d.D3DRTTexture;
import com.sun.prism.d3d.D3DResource;
import com.sun.prism.d3d.D3DShader;
import com.sun.prism.d3d.D3DSwapChain;
import com.sun.prism.d3d.D3DTexture;
import com.sun.prism.d3d.D3DVramPool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.ps.BaseShaderFactory;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

class D3DResourceFactory
extends BaseShaderFactory {
    private static final Map<Image, Texture> clampTexCache = new WeakHashMap<Image, Texture>();
    private static final Map<Image, Texture> repeatTexCache = new WeakHashMap<Image, Texture>();
    private static final Map<Image, Texture> mipmapTexCache = new WeakHashMap<Image, Texture>();
    private final D3DContext context;
    private final int maxTextureSize;
    private final LinkedList<D3DResource.D3DRecord> records = new LinkedList();
    static final int STATS_FREQUENCY = PrismSettings.prismStatFrequency;
    private int nFrame = -1;
    private D3DFrameStats frameStats;

    D3DResourceFactory(long l, Screen screen) {
        super(clampTexCache, repeatTexCache, mipmapTexCache);
        this.context = new D3DContext(l, screen, this);
        this.context.initState();
        this.maxTextureSize = this.computeMaxTextureSize();
        if (PrismSettings.noClampToZero && PrismSettings.verbose) {
            System.out.println("prism.noclamptozero not supported by D3D");
        }
    }

    D3DContext getContext() {
        return this.context;
    }

    @Override
    public TextureResourcePool getTextureResourcePool() {
        return D3DVramPool.instance;
    }

    private void displayPrismStatistics() {
        if (STATS_FREQUENCY > 0 && ++this.nFrame == STATS_FREQUENCY) {
            this.nFrame = 0;
            this.frameStats = this.context.getFrameStats(true, this.frameStats);
            if (this.frameStats != null) {
                System.err.println(this.frameStats.toDebugString(STATS_FREQUENCY));
            }
        }
    }

    @Override
    public boolean isDeviceReady() {
        if (this.isDisposed()) {
            return false;
        }
        this.displayPrismStatistics();
        return this.context.testLostStateAndReset();
    }

    static int nextPowerOfTwo(int n, int n2) {
        int n3;
        if (n > n2) {
            return 0;
        }
        for (n3 = 1; n3 < n; n3 *= 2) {
        }
        return n3;
    }

    @Override
    public boolean isCompatibleTexture(Texture texture) {
        return texture instanceof D3DTexture;
    }

    @Override
    public D3DTexture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int n, int n2) {
        return this.createTexture(pixelFormat, usage, wrapMode, n, n2, false);
    }

    @Override
    public D3DTexture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int n, int n2, boolean bl) {
        int n3;
        int n4;
        if (this.checkDisposed()) {
            return null;
        }
        if (!this.isFormatSupported(pixelFormat)) {
            throw new UnsupportedOperationException("Pixel format " + pixelFormat + " not supported on this device");
        }
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new UnsupportedOperationException("MULTI_YCbCr_420 textures require a MediaFrame");
        }
        if (PrismSettings.forcePow2) {
            n4 = D3DResourceFactory.nextPowerOfTwo(n, Integer.MAX_VALUE);
            n3 = D3DResourceFactory.nextPowerOfTwo(n2, Integer.MAX_VALUE);
        } else {
            n4 = n;
            n3 = n2;
        }
        D3DVramPool d3DVramPool = D3DVramPool.instance;
        long l = d3DVramPool.estimateTextureSize(n4, n3, pixelFormat);
        if (!d3DVramPool.prepareForAllocation(l)) {
            return null;
        }
        long l2 = D3DResourceFactory.nCreateTexture(this.context.getContextHandle(), pixelFormat.ordinal(), usage.ordinal(), false, n4, n3, 0, bl);
        if (l2 == 0L) {
            return null;
        }
        int n5 = D3DResourceFactory.nGetTextureWidth(l2);
        int n6 = D3DResourceFactory.nGetTextureHeight(l2);
        if (wrapMode != Texture.WrapMode.CLAMP_NOT_NEEDED && (n < n5 || n2 < n6)) {
            wrapMode = wrapMode.simulatedVersion();
        }
        return new D3DTexture(this.context, pixelFormat, wrapMode, l2, n5, n6, n, n2, bl);
    }

    @Override
    public Texture createTexture(MediaFrame mediaFrame) {
        if (this.checkDisposed()) {
            return null;
        }
        mediaFrame.holdFrame();
        int n = mediaFrame.getWidth();
        int n2 = mediaFrame.getHeight();
        int n3 = mediaFrame.getEncodedWidth();
        int n4 = mediaFrame.getEncodedHeight();
        PixelFormat pixelFormat = mediaFrame.getPixelFormat();
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            MultiTexture multiTexture = new MultiTexture(pixelFormat, Texture.WrapMode.CLAMP_TO_EDGE, n, n2);
            for (int i = 0; i < mediaFrame.planeCount(); ++i) {
                D3DTexture d3DTexture;
                int n5 = n3;
                int n6 = n4;
                if (i == 2 || i == 1) {
                    n5 /= 2;
                    n6 /= 2;
                }
                if ((d3DTexture = this.createTexture(PixelFormat.BYTE_ALPHA, Texture.Usage.DYNAMIC, Texture.WrapMode.CLAMP_TO_EDGE, n5, n6)) == null) {
                    multiTexture.dispose();
                    return null;
                }
                multiTexture.setTexture(d3DTexture, i);
            }
            mediaFrame.releaseFrame();
            return multiTexture;
        }
        D3DVramPool d3DVramPool = D3DVramPool.instance;
        long l = d3DVramPool.estimateTextureSize(n3, n4, pixelFormat);
        if (!d3DVramPool.prepareForAllocation(l)) {
            return null;
        }
        long l2 = D3DResourceFactory.nCreateTexture(this.context.getContextHandle(), pixelFormat.ordinal(), Texture.Usage.DYNAMIC.ordinal(), false, n3, n4, 0, false);
        if (0L == l2) {
            return null;
        }
        int n7 = D3DResourceFactory.nGetTextureWidth(l2);
        int n8 = D3DResourceFactory.nGetTextureHeight(l2);
        Texture.WrapMode wrapMode = n3 < n7 || n4 < n8 ? Texture.WrapMode.CLAMP_TO_EDGE_SIMULATED : Texture.WrapMode.CLAMP_TO_EDGE;
        D3DTexture d3DTexture = new D3DTexture(this.context, pixelFormat, wrapMode, l2, n7, n8, n, n2, false);
        mediaFrame.releaseFrame();
        return d3DTexture;
    }

    @Override
    public int getRTTWidth(int n, Texture.WrapMode wrapMode) {
        return n;
    }

    @Override
    public int getRTTHeight(int n, Texture.WrapMode wrapMode) {
        return n;
    }

    @Override
    public D3DRTTexture createRTTexture(int n, int n2, Texture.WrapMode wrapMode) {
        return this.createRTTexture(n, n2, wrapMode, false);
    }

    @Override
    public D3DRTTexture createRTTexture(int n, int n2, Texture.WrapMode wrapMode, boolean bl) {
        int n3;
        if (this.checkDisposed()) {
            return null;
        }
        if (PrismSettings.verbose && this.context.isLost()) {
            System.err.println("RT Texture allocation while the device is lost");
        }
        int n4 = n;
        int n5 = n2;
        int n6 = 0;
        int n7 = 0;
        if (PrismSettings.forcePow2) {
            n4 = D3DResourceFactory.nextPowerOfTwo(n4, Integer.MAX_VALUE);
            n5 = D3DResourceFactory.nextPowerOfTwo(n5, Integer.MAX_VALUE);
        }
        D3DVramPool d3DVramPool = D3DVramPool.instance;
        int n8 = bl ? ((n3 = D3DPipeline.getInstance().getMaxSamples()) < 2 ? 0 : (n3 < 4 ? 2 : 4)) : 0;
        long l = d3DVramPool.estimateRTTextureSize(n, n2, false);
        if (!d3DVramPool.prepareForAllocation(l)) {
            return null;
        }
        long l2 = D3DResourceFactory.nCreateTexture(this.context.getContextHandle(), PixelFormat.INT_ARGB_PRE.ordinal(), Texture.Usage.DEFAULT.ordinal(), true, n4, n5, n8, false);
        if (l2 == 0L) {
            return null;
        }
        int n9 = D3DResourceFactory.nGetTextureWidth(l2);
        int n10 = D3DResourceFactory.nGetTextureHeight(l2);
        D3DRTTexture d3DRTTexture = new D3DRTTexture(this.context, wrapMode, l2, n9, n10, n6, n7, n, n2, n8);
        d3DRTTexture.createGraphics().clear();
        return d3DRTTexture;
    }

    @Override
    public Presentable createPresentable(PresentableState presentableState) {
        long l;
        if (this.checkDisposed()) {
            return null;
        }
        if (PrismSettings.verbose && this.context.isLost()) {
            System.err.println("SwapChain allocation while the device is lost");
        }
        if ((l = D3DResourceFactory.nCreateSwapChain(this.context.getContextHandle(), presentableState.getNativeView(), PrismSettings.isVsyncEnabled)) != 0L) {
            int n = presentableState.getRenderWidth();
            int n2 = presentableState.getRenderHeight();
            D3DRTTexture d3DRTTexture = this.createRTTexture(n, n2, Texture.WrapMode.CLAMP_NOT_NEEDED, presentableState.isMSAA());
            if (PrismSettings.dirtyOptsEnabled) {
                d3DRTTexture.contentsUseful();
            }
            if (d3DRTTexture != null) {
                return new D3DSwapChain(this.context, l, d3DRTTexture, presentableState.getRenderScaleX(), presentableState.getRenderScaleY());
            }
            D3DResourceFactory.nReleaseResource(this.context.getContextHandle(), l);
        }
        return null;
    }

    private static ByteBuffer getBuffer(InputStream inputStream) {
        if (inputStream == null) {
            throw new RuntimeException("InputStream must be non-null");
        }
        try {
            Object object;
            int n = 4096;
            Object object2 = new byte[n];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, n);
            int n2 = 0;
            int n3 = -1;
            while ((n3 = bufferedInputStream.read((byte[])object2, n2, n - n2)) != -1) {
                if (n - (n2 += n3) != 0) continue;
                object = new byte[n *= 2];
                System.arraycopy(object2, 0, object, 0, ((byte[])object2).length);
                object2 = object;
            }
            bufferedInputStream.close();
            object = ByteBuffer.allocateDirect(n2);
            ((ByteBuffer)object).put((byte[])object2, 0, n2);
            return object;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Error loading D3D shader object", iOException);
        }
    }

    @Override
    public Shader createShader(InputStream inputStream, Map<String, Integer> map, Map<String, Integer> map2, int n, boolean bl, boolean bl2) {
        if (this.checkDisposed()) {
            return null;
        }
        long l = D3DShader.init(this.context.getContextHandle(), D3DResourceFactory.getBuffer(inputStream), n, bl, bl2);
        return new D3DShader(this.context, l, map2);
    }

    @Override
    public Shader createStockShader(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Shader name must be non-null");
        }
        try {
            InputStream inputStream = AccessController.doPrivileged(() -> D3DResourceFactory.class.getResourceAsStream("hlsl/" + string + ".obj"));
            Class<?> class_ = Class.forName("com.sun.prism.shader." + string + "_Loader");
            Method method = class_.getMethod("loadShader", ShaderFactory.class, InputStream.class);
            return (Shader)method.invoke(null, this, inputStream);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new InternalError("Error loading stock shader " + string);
        }
    }

    @Override
    public boolean isFormatSupported(PixelFormat pixelFormat) {
        return true;
    }

    private int computeMaxTextureSize() {
        int n = D3DResourceFactory.nGetMaximumTextureSize(this.context.getContextHandle());
        if (PrismSettings.verbose) {
            System.err.println("Maximum supported texture size: " + n);
        }
        if (n > PrismSettings.maxTextureSize) {
            n = PrismSettings.maxTextureSize;
            if (PrismSettings.verbose) {
                System.err.println("Maximum texture size clamped to " + n);
            }
        }
        return n;
    }

    @Override
    public int getMaximumTextureSize() {
        return this.maxTextureSize;
    }

    @Override
    protected void notifyReset() {
        ListIterator listIterator = this.records.listIterator();
        while (listIterator.hasNext()) {
            D3DResource.D3DRecord d3DRecord = (D3DResource.D3DRecord)listIterator.next();
            if (!d3DRecord.isDefaultPool()) continue;
            d3DRecord.markDisposed();
            listIterator.remove();
        }
        super.notifyReset();
    }

    @Override
    public void dispose() {
        this.context.dispose();
        ListIterator listIterator = this.records.listIterator();
        while (listIterator.hasNext()) {
            D3DResource.D3DRecord d3DRecord = (D3DResource.D3DRecord)listIterator.next();
            d3DRecord.markDisposed();
        }
        this.records.clear();
        super.dispose();
    }

    void addRecord(D3DResource.D3DRecord d3DRecord) {
        this.records.add(d3DRecord);
    }

    void removeRecord(D3DResource.D3DRecord d3DRecord) {
        this.records.remove(d3DRecord);
    }

    @Override
    public PhongMaterial createPhongMaterial() {
        if (this.checkDisposed()) {
            return null;
        }
        return D3DPhongMaterial.create(this.context);
    }

    @Override
    public MeshView createMeshView(Mesh mesh) {
        if (this.checkDisposed()) {
            return null;
        }
        return D3DMeshView.create(this.context, (D3DMesh)mesh);
    }

    @Override
    public Mesh createMesh() {
        if (this.checkDisposed()) {
            return null;
        }
        return D3DMesh.create(this.context);
    }

    static native long nGetContext(int var0);

    static native boolean nIsDefaultPool(long var0);

    static native int nTestCooperativeLevel(long var0);

    static native int nResetDevice(long var0);

    static native long nCreateTexture(long var0, int var2, int var3, boolean var4, int var5, int var6, int var7, boolean var8);

    static native long nCreateSwapChain(long var0, long var2, boolean var4);

    static native int nReleaseResource(long var0, long var2);

    static native int nGetMaximumTextureSize(long var0);

    static native int nGetTextureWidth(long var0);

    static native int nGetTextureHeight(long var0);

    static native int nReadPixelsI(long var0, long var2, long var4, Buffer var6, int[] var7, int var8, int var9);

    static native int nReadPixelsB(long var0, long var2, long var4, Buffer var6, byte[] var7, int var8, int var9);

    static native int nUpdateTextureI(long var0, long var2, IntBuffer var4, int[] var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12);

    static native int nUpdateTextureF(long var0, long var2, FloatBuffer var4, float[] var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12);

    static native int nUpdateTextureB(long var0, long var2, ByteBuffer var4, byte[] var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13);

    static native long nGetDevice(long var0);

    static native long nGetNativeTextureObject(long var0);
}

