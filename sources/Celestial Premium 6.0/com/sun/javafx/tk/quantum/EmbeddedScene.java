/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.collections.ObservableList
 *  javafx.event.EventType
 *  javafx.scene.image.PixelFormat
 *  javafx.scene.input.InputMethodEvent
 *  javafx.scene.input.InputMethodRequests
 *  javafx.scene.input.InputMethodTextRun
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.embed.AbstractEvents;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.EmbeddedSceneDnD;
import com.sun.javafx.tk.quantum.EmbeddedState;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.PaintRenderJob;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.UploadingPainter;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.security.AccessController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.image.PixelFormat;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

final class EmbeddedScene
extends GlassScene
implements EmbeddedSceneInterface {
    private HostInterface host;
    private UploadingPainter painter;
    private PaintRenderJob paintRenderJob;
    private float renderScaleX;
    private float renderScaleY;
    private final EmbeddedSceneDnD embeddedDnD;
    private volatile IntBuffer texBits;
    private volatile int texLineStride;
    private volatile float texScaleFactorX = 1.0f;
    private volatile float texScaleFactorY = 1.0f;
    private volatile PixelFormat<?> pixelFormat;

    public EmbeddedScene(HostInterface hostInterface, boolean bl, boolean bl2) {
        super(bl, bl2);
        this.sceneState = new EmbeddedState(this);
        this.host = hostInterface;
        this.embeddedDnD = new EmbeddedSceneDnD(this);
        PaintCollector paintCollector = PaintCollector.getInstance();
        this.painter = new UploadingPainter(this);
        this.paintRenderJob = new PaintRenderJob(this, paintCollector.getRendered(), this.painter);
        int n = Pixels.getNativeFormat();
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        if (n == 1 && byteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.pixelFormat = PixelFormat.getIntArgbPreInstance();
        } else if (n == 2 && byteOrder == ByteOrder.BIG_ENDIAN) {
            this.pixelFormat = PixelFormat.getIntArgbInstance();
        }
        assert (this.pixelFormat != null);
    }

    @Override
    public void dispose() {
        assert (this.host != null);
        QuantumToolkit.runWithRenderLock(() -> {
            this.host.setEmbeddedScene(null);
            this.host = null;
            this.updateSceneState();
            this.painter = null;
            this.paintRenderJob = null;
            this.texBits = null;
            return null;
        });
        super.dispose();
    }

    @Override
    void setStage(GlassStage glassStage) {
        super.setStage(glassStage);
        assert (this.host != null);
        this.host.setEmbeddedScene(glassStage != null ? this : null);
    }

    @Override
    protected boolean isSynchronous() {
        return false;
    }

    @Override
    public void setRoot(NGNode nGNode) {
        super.setRoot(nGNode);
        this.painter.setRoot(nGNode);
    }

    @Override
    public TKClipboard createDragboard(boolean bl) {
        return this.embeddedDnD.createDragboard(bl);
    }

    @Override
    public void enableInputMethodEvents(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.enableInputMethodEvents " + bl);
        }
    }

    @Override
    public void finishInputMethodComposition() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.finishInputMethodComposition");
        }
    }

    @Override
    public void setPixelScaleFactors(float f, float f2) {
        this.renderScaleX = f;
        this.renderScaleY = f2;
        this.entireSceneNeedsRepaint();
    }

    public float getRenderScaleX() {
        return this.renderScaleX;
    }

    public float getRenderScaleY() {
        return this.renderScaleY;
    }

    @Override
    public PixelFormat<?> getPixelFormat() {
        return this.pixelFormat;
    }

    void uploadPixels(Pixels pixels) {
        this.texBits = (IntBuffer)pixels.getPixels();
        this.texLineStride = pixels.getWidthUnsafe();
        this.texScaleFactorX = pixels.getScaleXUnsafe();
        this.texScaleFactorY = pixels.getScaleYUnsafe();
        if (this.host != null) {
            this.host.repaint();
        }
    }

    @Override
    public void repaint() {
        Toolkit toolkit = Toolkit.getToolkit();
        toolkit.addRenderJob(this.paintRenderJob);
    }

    @Override
    public boolean traverseOut(Direction direction) {
        if (direction == Direction.NEXT) {
            return this.host.traverseFocusOut(true);
        }
        if (direction == Direction.PREVIOUS) {
            return this.host.traverseFocusOut(false);
        }
        return false;
    }

    @Override
    public void setSize(int n, int n2) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
                this.sceneListener.changedSize(n, n2);
            }
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public boolean getPixels(IntBuffer intBuffer, int n, int n2) {
        return QuantumToolkit.runWithRenderLock(() -> {
            int n3 = n;
            int n4 = n2;
            if (this.getRenderScaleX() != this.texScaleFactorX || this.getRenderScaleY() != this.texScaleFactorY || this.texBits == null) {
                return false;
            }
            n3 = (int)Math.ceil((float)n3 * this.texScaleFactorX);
            n4 = (int)Math.ceil((float)n4 * this.texScaleFactorY);
            intBuffer.rewind();
            this.texBits.rewind();
            if (intBuffer.capacity() != this.texBits.capacity()) {
                int n5 = Math.min(n3, this.texLineStride);
                int n6 = Math.min(n4, this.texBits.capacity() / this.texLineStride);
                int[] arrn = new int[n5];
                for (int i = 0; i < n6; ++i) {
                    this.texBits.position(i * this.texLineStride);
                    this.texBits.get(arrn, 0, n5);
                    intBuffer.position(i * n3);
                    intBuffer.put(arrn);
                }
                return true;
            }
            intBuffer.put(this.texBits);
            return true;
        });
    }

    @Override
    protected Color getClearColor() {
        if (this.fillPaint != null && this.fillPaint.getType() == Paint.Type.COLOR && ((Color)this.fillPaint).getAlpha() == 0.0f) {
            return (Color)this.fillPaint;
        }
        return super.getClearColor();
    }

    @Override
    public void mouseEvent(int n, int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, int n3, int n4, int n5, int n6, boolean bl6, boolean bl7, boolean bl8, boolean bl9, boolean bl10) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener == null) {
                return null;
            }
            assert (n != 2);
            EventType<MouseEvent> eventType = AbstractEvents.mouseIDToFXEventID(n);
            this.sceneListener.mouseEvent(eventType, n3, n4, n5, n6, AbstractEvents.mouseButtonToFXMouseButton(n2), bl10, false, bl6, bl7, bl8, bl9, bl, bl2, bl3, bl4, bl5);
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void scrollEvent(int n, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener == null) {
                return null;
            }
            this.sceneListener.scrollEvent(AbstractEvents.scrollIDToFXEventType(n), d, d2, d3, d4, d5, d6, 0, 0, 0, 0, 0, d7, d8, d9, d10, bl, bl2, bl3, bl4, false, bl5);
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void inputMethodEvent(EventType<InputMethodEvent> eventType, ObservableList<InputMethodTextRun> observableList, String string, int n) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
                this.sceneListener.inputMethodEvent(eventType, observableList, string, n);
            }
            return null;
        }));
    }

    @Override
    public void menuEvent(int n, int n2, int n3, int n4, boolean bl) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
                this.sceneListener.menuEvent(n, n2, n3, n4, bl);
            }
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void keyEvent(int n, int n2, char[] arrc, int n3) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
                String string;
                boolean bl = (n3 & 1) != 0;
                boolean bl2 = (n3 & 2) != 0;
                boolean bl3 = (n3 & 4) != 0;
                boolean bl4 = (n3 & 8) != 0;
                String string2 = string = new String(arrc);
                KeyEvent keyEvent = new KeyEvent(AbstractEvents.keyIDToFXEventType(n), string, string2, KeyCodeMap.valueOf(n2), bl, bl2, bl3, bl4);
                this.sceneListener.keyEvent(keyEvent);
            }
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void zoomEvent(int n, double d, double d2, double d3, double d4, double d5, double d6, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener == null) {
                return null;
            }
            this.sceneListener.zoomEvent(AbstractEvents.zoomIDToFXEventType(n), d, d2, d3, d4, d5, d6, bl, bl2, bl3, bl4, false, bl5);
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void rotateEvent(int n, double d, double d2, double d3, double d4, double d5, double d6, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener == null) {
                return null;
            }
            this.sceneListener.rotateEvent(AbstractEvents.rotateIDToFXEventType(n), d, d2, d3, d4, d5, d6, bl, bl2, bl3, bl4, false, bl5);
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void swipeEvent(int n, double d, double d2, double d3, double d4, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener == null) {
                return null;
            }
            this.sceneListener.swipeEvent(AbstractEvents.swipeIDToFXEventType(n), 0, d, d2, d3, d4, bl, bl2, bl3, bl4, false);
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void setCursor(Object object) {
        super.setCursor(object);
        this.host.setCursor((CursorFrame)object);
    }

    @Override
    public void setDragStartListener(HostDragStartListener hostDragStartListener) {
        this.embeddedDnD.setDragStartListener(hostDragStartListener);
    }

    @Override
    public EmbeddedSceneDTInterface createDropTarget() {
        return this.embeddedDnD.createDropTarget();
    }

    @Override
    public InputMethodRequests getInputMethodRequests() {
        return this.inputMethodRequests;
    }
}

