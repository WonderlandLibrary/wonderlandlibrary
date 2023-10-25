/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.Image
 *  javafx.scene.input.DataFormat
 *  javafx.scene.input.TransferMode
 *  javafx.util.Pair
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.TKClipboard;
import java.security.AccessControlContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.util.Pair;

final class LocalClipboard
implements TKClipboard {
    private final Map<DataFormat, Object> values = new HashMap<DataFormat, Object>();

    @Override
    public void setSecurityContext(AccessControlContext accessControlContext) {
    }

    @Override
    public Set<DataFormat> getContentTypes() {
        return Collections.unmodifiableSet(new HashSet<DataFormat>(this.values.keySet()));
    }

    @Override
    public boolean putContent(Pair<DataFormat, Object> ... arrpair) {
        for (Pair<DataFormat, Object> pair : arrpair) {
            if (pair.getKey() == null) {
                throw new NullPointerException("Clipboard.putContent: null data format");
            }
            if (pair.getValue() != null) continue;
            throw new NullPointerException("Clipboard.putContent: null data");
        }
        this.values.clear();
        for (Pair<DataFormat, Object> pair : arrpair) {
            this.values.put((DataFormat)pair.getKey(), pair.getValue());
        }
        return true;
    }

    @Override
    public Object getContent(DataFormat dataFormat) {
        return this.values.get((Object)dataFormat);
    }

    @Override
    public boolean hasContent(DataFormat dataFormat) {
        return this.values.containsKey((Object)dataFormat);
    }

    @Override
    public Set<TransferMode> getTransferModes() {
        throw new IllegalStateException();
    }

    @Override
    public void setDragView(Image image) {
        throw new IllegalStateException();
    }

    @Override
    public void setDragViewOffsetX(double d) {
        throw new IllegalStateException();
    }

    @Override
    public void setDragViewOffsetY(double d) {
        throw new IllegalStateException();
    }

    @Override
    public Image getDragView() {
        throw new IllegalStateException();
    }

    @Override
    public double getDragViewOffsetX() {
        throw new IllegalStateException();
    }

    @Override
    public double getDragViewOffsetY() {
        throw new IllegalStateException();
    }
}

