/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.hud.designer;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.hud.designer.EditorPanel;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u000eH\u0016J\u0018\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0010H\u0016J \u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u0010H\u0016J \u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0010H\u0016J\b\u0010\u001d\u001a\u00020\u000eH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/designer/GuiHudDesigner;", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;", "()V", "buttonAction", "", "editorPanel", "Lnet/ccbluex/liquidbounce/ui/client/hud/designer/EditorPanel;", "selectedElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "getSelectedElement", "()Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "setSelectedElement", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;)V", "drawScreen", "", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "mouseReleased", "state", "onGuiClosed", "LiKingSense"})
public final class GuiHudDesigner
extends WrappedGuiScreen {
    private EditorPanel editorPanel;
    @Nullable
    private Element selectedElement;
    private boolean buttonAction;

    @Nullable
    public final Element getSelectedElement() {
        return this.selectedElement;
    }

    public final void setSelectedElement(@Nullable Element element) {
        this.selectedElement = element;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.editorPanel = new EditorPanel(this, this.getRepresentedScreen().getWidth() / 2, this.getRepresentedScreen().getHeight() / 2);
    }

    /*
     * Exception decompiling
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl4 : INVOKEVIRTUAL - null : Stack underflow
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

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        block7: {
            block6: {
                this.getRepresentedScreen().superMouseClicked(mouseX, mouseY, mouseButton);
                if (this.buttonAction) {
                    this.buttonAction = false;
                    return;
                }
                LiquidBounce.INSTANCE.getHud().handleMouseClick(mouseX, mouseY, mouseButton);
                if (mouseX < this.editorPanel.getX() || mouseX > this.editorPanel.getX() + this.editorPanel.getWidth() || mouseY < this.editorPanel.getY()) break block6;
                int n = this.editorPanel.getRealHeight();
                int n2 = 200;
                int n3 = this.editorPanel.getY();
                int n4 = mouseY;
                boolean bl = false;
                int n5 = Math.min(n, n2);
                if (n4 <= n3 + n5) break block7;
            }
            this.selectedElement = null;
            this.editorPanel.setCreate(false);
        }
        if (mouseButton == 0) {
            for (Element element : LiquidBounce.INSTANCE.getHud().getElements()) {
                if (!element.isInBorder((double)((float)mouseX / element.getScale()) - element.getRenderX(), (double)((float)mouseY / element.getScale()) - element.getRenderY())) continue;
                this.selectedElement = element;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.getRepresentedScreen().superMouseReleased(mouseX, mouseY, state);
        LiquidBounce.INSTANCE.getHud().handleMouseReleased();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
        super.onGuiClosed();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        switch (keyCode) {
            case 211: {
                if (211 != keyCode || this.selectedElement == null) break;
                LiquidBounce.INSTANCE.getHud().removeElement(this.selectedElement);
                break;
            }
            case 1: {
                this.selectedElement = null;
                this.editorPanel.setCreate(false);
                break;
            }
            default: {
                LiquidBounce.INSTANCE.getHud().handleKey(typedChar, keyCode);
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    public GuiHudDesigner() {
        EditorPanel editorPanel;
        GuiHudDesigner guiHudDesigner = this;
        EditorPanel editorPanel2 = editorPanel;
        EditorPanel editorPanel3 = editorPanel;
        this.editorPanel = (EditorPanel)0;
    }
}

