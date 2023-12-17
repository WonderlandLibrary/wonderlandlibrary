// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.resources.I18n;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.Minecraft;

public class GuiKeyBindingList extends GuiListExtended
{
    private final GuiControls controlsScreen;
    private final Minecraft mc;
    private final IGuiListEntry[] listEntries;
    private int maxListLabelWidth;
    
    public GuiKeyBindingList(final GuiControls controls, final Minecraft mcIn) {
        super(mcIn, controls.width + 45, controls.height, 63, controls.height - 32, 20);
        this.controlsScreen = controls;
        this.mc = mcIn;
        final KeyBinding[] akeybinding = (KeyBinding[])ArrayUtils.clone((Object[])mcIn.gameSettings.keyBindings);
        this.listEntries = new IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
        Arrays.sort(akeybinding);
        int i = 0;
        String s = null;
        for (final KeyBinding keybinding : akeybinding) {
            final String s2 = keybinding.getKeyCategory();
            if (!s2.equals(s)) {
                s = s2;
                this.listEntries[i++] = new CategoryEntry(s2);
            }
            final int j = mcIn.fontRenderer.getStringWidth(I18n.format(keybinding.getKeyDescription(), new Object[0]));
            if (j > this.maxListLabelWidth) {
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
    public IGuiListEntry getListEntry(final int index) {
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
    
    public class CategoryEntry implements IGuiListEntry
    {
        private final String labelText;
        private final int labelWidth;
        
        public CategoryEntry(final String name) {
            this.labelText = I18n.format(name, new Object[0]);
            this.labelWidth = GuiKeyBindingList.this.mc.fontRenderer.getStringWidth(this.labelText);
        }
        
        @Override
        public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float partialTicks) {
            GuiKeyBindingList.this.mc.fontRenderer.drawString(this.labelText, GuiKeyBindingList.this.mc.currentScreen.width / 2 - this.labelWidth / 2, y + slotHeight - GuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }
        
        @Override
        public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
            return false;
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
        }
        
        @Override
        public void updatePosition(final int slotIndex, final int x, final int y, final float partialTicks) {
        }
    }
    
    public class KeyEntry implements IGuiListEntry
    {
        private final KeyBinding keybinding;
        private final String keyDesc;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;
        
        private KeyEntry(final KeyBinding name) {
            this.keybinding = name;
            this.keyDesc = I18n.format(name.getKeyDescription(), new Object[0]);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format(name.getKeyDescription(), new Object[0]));
            this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset", new Object[0]));
        }
        
        @Override
        public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float partialTicks) {
            final boolean flag = GuiKeyBindingList.this.controlsScreen.buttonId == this.keybinding;
            GuiKeyBindingList.this.mc.fontRenderer.drawString(this.keyDesc, x + 90 - GuiKeyBindingList.this.maxListLabelWidth, y + slotHeight / 2 - GuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnReset.x = x + 190;
            this.btnReset.y = y;
            this.btnReset.enabled = (this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault());
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY, partialTicks);
            this.btnChangeKeyBinding.x = x + 105;
            this.btnChangeKeyBinding.y = y;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());
            boolean flag2 = false;
            if (this.keybinding.getKeyCode() != 0) {
                for (final KeyBinding keybinding : GuiKeyBindingList.this.mc.gameSettings.keyBindings) {
                    if (keybinding != this.keybinding && keybinding.getKeyCode() == this.keybinding.getKeyCode()) {
                        flag2 = true;
                        break;
                    }
                }
            }
            if (flag) {
                this.btnChangeKeyBinding.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + this.btnChangeKeyBinding.displayString + TextFormatting.WHITE + " <";
            }
            else if (flag2) {
                this.btnChangeKeyBinding.displayString = TextFormatting.RED + this.btnChangeKeyBinding.displayString;
            }
            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY, partialTicks);
        }
        
        @Override
        public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, mouseX, mouseY)) {
                GuiKeyBindingList.this.controlsScreen.buttonId = this.keybinding;
                return true;
            }
            if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, mouseX, mouseY)) {
                GuiKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
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
        public void updatePosition(final int slotIndex, final int x, final int y, final float partialTicks) {
        }
    }
}
