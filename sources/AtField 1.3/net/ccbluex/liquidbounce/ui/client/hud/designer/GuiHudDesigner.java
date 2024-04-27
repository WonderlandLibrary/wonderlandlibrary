/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.hud.designer;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.hud.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.designer.EditorPanel;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class GuiHudDesigner
extends WrappedGuiScreen {
    private EditorPanel editorPanel = new EditorPanel(this, 2, 2);
    private boolean buttonAction;
    private Element selectedElement;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.editorPanel = new EditorPanel(this, this.getRepresentedScreen().getWidth() / 2, this.getRepresentedScreen().getHeight() / 2);
    }

    public final Element getSelectedElement() {
        return this.selectedElement;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        LiquidBounce.INSTANCE.getHud().render(true);
        LiquidBounce.INSTANCE.getHud().handleMouseMove(n, n2);
        if (!CollectionsKt.contains((Iterable)LiquidBounce.INSTANCE.getHud().getElements(), (Object)this.selectedElement)) {
            this.selectedElement = null;
        }
        int n3 = Mouse.getDWheel();
        this.editorPanel.drawPanel(n, n2, n3);
        if (n3 != 0) {
            for (Element element : LiquidBounce.INSTANCE.getHud().getElements()) {
                if (!element.isInBorder((double)((float)n / element.getScale()) - element.getRenderX(), (double)((float)n2 / element.getScale()) - element.getRenderY())) continue;
                element.setScale(element.getScale() + (n3 > 0 ? 0.05f : -0.05f));
                break;
            }
        }
    }

    public final void setSelectedElement(@Nullable Element element) {
        this.selectedElement = element;
    }

    @Override
    public void keyTyped(char c, int n) {
        switch (n) {
            case 211: {
                if (211 != n || this.selectedElement == null) break;
                HUD hUD = LiquidBounce.INSTANCE.getHud();
                Element element = this.selectedElement;
                if (element == null) {
                    Intrinsics.throwNpe();
                }
                hUD.removeElement(element);
                break;
            }
            case 1: {
                this.selectedElement = null;
                this.editorPanel.setCreate(false);
                break;
            }
            default: {
                LiquidBounce.INSTANCE.getHud().handleKey(c, n);
            }
        }
        super.keyTyped(c, n);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
        super.onGuiClosed();
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        block7: {
            block6: {
                this.getRepresentedScreen().superMouseClicked(n, n2, n3);
                if (this.buttonAction) {
                    this.buttonAction = false;
                    return;
                }
                LiquidBounce.INSTANCE.getHud().handleMouseClick(n, n2, n3);
                if (n < this.editorPanel.getX() || n > this.editorPanel.getX() + this.editorPanel.getWidth() || n2 < this.editorPanel.getY()) break block6;
                int n4 = this.editorPanel.getRealHeight();
                int n5 = 200;
                int n6 = this.editorPanel.getY();
                int n7 = n2;
                boolean bl = false;
                int n8 = Math.min(n4, n5);
                if (n7 <= n6 + n8) break block7;
            }
            this.selectedElement = null;
            this.editorPanel.setCreate(false);
        }
        if (n3 == 0) {
            for (Element element : LiquidBounce.INSTANCE.getHud().getElements()) {
                if (!element.isInBorder((double)((float)n / element.getScale()) - element.getRenderX(), (double)((float)n2 / element.getScale()) - element.getRenderY())) continue;
                this.selectedElement = element;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        this.getRepresentedScreen().superMouseReleased(n, n2, n3);
        LiquidBounce.INSTANCE.getHud().handleMouseReleased();
    }
}

