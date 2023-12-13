// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.resources.I18n;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.Minecraft;

public class GuiKeyBindingList extends GuiListExtended
{
    private final GuiControls field_148191_k;
    private final Minecraft mc;
    private final IGuiListEntry[] listEntries;
    private int maxListLabelWidth;
    private static final String __OBFID = "CL_00000732";
    
    public GuiKeyBindingList(final GuiControls p_i45031_1_, final Minecraft mcIn) {
        super(mcIn, p_i45031_1_.width, p_i45031_1_.height, 63, p_i45031_1_.height - 32, 20);
        this.maxListLabelWidth = 0;
        this.field_148191_k = p_i45031_1_;
        this.mc = mcIn;
        final KeyBinding[] var3 = (KeyBinding[])ArrayUtils.clone((Object[])mcIn.gameSettings.keyBindings);
        this.listEntries = new IGuiListEntry[var3.length + KeyBinding.getKeybinds().size()];
        Arrays.sort(var3);
        int var4 = 0;
        String var5 = null;
        final KeyBinding[] var6 = var3;
        for (int var7 = var3.length, var8 = 0; var8 < var7; ++var8) {
            final KeyBinding var9 = var6[var8];
            final String var10 = var9.getKeyCategory();
            if (!var10.equals(var5)) {
                var5 = var10;
                this.listEntries[var4++] = new CategoryEntry(var10);
            }
            final int var11 = mcIn.fontRendererObj.getStringWidth(I18n.format(var9.getKeyDescription(), new Object[0]));
            if (var11 > this.maxListLabelWidth) {
                this.maxListLabelWidth = var11;
            }
            this.listEntries[var4++] = new KeyEntry(var9, null);
        }
    }
    
    @Override
    protected int getSize() {
        return this.listEntries.length;
    }
    
    @Override
    public IGuiListEntry getListEntry(final int p_148180_1_) {
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
    
    public class CategoryEntry implements IGuiListEntry
    {
        private final String labelText;
        private final int labelWidth;
        private static final String __OBFID = "CL_00000734";
        
        public CategoryEntry(final String p_i45028_2_) {
            this.labelText = I18n.format(p_i45028_2_, new Object[0]);
            this.labelWidth = GuiKeyBindingList.this.mc.fontRendererObj.getStringWidth(this.labelText);
        }
        
        @Override
        public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
            GuiKeyBindingList.this.mc.fontRendererObj.drawString(this.labelText, GuiKeyBindingList.this.mc.currentScreen.width / 2 - this.labelWidth / 2, y + slotHeight - GuiKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT - 1, 16777215);
        }
        
        @Override
        public boolean mousePressed(final int p_148278_1_, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
            return false;
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
        }
        
        @Override
        public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
        }
    }
    
    public class KeyEntry implements IGuiListEntry
    {
        private final KeyBinding field_148282_b;
        private final String field_148283_c;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;
        private static final String __OBFID = "CL_00000735";
        
        private KeyEntry(final KeyBinding p_i45029_2_) {
            this.field_148282_b = p_i45029_2_;
            this.field_148283_c = I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 18, I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]));
            this.btnReset = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
        }
        
        @Override
        public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
            final boolean var9 = GuiKeyBindingList.this.field_148191_k.buttonId == this.field_148282_b;
            GuiKeyBindingList.this.mc.fontRendererObj.drawString(this.field_148283_c, x + 90 - GuiKeyBindingList.this.maxListLabelWidth, y + slotHeight / 2 - GuiKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT / 2, 16777215);
            this.btnReset.xPosition = x + 190;
            this.btnReset.yPosition = y;
            this.btnReset.enabled = (this.field_148282_b.getKeyCode() != this.field_148282_b.getKeyCodeDefault());
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
            this.btnChangeKeyBinding.xPosition = x + 105;
            this.btnChangeKeyBinding.yPosition = y;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.field_148282_b.getKeyCode());
            boolean var10 = false;
            if (this.field_148282_b.getKeyCode() != 0) {
                for (final KeyBinding var14 : GuiKeyBindingList.this.mc.gameSettings.keyBindings) {
                    if (var14 != this.field_148282_b && var14.getKeyCode() == this.field_148282_b.getKeyCode()) {
                        var10 = true;
                        break;
                    }
                }
            }
            if (var9) {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
            }
            else if (var10) {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString;
            }
            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
        }
        
        @Override
        public boolean mousePressed(final int p_148278_1_, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_)) {
                GuiKeyBindingList.this.field_148191_k.buttonId = this.field_148282_b;
                return true;
            }
            if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_)) {
                GuiKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.field_148282_b, this.field_148282_b.getKeyCodeDefault());
                KeyBinding.resetKeyBindingArrayAndHash();
                return true;
            }
            return false;
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
            this.btnReset.mouseReleased(x, y);
        }
        
        @Override
        public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
        }
        
        KeyEntry(final GuiKeyBindingList this$0, final KeyBinding p_i45030_2_, final Object p_i45030_3_) {
            this(this$0, p_i45030_2_);
        }
    }
}
