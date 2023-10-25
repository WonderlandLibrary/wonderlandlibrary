/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiListButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.IntHashMap;

public class GuiPageButtonList
extends GuiListExtended {
    private int field_178077_y;
    private Gui field_178075_A;
    private GuiResponder field_178076_z;
    private final List<GuiTextField> field_178072_w;
    private final List<GuiEntry> field_178074_u = Lists.newArrayList();
    private final IntHashMap<Gui> field_178073_v = new IntHashMap();
    private final GuiListEntry[][] field_178078_x;

    public Gui func_178061_c(int n) {
        return this.field_178073_v.lookup(n);
    }

    private GuiTextField func_178068_a(int n, int n2, EditBoxEntry editBoxEntry) {
        GuiTextField guiTextField = new GuiTextField(editBoxEntry.func_178935_b(), Minecraft.fontRendererObj, n, n2, 150, 20);
        guiTextField.setText(editBoxEntry.func_178936_c());
        guiTextField.func_175207_a(this.field_178076_z);
        guiTextField.setVisible(editBoxEntry.func_178934_d());
        guiTextField.func_175205_a(editBoxEntry.func_178950_a());
        return guiTextField;
    }

    public int func_178059_e() {
        return this.field_178077_y;
    }

    private void func_178060_e(int n, int n2) {
        GuiListEntry guiListEntry;
        GuiListEntry[] guiListEntryArray = this.field_178078_x[n];
        int n3 = guiListEntryArray.length;
        int n4 = 0;
        while (n4 < n3) {
            guiListEntry = guiListEntryArray[n4];
            if (guiListEntry != null) {
                this.func_178066_a(this.field_178073_v.lookup(guiListEntry.func_178935_b()), false);
            }
            ++n4;
        }
        guiListEntryArray = this.field_178078_x[n2];
        n3 = guiListEntryArray.length;
        n4 = 0;
        while (n4 < n3) {
            guiListEntry = guiListEntryArray[n4];
            if (guiListEntry != null) {
                this.func_178066_a(this.field_178073_v.lookup(guiListEntry.func_178935_b()), true);
            }
            ++n4;
        }
    }

    @Override
    public GuiEntry getListEntry(int n) {
        return this.field_178074_u.get(n);
    }

    public void func_178064_i() {
        if (this.field_178077_y < this.field_178078_x.length - 1) {
            this.func_181156_c(this.field_178077_y + 1);
        }
    }

    private void func_178066_a(Gui gui, boolean bl) {
        if (gui instanceof GuiButton) {
            ((GuiButton)gui).visible = bl;
        } else if (gui instanceof GuiTextField) {
            ((GuiTextField)gui).setVisible(bl);
        } else if (gui instanceof GuiLabel) {
            ((GuiLabel)gui).visible = bl;
        }
    }

    private GuiSlider func_178067_a(int n, int n2, GuiSlideEntry guiSlideEntry) {
        GuiSlider guiSlider = new GuiSlider(this.field_178076_z, guiSlideEntry.func_178935_b(), n, n2, guiSlideEntry.func_178936_c(), guiSlideEntry.func_178943_e(), guiSlideEntry.func_178944_f(), guiSlideEntry.func_178942_g(), guiSlideEntry.func_178945_a());
        guiSlider.visible = guiSlideEntry.func_178934_d();
        return guiSlider;
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }

    @Override
    public int getSize() {
        return this.field_178074_u.size();
    }

    public Gui func_178056_g() {
        return this.field_178075_A;
    }

    private Gui func_178058_a(GuiListEntry guiListEntry, int n, boolean bl) {
        return guiListEntry instanceof GuiSlideEntry ? this.func_178067_a(this.width / 2 - 155 + n, 0, (GuiSlideEntry)guiListEntry) : (guiListEntry instanceof GuiButtonEntry ? this.func_178065_a(this.width / 2 - 155 + n, 0, (GuiButtonEntry)guiListEntry) : (guiListEntry instanceof EditBoxEntry ? this.func_178068_a(this.width / 2 - 155 + n, 0, (EditBoxEntry)guiListEntry) : (guiListEntry instanceof GuiLabelEntry ? this.func_178063_a(this.width / 2 - 155 + n, 0, (GuiLabelEntry)guiListEntry, bl) : null)));
    }

    public void func_181155_a(boolean bl) {
        for (GuiEntry guiEntry : this.field_178074_u) {
            if (guiEntry.field_178029_b instanceof GuiButton) {
                ((GuiButton)((GuiEntry)guiEntry).field_178029_b).enabled = bl;
            }
            if (!(guiEntry.field_178030_c instanceof GuiButton)) continue;
            ((GuiButton)((GuiEntry)guiEntry).field_178030_c).enabled = bl;
        }
    }

    public GuiPageButtonList(Minecraft minecraft, int n, int n2, int n3, int n4, int n5, GuiResponder guiResponder, GuiListEntry[] ... guiListEntryArray) {
        super(minecraft, n, n2, n3, n4, n5);
        this.field_178072_w = Lists.newArrayList();
        this.field_178076_z = guiResponder;
        this.field_178078_x = guiListEntryArray;
        this.field_148163_i = false;
        this.func_178069_s();
        this.func_178055_t();
    }

    public void func_178062_a(char c, int n) {
        block1: {
            int n2;
            block2: {
                GuiTextField guiTextField;
                block3: {
                    int n3;
                    block4: {
                        if (!(this.field_178075_A instanceof GuiTextField)) break block1;
                        guiTextField = (GuiTextField)this.field_178075_A;
                        if (GuiScreen.isKeyComboCtrlV(n)) break block2;
                        if (n != 15) break block3;
                        guiTextField.setFocused(false);
                        int n4 = this.field_178072_w.indexOf(this.field_178075_A);
                        n4 = GuiScreen.isShiftKeyDown() ? (n4 == 0 ? this.field_178072_w.size() - 1 : --n4) : (n4 == this.field_178072_w.size() - 1 ? 0 : ++n4);
                        this.field_178075_A = this.field_178072_w.get(n4);
                        guiTextField = (GuiTextField)this.field_178075_A;
                        guiTextField.setFocused(true);
                        int n5 = guiTextField.yPosition + this.slotHeight;
                        n3 = guiTextField.yPosition;
                        if (n5 <= this.bottom) break block4;
                        this.amountScrolled += (float)(n5 - this.bottom);
                        break block1;
                    }
                    if (n3 >= this.top) break block1;
                    this.amountScrolled = n3;
                    break block1;
                }
                guiTextField.textboxKeyTyped(c, n);
                break block1;
            }
            String string = GuiScreen.getClipboardString();
            String[] stringArray = string.split(";");
            int n6 = n2 = this.field_178072_w.indexOf(this.field_178075_A);
            String[] stringArray2 = stringArray;
            int n7 = stringArray.length;
            int n8 = 0;
            while (n8 < n7) {
                String string2 = stringArray2[n8];
                this.field_178072_w.get(n6).setText(string2);
                n6 = n6 == this.field_178072_w.size() - 1 ? 0 : ++n6;
                if (n6 == n2) break;
                ++n8;
            }
        }
    }

    public void func_181156_c(int n) {
        if (n != this.field_178077_y) {
            int n2 = this.field_178077_y;
            this.field_178077_y = n;
            this.func_178055_t();
            this.func_178060_e(n2, n);
            this.amountScrolled = 0.0f;
        }
    }

    public int func_178057_f() {
        return this.field_178078_x.length;
    }

    public void func_178071_h() {
        if (this.field_178077_y > 0) {
            this.func_181156_c(this.field_178077_y - 1);
        }
    }

    private GuiListButton func_178065_a(int n, int n2, GuiButtonEntry guiButtonEntry) {
        GuiListButton guiListButton = new GuiListButton(this.field_178076_z, guiButtonEntry.func_178935_b(), n, n2, guiButtonEntry.func_178936_c(), guiButtonEntry.func_178940_a());
        guiListButton.visible = guiButtonEntry.func_178934_d();
        return guiListButton;
    }

    private GuiLabel func_178063_a(int n, int n2, GuiLabelEntry guiLabelEntry, boolean bl) {
        GuiLabel guiLabel = bl ? new GuiLabel(Minecraft.fontRendererObj, guiLabelEntry.func_178935_b(), n, n2, this.width - n * 2, 20, -1) : new GuiLabel(Minecraft.fontRendererObj, guiLabelEntry.func_178935_b(), n, n2, 150, 20, -1);
        guiLabel.visible = guiLabelEntry.func_178934_d();
        guiLabel.func_175202_a(guiLabelEntry.func_178936_c());
        guiLabel.setCentered();
        return guiLabel;
    }

    private void func_178069_s() {
        GuiListEntry[][] guiListEntryArray = this.field_178078_x;
        int n = this.field_178078_x.length;
        int n2 = 0;
        while (n2 < n) {
            GuiListEntry[] guiListEntryArray2 = guiListEntryArray[n2];
            int n3 = 0;
            while (n3 < guiListEntryArray2.length) {
                GuiListEntry guiListEntry = guiListEntryArray2[n3];
                GuiListEntry guiListEntry2 = n3 < guiListEntryArray2.length - 1 ? guiListEntryArray2[n3 + 1] : null;
                Gui gui = this.func_178058_a(guiListEntry, 0, guiListEntry2 == null);
                Gui gui2 = this.func_178058_a(guiListEntry2, 160, guiListEntry == null);
                GuiEntry guiEntry = new GuiEntry(gui, gui2);
                this.field_178074_u.add(guiEntry);
                if (guiListEntry != null && gui != null) {
                    this.field_178073_v.addKey(guiListEntry.func_178935_b(), gui);
                    if (gui instanceof GuiTextField) {
                        this.field_178072_w.add((GuiTextField)gui);
                    }
                }
                if (guiListEntry2 != null && gui2 != null) {
                    this.field_178073_v.addKey(guiListEntry2.func_178935_b(), gui2);
                    if (gui2 instanceof GuiTextField) {
                        this.field_178072_w.add((GuiTextField)gui2);
                    }
                }
                n3 += 2;
            }
            ++n2;
        }
    }

    @Override
    public boolean mouseClicked(int n, int n2, int n3) {
        boolean bl = super.mouseClicked(n, n2, n3);
        int n4 = this.getSlotIndexFromScreenCoords(n, n2);
        if (n4 >= 0) {
            GuiEntry guiEntry = this.getListEntry(n4);
            if (this.field_178075_A != guiEntry.field_178028_d && this.field_178075_A != null && this.field_178075_A instanceof GuiTextField) {
                ((GuiTextField)this.field_178075_A).setFocused(false);
            }
            this.field_178075_A = guiEntry.field_178028_d;
        }
        return bl;
    }

    private void func_178055_t() {
        this.field_178074_u.clear();
        int n = 0;
        while (n < this.field_178078_x[this.field_178077_y].length) {
            GuiListEntry guiListEntry = this.field_178078_x[this.field_178077_y][n];
            GuiListEntry guiListEntry2 = n < this.field_178078_x[this.field_178077_y].length - 1 ? this.field_178078_x[this.field_178077_y][n + 1] : null;
            Gui gui = this.field_178073_v.lookup(guiListEntry.func_178935_b());
            Gui gui2 = guiListEntry2 != null ? this.field_178073_v.lookup(guiListEntry2.func_178935_b()) : null;
            GuiEntry guiEntry = new GuiEntry(gui, gui2);
            this.field_178074_u.add(guiEntry);
            n += 2;
        }
    }

    @Override
    public int getListWidth() {
        return 400;
    }

    public static class GuiListEntry {
        private final int field_178939_a;
        private final String field_178937_b;
        private final boolean field_178938_c;

        public GuiListEntry(int n, String string, boolean bl) {
            this.field_178939_a = n;
            this.field_178937_b = string;
            this.field_178938_c = bl;
        }

        public boolean func_178934_d() {
            return this.field_178938_c;
        }

        public String func_178936_c() {
            return this.field_178937_b;
        }

        public int func_178935_b() {
            return this.field_178939_a;
        }
    }

    public static class GuiSlideEntry
    extends GuiListEntry {
        private final GuiSlider.FormatHelper field_178949_a;
        private final float field_178946_d;
        private final float field_178947_b;
        private final float field_178948_c;

        public GuiSlider.FormatHelper func_178945_a() {
            return this.field_178949_a;
        }

        public float func_178943_e() {
            return this.field_178947_b;
        }

        public float func_178944_f() {
            return this.field_178948_c;
        }

        public float func_178942_g() {
            return this.field_178946_d;
        }

        public GuiSlideEntry(int n, String string, boolean bl, GuiSlider.FormatHelper formatHelper, float f, float f2, float f3) {
            super(n, string, bl);
            this.field_178949_a = formatHelper;
            this.field_178947_b = f;
            this.field_178948_c = f2;
            this.field_178946_d = f3;
        }
    }

    public static class GuiLabelEntry
    extends GuiListEntry {
        public GuiLabelEntry(int n, String string, boolean bl) {
            super(n, string, bl);
        }
    }

    public static class EditBoxEntry
    extends GuiListEntry {
        private final Predicate<String> field_178951_a;

        public Predicate<String> func_178950_a() {
            return this.field_178951_a;
        }

        public EditBoxEntry(int n, String string, boolean bl, Predicate<String> predicate) {
            super(n, string, bl);
            this.field_178951_a = (Predicate)Objects.firstNonNull(predicate, (Object)Predicates.alwaysTrue());
        }
    }

    public static interface GuiResponder {
        public void func_175321_a(int var1, boolean var2);

        public void onTick(int var1, float var2);

        public void func_175319_a(int var1, String var2);
    }

    public static class GuiButtonEntry
    extends GuiListEntry {
        private final boolean field_178941_a;

        public GuiButtonEntry(int n, String string, boolean bl, boolean bl2) {
            super(n, string, bl);
            this.field_178941_a = bl2;
        }

        public boolean func_178940_a() {
            return this.field_178941_a;
        }
    }

    public static class GuiEntry
    implements GuiListExtended.IGuiListEntry {
        private final Gui field_178029_b;
        private final Minecraft field_178031_a = Minecraft.getMinecraft();
        private Gui field_178028_d;
        private final Gui field_178030_c;

        private void func_178025_a(GuiLabel guiLabel, int n, int n2, int n3, boolean bl) {
            guiLabel.field_146174_h = n;
            if (!bl) {
                guiLabel.drawLabel(this.field_178031_a, n2, n3);
            }
        }

        public GuiEntry(Gui gui, Gui gui2) {
            this.field_178029_b = gui;
            this.field_178030_c = gui2;
        }

        private void func_178017_a(Gui gui, int n, int n2, int n3, boolean bl) {
            if (gui != null) {
                if (gui instanceof GuiButton) {
                    this.func_178024_a((GuiButton)gui, n, n2, n3, bl);
                } else if (gui instanceof GuiTextField) {
                    this.func_178027_a((GuiTextField)gui, n, bl);
                } else if (gui instanceof GuiLabel) {
                    this.func_178025_a((GuiLabel)gui, n, n2, n3, bl);
                }
            }
        }

        private void func_178027_a(GuiTextField guiTextField, int n, boolean bl) {
            guiTextField.yPosition = n;
            if (!bl) {
                guiTextField.drawTextBox();
            }
        }

        @Override
        public void setSelected(int n, int n2, int n3) {
            this.func_178017_a(this.field_178029_b, n3, 0, 0, true);
            this.func_178017_a(this.field_178030_c, n3, 0, 0, true);
        }

        private void func_178016_b(Gui gui, int n, int n2, int n3) {
            if (gui != null && gui instanceof GuiButton) {
                this.func_178019_b((GuiButton)gui, n, n2, n3);
            }
        }

        private void func_178024_a(GuiButton guiButton, int n, int n2, int n3, boolean bl) {
            guiButton.yPosition = n;
            if (!bl) {
                guiButton.drawButton(this.field_178031_a, n2, n3);
            }
        }

        public Gui func_178022_a() {
            return this.field_178029_b;
        }

        private boolean func_178026_a(Gui gui, int n, int n2, int n3) {
            if (gui == null) {
                return false;
            }
            if (gui instanceof GuiButton) {
                return this.func_178023_a((GuiButton)gui, n, n2, n3);
            }
            if (gui instanceof GuiTextField) {
                this.func_178018_a((GuiTextField)gui, n, n2, n3);
            }
            return false;
        }

        @Override
        public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
            this.func_178016_b(this.field_178029_b, n2, n3, n4);
            this.func_178016_b(this.field_178030_c, n2, n3, n4);
        }

        @Override
        public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
            this.func_178017_a(this.field_178029_b, n3, n6, n7, false);
            this.func_178017_a(this.field_178030_c, n3, n6, n7, false);
        }

        private boolean func_178023_a(GuiButton guiButton, int n, int n2, int n3) {
            boolean bl = guiButton.mousePressed(this.field_178031_a, n, n2);
            if (bl) {
                this.field_178028_d = guiButton;
            }
            return bl;
        }

        @Override
        public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
            boolean bl = this.func_178026_a(this.field_178029_b, n2, n3, n4);
            boolean bl2 = this.func_178026_a(this.field_178030_c, n2, n3, n4);
            return bl || bl2;
        }

        private void func_178019_b(GuiButton guiButton, int n, int n2, int n3) {
            guiButton.mouseReleased(n, n2);
        }

        private void func_178018_a(GuiTextField guiTextField, int n, int n2, int n3) {
            guiTextField.mouseClicked(n, n2, n3);
            if (guiTextField.isFocused()) {
                this.field_178028_d = guiTextField;
            }
        }

        public Gui func_178021_b() {
            return this.field_178030_c;
        }
    }
}

