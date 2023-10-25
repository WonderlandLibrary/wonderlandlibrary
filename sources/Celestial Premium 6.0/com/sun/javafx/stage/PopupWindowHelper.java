/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.scene.Node
 *  javafx.stage.PopupWindow
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.util.Utils;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

public class PopupWindowHelper
extends WindowHelper {
    private static final PopupWindowHelper theInstance = new PopupWindowHelper();
    private static PopupWindowAccessor popupWindowAccessor;

    private static WindowHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(PopupWindow popupWindow) {
        PopupWindowHelper.setHelper((Window)popupWindow, PopupWindowHelper.getInstance());
    }

    @Override
    protected void visibleChangingImpl(Window window, boolean bl) {
        super.visibleChangingImpl(window, bl);
        popupWindowAccessor.doVisibleChanging(window, bl);
    }

    @Override
    protected void visibleChangedImpl(Window window, boolean bl) {
        super.visibleChangedImpl(window, bl);
        popupWindowAccessor.doVisibleChanged(window, bl);
    }

    public static ObservableList<Node> getContent(PopupWindow popupWindow) {
        return popupWindowAccessor.getContent(popupWindow);
    }

    public static void setPopupWindowAccessor(PopupWindowAccessor popupWindowAccessor) {
        if (PopupWindowHelper.popupWindowAccessor != null) {
            throw new IllegalStateException();
        }
        PopupWindowHelper.popupWindowAccessor = popupWindowAccessor;
    }

    static {
        Utils.forceInit(PopupWindow.class);
    }

    public static interface PopupWindowAccessor {
        public ObservableList<Node> getContent(PopupWindow var1);

        public void doVisibleChanging(Window var1, boolean var2);

        public void doVisibleChanged(Window var1, boolean var2);
    }
}

