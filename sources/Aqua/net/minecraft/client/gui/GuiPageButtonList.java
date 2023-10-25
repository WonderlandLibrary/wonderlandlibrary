package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiListButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.IntHashMap;

/*
 * Exception performing whole class analysis ignored.
 */
public class GuiPageButtonList
extends GuiListExtended {
    private final List<GuiEntry> field_178074_u = Lists.newArrayList();
    private final IntHashMap<Gui> field_178073_v = new IntHashMap();
    private final List<GuiTextField> field_178072_w = Lists.newArrayList();
    private final GuiListEntry[][] field_178078_x;
    private int field_178077_y;
    private GuiResponder field_178076_z;
    private Gui field_178075_A;

    public GuiPageButtonList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn, GuiResponder p_i45536_7_, GuiListEntry[] ... p_i45536_8_) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.field_178076_z = p_i45536_7_;
        this.field_178078_x = p_i45536_8_;
        this.field_148163_i = false;
        this.func_178069_s();
        this.func_178055_t();
    }

    private void func_178069_s() {
        for (GuiListEntry[] aguipagebuttonlist$guilistentry : this.field_178078_x) {
            for (int i = 0; i < aguipagebuttonlist$guilistentry.length; i += 2) {
                GuiListEntry guipagebuttonlist$guilistentry = aguipagebuttonlist$guilistentry[i];
                GuiListEntry guipagebuttonlist$guilistentry1 = i < aguipagebuttonlist$guilistentry.length - 1 ? aguipagebuttonlist$guilistentry[i + 1] : null;
                Gui gui = this.func_178058_a(guipagebuttonlist$guilistentry, 0, guipagebuttonlist$guilistentry1 == null);
                Gui gui1 = this.func_178058_a(guipagebuttonlist$guilistentry1, 160, guipagebuttonlist$guilistentry == null);
                GuiEntry guipagebuttonlist$guientry = new GuiEntry(gui, gui1);
                this.field_178074_u.add((Object)guipagebuttonlist$guientry);
                if (guipagebuttonlist$guilistentry != null && gui != null) {
                    this.field_178073_v.addKey(guipagebuttonlist$guilistentry.func_178935_b(), (Object)gui);
                    if (gui instanceof GuiTextField) {
                        this.field_178072_w.add((Object)((GuiTextField)gui));
                    }
                }
                if (guipagebuttonlist$guilistentry1 == null || gui1 == null) continue;
                this.field_178073_v.addKey(guipagebuttonlist$guilistentry1.func_178935_b(), (Object)gui1);
                if (!(gui1 instanceof GuiTextField)) continue;
                this.field_178072_w.add((Object)((GuiTextField)gui1));
            }
        }
    }

    private void func_178055_t() {
        this.field_178074_u.clear();
        for (int i = 0; i < this.field_178078_x[this.field_178077_y].length; i += 2) {
            GuiListEntry guipagebuttonlist$guilistentry = this.field_178078_x[this.field_178077_y][i];
            GuiListEntry guipagebuttonlist$guilistentry1 = i < this.field_178078_x[this.field_178077_y].length - 1 ? this.field_178078_x[this.field_178077_y][i + 1] : null;
            Gui gui = (Gui)this.field_178073_v.lookup(guipagebuttonlist$guilistentry.func_178935_b());
            Gui gui1 = guipagebuttonlist$guilistentry1 != null ? (Gui)this.field_178073_v.lookup(guipagebuttonlist$guilistentry1.func_178935_b()) : null;
            GuiEntry guipagebuttonlist$guientry = new GuiEntry(gui, gui1);
            this.field_178074_u.add((Object)guipagebuttonlist$guientry);
        }
    }

    public void func_181156_c(int p_181156_1_) {
        if (p_181156_1_ != this.field_178077_y) {
            int i = this.field_178077_y;
            this.field_178077_y = p_181156_1_;
            this.func_178055_t();
            this.func_178060_e(i, p_181156_1_);
            this.amountScrolled = 0.0f;
        }
    }

    public int func_178059_e() {
        return this.field_178077_y;
    }

    public int func_178057_f() {
        return this.field_178078_x.length;
    }

    public Gui func_178056_g() {
        return this.field_178075_A;
    }

    public void func_178071_h() {
        if (this.field_178077_y > 0) {
            this.func_181156_c(this.field_178077_y - 1);
        }
    }

    public void func_178064_i() {
        if (this.field_178077_y < this.field_178078_x.length - 1) {
            this.func_181156_c(this.field_178077_y + 1);
        }
    }

    public Gui func_178061_c(int p_178061_1_) {
        return (Gui)this.field_178073_v.lookup(p_178061_1_);
    }

    private void func_178060_e(int p_178060_1_, int p_178060_2_) {
        for (GuiListEntry guipagebuttonlist$guilistentry : this.field_178078_x[p_178060_1_]) {
            if (guipagebuttonlist$guilistentry == null) continue;
            this.func_178066_a((Gui)this.field_178073_v.lookup(guipagebuttonlist$guilistentry.func_178935_b()), false);
        }
        for (GuiListEntry guipagebuttonlist$guilistentry1 : this.field_178078_x[p_178060_2_]) {
            if (guipagebuttonlist$guilistentry1 == null) continue;
            this.func_178066_a((Gui)this.field_178073_v.lookup(guipagebuttonlist$guilistentry1.func_178935_b()), true);
        }
    }

    private void func_178066_a(Gui p_178066_1_, boolean p_178066_2_) {
        if (p_178066_1_ instanceof GuiButton) {
            ((GuiButton)p_178066_1_).visible = p_178066_2_;
        } else if (p_178066_1_ instanceof GuiTextField) {
            ((GuiTextField)p_178066_1_).setVisible(p_178066_2_);
        } else if (p_178066_1_ instanceof GuiLabel) {
            ((GuiLabel)p_178066_1_).visible = p_178066_2_;
        }
    }

    private Gui func_178058_a(GuiListEntry p_178058_1_, int p_178058_2_, boolean p_178058_3_) {
        return p_178058_1_ instanceof GuiSlideEntry ? this.func_178067_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiSlideEntry)p_178058_1_) : (p_178058_1_ instanceof GuiButtonEntry ? this.func_178065_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiButtonEntry)p_178058_1_) : (p_178058_1_ instanceof EditBoxEntry ? this.func_178068_a(this.width / 2 - 155 + p_178058_2_, 0, (EditBoxEntry)p_178058_1_) : (p_178058_1_ instanceof GuiLabelEntry ? this.func_178063_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiLabelEntry)p_178058_1_, p_178058_3_) : null)));
    }

    public void func_181155_a(boolean p_181155_1_) {
        for (GuiEntry guipagebuttonlist$guientry : this.field_178074_u) {
            if (GuiEntry.access$000((GuiEntry)guipagebuttonlist$guientry) instanceof GuiButton) {
                ((GuiButton)GuiEntry.access$000((GuiEntry)guipagebuttonlist$guientry)).enabled = p_181155_1_;
            }
            if (!(GuiEntry.access$100((GuiEntry)guipagebuttonlist$guientry) instanceof GuiButton)) continue;
            ((GuiButton)GuiEntry.access$100((GuiEntry)guipagebuttonlist$guientry)).enabled = p_181155_1_;
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseEvent);
        int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
        if (i >= 0) {
            GuiEntry guipagebuttonlist$guientry = this.getListEntry(i);
            if (this.field_178075_A != GuiEntry.access$200((GuiEntry)guipagebuttonlist$guientry) && this.field_178075_A != null && this.field_178075_A instanceof GuiTextField) {
                ((GuiTextField)this.field_178075_A).setFocused(false);
            }
            this.field_178075_A = GuiEntry.access$200((GuiEntry)guipagebuttonlist$guientry);
        }
        return flag;
    }

    private GuiSlider func_178067_a(int p_178067_1_, int p_178067_2_, GuiSlideEntry p_178067_3_) {
        GuiSlider guislider = new GuiSlider(this.field_178076_z, p_178067_3_.func_178935_b(), p_178067_1_, p_178067_2_, p_178067_3_.func_178936_c(), p_178067_3_.func_178943_e(), p_178067_3_.func_178944_f(), p_178067_3_.func_178942_g(), p_178067_3_.func_178945_a());
        guislider.visible = p_178067_3_.func_178934_d();
        return guislider;
    }

    private GuiListButton func_178065_a(int p_178065_1_, int p_178065_2_, GuiButtonEntry p_178065_3_) {
        GuiListButton guilistbutton = new GuiListButton(this.field_178076_z, p_178065_3_.func_178935_b(), p_178065_1_, p_178065_2_, p_178065_3_.func_178936_c(), p_178065_3_.func_178940_a());
        guilistbutton.visible = p_178065_3_.func_178934_d();
        return guilistbutton;
    }

    private GuiTextField func_178068_a(int p_178068_1_, int p_178068_2_, EditBoxEntry p_178068_3_) {
        GuiTextField guitextfield = new GuiTextField(p_178068_3_.func_178935_b(), this.mc.fontRendererObj, p_178068_1_, p_178068_2_, 150, 20);
        guitextfield.setText(p_178068_3_.func_178936_c());
        guitextfield.func_175207_a(this.field_178076_z);
        guitextfield.setVisible(p_178068_3_.func_178934_d());
        guitextfield.setValidator(p_178068_3_.func_178950_a());
        return guitextfield;
    }

    private GuiLabel func_178063_a(int p_178063_1_, int p_178063_2_, GuiLabelEntry p_178063_3_, boolean p_178063_4_) {
        GuiLabel guilabel = p_178063_4_ ? new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, this.width - p_178063_1_ * 2, 20, -1) : new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, 150, 20, -1);
        guilabel.visible = p_178063_3_.func_178934_d();
        guilabel.func_175202_a(p_178063_3_.func_178936_c());
        guilabel.setCentered();
        return guilabel;
    }

    public void func_178062_a(char p_178062_1_, int p_178062_2_) {
        block1: {
            int i;
            block2: {
                GuiTextField guitextfield;
                block3: {
                    int i1;
                    block4: {
                        if (!(this.field_178075_A instanceof GuiTextField)) break block1;
                        guitextfield = (GuiTextField)this.field_178075_A;
                        if (GuiScreen.isKeyComboCtrlV((int)p_178062_2_)) break block2;
                        if (p_178062_2_ != 15) break block3;
                        guitextfield.setFocused(false);
                        int k = this.field_178072_w.indexOf((Object)this.field_178075_A);
                        k = GuiScreen.isShiftKeyDown() ? (k == 0 ? this.field_178072_w.size() - 1 : --k) : (k == this.field_178072_w.size() - 1 ? 0 : ++k);
                        this.field_178075_A = (Gui)this.field_178072_w.get(k);
                        guitextfield = (GuiTextField)this.field_178075_A;
                        guitextfield.setFocused(true);
                        int l = guitextfield.yPosition + this.slotHeight;
                        i1 = guitextfield.yPosition;
                        if (l <= this.bottom) break block4;
                        this.amountScrolled += (float)(l - this.bottom);
                        break block1;
                    }
                    if (i1 >= this.top) break block1;
                    this.amountScrolled = i1;
                    break block1;
                }
                guitextfield.textboxKeyTyped(p_178062_1_, p_178062_2_);
                break block1;
            }
            String s = GuiScreen.getClipboardString();
            String[] astring = s.split(";");
            int j = i = this.field_178072_w.indexOf((Object)this.field_178075_A);
            for (String s1 : astring) {
                ((GuiTextField)this.field_178072_w.get(j)).setText(s1);
                j = j == this.field_178072_w.size() - 1 ? 0 : ++j;
                if (j == i) break;
            }
        }
    }

    public GuiEntry getListEntry(int index) {
        return (GuiEntry)this.field_178074_u.get(index);
    }

    public int getSize() {
        return this.field_178074_u.size();
    }

    public int getListWidth() {
        return 400;
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }
}
