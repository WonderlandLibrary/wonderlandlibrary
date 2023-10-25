/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.ArrayUtils
 */
package net.minecraft.client.gui;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList
extends GuiListExtended {
    private final Minecraft mc;
    private final GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth = 0;
    private final GuiControls field_148191_k;

    public GuiKeyBindingList(GuiControls guiControls, Minecraft minecraft) {
        super(minecraft, GuiControls.width, GuiControls.height, 63, GuiControls.height - 32, 20);
        this.field_148191_k = guiControls;
        this.mc = minecraft;
        Object[] objectArray = (KeyBinding[])ArrayUtils.clone((Object[])Minecraft.gameSettings.keyBindings);
        this.listEntries = new GuiListExtended.IGuiListEntry[objectArray.length + KeyBinding.getKeybinds().size()];
        Arrays.sort(objectArray);
        int n = 0;
        String string = null;
        Object[] objectArray2 = objectArray;
        int n2 = objectArray.length;
        int n3 = 0;
        while (n3 < n2) {
            int n4;
            Object object = objectArray2[n3];
            String string2 = ((KeyBinding)object).getKeyCategory();
            if (!string2.equals(string)) {
                string = string2;
                this.listEntries[n++] = new CategoryEntry(string2);
            }
            if ((n4 = Minecraft.fontRendererObj.getStringWidth(I18n.format(((KeyBinding)object).getKeyDescription(), new Object[0]))) > this.maxListLabelWidth) {
                this.maxListLabelWidth = n4;
            }
            this.listEntries[n++] = new KeyEntry((KeyBinding)object);
            ++n3;
        }
    }

    @Override
    protected int getSize() {
        return this.listEntries.length;
    }

    @Override
    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int n) {
        return this.listEntries[n];
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

    public class KeyEntry
    implements GuiListExtended.IGuiListEntry {
        private final KeyBinding keybinding;
        private final GuiButton btnChangeKeyBinding;
        private final String keyDesc;
        private final GuiButton btnReset;

        @Override
        public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
            boolean bl2 = ((GuiKeyBindingList)GuiKeyBindingList.this).field_148191_k.buttonId == this.keybinding;
            Minecraft.fontRendererObj.drawString(this.keyDesc, n2 + 90 - GuiKeyBindingList.this.maxListLabelWidth, n3 + n5 / 2 - Minecraft.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);
            this.btnReset.xPosition = n2 + 190;
            this.btnReset.yPosition = n3;
            this.btnReset.enabled = this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault();
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, n6, n7);
            this.btnChangeKeyBinding.xPosition = n2 + 105;
            this.btnChangeKeyBinding.yPosition = n3;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());
            boolean bl3 = false;
            if (this.keybinding.getKeyCode() != 0) {
                KeyBinding[] keyBindingArray = Minecraft.gameSettings.keyBindings;
                int n8 = Minecraft.gameSettings.keyBindings.length;
                int n9 = 0;
                while (n9 < n8) {
                    KeyBinding keyBinding = keyBindingArray[n9];
                    if (keyBinding != this.keybinding && keyBinding.getKeyCode() == this.keybinding.getKeyCode()) {
                        bl3 = true;
                        break;
                    }
                    ++n9;
                }
            }
            if (bl2) {
                this.btnChangeKeyBinding.displayString = (Object)((Object)EnumChatFormatting.WHITE) + "> " + (Object)((Object)EnumChatFormatting.YELLOW) + this.btnChangeKeyBinding.displayString + (Object)((Object)EnumChatFormatting.WHITE) + " <";
            } else if (bl3) {
                this.btnChangeKeyBinding.displayString = (Object)((Object)EnumChatFormatting.RED) + this.btnChangeKeyBinding.displayString;
            }
            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, n6, n7);
        }

        private KeyEntry(KeyBinding keyBinding) {
            this.keybinding = keyBinding;
            this.keyDesc = I18n.format(keyBinding.getKeyDescription(), new Object[0]);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format(keyBinding.getKeyDescription(), new Object[0]));
            this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset", new Object[0]));
        }

        @Override
        public void setSelected(int n, int n2, int n3) {
        }

        @Override
        public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, n2, n3)) {
                ((GuiKeyBindingList)GuiKeyBindingList.this).field_148191_k.buttonId = this.keybinding;
                return true;
            }
            if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, n2, n3)) {
                Minecraft.gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
                KeyBinding.resetKeyBindingArrayAndHash();
                return true;
            }
            return false;
        }

        @Override
        public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
            this.btnChangeKeyBinding.mouseReleased(n2, n3);
            this.btnReset.mouseReleased(n2, n3);
        }
    }

    public class CategoryEntry
    implements GuiListExtended.IGuiListEntry {
        private final int labelWidth;
        private final String labelText;

        @Override
        public void setSelected(int n, int n2, int n3) {
        }

        @Override
        public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
            return false;
        }

        public CategoryEntry(String string) {
            this.labelText = I18n.format(string, new Object[0]);
            this.labelWidth = Minecraft.fontRendererObj.getStringWidth(this.labelText);
        }

        @Override
        public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
        }

        @Override
        public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
            GuiScreen cfr_ignored_0 = ((GuiKeyBindingList)GuiKeyBindingList.this).mc.currentScreen;
            Minecraft.fontRendererObj.drawString(this.labelText, GuiScreen.width / 2 - this.labelWidth / 2, n3 + n5 - Minecraft.fontRendererObj.FONT_HEIGHT - 1, 0xFFFFFF);
        }
    }
}

