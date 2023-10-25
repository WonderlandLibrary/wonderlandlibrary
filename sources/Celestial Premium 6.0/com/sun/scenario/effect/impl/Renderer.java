/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.LockableResource;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.ImagePool;
import com.sun.scenario.effect.impl.PoolFilterable;
import com.sun.scenario.effect.impl.RendererFactory;
import java.security.AccessController;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Renderer {
    public static final String rootPkg = "com.sun.scenario.effect";
    private static final Map<FilterContext, Renderer> rendererMap = new HashMap<FilterContext, Renderer>(1);
    private Map<String, EffectPeer> peerCache = Collections.synchronizedMap(new HashMap(5));
    private final ImagePool imagePool = new ImagePool();
    protected static final boolean verbose = AccessController.doPrivileged(() -> Boolean.getBoolean("decora.verbose"));

    protected Renderer() {
    }

    public abstract Effect.AccelType getAccelType();

    public abstract int getCompatibleWidth(int var1);

    public abstract int getCompatibleHeight(int var1);

    public abstract PoolFilterable createCompatibleImage(int var1, int var2);

    public PoolFilterable getCompatibleImage(int n, int n2) {
        return this.imagePool.checkOut(this, n, n2);
    }

    public void releaseCompatibleImage(Filterable filterable) {
        ImagePool imagePool;
        if (filterable instanceof PoolFilterable && (imagePool = ((PoolFilterable)filterable).getImagePool()) != null) {
            imagePool.checkIn((PoolFilterable)filterable);
            return;
        }
        filterable.unlock();
    }

    public void releasePurgatory() {
        this.imagePool.releasePurgatory();
    }

    public abstract void clearImage(Filterable var1);

    public abstract ImageData createImageData(FilterContext var1, Filterable var2);

    public ImageData transform(FilterContext filterContext, ImageData imageData, int n, int n2) {
        double d;
        if (!imageData.getTransform().isIdentity()) {
            throw new InternalError("transform by powers of 2 requires untransformed source");
        }
        if ((n | n2) == 0) {
            return imageData;
        }
        Affine2D affine2D = new Affine2D();
        while (n < -1 || n2 < -1) {
            Rectangle rectangle = imageData.getUntransformedBounds();
            Rectangle rectangle2 = new Rectangle(rectangle);
            d = 1.0;
            double d2 = 1.0;
            if (n < 0) {
                d = 0.5;
                rectangle2.width = (rectangle.width + 1) / 2;
                rectangle2.x /= 2;
                ++n;
            }
            if (n2 < 0) {
                d2 = 0.5;
                rectangle2.height = (rectangle.height + 1) / 2;
                rectangle2.y /= 2;
                ++n2;
            }
            affine2D.setToScale(d, d2);
            imageData = this.transform(filterContext, imageData, (BaseTransform)affine2D, rectangle, rectangle2);
        }
        if ((n | n2) != 0) {
            double d3 = n < 0 ? 0.5 : (double)(1 << n);
            d = n2 < 0 ? 0.5 : (double)(1 << n2);
            affine2D.setToScale(d3, d);
            imageData = imageData.transform(affine2D);
        }
        return imageData;
    }

    public abstract Filterable transform(FilterContext var1, Filterable var2, BaseTransform var3, Rectangle var4, Rectangle var5);

    public abstract ImageData transform(FilterContext var1, ImageData var2, BaseTransform var3, Rectangle var4, Rectangle var5);

    public LockableResource createFloatTexture(int n, int n2) {
        throw new InternalError();
    }

    public void updateFloatTexture(LockableResource lockableResource, FloatMap floatMap) {
        throw new InternalError();
    }

    public final synchronized EffectPeer getPeerInstance(FilterContext filterContext, String string, int n) {
        EffectPeer effectPeer = this.peerCache.get(string);
        if (effectPeer != null) {
            return effectPeer;
        }
        if (n > 0 && (effectPeer = this.peerCache.get(string + "_" + n)) != null) {
            return effectPeer;
        }
        effectPeer = this.createPeer(filterContext, string, n);
        if (effectPeer == null) {
            throw new RuntimeException("Could not create peer  " + string + " for renderer " + this);
        }
        this.peerCache.put(effectPeer.getUniqueName(), effectPeer);
        return effectPeer;
    }

    public abstract RendererState getRendererState();

    protected abstract EffectPeer createPeer(FilterContext var1, String var2, int var3);

    protected Collection<EffectPeer> getPeers() {
        return this.peerCache.values();
    }

    protected static Renderer getSoftwareRenderer() {
        return RendererFactory.getSoftwareRenderer();
    }

    protected abstract Renderer getBackupRenderer();

    protected Renderer getRendererForSize(Effect effect, int n, int n2) {
        return this;
    }

    public static synchronized Renderer getRenderer(FilterContext filterContext) {
        if (filterContext == null) {
            throw new IllegalArgumentException("FilterContext must be non-null");
        }
        Renderer renderer = rendererMap.get(filterContext);
        if (renderer != null) {
            if (renderer.getRendererState() == RendererState.NOTREADY) {
                return renderer;
            }
            if (renderer.getRendererState() == RendererState.OK) {
                return renderer;
            }
            if (renderer.getRendererState() == RendererState.LOST) {
                return renderer.getBackupRenderer();
            }
            if (renderer.getRendererState() == RendererState.DISPOSED) {
                renderer = null;
            }
        }
        if (renderer == null) {
            Object object;
            Collection<Renderer> collection = rendererMap.values();
            Object object2 = collection.iterator();
            while (object2.hasNext()) {
                object = object2.next();
                if (((Renderer)object).getRendererState() != RendererState.DISPOSED) continue;
                ((Renderer)object).imagePool.dispose();
                object2.remove();
            }
            renderer = RendererFactory.createRenderer(filterContext);
            if (renderer == null) {
                throw new RuntimeException("Error creating a Renderer");
            }
            if (verbose) {
                object2 = renderer.getClass().getName();
                object = ((String)object2).substring(((String)object2).lastIndexOf(".") + 1);
                Object object3 = filterContext.getReferent();
                System.out.println("Created " + (String)object + " (AccelType=" + renderer.getAccelType() + ") for " + object3);
            }
            rendererMap.put(filterContext, renderer);
        }
        return renderer;
    }

    public static Renderer getRenderer(FilterContext filterContext, Effect effect, int n, int n2) {
        return Renderer.getRenderer(filterContext).getRendererForSize(effect, n, n2);
    }

    public abstract boolean isImageDataCompatible(ImageData var1);

    public static final class RendererState
    extends Enum<RendererState> {
        public static final /* enum */ RendererState NOTREADY = new RendererState();
        public static final /* enum */ RendererState OK = new RendererState();
        public static final /* enum */ RendererState LOST = new RendererState();
        public static final /* enum */ RendererState DISPOSED = new RendererState();
        private static final /* synthetic */ RendererState[] $VALUES;

        public static RendererState[] values() {
            return (RendererState[])$VALUES.clone();
        }

        public static RendererState valueOf(String string) {
            return Enum.valueOf(RendererState.class, string);
        }

        private static /* synthetic */ RendererState[] $values() {
            return new RendererState[]{NOTREADY, OK, LOST, DISPOSED};
        }

        static {
            $VALUES = RendererState.$values();
        }
    }
}

