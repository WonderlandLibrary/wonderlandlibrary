/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PulseLogger
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.SceneState;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.Presentable;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

abstract class ViewPainter
implements Runnable {
    private static NodePath[] ROOT_PATHS = new NodePath[PrismSettings.dirtyRegionCount];
    protected static final ReentrantLock renderLock = new ReentrantLock();
    protected int penWidth = -1;
    protected int penHeight = -1;
    protected int viewWidth;
    protected int viewHeight;
    protected final SceneState sceneState;
    protected Presentable presentable;
    protected ResourceFactory factory;
    protected boolean freshBackBuffer;
    private int width;
    private int height;
    private NGNode root;
    private NGNode overlayRoot;
    private Rectangle dirtyRect;
    private RectBounds clip;
    private RectBounds dirtyRegionTemp;
    private DirtyRegionPool dirtyRegionPool;
    private DirtyRegionContainer dirtyRegionContainer;
    private Affine3D tx;
    private Affine3D scaleTx;
    private GeneralTransform3D viewProjTx;
    private GeneralTransform3D projTx;
    private RTTexture sceneBuffer;

    protected ViewPainter(GlassScene glassScene) {
        this.sceneState = glassScene.getSceneState();
        if (this.sceneState == null) {
            throw new NullPointerException("Scene state is null");
        }
        if (PrismSettings.dirtyOptsEnabled) {
            this.tx = new Affine3D();
            this.viewProjTx = new GeneralTransform3D();
            this.projTx = new GeneralTransform3D();
            this.scaleTx = new Affine3D();
            this.clip = new RectBounds();
            this.dirtyRect = new Rectangle();
            this.dirtyRegionTemp = new RectBounds();
            this.dirtyRegionPool = new DirtyRegionPool(PrismSettings.dirtyRegionCount);
            this.dirtyRegionContainer = this.dirtyRegionPool.checkOut();
        }
    }

    protected final void setRoot(NGNode nGNode) {
        this.root = nGNode;
    }

    protected final void setOverlayRoot(NGNode nGNode) {
        this.overlayRoot = nGNode;
    }

    private void adjustPerspective(NGCamera nGCamera) {
        assert (PrismSettings.dirtyOptsEnabled);
        if (nGCamera instanceof NGPerspectiveCamera) {
            this.scaleTx.setToScale((double)this.width / 2.0, (double)(-this.height) / 2.0, 1.0);
            this.scaleTx.translate(1.0, -1.0);
            this.projTx.mul(this.scaleTx);
            this.viewProjTx = nGCamera.getProjViewTx(this.viewProjTx);
            this.projTx.mul(this.viewProjTx);
        }
    }

    protected void paintImpl(Graphics graphics) {
        int n;
        Object object;
        int n2;
        int n3;
        boolean bl;
        if (this.width <= 0 || this.height <= 0 || graphics == null) {
            this.root.renderForcedContent(graphics);
            return;
        }
        Graphics graphics2 = graphics;
        float f = this.getPixelScaleFactorX();
        float f2 = this.getPixelScaleFactorY();
        graphics2.setPixelScaleFactors(f, f2);
        boolean bl2 = this.overlayRoot != null || this.freshBackBuffer || this.sceneState.getScene().isEntireSceneDirty() || this.sceneState.getScene().getDepthBuffer() || !PrismSettings.dirtyOptsEnabled;
        boolean bl3 = bl = PrismSettings.showDirtyRegions || PrismSettings.showOverdraw;
        if (bl && !this.sceneState.getScene().getDepthBuffer()) {
            n3 = (int)Math.ceil((float)this.width * f);
            n2 = (int)Math.ceil((float)this.height * f2);
            if (this.sceneBuffer != null) {
                this.sceneBuffer.lock();
                if (this.sceneBuffer.isSurfaceLost() || n3 != this.sceneBuffer.getContentWidth() || n2 != this.sceneBuffer.getContentHeight()) {
                    this.sceneBuffer.unlock();
                    this.sceneBuffer.dispose();
                    this.sceneBuffer = null;
                }
            }
            if (this.sceneBuffer == null) {
                this.sceneBuffer = graphics2.getResourceFactory().createRTTexture(n3, n2, Texture.WrapMode.CLAMP_TO_ZERO, false);
                bl2 = true;
            }
            this.sceneBuffer.contentsUseful();
            graphics2 = this.sceneBuffer.createGraphics();
            graphics2.setPixelScaleFactors(f, f2);
            graphics2.scale(f, f2);
        } else if (this.sceneBuffer != null) {
            this.sceneBuffer.dispose();
            this.sceneBuffer = null;
        }
        n3 = -1;
        if (!bl2) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newPhase((String)"Dirty Opts Computed");
            }
            this.clip.setBounds(0.0f, 0.0f, this.width, this.height);
            this.dirtyRegionTemp.makeEmpty();
            this.dirtyRegionContainer.reset();
            this.tx.setToIdentity();
            this.projTx.setIdentity();
            this.adjustPerspective(this.sceneState.getCamera());
            n3 = this.root.accumulateDirtyRegions(this.clip, this.dirtyRegionTemp, this.dirtyRegionPool, this.dirtyRegionContainer, this.tx, this.projTx);
            this.dirtyRegionContainer.roundOut();
            if (n3 == 1) {
                this.root.doPreCulling(this.dirtyRegionContainer, this.tx, this.projTx);
            }
        }
        int n4 = n2 = n3 == 1 ? this.dirtyRegionContainer.size() : 0;
        if (n2 > 0) {
            int n5;
            int n6;
            graphics2.setHasPreCullingBits(true);
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newPhase((String)"Render Roots Discovered");
            }
            for (n6 = 0; n6 < n2; ++n6) {
                object = ViewPainter.getRootPath(n6);
                ((NodePath)object).clear();
                this.root.getRenderRoot(ViewPainter.getRootPath(n6), this.dirtyRegionContainer.getDirtyRegion(n6), n6, this.tx, this.projTx);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.addMessage((String)(n2 + " different dirty regions to render"));
                for (n6 = 0; n6 < n2; ++n6) {
                    PulseLogger.addMessage((String)("Dirty Region " + n6 + ": " + this.dirtyRegionContainer.getDirtyRegion(n6)));
                    PulseLogger.addMessage((String)("Render Root Path " + n6 + ": " + ViewPainter.getRootPath(n6)));
                }
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED && PrismSettings.printRenderGraph) {
                StringBuilder stringBuilder = new StringBuilder();
                object = new ArrayList();
                for (n5 = 0; n5 < n2; ++n5) {
                    NodePath nodePath;
                    RectBounds rectBounds = this.dirtyRegionContainer.getDirtyRegion(n5);
                    if (!(rectBounds.getWidth() > 0.0f) || !(rectBounds.getHeight() > 0.0f) || (nodePath = ViewPainter.getRootPath(n5)).isEmpty()) continue;
                    object.add(nodePath.last());
                }
                this.root.printDirtyOpts(stringBuilder, (List<NGNode>)object);
                PulseLogger.addMessage((String)stringBuilder.toString());
            }
            for (n = 0; n < n2; ++n) {
                int n7;
                object = this.dirtyRegionContainer.getDirtyRegion(n);
                if (!(((RectBounds)object).getWidth() > 0.0f) || !(((RectBounds)object).getHeight() > 0.0f)) continue;
                this.dirtyRect.x = n5 = (int)Math.floor(((RectBounds)object).getMinX() * f);
                this.dirtyRect.y = n7 = (int)Math.floor(((RectBounds)object).getMinY() * f2);
                this.dirtyRect.width = (int)Math.ceil(((RectBounds)object).getMaxX() * f) - n5;
                this.dirtyRect.height = (int)Math.ceil(((RectBounds)object).getMaxY() * f2) - n7;
                graphics2.setClipRect(this.dirtyRect);
                graphics2.setClipRectIndex(n);
                this.doPaint(graphics2, ViewPainter.getRootPath(n));
                ViewPainter.getRootPath(n).clear();
            }
        } else {
            graphics2.setHasPreCullingBits(false);
            graphics2.setClipRect(null);
            this.doPaint(graphics2, null);
        }
        this.root.renderForcedContent(graphics2);
        if (this.overlayRoot != null) {
            this.overlayRoot.render(graphics2);
        }
        if (bl) {
            if (this.sceneBuffer != null) {
                graphics2.sync();
                graphics.clear();
                graphics.drawTexture(this.sceneBuffer, 0.0f, 0.0f, this.width, this.height, this.sceneBuffer.getContentX(), this.sceneBuffer.getContentY(), this.sceneBuffer.getContentX() + this.sceneBuffer.getContentWidth(), this.sceneBuffer.getContentY() + this.sceneBuffer.getContentHeight());
                this.sceneBuffer.unlock();
            }
            if (PrismSettings.showOverdraw) {
                if (n2 > 0) {
                    for (n = 0; n < n2; ++n) {
                        object = new Rectangle(this.dirtyRegionContainer.getDirtyRegion(n));
                        graphics.setClipRectIndex(n);
                        this.paintOverdraw(graphics, (Rectangle)object);
                        graphics.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.3f));
                        graphics.drawRect(((Rectangle)object).x, ((Rectangle)object).y, ((Rectangle)object).width, ((Rectangle)object).height);
                    }
                } else {
                    Rectangle rectangle = new Rectangle(0, 0, this.width, this.height);
                    assert (graphics.getClipRectIndex() == 0);
                    this.paintOverdraw(graphics, rectangle);
                    graphics.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.3f));
                    graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
            } else if (n2 > 0) {
                graphics.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.3f));
                for (n = 0; n < n2; ++n) {
                    object = this.dirtyRegionContainer.getDirtyRegion(n);
                    graphics.fillRect(((RectBounds)object).getMinX(), ((RectBounds)object).getMinY(), ((RectBounds)object).getWidth(), ((RectBounds)object).getHeight());
                }
            } else {
                graphics.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.3f));
                graphics.fillRect(0.0f, 0.0f, this.width, this.height);
            }
            this.root.clearPainted();
        }
    }

    private void paintOverdraw(Graphics graphics, Rectangle rectangle) {
        int[] arrn = new int[rectangle.width * rectangle.height];
        this.root.drawDirtyOpts(BaseTransform.IDENTITY_TRANSFORM, this.projTx, rectangle, arrn, graphics.getClipRectIndex());
        Image image = Image.fromIntArgbPreData(arrn, rectangle.width, rectangle.height);
        Texture texture = this.factory.getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
        graphics.drawTexture(texture, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, 0.0f, 0.0f, rectangle.width, rectangle.height);
        texture.unlock();
    }

    private static NodePath getRootPath(int n) {
        if (ROOT_PATHS[n] == null) {
            ViewPainter.ROOT_PATHS[n] = new NodePath();
        }
        return ROOT_PATHS[n];
    }

    protected void disposePresentable() {
        if (this.presentable instanceof GraphicsResource) {
            ((GraphicsResource)((Object)this.presentable)).dispose();
        }
        this.presentable = null;
    }

    protected boolean validateStageGraphics() {
        if (!this.sceneState.isValid()) {
            return false;
        }
        this.width = this.viewWidth = this.sceneState.getWidth();
        this.height = this.viewHeight = this.sceneState.getHeight();
        return this.sceneState.isWindowVisible() && !this.sceneState.isWindowMinimized();
    }

    protected float getPixelScaleFactorX() {
        return this.presentable == null ? 1.0f : this.presentable.getPixelScaleFactorX();
    }

    protected float getPixelScaleFactorY() {
        return this.presentable == null ? 1.0f : this.presentable.getPixelScaleFactorY();
    }

    private void doPaint(Graphics graphics, NodePath nodePath) {
        Paint paint;
        if (nodePath != null) {
            if (nodePath.isEmpty()) {
                this.root.clearDirtyTree();
                return;
            }
            assert (nodePath.getCurrentNode() == this.root);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newPhase((String)"Painting");
        }
        GlassScene glassScene = this.sceneState.getScene();
        glassScene.clearEntireSceneDirty();
        graphics.setLights(glassScene.getLights());
        graphics.setDepthBuffer(glassScene.getDepthBuffer());
        Color color = this.sceneState.getClearColor();
        if (color != null) {
            graphics.clear(color);
        }
        if ((paint = this.sceneState.getCurrentPaint()) != null) {
            if (paint.getType() != Paint.Type.COLOR) {
                graphics.getRenderTarget().setOpaque(paint.isOpaque());
            }
            graphics.setPaint(paint);
            graphics.fillQuad(0.0f, 0.0f, this.width, this.height);
        }
        graphics.setCamera(this.sceneState.getCamera());
        graphics.setRenderRoot(nodePath);
        this.root.render(graphics);
    }
}

