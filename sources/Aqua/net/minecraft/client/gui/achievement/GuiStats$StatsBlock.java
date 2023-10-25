package net.minecraft.client.gui.achievement;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiStats.StatsBlock
extends GuiStats.Stats {
    public GuiStats.StatsBlock(Minecraft mcIn) {
        super(GuiStats.this, mcIn);
        this.statsHolder = Lists.newArrayList();
        for (StatCrafting statcrafting : StatList.objectMineStats) {
            boolean flag = false;
            int i = Item.getIdFromItem((Item)statcrafting.func_150959_a());
            if (GuiStats.access$100((GuiStats)GuiStats.this).readStat((StatBase)statcrafting) > 0) {
                flag = true;
            } else if (StatList.objectUseStats[i] != null && GuiStats.access$100((GuiStats)GuiStats.this).readStat(StatList.objectUseStats[i]) > 0) {
                flag = true;
            } else if (StatList.objectCraftStats[i] != null && GuiStats.access$100((GuiStats)GuiStats.this).readStat(StatList.objectCraftStats[i]) > 0) {
                flag = true;
            }
            if (!flag) continue;
            this.statsHolder.add((Object)statcrafting);
        }
        this.statSorter = new /* Unavailable Anonymous Inner Class!! */;
    }

    protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
        super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
        if (this.field_148218_l == 0) {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 115 - 18 + 1), (int)(p_148129_2_ + 1 + 1), (int)18, (int)18);
        } else {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 115 - 18), (int)(p_148129_2_ + 1), (int)18, (int)18);
        }
        if (this.field_148218_l == 1) {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 165 - 18 + 1), (int)(p_148129_2_ + 1 + 1), (int)36, (int)18);
        } else {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 165 - 18), (int)(p_148129_2_ + 1), (int)36, (int)18);
        }
        if (this.field_148218_l == 2) {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 215 - 18 + 1), (int)(p_148129_2_ + 1 + 1), (int)54, (int)18);
        } else {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 215 - 18), (int)(p_148129_2_ + 1), (int)54, (int)18);
        }
    }

    protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
        StatCrafting statcrafting = this.func_148211_c(entryID);
        Item item = statcrafting.func_150959_a();
        GuiStats.access$1200((GuiStats)GuiStats.this, (int)(p_180791_2_ + 40), (int)p_180791_3_, (Item)item);
        int i = Item.getIdFromItem((Item)item);
        this.func_148209_a(StatList.objectCraftStats[i], p_180791_2_ + 115, p_180791_3_, entryID % 2 == 0);
        this.func_148209_a(StatList.objectUseStats[i], p_180791_2_ + 165, p_180791_3_, entryID % 2 == 0);
        this.func_148209_a((StatBase)statcrafting, p_180791_2_ + 215, p_180791_3_, entryID % 2 == 0);
    }

    protected String func_148210_b(int p_148210_1_) {
        return p_148210_1_ == 0 ? "stat.crafted" : (p_148210_1_ == 1 ? "stat.used" : "stat.mined");
    }
}
