/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  qiriyou.verV3Z.Loader
 */
package Verify1;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import qiriyou.verV3Z.Loader;

public final class GuiPasswordField
extends Gui {
    private final int xPosition;
    private final int yPosition;
    private final FontRenderer fontRendererInstance;
    private boolean canLoseFocus;
    private boolean isFocused;
    private int cursorCounter;
    private boolean enableBackgroundDrawing;
    private String text;
    private int maxStringLength;
    private final int width;
    private final int height;
    private boolean visible;
    private int disabledColor;
    private int enabledColor;
    private int selectionEnd;
    private int cursorPosition;
    private int lineScrollOffset;
    private boolean isEnabled;

    /*
     * Exception decompiling
     */
    public GuiPasswordField(FontRenderer var1, int var2, int var3, int var4, int var5) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl23 : INVOKEVIRTUAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public native void drawTextBox();

    public native void drawTextBox2();

    public native void deleteFromCursor(int var1);

    public native boolean getVisible();

    public native void deleteWords(int var1);

    public native boolean getEnableBackgroundDrawing();

    public native void moveCursorBy(int var1);

    public native int getNthWordFromPos(int var1, int var2);

    public native void setEnabled(boolean var1);

    public native void setEnableBackgroundDrawing(boolean var1);

    public native int getSelectionEnd();

    public native int getNthWordFromCursor(int var1);

    private native void drawCursorVertical(int var1, int var2, int var3, int var4);

    public native void setVisible(boolean var1);

    public native void setCursorPosition(int var1);

    public native void writeText(String var1);

    public native void setTextColor(int var1);

    public native void setCursorPositionZero();

    public native int func_146197_a(int var1, int var2, boolean var3);

    public native int getCursorPosition();

    public native void setSelectionPos(int var1);

    public native int getWidth();

    public native void setCursorPositionEnd();

    public native void setMaxStringLength(int var1);

    public native void setDisabledTextColour(int var1);

    public native void setCanLoseFocus(boolean var1);

    public native String getSelectedText();

    public native int getMaxStringLength();

    public native String getText();

    public native boolean isFocused();

    public native void mouseClicked(int var1, int var2, int var3);

    public native void setFocused(boolean var1);

    public native void setText(String var1);

    public native boolean textboxKeyTyped(char var1, int var2);

    public native void updateCursorCounter();

    static {
        Loader.registerNativesForClass((int)9, GuiPasswordField.class);
        GuiPasswordField.$qiriyouLoader();
    }

    public static native /* synthetic */ void $qiriyouLoader();
}

