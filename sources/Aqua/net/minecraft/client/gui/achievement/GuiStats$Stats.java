package net.minecraft.client.gui.achievement;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

/*
 * Exception performing whole class analysis ignored.
 */
abstract class GuiStats.Stats
extends GuiSlot {
    protected int field_148218_l;
    protected List<StatCrafting> statsHolder;
    protected Comparator<StatCrafting> statSorter;
    protected int field_148217_o;
    protected int field_148215_p;

    protected GuiStats.Stats(Minecraft mcIn) {
        super(mcIn, GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, 20);
        this.field_148218_l = -1;
        this.field_148217_o = -1;
        this.setShowSelectionBox(false);
        this.setHasListHeader(true, 20);
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    }

    protected boolean isSelected(int slotIndex) {
        return false;
    }

    protected void drawBackground() {
        GuiStats.this.drawDefaultBackground();
    }

    protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
        if (!Mouse.isButtonDown((int)0)) {
            this.field_148218_l = -1;
        }
        if (this.field_148218_l == 0) {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 115 - 18), (int)(p_148129_2_ + 1), (int)0, (int)0);
        } else {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 115 - 18), (int)(p_148129_2_ + 1), (int)0, (int)18);
        }
        if (this.field_148218_l == 1) {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 165 - 18), (int)(p_148129_2_ + 1), (int)0, (int)0);
        } else {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 165 - 18), (int)(p_148129_2_ + 1), (int)0, (int)18);
        }
        if (this.field_148218_l == 2) {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 215 - 18), (int)(p_148129_2_ + 1), (int)0, (int)0);
        } else {
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + 215 - 18), (int)(p_148129_2_ + 1), (int)0, (int)18);
        }
        if (this.field_148217_o != -1) {
            int i = 79;
            int j = 18;
            if (this.field_148217_o == 1) {
                i = 129;
            } else if (this.field_148217_o == 2) {
                i = 179;
            }
            if (this.field_148215_p == 1) {
                j = 36;
            }
            GuiStats.access$000((GuiStats)GuiStats.this, (int)(p_148129_1_ + i), (int)(p_148129_2_ + 1), (int)j, (int)0);
        }
    }

    protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
        this.field_148218_l = -1;
        if (p_148132_1_ >= 79 && p_148132_1_ < 115) {
            this.field_148218_l = 0;
        } else if (p_148132_1_ >= 129 && p_148132_1_ < 165) {
            this.field_148218_l = 1;
        } else if (p_148132_1_ >= 179 && p_148132_1_ < 215) {
            this.field_148218_l = 2;
        }
        if (this.field_148218_l >= 0) {
            this.func_148212_h(this.field_148218_l);
            this.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
        }
    }

    protected final int getSize() {
        return this.statsHolder.size();
    }

    protected final StatCrafting func_148211_c(int p_148211_1_) {
        return (StatCrafting)this.statsHolder.get(p_148211_1_);
    }

    protected abstract String func_148210_b(int var1);

    protected void func_148209_a(StatBase p_148209_1_, int p_148209_2_, int p_148209_3_, boolean p_148209_4_) {
        if (p_148209_1_ != null) {
            String s = p_148209_1_.format(GuiStats.access$100((GuiStats)GuiStats.this).readStat(p_148209_1_));
            GuiStats.this.drawString(GuiStats.access$200((GuiStats)GuiStats.this), s, p_148209_2_ - GuiStats.access$300((GuiStats)GuiStats.this).getStringWidth(s), p_148209_3_ + 5, p_148209_4_ ? 0xFFFFFF : 0x909090);
        } else {
            String s1 = "-";
            GuiStats.this.drawString(GuiStats.access$400((GuiStats)GuiStats.this), s1, p_148209_2_ - GuiStats.access$500((GuiStats)GuiStats.this).getStringWidth(s1), p_148209_3_ + 5, p_148209_4_ ? 0xFFFFFF : 0x909090);
        }
    }

    protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
        if (p_148142_2_ >= this.top && p_148142_2_ <= this.bottom) {
            int i = this.getSlotIndexFromScreenCoords(p_148142_1_, p_148142_2_);
            int j = this.width / 2 - 92 - 16;
            if (i >= 0) {
                if (p_148142_1_ < j + 40 || p_148142_1_ > j + 40 + 20) {
                    return;
                }
                StatCrafting statcrafting = this.func_148211_c(i);
                this.func_148213_a(statcrafting, p_148142_1_, p_148142_2_);
            } else {
                String s = "";
                if (p_148142_1_ >= j + 115 - 18 && p_148142_1_ <= j + 115) {
                    s = this.func_148210_b(0);
                } else if (p_148142_1_ >= j + 165 - 18 && p_148142_1_ <= j + 165) {
                    s = this.func_148210_b(1);
                } else {
                    if (p_148142_1_ < j + 215 - 18 || p_148142_1_ > j + 215) {
                        return;
                    }
                    s = this.func_148210_b(2);
                }
                s = ("" + I18n.format((String)s, (Object[])new Object[0])).trim();
                if (s.length() > 0) {
                    int k = p_148142_1_ + 12;
                    int l = p_148142_2_ - 12;
                    int i1 = GuiStats.access$600((GuiStats)GuiStats.this).getStringWidth(s);
                    GuiStats.access$700((GuiStats)GuiStats.this, (int)(k - 3), (int)(l - 3), (int)(k + i1 + 3), (int)(l + 8 + 3), (int)-1073741824, (int)-1073741824);
                    GuiStats.access$800((GuiStats)GuiStats.this).drawStringWithShadow(s, (float)k, (float)l, -1);
                }
            }
        }
    }

    protected void func_148213_a(StatCrafting p_148213_1_, int p_148213_2_, int p_148213_3_) {
        if (p_148213_1_ != null) {
            Item item = p_148213_1_.func_150959_a();
            ItemStack itemstack = new ItemStack(item);
            String s = itemstack.getUnlocalizedName();
            String s1 = ("" + I18n.format((String)(s + ".name"), (Object[])new Object[0])).trim();
            if (s1.length() > 0) {
                int i = p_148213_2_ + 12;
                int j = p_148213_3_ - 12;
                int k = GuiStats.access$900((GuiStats)GuiStats.this).getStringWidth(s1);
                GuiStats.access$1000((GuiStats)GuiStats.this, (int)(i - 3), (int)(j - 3), (int)(i + k + 3), (int)(j + 8 + 3), (int)-1073741824, (int)-1073741824);
                GuiStats.access$1100((GuiStats)GuiStats.this).drawStringWithShadow(s1, (float)i, (float)j, -1);
            }
        }
    }

    protected void func_148212_h(int p_148212_1_) {
        if (p_148212_1_ != this.field_148217_o) {
            this.field_148217_o = p_148212_1_;
            this.field_148215_p = -1;
        } else if (this.field_148215_p == -1) {
            this.field_148215_p = 1;
        } else {
            this.field_148217_o = -1;
            this.field_148215_p = 0;
        }
        Collections.sort(this.statsHolder, this.statSorter);
    }
}
