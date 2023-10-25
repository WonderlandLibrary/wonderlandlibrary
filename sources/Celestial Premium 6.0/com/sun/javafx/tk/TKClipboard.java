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

import java.security.AccessControlContext;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.util.Pair;

public interface TKClipboard {
    public void setSecurityContext(AccessControlContext var1);

    public Set<DataFormat> getContentTypes();

    public boolean putContent(Pair<DataFormat, Object> ... var1);

    public Object getContent(DataFormat var1);

    public boolean hasContent(DataFormat var1);

    public Set<TransferMode> getTransferModes();

    public void setDragView(Image var1);

    public void setDragViewOffsetX(double var1);

    public void setDragViewOffsetY(double var1);

    public Image getDragView();

    public double getDragViewOffsetX();

    public double getDragViewOffsetY();
}

