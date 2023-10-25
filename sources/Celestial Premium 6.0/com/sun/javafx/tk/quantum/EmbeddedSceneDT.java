/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.TransferMode
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.tk.quantum.EmbeddedSceneDnD;
import com.sun.javafx.tk.quantum.GlassSceneDnDEventHandler;
import com.sun.javafx.tk.quantum.QuantumClipboard;
import javafx.scene.input.TransferMode;

final class EmbeddedSceneDT
implements EmbeddedSceneDTInterface {
    private final EmbeddedSceneDnD dnd;
    private final GlassSceneDnDEventHandler dndHandler;
    private EmbeddedSceneDSInterface dragSource;
    private ClipboardAssistance assistant;

    public EmbeddedSceneDT(EmbeddedSceneDnD embeddedSceneDnD, GlassSceneDnDEventHandler glassSceneDnDEventHandler) {
        this.dnd = embeddedSceneDnD;
        this.dndHandler = glassSceneDnDEventHandler;
    }

    private void close() {
        this.dnd.onDropTargetReleased(this);
        this.assistant = null;
    }

    @Override
    public TransferMode handleDragEnter(int n, int n2, int n3, int n4, TransferMode transferMode, EmbeddedSceneDSInterface embeddedSceneDSInterface) {
        assert (this.dnd.isHostThread());
        return this.dnd.executeOnFXThread(() -> {
            this.dragSource = embeddedSceneDSInterface;
            this.assistant = new EmbeddedDTAssistant(this.dragSource);
            return this.dndHandler.handleDragEnter(n, n2, n3, n4, transferMode, this.assistant);
        });
    }

    @Override
    public void handleDragLeave() {
        assert (this.dnd.isHostThread());
        this.dnd.executeOnFXThread(() -> {
            assert (this.assistant != null);
            try {
                this.dndHandler.handleDragLeave(this.assistant);
            }
            finally {
                this.close();
            }
            return null;
        });
    }

    @Override
    public TransferMode handleDragDrop(int n, int n2, int n3, int n4, TransferMode transferMode) {
        assert (this.dnd.isHostThread());
        return this.dnd.executeOnFXThread(() -> {
            assert (this.assistant != null);
            try {
                TransferMode transferMode2 = this.dndHandler.handleDragDrop(n, n2, n3, n4, transferMode, this.assistant);
                return transferMode2;
            }
            finally {
                this.close();
            }
        });
    }

    @Override
    public TransferMode handleDragOver(int n, int n2, int n3, int n4, TransferMode transferMode) {
        assert (this.dnd.isHostThread());
        return this.dnd.executeOnFXThread(() -> {
            assert (this.assistant != null);
            return this.dndHandler.handleDragOver(n, n2, n3, n4, transferMode, this.assistant);
        });
    }

    private static class EmbeddedDTAssistant
    extends ClipboardAssistance {
        private EmbeddedSceneDSInterface dragSource;

        EmbeddedDTAssistant(EmbeddedSceneDSInterface embeddedSceneDSInterface) {
            super("DND-Embedded");
            this.dragSource = embeddedSceneDSInterface;
        }

        @Override
        public void flush() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getData(String string) {
            return this.dragSource.getData(string);
        }

        @Override
        public int getSupportedSourceActions() {
            return QuantumClipboard.transferModesToClipboardActions(this.dragSource.getSupportedActions());
        }

        @Override
        public void setTargetAction(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String[] getMimeTypes() {
            return this.dragSource.getMimeTypes();
        }
    }
}

