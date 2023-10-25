/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.TransferMode
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.tk.quantum.EmbeddedSceneDnD;
import com.sun.javafx.tk.quantum.GlassSceneDnDEventHandler;
import com.sun.javafx.tk.quantum.QuantumClipboard;
import java.util.Arrays;
import java.util.Set;
import javafx.scene.input.TransferMode;

final class EmbeddedSceneDS
implements EmbeddedSceneDSInterface {
    private final EmbeddedSceneDnD dnd;
    private final ClipboardAssistance assistant;
    private final GlassSceneDnDEventHandler dndHandler;

    public EmbeddedSceneDS(EmbeddedSceneDnD embeddedSceneDnD, ClipboardAssistance clipboardAssistance, GlassSceneDnDEventHandler glassSceneDnDEventHandler) {
        this.dnd = embeddedSceneDnD;
        this.assistant = clipboardAssistance;
        this.dndHandler = glassSceneDnDEventHandler;
    }

    @Override
    public Set<TransferMode> getSupportedActions() {
        assert (this.dnd.isHostThread());
        return this.dnd.executeOnFXThread(() -> QuantumClipboard.clipboardActionsToTransferModes(this.assistant.getSupportedSourceActions()));
    }

    @Override
    public Object getData(String string) {
        assert (this.dnd.isHostThread());
        return this.dnd.executeOnFXThread(() -> this.assistant.getData(string));
    }

    @Override
    public String[] getMimeTypes() {
        assert (this.dnd.isHostThread());
        return this.dnd.executeOnFXThread(() -> this.assistant.getMimeTypes());
    }

    @Override
    public boolean isMimeTypeAvailable(String string) {
        assert (this.dnd.isHostThread());
        return this.dnd.executeOnFXThread(() -> Arrays.asList(this.assistant.getMimeTypes()).contains(string));
    }

    @Override
    public void dragDropEnd(TransferMode transferMode) {
        assert (this.dnd.isHostThread());
        this.dnd.executeOnFXThread(() -> {
            try {
                this.dndHandler.handleDragEnd(transferMode, this.assistant);
            }
            finally {
                this.dnd.onDragSourceReleased(this);
            }
            return null;
        });
    }
}

