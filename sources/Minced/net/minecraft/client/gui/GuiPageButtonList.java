// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IntHashMap;
import java.util.List;

public class GuiPageButtonList extends GuiListExtended
{
    private final List<GuiEntry> entries;
    private final IntHashMap<Gui> componentMap;
    private final List<GuiTextField> editBoxes;
    private final GuiListEntry[][] pages;
    private int page;
    private final GuiResponder responder;
    private Gui focusedControl;
    
    public GuiPageButtonList(final Minecraft mcIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn, final GuiResponder p_i45536_7_, final GuiListEntry[]... p_i45536_8_) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.entries = (List<GuiEntry>)Lists.newArrayList();
        this.componentMap = new IntHashMap<Gui>();
        this.editBoxes = (List<GuiTextField>)Lists.newArrayList();
        this.responder = p_i45536_7_;
        this.pages = p_i45536_8_;
        this.centerListVertically = false;
        this.populateComponents();
        this.populateEntries();
    }
    
    private void populateComponents() {
        for (final GuiListEntry[] aguipagebuttonlist$guilistentry : this.pages) {
            for (int i = 0; i < aguipagebuttonlist$guilistentry.length; i += 2) {
                final GuiListEntry guipagebuttonlist$guilistentry = aguipagebuttonlist$guilistentry[i];
                final GuiListEntry guipagebuttonlist$guilistentry2 = (i < aguipagebuttonlist$guilistentry.length - 1) ? aguipagebuttonlist$guilistentry[i + 1] : null;
                final Gui gui = this.createEntry(guipagebuttonlist$guilistentry, 0, guipagebuttonlist$guilistentry2 == null);
                final Gui gui2 = this.createEntry(guipagebuttonlist$guilistentry2, 160, guipagebuttonlist$guilistentry == null);
                final GuiEntry guipagebuttonlist$guientry = new GuiEntry(gui, gui2);
                this.entries.add(guipagebuttonlist$guientry);
                if (guipagebuttonlist$guilistentry != null && gui != null) {
                    this.componentMap.addKey(guipagebuttonlist$guilistentry.getId(), gui);
                    if (gui instanceof GuiTextField) {
                        this.editBoxes.add((GuiTextField)gui);
                    }
                }
                if (guipagebuttonlist$guilistentry2 != null && gui2 != null) {
                    this.componentMap.addKey(guipagebuttonlist$guilistentry2.getId(), gui2);
                    if (gui2 instanceof GuiTextField) {
                        this.editBoxes.add((GuiTextField)gui2);
                    }
                }
            }
        }
    }
    
    private void populateEntries() {
        this.entries.clear();
        for (int i = 0; i < this.pages[this.page].length; i += 2) {
            final GuiListEntry guipagebuttonlist$guilistentry = this.pages[this.page][i];
            final GuiListEntry guipagebuttonlist$guilistentry2 = (i < this.pages[this.page].length - 1) ? this.pages[this.page][i + 1] : null;
            final Gui gui = this.componentMap.lookup(guipagebuttonlist$guilistentry.getId());
            final Gui gui2 = (guipagebuttonlist$guilistentry2 != null) ? this.componentMap.lookup(guipagebuttonlist$guilistentry2.getId()) : null;
            final GuiEntry guipagebuttonlist$guientry = new GuiEntry(gui, gui2);
            this.entries.add(guipagebuttonlist$guientry);
        }
    }
    
    public void setPage(final int p_181156_1_) {
        if (p_181156_1_ != this.page) {
            final int i = this.page;
            this.page = p_181156_1_;
            this.populateEntries();
            this.markVisibility(i, p_181156_1_);
            this.amountScrolled = 0.0f;
        }
    }
    
    public int getPage() {
        return this.page;
    }
    
    public int getPageCount() {
        return this.pages.length;
    }
    
    public Gui getFocusedControl() {
        return this.focusedControl;
    }
    
    public void previousPage() {
        if (this.page > 0) {
            this.setPage(this.page - 1);
        }
    }
    
    public void nextPage() {
        if (this.page < this.pages.length - 1) {
            this.setPage(this.page + 1);
        }
    }
    
    public Gui getComponent(final int p_178061_1_) {
        return this.componentMap.lookup(p_178061_1_);
    }
    
    private void markVisibility(final int p_178060_1_, final int p_178060_2_) {
        for (final GuiListEntry guipagebuttonlist$guilistentry : this.pages[p_178060_1_]) {
            if (guipagebuttonlist$guilistentry != null) {
                this.setComponentVisibility(this.componentMap.lookup(guipagebuttonlist$guilistentry.getId()), false);
            }
        }
        for (final GuiListEntry guipagebuttonlist$guilistentry2 : this.pages[p_178060_2_]) {
            if (guipagebuttonlist$guilistentry2 != null) {
                this.setComponentVisibility(this.componentMap.lookup(guipagebuttonlist$guilistentry2.getId()), true);
            }
        }
    }
    
    private void setComponentVisibility(final Gui p_178066_1_, final boolean p_178066_2_) {
        if (p_178066_1_ instanceof GuiButton) {
            ((GuiButton)p_178066_1_).visible = p_178066_2_;
        }
        else if (p_178066_1_ instanceof GuiTextField) {
            ((GuiTextField)p_178066_1_).setVisible(p_178066_2_);
        }
        else if (p_178066_1_ instanceof GuiLabel) {
            ((GuiLabel)p_178066_1_).visible = p_178066_2_;
        }
    }
    
    @Nullable
    private Gui createEntry(@Nullable final GuiListEntry p_178058_1_, final int p_178058_2_, final boolean p_178058_3_) {
        if (p_178058_1_ instanceof GuiSlideEntry) {
            return this.createSlider(this.width / 2 - 155 + p_178058_2_, 0, (GuiSlideEntry)p_178058_1_);
        }
        if (p_178058_1_ instanceof GuiButtonEntry) {
            return this.createButton(this.width / 2 - 155 + p_178058_2_, 0, (GuiButtonEntry)p_178058_1_);
        }
        if (p_178058_1_ instanceof EditBoxEntry) {
            return this.createTextField(this.width / 2 - 155 + p_178058_2_, 0, (EditBoxEntry)p_178058_1_);
        }
        return (p_178058_1_ instanceof GuiLabelEntry) ? this.createLabel(this.width / 2 - 155 + p_178058_2_, 0, (GuiLabelEntry)p_178058_1_, p_178058_3_) : null;
    }
    
    public void setActive(final boolean p_181155_1_) {
        for (final GuiEntry guipagebuttonlist$guientry : this.entries) {
            if (guipagebuttonlist$guientry.component1 instanceof GuiButton) {
                ((GuiButton)guipagebuttonlist$guientry.component1).enabled = p_181155_1_;
            }
            if (guipagebuttonlist$guientry.component2 instanceof GuiButton) {
                ((GuiButton)guipagebuttonlist$guientry.component2).enabled = p_181155_1_;
            }
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseEvent) {
        final boolean flag = super.mouseClicked(mouseX, mouseY, mouseEvent);
        final int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
        if (i >= 0) {
            final GuiEntry guipagebuttonlist$guientry = this.getListEntry(i);
            if (this.focusedControl != guipagebuttonlist$guientry.focusedControl && this.focusedControl != null && this.focusedControl instanceof GuiTextField) {
                ((GuiTextField)this.focusedControl).setFocused(false);
            }
            this.focusedControl = guipagebuttonlist$guientry.focusedControl;
        }
        return flag;
    }
    
    private GuiSlider createSlider(final int p_178067_1_, final int p_178067_2_, final GuiSlideEntry p_178067_3_) {
        final GuiSlider guislider = new GuiSlider(this.responder, p_178067_3_.getId(), p_178067_1_, p_178067_2_, p_178067_3_.getCaption(), p_178067_3_.getMinValue(), p_178067_3_.getMaxValue(), p_178067_3_.getInitalValue(), p_178067_3_.getFormatter());
        guislider.visible = p_178067_3_.shouldStartVisible();
        return guislider;
    }
    
    private GuiListButton createButton(final int p_178065_1_, final int p_178065_2_, final GuiButtonEntry p_178065_3_) {
        final GuiListButton guilistbutton = new GuiListButton(this.responder, p_178065_3_.getId(), p_178065_1_, p_178065_2_, p_178065_3_.getCaption(), p_178065_3_.getInitialValue());
        guilistbutton.visible = p_178065_3_.shouldStartVisible();
        return guilistbutton;
    }
    
    private GuiTextField createTextField(final int p_178068_1_, final int p_178068_2_, final EditBoxEntry p_178068_3_) {
        final GuiTextField guitextfield = new GuiTextField(p_178068_3_.getId(), this.mc.fontRenderer, p_178068_1_, p_178068_2_, 150, 20);
        guitextfield.setText(p_178068_3_.getCaption());
        guitextfield.setGuiResponder(this.responder);
        guitextfield.setVisible(p_178068_3_.shouldStartVisible());
        guitextfield.setValidator(p_178068_3_.getFilter());
        return guitextfield;
    }
    
    private GuiLabel createLabel(final int p_178063_1_, final int p_178063_2_, final GuiLabelEntry p_178063_3_, final boolean p_178063_4_) {
        GuiLabel guilabel;
        if (p_178063_4_) {
            guilabel = new GuiLabel(this.mc.fontRenderer, p_178063_3_.getId(), p_178063_1_, p_178063_2_, this.width - p_178063_1_ * 2, 20, -1);
        }
        else {
            guilabel = new GuiLabel(this.mc.fontRenderer, p_178063_3_.getId(), p_178063_1_, p_178063_2_, 150, 20, -1);
        }
        guilabel.visible = p_178063_3_.shouldStartVisible();
        guilabel.addLine(p_178063_3_.getCaption());
        guilabel.setCentered();
        return guilabel;
    }
    
    public void onKeyPressed(final char p_178062_1_, final int p_178062_2_) {
        if (this.focusedControl instanceof GuiTextField) {
            GuiTextField guitextfield = (GuiTextField)this.focusedControl;
            if (!GuiScreen.isKeyComboCtrlV(p_178062_2_)) {
                if (p_178062_2_ == 15) {
                    guitextfield.setFocused(false);
                    int k = this.editBoxes.indexOf(this.focusedControl);
                    if (GuiScreen.isShiftKeyDown()) {
                        if (k == 0) {
                            k = this.editBoxes.size() - 1;
                        }
                        else {
                            --k;
                        }
                    }
                    else if (k == this.editBoxes.size() - 1) {
                        k = 0;
                    }
                    else {
                        ++k;
                    }
                    this.focusedControl = this.editBoxes.get(k);
                    guitextfield = (GuiTextField)this.focusedControl;
                    guitextfield.setFocused(true);
                    final int l = guitextfield.y + this.slotHeight;
                    final int i1 = guitextfield.y;
                    if (l > this.bottom) {
                        this.amountScrolled += l - this.bottom;
                    }
                    else if (i1 < this.top) {
                        this.amountScrolled = (float)i1;
                    }
                }
                else {
                    guitextfield.textboxKeyTyped(p_178062_1_, p_178062_2_);
                }
            }
            else {
                final String s = GuiScreen.getClipboardString();
                final String[] astring = s.split(";");
                int m;
                final int j = m = this.editBoxes.indexOf(this.focusedControl);
                for (final String s2 : astring) {
                    final GuiTextField guitextfield2 = this.editBoxes.get(m);
                    guitextfield2.setText(s2);
                    guitextfield2.setResponderEntryValue(guitextfield2.getId(), s2);
                    if (m == this.editBoxes.size() - 1) {
                        m = 0;
                    }
                    else {
                        ++m;
                    }
                    if (m == j) {
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public GuiEntry getListEntry(final int index) {
        return this.entries.get(index);
    }
    
    public int getSize() {
        return this.entries.size();
    }
    
    @Override
    public int getListWidth() {
        return 400;
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }
    
    public static class EditBoxEntry extends GuiListEntry
    {
        private final Predicate<String> filter;
        
        public EditBoxEntry(final int p_i45534_1_, final String p_i45534_2_, final boolean p_i45534_3_, final Predicate<String> p_i45534_4_) {
            super(p_i45534_1_, p_i45534_2_, p_i45534_3_);
            this.filter = (Predicate<String>)MoreObjects.firstNonNull((Object)p_i45534_4_, (Object)Predicates.alwaysTrue());
        }
        
        public Predicate<String> getFilter() {
            return this.filter;
        }
    }
    
    public static class GuiButtonEntry extends GuiListEntry
    {
        private final boolean initialValue;
        
        public GuiButtonEntry(final int p_i45535_1_, final String p_i45535_2_, final boolean p_i45535_3_, final boolean p_i45535_4_) {
            super(p_i45535_1_, p_i45535_2_, p_i45535_3_);
            this.initialValue = p_i45535_4_;
        }
        
        public boolean getInitialValue() {
            return this.initialValue;
        }
    }
    
    public static class GuiEntry implements IGuiListEntry
    {
        private final Minecraft client;
        private final Gui component1;
        private final Gui component2;
        private Gui focusedControl;
        
        public GuiEntry(@Nullable final Gui p_i45533_1_, @Nullable final Gui p_i45533_2_) {
            this.client = Minecraft.getMinecraft();
            this.component1 = p_i45533_1_;
            this.component2 = p_i45533_2_;
        }
        
        public Gui getComponent1() {
            return this.component1;
        }
        
        public Gui getComponent2() {
            return this.component2;
        }
        
        @Override
        public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float partialTicks) {
            this.renderComponent(this.component1, y, mouseX, mouseY, false, partialTicks);
            this.renderComponent(this.component2, y, mouseX, mouseY, false, partialTicks);
        }
        
        private void renderComponent(final Gui p_192636_1_, final int p_192636_2_, final int p_192636_3_, final int p_192636_4_, final boolean p_192636_5_, final float p_192636_6_) {
            if (p_192636_1_ != null) {
                if (p_192636_1_ instanceof GuiButton) {
                    this.renderButton((GuiButton)p_192636_1_, p_192636_2_, p_192636_3_, p_192636_4_, p_192636_5_, p_192636_6_);
                }
                else if (p_192636_1_ instanceof GuiTextField) {
                    this.renderTextField((GuiTextField)p_192636_1_, p_192636_2_, p_192636_5_);
                }
                else if (p_192636_1_ instanceof GuiLabel) {
                    this.renderLabel((GuiLabel)p_192636_1_, p_192636_2_, p_192636_3_, p_192636_4_, p_192636_5_);
                }
            }
        }
        
        private void renderButton(final GuiButton p_192635_1_, final int p_192635_2_, final int p_192635_3_, final int p_192635_4_, final boolean p_192635_5_, final float p_192635_6_) {
            p_192635_1_.y = p_192635_2_;
            if (!p_192635_5_) {
                p_192635_1_.drawButton(this.client, p_192635_3_, p_192635_4_, p_192635_6_);
            }
        }
        
        private void renderTextField(final GuiTextField p_178027_1_, final int p_178027_2_, final boolean p_178027_3_) {
            p_178027_1_.y = p_178027_2_;
            if (!p_178027_3_) {
                p_178027_1_.drawTextBox();
            }
        }
        
        private void renderLabel(final GuiLabel p_178025_1_, final int p_178025_2_, final int p_178025_3_, final int p_178025_4_, final boolean p_178025_5_) {
            p_178025_1_.y = p_178025_2_;
            if (!p_178025_5_) {
                p_178025_1_.drawLabel(this.client, p_178025_3_, p_178025_4_);
            }
        }
        
        @Override
        public void updatePosition(final int slotIndex, final int x, final int y, final float partialTicks) {
            this.renderComponent(this.component1, y, 0, 0, true, partialTicks);
            this.renderComponent(this.component2, y, 0, 0, true, partialTicks);
        }
        
        @Override
        public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
            final boolean flag = this.clickComponent(this.component1, mouseX, mouseY, mouseEvent);
            final boolean flag2 = this.clickComponent(this.component2, mouseX, mouseY, mouseEvent);
            return flag || flag2;
        }
        
        private boolean clickComponent(final Gui p_178026_1_, final int p_178026_2_, final int p_178026_3_, final int p_178026_4_) {
            if (p_178026_1_ == null) {
                return false;
            }
            if (p_178026_1_ instanceof GuiButton) {
                return this.clickButton((GuiButton)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
            }
            if (p_178026_1_ instanceof GuiTextField) {
                this.clickTextField((GuiTextField)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
            }
            return false;
        }
        
        private boolean clickButton(final GuiButton p_178023_1_, final int p_178023_2_, final int p_178023_3_, final int p_178023_4_) {
            final boolean flag = p_178023_1_.mousePressed(this.client, p_178023_2_, p_178023_3_);
            if (flag) {
                this.focusedControl = p_178023_1_;
            }
            return flag;
        }
        
        private void clickTextField(final GuiTextField p_178018_1_, final int p_178018_2_, final int p_178018_3_, final int p_178018_4_) {
            p_178018_1_.mouseClicked(p_178018_2_, p_178018_3_, p_178018_4_);
            if (p_178018_1_.isFocused()) {
                this.focusedControl = p_178018_1_;
            }
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            this.releaseComponent(this.component1, x, y, mouseEvent);
            this.releaseComponent(this.component2, x, y, mouseEvent);
        }
        
        private void releaseComponent(final Gui p_178016_1_, final int p_178016_2_, final int p_178016_3_, final int p_178016_4_) {
            if (p_178016_1_ != null && p_178016_1_ instanceof GuiButton) {
                this.releaseButton((GuiButton)p_178016_1_, p_178016_2_, p_178016_3_, p_178016_4_);
            }
        }
        
        private void releaseButton(final GuiButton p_178019_1_, final int p_178019_2_, final int p_178019_3_, final int p_178019_4_) {
            p_178019_1_.mouseReleased(p_178019_2_, p_178019_3_);
        }
    }
    
    public static class GuiLabelEntry extends GuiListEntry
    {
        public GuiLabelEntry(final int p_i45532_1_, final String p_i45532_2_, final boolean p_i45532_3_) {
            super(p_i45532_1_, p_i45532_2_, p_i45532_3_);
        }
    }
    
    public static class GuiListEntry
    {
        private final int id;
        private final String caption;
        private final boolean startVisible;
        
        public GuiListEntry(final int p_i45531_1_, final String p_i45531_2_, final boolean p_i45531_3_) {
            this.id = p_i45531_1_;
            this.caption = p_i45531_2_;
            this.startVisible = p_i45531_3_;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getCaption() {
            return this.caption;
        }
        
        public boolean shouldStartVisible() {
            return this.startVisible;
        }
    }
    
    public static class GuiSlideEntry extends GuiListEntry
    {
        private final GuiSlider.FormatHelper formatter;
        private final float minValue;
        private final float maxValue;
        private final float initialValue;
        
        public GuiSlideEntry(final int p_i45530_1_, final String p_i45530_2_, final boolean p_i45530_3_, final GuiSlider.FormatHelper p_i45530_4_, final float p_i45530_5_, final float p_i45530_6_, final float p_i45530_7_) {
            super(p_i45530_1_, p_i45530_2_, p_i45530_3_);
            this.formatter = p_i45530_4_;
            this.minValue = p_i45530_5_;
            this.maxValue = p_i45530_6_;
            this.initialValue = p_i45530_7_;
        }
        
        public GuiSlider.FormatHelper getFormatter() {
            return this.formatter;
        }
        
        public float getMinValue() {
            return this.minValue;
        }
        
        public float getMaxValue() {
            return this.maxValue;
        }
        
        public float getInitalValue() {
            return this.initialValue;
        }
    }
    
    public interface GuiResponder
    {
        void setEntryValue(final int p0, final boolean p1);
        
        void setEntryValue(final int p0, final float p1);
        
        void setEntryValue(final int p0, final String p1);
    }
}
