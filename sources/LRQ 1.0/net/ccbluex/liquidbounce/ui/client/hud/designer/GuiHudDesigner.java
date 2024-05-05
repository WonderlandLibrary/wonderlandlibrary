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
    private Element selectedElement;
    private boolean buttonAction;

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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LiquidBounce.INSTANCE.getHud().render(true);
        LiquidBounce.INSTANCE.getHud().handleMouseMove(mouseX, mouseY);
        if (!CollectionsKt.contains((Iterable)LiquidBounce.INSTANCE.getHud().getElements(), (Object)this.selectedElement)) {
            this.selectedElement = null;
        }
        int wheel = Mouse.getDWheel();
        this.editorPanel.drawPanel(mouseX, mouseY, wheel);
        if (wheel != 0) {
            for (Element element : LiquidBounce.INSTANCE.getHud().getElements()) {
                if (!element.isInBorder((double)((float)mouseX / element.getScale()) - element.getRenderX(), (double)((float)mouseY / element.getScale()) - element.getRenderY())) continue;
                element.setScale(element.getScale() + (wheel > 0 ? 0.05f : -0.05f));
                break;
            }
        }
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
                LiquidBounce.INSTANCE.getHud().handleKey(typedChar, keyCode);
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}

