/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 */
package com.sun.javafx.menu;

import com.sun.javafx.menu.MenuItemBase;
import javafx.beans.property.BooleanProperty;

public interface RadioMenuItemBase
extends MenuItemBase {
    public void setSelected(boolean var1);

    public boolean isSelected();

    public BooleanProperty selectedProperty();
}

