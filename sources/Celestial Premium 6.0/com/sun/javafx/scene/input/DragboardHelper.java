/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.Dragboard
 */
package com.sun.javafx.scene.input;

import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.util.Utils;
import javafx.scene.input.Dragboard;

public class DragboardHelper {
    private static DragboardAccessor dragboardAccessor;

    private DragboardHelper() {
    }

    public static void setDataAccessRestriction(Dragboard dragboard, boolean bl) {
        dragboardAccessor.setDataAccessRestriction(dragboard, bl);
    }

    public static TKClipboard getPeer(Dragboard dragboard) {
        return dragboardAccessor.getPeer(dragboard);
    }

    public static Dragboard createDragboard(TKClipboard tKClipboard) {
        return dragboardAccessor.createDragboard(tKClipboard);
    }

    public static void setDragboardAccessor(DragboardAccessor dragboardAccessor) {
        if (DragboardHelper.dragboardAccessor != null) {
            throw new IllegalStateException();
        }
        DragboardHelper.dragboardAccessor = dragboardAccessor;
    }

    static {
        Utils.forceInit(Dragboard.class);
    }

    public static interface DragboardAccessor {
        public void setDataAccessRestriction(Dragboard var1, boolean var2);

        public TKClipboard getPeer(Dragboard var1);

        public Dragboard createDragboard(TKClipboard var1);
    }
}

