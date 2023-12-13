/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiSlot;
import optifine.Lang;
import shadersmod.client.GuiShaders;
import shadersmod.client.Shaders;

class GuiSlotShaders
extends GuiSlot {
    private ArrayList shaderslist;
    private int selectedIndex;
    private long lastClickedCached = 0L;
    final GuiShaders shadersGui;

    public GuiSlotShaders(GuiShaders par1GuiShaders, int width, int height, int top, int bottom, int slotHeight) {
        super(par1GuiShaders.getMc(), width, height, top, bottom, slotHeight);
        this.shadersGui = par1GuiShaders;
        this.updateList();
        this.amountScrolled = 0.0f;
        int posYSelected = this.selectedIndex * slotHeight;
        int wMid = (bottom - top) / 2;
        if (posYSelected > wMid) {
            this.scrollBy(posYSelected - wMid);
        }
    }

    @Override
    public int getListWidth() {
        return this.width - 20;
    }

    public void updateList() {
        this.shaderslist = Shaders.listOfShaders();
        this.selectedIndex = 0;
        int n = this.shaderslist.size();
        for (int i = 0; i < n; ++i) {
            if (!((String)this.shaderslist.get(i)).equals(Shaders.currentshadername)) continue;
            this.selectedIndex = i;
            break;
        }
    }

    @Override
    protected int getSize() {
        return this.shaderslist.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClicked, int mouseX, int mouseY) {
        if (index != this.selectedIndex || this.lastClicked != this.lastClickedCached) {
            this.selectedIndex = index;
            this.lastClickedCached = this.lastClicked;
            Shaders.setShaderPack((String)this.shaderslist.get(index));
            Shaders.uninit();
            this.shadersGui.updateButtons();
        }
    }

    @Override
    protected boolean isSelected(int index) {
        return index == this.selectedIndex;
    }

    @Override
    protected int getScrollBarX() {
        return this.width - 6;
    }

    @Override
    protected int getContentHeight() {
        return this.getSize() * 18;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int index, int posX, int posY, int contentY, int mouseX, int mouseY) {
        String label = (String)this.shaderslist.get(index);
        if (label.equals(Shaders.packNameNone)) {
            label = Lang.get("of.options.shaders.packNone");
        } else if (label.equals(Shaders.packNameDefault)) {
            label = Lang.get("of.options.shaders.packDefault");
        }
        this.shadersGui.drawCenteredString(label, this.width / 2, posY + 1, 0xFFFFFF);
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }
}

