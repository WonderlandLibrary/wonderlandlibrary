package net.minecraft.client.gui;

import intent.AquaDev.aqua.Aqua;
import java.util.Date;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.storage.SaveFormatComparator;
import org.apache.commons.lang3.StringUtils;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiSelectWorld.List
extends GuiSlot {
    public GuiSelectWorld.List(Minecraft mcIn) {
        super(mcIn, GuiSelectWorld.width, GuiSelectWorld.height, 32, GuiSelectWorld.height - 64, 36);
    }

    protected int getSize() {
        return GuiSelectWorld.access$000((GuiSelectWorld)GuiSelectWorld.this).size();
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        boolean flag;
        GuiSelectWorld.access$102((GuiSelectWorld)GuiSelectWorld.this, (int)slotIndex);
        GuiSelectWorld.access$200((GuiSelectWorld)GuiSelectWorld.this).enabled = flag = GuiSelectWorld.access$100((GuiSelectWorld)GuiSelectWorld.this) >= 0 && GuiSelectWorld.access$100((GuiSelectWorld)GuiSelectWorld.this) < this.getSize();
        GuiSelectWorld.access$300((GuiSelectWorld)GuiSelectWorld.this).enabled = flag;
        GuiSelectWorld.access$400((GuiSelectWorld)GuiSelectWorld.this).enabled = flag;
        GuiSelectWorld.access$500((GuiSelectWorld)GuiSelectWorld.this).enabled = flag;
        if (isDoubleClick && flag) {
            GuiSelectWorld.this.func_146615_e(slotIndex);
        }
    }

    protected boolean isSelected(int slotIndex) {
        return slotIndex == GuiSelectWorld.access$100((GuiSelectWorld)GuiSelectWorld.this);
    }

    protected int getContentHeight() {
        return GuiSelectWorld.access$000((GuiSelectWorld)GuiSelectWorld.this).size() * 36;
    }

    protected void drawBackground() {
        Aqua.INSTANCE.shaderBackground.renderShader();
    }

    protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
        SaveFormatComparator saveformatcomparator = (SaveFormatComparator)GuiSelectWorld.access$000((GuiSelectWorld)GuiSelectWorld.this).get(entryID);
        String s = saveformatcomparator.getDisplayName();
        if (StringUtils.isEmpty((CharSequence)s)) {
            s = GuiSelectWorld.access$600((GuiSelectWorld)GuiSelectWorld.this) + " " + (entryID + 1);
        }
        String s1 = saveformatcomparator.getFileName();
        s1 = s1 + " (" + GuiSelectWorld.access$700((GuiSelectWorld)GuiSelectWorld.this).format(new Date(saveformatcomparator.getLastTimePlayed()));
        s1 = s1 + ")";
        String s2 = "";
        if (saveformatcomparator.requiresConversion()) {
            s2 = GuiSelectWorld.access$800((GuiSelectWorld)GuiSelectWorld.this) + " " + s2;
        } else {
            s2 = GuiSelectWorld.access$900((GuiSelectWorld)GuiSelectWorld.this)[saveformatcomparator.getEnumGameType().getID()];
            if (saveformatcomparator.isHardcoreModeEnabled()) {
                s2 = EnumChatFormatting.DARK_RED + I18n.format((String)"gameMode.hardcore", (Object[])new Object[0]) + EnumChatFormatting.RESET;
            }
            if (saveformatcomparator.getCheatsEnabled()) {
                s2 = s2 + ", " + I18n.format((String)"selectWorld.cheats", (Object[])new Object[0]);
            }
        }
        Aqua.INSTANCE.comfortaa3.drawString(s, (float)(p_180791_2_ + 2), (float)(p_180791_3_ + 1), 0xFFFFFF);
        Aqua.INSTANCE.comfortaa3.drawString(s1, (float)(p_180791_2_ + 2), (float)(p_180791_3_ + 12), 0x808080);
        Aqua.INSTANCE.comfortaa3.drawString(s2, (float)(p_180791_2_ + 2), (float)(p_180791_3_ + 12 + 10), 0x808080);
    }
}
