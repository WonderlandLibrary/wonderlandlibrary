/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList
extends GuiListExtended {
    private final GuiControls field_148191_k;
    private final Minecraft mc;
    private final GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth = 0;
    private static final String __OBFID = "CL_00000732";

    public GuiKeyBindingList(GuiControls p_i45031_1_, Minecraft mcIn) {
        super(mcIn, p_i45031_1_.width, p_i45031_1_.height, 63, p_i45031_1_.height - 32, 20);
        this.field_148191_k = p_i45031_1_;
        this.mc = mcIn;
        Object[] var3 = (KeyBinding[])ArrayUtils.clone((Object[])mcIn.gameSettings.keyBindings);
        this.listEntries = new GuiListExtended.IGuiListEntry[var3.length + KeyBinding.getKeybinds().size()];
        Arrays.sort(var3);
        int var4 = 0;
        String var5 = null;
        Object[] var6 = var3;
        int var7 = var3.length;
        for (int var8 = 0; var8 < var7; ++var8) {
            int var11;
            Object var9 = var6[var8];
            String var10 = ((KeyBinding)var9).getKeyCategory();
            if (!var10.equals(var5)) {
                var5 = var10;
                this.listEntries[var4++] = new CategoryEntry(var10);
            }
            if ((var11 = mcIn.fontRendererObj.getStringWidth(I18n.format(((KeyBinding)var9).getKeyDescription(), new Object[0]))) > this.maxListLabelWidth) {
                this.maxListLabelWidth = var11;
            }
            this.listEntries[var4++] = new KeyEntry((KeyBinding)var9, null);
        }
    }

    @Override
    protected int getSize() {
        return this.listEntries.length;
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
        return this.listEntries[p_148180_1_];
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

    @Override
    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    public class CategoryEntry
    implements GuiListExtended.IGuiListEntry {
        private final String labelText;
        private final int labelWidth;
        private static final String __OBFID = "CL_00000734";

        public CategoryEntry(String p_i45028_2_) {
            this.labelText = I18n.format(p_i45028_2_, new Object[0]);
            this.labelWidth = ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.getStringWidth(this.labelText);
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.drawString(this.labelText, ((GuiKeyBindingList)GuiKeyBindingList.this).mc.currentScreen.width / 2 - this.labelWidth / 2, y + slotHeight - ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.FONT_HEIGHT - 1, 0xFFFFFF);
        }

        @Override
        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        }

        @Override
        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
        }
    }

    public class KeyEntry
    implements GuiListExtended.IGuiListEntry {
        private final KeyBinding field_148282_b;
        private final String field_148283_c;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;
        private static final String __OBFID = "CL_00000735";

        private KeyEntry(KeyBinding p_i45029_2_) {
            this.field_148282_b = p_i45029_2_;
            this.field_148283_c = I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 18, I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]));
            this.btnReset = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            boolean var9 = ((GuiKeyBindingList)GuiKeyBindingList.this).field_148191_k.buttonId == this.field_148282_b;
            ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.drawString(this.field_148283_c, x + 90 - GuiKeyBindingList.this.maxListLabelWidth, y + slotHeight / 2 - ((GuiKeyBindingList)GuiKeyBindingList.this).mc.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);
            this.btnReset.xPosition = x + 190;
            this.btnReset.yPosition = y;
            this.btnReset.enabled = this.field_148282_b.getKeyCode() != this.field_148282_b.getKeyCodeDefault();
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
            this.btnChangeKeyBinding.xPosition = x + 105;
            this.btnChangeKeyBinding.yPosition = y;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.field_148282_b.getKeyCode());
            boolean var10 = false;
            if (this.field_148282_b.getKeyCode() != 0) {
                for (KeyBinding var14 : ((GuiKeyBindingList)GuiKeyBindingList.this).mc.gameSettings.keyBindings) {
                    if (var14 == this.field_148282_b || var14.getKeyCode() != this.field_148282_b.getKeyCode()) continue;
                    var10 = true;
                    break;
                }
            }
            if (var9) {
                this.btnChangeKeyBinding.displayString = (Object)((Object)EnumChatFormatting.WHITE) + "> " + (Object)((Object)EnumChatFormatting.YELLOW) + this.btnChangeKeyBinding.displayString + (Object)((Object)EnumChatFormatting.WHITE) + " <";
            } else if (var10) {
                this.btnChangeKeyBinding.displayString = (Object)((Object)EnumChatFormatting.RED) + this.btnChangeKeyBinding.displayString;
            }
            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
        }

        @Override
        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_)) {
                ((GuiKeyBindingList)GuiKeyBindingList.this).field_148191_k.buttonId = this.field_148282_b;
                return true;
            }
            if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_)) {
                ((GuiKeyBindingList)GuiKeyBindingList.this).mc.gameSettings.setOptionKeyBinding(this.field_148282_b, this.field_148282_b.getKeyCodeDefault());
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
        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
        }

        KeyEntry(KeyBinding p_i45030_2_, Object p_i45030_3_) {
            this(p_i45030_2_);
        }
    }
}

