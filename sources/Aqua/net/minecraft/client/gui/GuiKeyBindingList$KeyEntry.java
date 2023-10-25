package net.minecraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

/*
 * Exception performing whole class analysis ignored.
 */
public class GuiKeyBindingList.KeyEntry
implements GuiListExtended.IGuiListEntry {
    private final KeyBinding keybinding;
    private final String keyDesc;
    private final GuiButton btnChangeKeyBinding;
    private final GuiButton btnReset;

    private GuiKeyBindingList.KeyEntry(KeyBinding p_i45029_2_) {
        this.keybinding = p_i45029_2_;
        this.keyDesc = I18n.format((String)p_i45029_2_.getKeyDescription(), (Object[])new Object[0]);
        this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format((String)p_i45029_2_.getKeyDescription(), (Object[])new Object[0]));
        this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format((String)"controls.reset", (Object[])new Object[0]));
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        boolean flag = GuiKeyBindingList.access$200((GuiKeyBindingList)GuiKeyBindingList.this).buttonId == this.keybinding;
        FontRenderer cfr_ignored_0 = GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this).fontRendererObj;
        GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this).fontRendererObj.drawString(this.keyDesc, x + 90 - GuiKeyBindingList.access$300((GuiKeyBindingList)GuiKeyBindingList.this), y + slotHeight / 2 - FontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
        this.btnReset.xPosition = x + 190;
        this.btnReset.yPosition = y;
        this.btnReset.enabled = this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault();
        this.btnReset.drawButton(GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this), mouseX, mouseY);
        this.btnChangeKeyBinding.xPosition = x + 105;
        this.btnChangeKeyBinding.yPosition = y;
        this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString((int)this.keybinding.getKeyCode());
        boolean flag1 = false;
        if (this.keybinding.getKeyCode() != 0) {
            for (KeyBinding keybinding : GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this).gameSettings.keyBindings) {
                if (keybinding == this.keybinding || keybinding.getKeyCode() != this.keybinding.getKeyCode()) continue;
                flag1 = true;
                break;
            }
        }
        if (flag) {
            this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
        } else if (flag1) {
            this.btnChangeKeyBinding.displayString = EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString;
        }
        this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this), mouseX, mouseY);
    }

    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this), p_148278_2_, p_148278_3_)) {
            GuiKeyBindingList.access$200((GuiKeyBindingList)GuiKeyBindingList.this).buttonId = this.keybinding;
            return true;
        }
        if (this.btnReset.mousePressed(GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this), p_148278_2_, p_148278_3_)) {
            GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this).gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
            KeyBinding.resetKeyBindingArrayAndHash();
            return true;
        }
        return false;
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        this.btnChangeKeyBinding.mouseReleased(x, y);
        this.btnReset.mouseReleased(x, y);
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
    }
}
