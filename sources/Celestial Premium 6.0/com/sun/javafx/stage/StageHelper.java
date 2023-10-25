/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.stage.Stage
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.util.Utils;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StageHelper
extends WindowHelper {
    private static final StageHelper theInstance = new StageHelper();
    private static StageAccessor stageAccessor;

    private static WindowHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Stage stage) {
        StageHelper.setHelper((Window)stage, StageHelper.getInstance());
    }

    @Override
    protected void visibleChangingImpl(Window window, boolean bl) {
        super.visibleChangingImpl(window, bl);
        stageAccessor.doVisibleChanging(window, bl);
    }

    @Override
    protected void visibleChangedImpl(Window window, boolean bl) {
        super.visibleChangedImpl(window, bl);
        stageAccessor.doVisibleChanged(window, bl);
    }

    public static void initSecurityDialog(Stage stage, boolean bl) {
        stageAccessor.initSecurityDialog(stage, bl);
    }

    public static void setPrimary(Stage stage, boolean bl) {
        stageAccessor.setPrimary(stage, bl);
    }

    public static void setImportant(Stage stage, boolean bl) {
        stageAccessor.setImportant(stage, bl);
    }

    public static void setStageAccessor(StageAccessor stageAccessor) {
        if (StageHelper.stageAccessor != null) {
            System.out.println("Warning: Stage accessor already set: " + StageHelper.stageAccessor);
            Thread.dumpStack();
        }
        StageHelper.stageAccessor = stageAccessor;
    }

    public static StageAccessor getStageAccessor() {
        return stageAccessor;
    }

    static {
        Utils.forceInit(Stage.class);
    }

    public static interface StageAccessor {
        public void doVisibleChanging(Window var1, boolean var2);

        public void doVisibleChanged(Window var1, boolean var2);

        public void initSecurityDialog(Stage var1, boolean var2);

        public void setPrimary(Stage var1, boolean var2);

        public void setImportant(Stage var1, boolean var2);
    }
}

