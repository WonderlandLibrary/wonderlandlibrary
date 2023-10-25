/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.CssMetaData
 *  javafx.css.StyleConverter
 *  javafx.css.StyleableProperty
 *  javafx.scene.Node
 */
package com.sun.javafx.css;

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.StyleableProperty;
import javafx.scene.Node;

public class SubCssMetaData<T>
extends CssMetaData<Node, T> {
    public SubCssMetaData(String string, StyleConverter styleConverter, T t) {
        super(string, styleConverter, t);
    }

    public SubCssMetaData(String string, StyleConverter styleConverter) {
        super(string, styleConverter);
    }

    public boolean isSettable(Node node) {
        return false;
    }

    public StyleableProperty<T> getStyleableProperty(Node node) {
        return null;
    }
}

