/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.newui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.NewGUI;
import net.ccbluex.liquidbounce.ui.client.newui.element.CategoryElement;
import net.ccbluex.liquidbounce.ui.client.newui.element.SearchElement;
import net.ccbluex.liquidbounce.ui.client.newui.element.module.ModuleElement;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class NewUi
extends GuiScreen {
    private static NewUi instance;
    public final List<CategoryElement> categoryElements = new ArrayList<CategoryElement>();
    private boolean got = false;
    private float startYAnim = (float)this.field_146295_m / 2.0f;
    private float endYAnim = (float)this.field_146295_m / 2.0f;
    private SearchElement searchElement;
    private float fading = 0.0f;

    public static NewUi getInstance() {
        return instance == null ? (instance = new NewUi()) : instance;
    }

    private NewUi() {
        for (ModuleCategory c : ModuleCategory.values()) {
            this.categoryElements.add(new CategoryElement(c));
        }
        this.categoryElements.get(0).setFocused(true);
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        for (CategoryElement ce : this.categoryElements) {
            for (ModuleElement me : ce.getModuleElements()) {
                if (!me.listeningKeybind()) continue;
                me.resetState();
            }
        }
        this.searchElement = new SearchElement(40.0f, 115.0f, 180.0f, 20.0f);
        super.func_73866_w_();
    }

    /*
     * Exception decompiling
     */
    public void func_146281_b() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl21 : INVOKEVIRTUAL - null : Stack underflow
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

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.drawFullSized(mouseX, mouseY, partialTicks, NewGUI.getAccentColor());
    }

    /*
     * Exception decompiling
     */
    private void drawFullSized(int mouseX, int mouseY, float partialTicks, Color accentColor) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl95 : INVOKESTATIC - null : Stack underflow
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

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, (float)this.field_146294_l - 54.0f, 30.0f, (float)this.field_146294_l - 30.0f, 50.0f)) {
            this.field_146297_k.func_147108_a(null);
            return;
        }
        float elementHeight = 24.0f;
        float startY = 140.0f;
        this.searchElement.handleMouseClick(mouseX, mouseY, mouseButton, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements);
        if (!this.searchElement.isTyping()) {
            for (CategoryElement ce : this.categoryElements) {
                if (ce.getFocused()) {
                    ce.handleMouseClick(mouseX, mouseY, mouseButton, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80);
                }
                if (MouseUtils.mouseWithinBounds(mouseX, mouseY, 30.0f, startY, 230.0f, startY + 24.0f) && !this.searchElement.isTyping()) {
                    this.categoryElements.forEach(e -> e.setFocused(false));
                    ce.setFocused(true);
                    return;
                }
                startY += 24.0f;
            }
        }
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        for (CategoryElement ce : this.categoryElements) {
            if (!ce.getFocused() || !ce.handleKeyTyped(typedChar, keyCode)) continue;
            return;
        }
        if (this.searchElement.handleTyping(typedChar, keyCode, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements)) {
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_146286_b(int mouseX, int mouseY, int state) {
        this.searchElement.handleMouseRelease(mouseX, mouseY, state, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements);
        if (!this.searchElement.isTyping()) {
            for (CategoryElement ce : this.categoryElements) {
                if (!ce.getFocused()) continue;
                ce.handleMouseRelease(mouseX, mouseY, state, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80);
            }
        }
        super.func_146286_b(mouseX, mouseY, state);
    }

    public boolean func_73868_f() {
        return false;
    }
}

