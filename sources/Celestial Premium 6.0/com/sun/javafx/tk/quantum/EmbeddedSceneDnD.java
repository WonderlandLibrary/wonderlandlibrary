/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.scene.input.TransferMode
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.EmbeddedSceneDS;
import com.sun.javafx.tk.quantum.EmbeddedSceneDT;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassSceneDnDEventHandler;
import com.sun.javafx.tk.quantum.QuantumClipboard;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.input.TransferMode;

final class EmbeddedSceneDnD {
    private final GlassSceneDnDEventHandler dndHandler;
    private HostDragStartListener dragStartListener;
    private EmbeddedSceneDSInterface fxDragSource;
    private EmbeddedSceneDTInterface fxDropTarget;
    private Thread hostThread;

    public EmbeddedSceneDnD(GlassScene glassScene) {
        this.dndHandler = new GlassSceneDnDEventHandler(glassScene);
    }

    private void startDrag() {
        assert (Platform.isFxApplicationThread());
        assert (this.fxDragSource != null);
        this.dragStartListener.dragStarted(this.fxDragSource, TransferMode.COPY);
    }

    private void setHostThread() {
        if (this.hostThread == null) {
            this.hostThread = Thread.currentThread();
        }
    }

    public boolean isHostThread() {
        return Thread.currentThread() == this.hostThread;
    }

    public void onDragSourceReleased(EmbeddedSceneDSInterface embeddedSceneDSInterface) {
        assert (this.fxDragSource == embeddedSceneDSInterface);
        this.fxDragSource = null;
        Toolkit.getToolkit().exitNestedEventLoop(this, null);
    }

    public void onDropTargetReleased(EmbeddedSceneDTInterface embeddedSceneDTInterface) {
        this.fxDropTarget = null;
    }

    <T> T executeOnFXThread(Callable<T> callable) {
        if (Platform.isFxApplicationThread()) {
            try {
                return callable.call();
            }
            catch (Exception exception) {
                return null;
            }
        }
        AtomicReference atomicReference = new AtomicReference();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                atomicReference.set(callable.call());
            }
            catch (Exception exception) {
            }
            finally {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        }
        catch (Exception exception) {
            // empty catch block
        }
        return (T)atomicReference.get();
    }

    public TKClipboard createDragboard(boolean bl) {
        assert (Platform.isFxApplicationThread());
        assert (this.fxDragSource == null);
        assert (bl);
        ClipboardAssistance clipboardAssistance = new ClipboardAssistance("DND-Embedded"){

            @Override
            public void flush() {
                super.flush();
                EmbeddedSceneDnD.this.startDrag();
                Toolkit.getToolkit().enterNestedEventLoop(EmbeddedSceneDnD.this);
            }
        };
        this.fxDragSource = new EmbeddedSceneDS(this, clipboardAssistance, this.dndHandler);
        return QuantumClipboard.getDragboardInstance(clipboardAssistance, bl);
    }

    public void setDragStartListener(HostDragStartListener hostDragStartListener) {
        this.setHostThread();
        this.dragStartListener = hostDragStartListener;
    }

    public EmbeddedSceneDTInterface createDropTarget() {
        this.setHostThread();
        return this.executeOnFXThread(() -> {
            assert (this.fxDropTarget == null);
            this.fxDropTarget = new EmbeddedSceneDT(this, this.dndHandler);
            return this.fxDropTarget;
        });
    }
}

