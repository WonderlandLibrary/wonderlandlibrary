/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList
extends GuiListExtended {
    private final GuiControls controlsScreen;
    private final Minecraft mc;
    private final GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth;

    public GuiKeyBindingList(GuiControls controls, Minecraft mcIn) {
        super(mcIn, controls.width + 45, controls.height, 63, controls.height - 32, 20);
        this.controlsScreen = controls;
        this.mc = mcIn;
        KeyBinding[] akeybinding = ArrayUtils.clone(mcIn.gameSettings.keyBindings);
        this.listEntries = new GuiListExtended.IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
        Arrays.sort(akeybinding);
        int i = 0;
        String s = null;
        for (KeyBinding keybinding : akeybinding) {
            int j;
            String s1 = keybinding.getKeyCategory();
            if (!s1.equals(s)) {
                s = s1;
                this.listEntries[i++] = new CategoryEntry(s1);
            }
            if ((j = mcIn.fontRendererObj.getStringWidth(I18n.format(keybinding.getKeyDescription(), new Object[0]))) > this.maxListLabelWidth) {
                this.maxListLabelWidth = j;
            }
            this.listEntries[i++] = new KeyEntry(keybinding);
        }
    }

    @Override
    protected int getSize() {
        return this.listEntries.length;
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int index) {
        return this.listEntries[index];
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

    @Override
    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    public class KeyEntry
    implements GuiListExtended.IGuiListEntry {
        private final KeyBinding keybinding;
        private final String keyDesc;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;

        private KeyEntry(KeyBinding name) {
            this.keybinding = name;
            this.keyDesc = I18n.format(name.getKeyDescription(), new Object[0]);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format(name.getKeyDescription(), new Object[0]));
            this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset", new Object[0]));
        }

        @Override
        public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_) {
            boolean flag = ((GuiKeyBindingList)GuiKeyBindingList.this).controlsScreen.buttonId == this.keybinding;
            ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.drawString(this.keyDesc, p_192634_2_ + 90 - GuiKeyBindingList.this.maxListLabelWidth, p_192634_3_ + p_192634_5_ / 2 - ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);
            this.btnReset.xPosition = p_192634_2_ + 190;
            this.btnReset.yPosition = p_192634_3_;
            this.btnReset.enabled = this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault();
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, p_192634_6_, p_192634_7_, p_192634_9_);
            this.btnChangeKeyBinding.xPosition = p_192634_2_ + 105;
            this.btnChangeKeyBinding.yPosition = p_192634_3_;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());
            boolean flag1 = false;
            if (this.keybinding.getKeyCode() != 0) {
                for (KeyBinding keybinding : ((GuiKeyBindingList)GuiKeyBindingList.this).mc.gameSettings.keyBindings) {
                    if (keybinding == this.keybinding || keybinding.getKeyCode() != this.keybinding.getKeyCode()) continue;
                    flag1 = true;
                    break;
                }
            }
            if (flag) {
                this.btnChangeKeyBinding.displayString = (Object)((Object)TextFormatting.WHITE) + "> " + (Object)((Object)TextFormatting.YELLOW) + this.btnChangeKeyBinding.displayString + (Object)((Object)TextFormatting.WHITE) + " <";
            } else if (flag1) {
                this.btnChangeKeyBinding.displayString = (Object)((Object)TextFormatting.RED) + this.btnChangeKeyBinding.displayString;
            }
            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, p_192634_6_, p_192634_7_, p_192634_9_);
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, mouseX, mouseY)) {
                ((GuiKeyBindingList)GuiKeyBindingList.this).controlsScreen.buttonId = this.keybinding;
                return true;
            }
            if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, mouseX, mouseY)) {
                ((GuiKeyBindingList)GuiKeyBindingList.this).mc.gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
                KeyBinding.resetKeyBindingArrayAndHash();
                return true;
            }
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
            this.btnReset.mouseReleased(x, y);
        }

        @Override
        public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
        }
    }

    public class CategoryEntry
    implements GuiListExtended.IGuiListEntry {
        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String name) {
            this.labelText = I18n.format(name, new Object[0]);
            this.labelWidth = ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.getStringWidth(this.labelText);
        }

        @Override
        public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_) {
            ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.drawString(this.labelText, ((GuiKeyBindingList)GuiKeyBindingList.this).mc.currentScreen.width / 2 - this.labelWidth / 2, p_192634_3_ + p_192634_5_ - ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.FONT_HEIGHT - 1, 0xFFFFFF);
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        }

        @Override
        public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
        }
    }
}

