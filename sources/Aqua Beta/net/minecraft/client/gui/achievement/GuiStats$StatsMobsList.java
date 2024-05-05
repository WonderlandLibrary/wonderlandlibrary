package net.minecraft.client.gui.achievement;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiStats.StatsMobsList
extends GuiSlot {
    private final List<EntityList.EntityEggInfo> field_148222_l;

    public GuiStats.StatsMobsList(Minecraft mcIn) {
        GuiStats.access$1600((GuiStats)GuiStats.this);
        super(mcIn, GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, FontRenderer.FONT_HEIGHT * 4);
        this.field_148222_l = Lists.newArrayList();
        this.setShowSelectionBox(false);
        for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.entityEggs.values()) {
            if (GuiStats.access$100((GuiStats)GuiStats.this).readStat(entitylist$entityegginfo.field_151512_d) <= 0 && GuiStats.access$100((GuiStats)GuiStats.this).readStat(entitylist$entityegginfo.field_151513_e) <= 0) continue;
            this.field_148222_l.add((Object)entitylist$entityegginfo);
        }
    }

    protected int getSize() {
        return this.field_148222_l.size();
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    }

    protected boolean isSelected(int slotIndex) {
        return false;
    }

    protected int getContentHeight() {
        int n = this.getSize();
        GuiStats.access$1700((GuiStats)GuiStats.this);
        return n * FontRenderer.FONT_HEIGHT * 4;
    }

    protected void drawBackground() {
        GuiStats.this.drawDefaultBackground();
    }

    protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
        EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)this.field_148222_l.get(entryID);
        String s = I18n.format((String)("entity." + EntityList.getStringFromID((int)entitylist$entityegginfo.spawnedID) + ".name"), (Object[])new Object[0]);
        int i = GuiStats.access$100((GuiStats)GuiStats.this).readStat(entitylist$entityegginfo.field_151512_d);
        int j = GuiStats.access$100((GuiStats)GuiStats.this).readStat(entitylist$entityegginfo.field_151513_e);
        String s1 = I18n.format((String)"stat.entityKills", (Object[])new Object[]{i, s});
        String s2 = I18n.format((String)"stat.entityKilledBy", (Object[])new Object[]{s, j});
        if (i == 0) {
            s1 = I18n.format((String)"stat.entityKills.none", (Object[])new Object[]{s});
        }
        if (j == 0) {
            s2 = I18n.format((String)"stat.entityKilledBy.none", (Object[])new Object[]{s});
        }
        GuiStats.this.drawString(GuiStats.access$1800((GuiStats)GuiStats.this), s, p_180791_2_ + 2 - 10, p_180791_3_ + 1, 0xFFFFFF);
        FontRenderer fontRenderer = GuiStats.access$1900((GuiStats)GuiStats.this);
        GuiStats.access$2000((GuiStats)GuiStats.this);
        GuiStats.this.drawString(fontRenderer, s1, p_180791_2_ + 2, p_180791_3_ + 1 + FontRenderer.FONT_HEIGHT, i == 0 ? 0x606060 : 0x909090);
        FontRenderer fontRenderer2 = GuiStats.access$2100((GuiStats)GuiStats.this);
        GuiStats.access$2200((GuiStats)GuiStats.this);
        GuiStats.this.drawString(fontRenderer2, s2, p_180791_2_ + 2, p_180791_3_ + 1 + FontRenderer.FONT_HEIGHT * 2, j == 0 ? 0x606060 : 0x909090);
    }
}
