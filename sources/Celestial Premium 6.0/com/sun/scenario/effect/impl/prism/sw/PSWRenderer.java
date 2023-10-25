/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrImage;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.prism.sw.PSWDrawable;
import com.sun.scenario.effect.impl.sw.RendererDelegate;
import java.lang.reflect.Constructor;

public class PSWRenderer
extends PrRenderer {
    private final Screen screen;
    private final ResourceFactory resourceFactory;
    private final RendererDelegate delegate;
    private Renderer.RendererState state;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private PSWRenderer(Screen screen, RendererDelegate rendererDelegate) {
        this.screen = screen;
        this.resourceFactory = null;
        this.delegate = rendererDelegate;
        PSWRenderer pSWRenderer = this;
        synchronized (pSWRenderer) {
            this.state = Renderer.RendererState.OK;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private PSWRenderer(ResourceFactory resourceFactory, RendererDelegate rendererDelegate) {
        this.screen = null;
        this.resourceFactory = resourceFactory;
        this.delegate = rendererDelegate;
        PSWRenderer pSWRenderer = this;
        synchronized (pSWRenderer) {
            this.state = Renderer.RendererState.OK;
        }
    }

    @Override
    public PrDrawable createDrawable(RTTexture rTTexture) {
        return PSWDrawable.create(rTTexture);
    }

    public static synchronized PSWRenderer createJSWInstance(Screen screen) {
        PSWRenderer pSWRenderer = null;
        try {
            Class<?> class_ = Class.forName("com.sun.scenario.effect.impl.sw.java.JSWRendererDelegate");
            RendererDelegate rendererDelegate = (RendererDelegate)class_.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            pSWRenderer = new PSWRenderer(screen, rendererDelegate);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return pSWRenderer;
    }

    public static synchronized PSWRenderer createJSWInstance(ResourceFactory resourceFactory) {
        PSWRenderer pSWRenderer = null;
        try {
            Class<?> class_ = Class.forName("com.sun.scenario.effect.impl.sw.java.JSWRendererDelegate");
            RendererDelegate rendererDelegate = (RendererDelegate)class_.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            pSWRenderer = new PSWRenderer(resourceFactory, rendererDelegate);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return pSWRenderer;
    }

    public static synchronized PSWRenderer createJSWInstance(FilterContext filterContext) {
        PSWRenderer pSWRenderer = null;
        try {
            ResourceFactory resourceFactory = (ResourceFactory)filterContext.getReferent();
            pSWRenderer = PSWRenderer.createJSWInstance(resourceFactory);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return pSWRenderer;
    }

    private static synchronized PSWRenderer createSSEInstance(Screen screen) {
        PSWRenderer pSWRenderer = null;
        try {
            Class<?> class_ = Class.forName("com.sun.scenario.effect.impl.sw.sse.SSERendererDelegate");
            RendererDelegate rendererDelegate = (RendererDelegate)class_.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            pSWRenderer = new PSWRenderer(screen, rendererDelegate);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return pSWRenderer;
    }

    public static Renderer createRenderer(FilterContext filterContext) {
        Object object = filterContext.getReferent();
        GraphicsPipeline graphicsPipeline = GraphicsPipeline.getPipeline();
        if (graphicsPipeline == null || !(object instanceof Screen)) {
            return null;
        }
        Screen screen = (Screen)object;
        PSWRenderer pSWRenderer = PSWRenderer.createSSEInstance(screen);
        if (pSWRenderer == null) {
            pSWRenderer = PSWRenderer.createJSWInstance(screen);
        }
        return pSWRenderer;
    }

    @Override
    public Effect.AccelType getAccelType() {
        return this.delegate.getAccelType();
    }

    @Override
    public synchronized Renderer.RendererState getRendererState() {
        return this.state;
    }

    @Override
    protected Renderer getBackupRenderer() {
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void dispose() {
        PSWRenderer pSWRenderer = this;
        synchronized (pSWRenderer) {
            this.state = Renderer.RendererState.DISPOSED;
        }
    }

    protected final synchronized void markLost() {
        if (this.state == Renderer.RendererState.OK) {
            this.state = Renderer.RendererState.LOST;
        }
    }

    @Override
    public int getCompatibleWidth(int n) {
        if (this.screen != null) {
            return PSWDrawable.getCompatibleWidth(this.screen, n);
        }
        return this.resourceFactory.getRTTWidth(n, Texture.WrapMode.CLAMP_TO_EDGE);
    }

    @Override
    public int getCompatibleHeight(int n) {
        if (this.screen != null) {
            return PSWDrawable.getCompatibleHeight(this.screen, n);
        }
        return this.resourceFactory.getRTTHeight(n, Texture.WrapMode.CLAMP_TO_EDGE);
    }

    @Override
    public final PSWDrawable createCompatibleImage(int n, int n2) {
        if (this.screen != null) {
            return PSWDrawable.create(this.screen, n, n2);
        }
        RTTexture rTTexture = this.resourceFactory.createRTTexture(n, n2, Texture.WrapMode.CLAMP_TO_EDGE);
        return PSWDrawable.create(rTTexture);
    }

    @Override
    public PSWDrawable getCompatibleImage(int n, int n2) {
        PSWDrawable pSWDrawable = (PSWDrawable)super.getCompatibleImage(n, n2);
        if (pSWDrawable == null) {
            this.markLost();
        }
        return pSWDrawable;
    }

    private EffectPeer createIntrinsicPeer(FilterContext filterContext, String string) {
        EffectPeer effectPeer;
        Class<?> class_ = null;
        try {
            class_ = Class.forName("com.sun.scenario.effect.impl.prism.Pr" + string + "Peer");
            Constructor<?> constructor = class_.getConstructor(FilterContext.class, Renderer.class, String.class);
            effectPeer = (EffectPeer)constructor.newInstance(filterContext, this, string);
        }
        catch (Exception exception) {
            return null;
        }
        return effectPeer;
    }

    private EffectPeer createPlatformPeer(FilterContext filterContext, String string, int n) {
        EffectPeer effectPeer;
        String string2 = this.delegate.getPlatformPeerName(string, n);
        try {
            Class<?> class_ = Class.forName(string2);
            Constructor<?> constructor = class_.getConstructor(FilterContext.class, Renderer.class, String.class);
            effectPeer = (EffectPeer)constructor.newInstance(filterContext, this, string);
        }
        catch (Exception exception) {
            System.err.println("Error: " + this.getAccelType() + " peer not found for: " + string + " due to error: " + exception.getMessage());
            return null;
        }
        return effectPeer;
    }

    @Override
    protected EffectPeer createPeer(FilterContext filterContext, String string, int n) {
        if (PrRenderer.isIntrinsicPeer(string)) {
            return this.createIntrinsicPeer(filterContext, string);
        }
        return this.createPlatformPeer(filterContext, string, n);
    }

    @Override
    public boolean isImageDataCompatible(ImageData imageData) {
        return this.getRendererState() == Renderer.RendererState.OK && imageData.getUntransformedImage() instanceof PSWDrawable;
    }

    @Override
    public void clearImage(Filterable filterable) {
        PSWDrawable pSWDrawable = (PSWDrawable)filterable;
        pSWDrawable.clear();
    }

    @Override
    public ImageData createImageData(FilterContext filterContext, Filterable filterable) {
        int n;
        if (!(filterable instanceof PrImage)) {
            throw new IllegalArgumentException("Identity source must be PrImage");
        }
        Image image = ((PrImage)filterable).getImage();
        int n2 = image.getWidth();
        PSWDrawable pSWDrawable = this.createCompatibleImage(n2, n = image.getHeight());
        if (pSWDrawable == null) {
            return null;
        }
        Graphics graphics = pSWDrawable.createGraphics();
        ResourceFactory resourceFactory = graphics.getResourceFactory();
        Texture texture = resourceFactory.createTexture(image, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
        graphics.drawTexture(texture, 0.0f, 0.0f, n2, n);
        graphics.sync();
        texture.dispose();
        return new ImageData(filterContext, pSWDrawable, new Rectangle(n2, n));
    }

    @Override
    public Filterable transform(FilterContext filterContext, Filterable filterable, BaseTransform baseTransform, Rectangle rectangle, Rectangle rectangle2) {
        PSWDrawable pSWDrawable = this.getCompatibleImage(rectangle2.width, rectangle2.height);
        if (pSWDrawable != null) {
            Graphics graphics = pSWDrawable.createGraphics();
            graphics.translate(-rectangle2.x, -rectangle2.y);
            graphics.transform(baseTransform);
            graphics.drawTexture(((PSWDrawable)filterable).getTextureObject(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        return pSWDrawable;
    }

    @Override
    public ImageData transform(FilterContext filterContext, ImageData imageData, BaseTransform baseTransform, Rectangle rectangle, Rectangle rectangle2) {
        PSWDrawable pSWDrawable = this.getCompatibleImage(rectangle2.width, rectangle2.height);
        if (pSWDrawable != null) {
            PSWDrawable pSWDrawable2 = (PSWDrawable)imageData.getUntransformedImage();
            Graphics graphics = pSWDrawable.createGraphics();
            graphics.translate(-rectangle2.x, -rectangle2.y);
            graphics.transform(baseTransform);
            graphics.drawTexture(pSWDrawable2.getTextureObject(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        imageData.unref();
        return new ImageData(filterContext, pSWDrawable, rectangle2);
    }
}

