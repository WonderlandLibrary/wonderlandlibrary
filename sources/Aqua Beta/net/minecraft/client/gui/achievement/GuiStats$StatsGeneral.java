package net.minecraft.client.gui.achievement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiStats.StatsGeneral
extends GuiSlot {
    public GuiStats.StatsGeneral(Minecraft mcIn) {
        super(mcIn, GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, 10);
        this.setShowSelectionBox(false);
    }

    protected int getSize() {
        return StatList.generalStats.size();
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    }

    protected boolean isSelected(int slotIndex) {
        return false;
    }

    protected int getContentHeight() {
        return this.getSize() * 10;
    }

    protected void drawBackground() {
        GuiStats.this.drawDefaultBackground();
    }

    protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
        StatBase statbase = (StatBase)StatList.generalStats.get(entryID);
        GuiStats.this.drawString(GuiStats.access$1300((GuiStats)GuiStats.this), statbase.getStatName().getUnformattedText(), p_180791_2_ + 2, p_180791_3_ + 1, entryID % 2 == 0 ? 0xFFFFFF : 0x909090);
        String s = statbase.format(GuiStats.access$100((GuiStats)GuiStats.this).readStat(statbase));
        GuiStats.this.drawString(GuiStats.access$1400((GuiStats)GuiStats.this), s, p_180791_2_ + 2 + 213 - GuiStats.access$1500((GuiStats)GuiStats.this).getStringWidth(s), p_180791_3_ + 1, entryID % 2 == 0 ? 0xFFFFFF : 0x909090);
    }
}
