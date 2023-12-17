// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.advancements;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Lists;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.Advancement;
import java.util.regex.Pattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiAdvancement extends Gui
{
    private static final ResourceLocation WIDGETS;
    private static final Pattern PATTERN;
    private final GuiAdvancementTab guiAdvancementTab;
    private final Advancement advancement;
    private final DisplayInfo displayInfo;
    private final String title;
    private final int width;
    private final List<String> description;
    private final Minecraft minecraft;
    private GuiAdvancement parent;
    private final List<GuiAdvancement> children;
    private AdvancementProgress advancementProgress;
    private final int x;
    private final int y;
    
    public GuiAdvancement(final GuiAdvancementTab p_i47385_1_, final Minecraft p_i47385_2_, final Advancement p_i47385_3_, final DisplayInfo p_i47385_4_) {
        this.children = (List<GuiAdvancement>)Lists.newArrayList();
        this.guiAdvancementTab = p_i47385_1_;
        this.advancement = p_i47385_3_;
        this.displayInfo = p_i47385_4_;
        this.minecraft = p_i47385_2_;
        this.title = p_i47385_2_.fontRenderer.trimStringToWidth(p_i47385_4_.getTitle().getFormattedText(), 163);
        this.x = MathHelper.floor(p_i47385_4_.getX() * 28.0f);
        this.y = MathHelper.floor(p_i47385_4_.getY() * 27.0f);
        final int i = p_i47385_3_.getRequirementCount();
        final int j = String.valueOf(i).length();
        final int k = (i > 1) ? (p_i47385_2_.fontRenderer.getStringWidth("  ") + p_i47385_2_.fontRenderer.getStringWidth("0") * j * 2 + p_i47385_2_.fontRenderer.getStringWidth("/")) : 0;
        int l = 29 + p_i47385_2_.fontRenderer.getStringWidth(this.title) + k;
        final String s = p_i47385_4_.getDescription().getFormattedText();
        this.description = this.findOptimalLines(s, l);
        for (final String s2 : this.description) {
            l = Math.max(l, p_i47385_2_.fontRenderer.getStringWidth(s2));
        }
        this.width = l + 3 + 5;
    }
    
    private List<String> findOptimalLines(final String p_192995_1_, final int p_192995_2_) {
        if (p_192995_1_.isEmpty()) {
            return Collections.emptyList();
        }
        final List<String> list = this.minecraft.fontRenderer.listFormattedStringToWidth(p_192995_1_, p_192995_2_);
        if (list.size() < 2) {
            return list;
        }
        final String s = list.get(0);
        final String s2 = list.get(1);
        final int i = this.minecraft.fontRenderer.getStringWidth(s + ' ' + s2.split(" ")[0]);
        if (i - p_192995_2_ <= 10) {
            return this.minecraft.fontRenderer.listFormattedStringToWidth(p_192995_1_, i);
        }
        final Matcher matcher = GuiAdvancement.PATTERN.matcher(s);
        if (matcher.matches()) {
            final int j = this.minecraft.fontRenderer.getStringWidth(matcher.group(1));
            if (p_192995_2_ - j <= 10) {
                return this.minecraft.fontRenderer.listFormattedStringToWidth(p_192995_1_, j);
            }
        }
        return list;
    }
    
    @Nullable
    private GuiAdvancement getFirstVisibleParent(Advancement advancementIn) {
        do {
            advancementIn = advancementIn.getParent();
        } while (advancementIn != null && advancementIn.getDisplay() == null);
        if (advancementIn != null && advancementIn.getDisplay() != null) {
            return this.guiAdvancementTab.getAdvancementGui(advancementIn);
        }
        return null;
    }
    
    public void drawConnectivity(final int p_191819_1_, final int p_191819_2_, final boolean p_191819_3_) {
        if (this.parent != null) {
            final int i = p_191819_1_ + this.parent.x + 13;
            final int j = p_191819_1_ + this.parent.x + 26 + 4;
            final int k = p_191819_2_ + this.parent.y + 13;
            final int l = p_191819_1_ + this.x + 13;
            final int i2 = p_191819_2_ + this.y + 13;
            final int j2 = p_191819_3_ ? -16777216 : -1;
            if (p_191819_3_) {
                this.drawHorizontalLine(j, i, k - 1, j2);
                this.drawHorizontalLine(j + 1, i, k, j2);
                this.drawHorizontalLine(j, i, k + 1, j2);
                this.drawHorizontalLine(l, j - 1, i2 - 1, j2);
                this.drawHorizontalLine(l, j - 1, i2, j2);
                this.drawHorizontalLine(l, j - 1, i2 + 1, j2);
                this.drawVerticalLine(j - 1, i2, k, j2);
                this.drawVerticalLine(j + 1, i2, k, j2);
            }
            else {
                this.drawHorizontalLine(j, i, k, j2);
                this.drawHorizontalLine(l, j, i2, j2);
                this.drawVerticalLine(j, i2, k, j2);
            }
        }
        for (final GuiAdvancement guiadvancement : this.children) {
            guiadvancement.drawConnectivity(p_191819_1_, p_191819_2_, p_191819_3_);
        }
    }
    
    public void draw(final int p_191817_1_, final int p_191817_2_) {
        if (!this.displayInfo.isHidden() || (this.advancementProgress != null && this.advancementProgress.isDone())) {
            final float f = (this.advancementProgress == null) ? 0.0f : this.advancementProgress.getPercent();
            AdvancementState advancementstate;
            if (f >= 1.0f) {
                advancementstate = AdvancementState.OBTAINED;
            }
            else {
                advancementstate = AdvancementState.UNOBTAINED;
            }
            this.minecraft.getTextureManager().bindTexture(GuiAdvancement.WIDGETS);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            this.drawTexturedModalRect(p_191817_1_ + this.x + 3, p_191817_2_ + this.y, this.displayInfo.getFrame().getIcon(), 128 + advancementstate.getId() * 26, 26, 26);
            RenderHelper.enableGUIStandardItemLighting();
            this.minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, this.displayInfo.getIcon(), p_191817_1_ + this.x + 8, p_191817_2_ + this.y + 5);
        }
        for (final GuiAdvancement guiadvancement : this.children) {
            guiadvancement.draw(p_191817_1_, p_191817_2_);
        }
    }
    
    public void setAdvancementProgress(final AdvancementProgress advancementProgressIn) {
        this.advancementProgress = advancementProgressIn;
    }
    
    public void addGuiAdvancement(final GuiAdvancement guiAdvancementIn) {
        this.children.add(guiAdvancementIn);
    }
    
    public void drawHover(final int p_191821_1_, final int p_191821_2_, final float p_191821_3_, final int p_191821_4_, final int p_191821_5_) {
        final boolean flag = p_191821_4_ + p_191821_1_ + this.x + this.width + 26 >= this.guiAdvancementTab.getScreen().width;
        final String s = (this.advancementProgress == null) ? null : this.advancementProgress.getProgressText();
        final int i = (s == null) ? 0 : this.minecraft.fontRenderer.getStringWidth(s);
        final boolean flag2 = 113 - p_191821_2_ - this.y - 26 <= 6 + this.description.size() * this.minecraft.fontRenderer.FONT_HEIGHT;
        final float f = (this.advancementProgress == null) ? 0.0f : this.advancementProgress.getPercent();
        int j = MathHelper.floor(f * this.width);
        AdvancementState advancementstate;
        AdvancementState advancementstate2;
        AdvancementState advancementstate3;
        if (f >= 1.0f) {
            j = this.width / 2;
            advancementstate = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.OBTAINED;
            advancementstate3 = AdvancementState.OBTAINED;
        }
        else if (j < 2) {
            j = this.width / 2;
            advancementstate = AdvancementState.UNOBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
            advancementstate3 = AdvancementState.UNOBTAINED;
        }
        else if (j > this.width - 2) {
            j = this.width / 2;
            advancementstate = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.OBTAINED;
            advancementstate3 = AdvancementState.UNOBTAINED;
        }
        else {
            advancementstate = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
            advancementstate3 = AdvancementState.UNOBTAINED;
        }
        final int k = this.width - j;
        this.minecraft.getTextureManager().bindTexture(GuiAdvancement.WIDGETS);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        final int l = p_191821_2_ + this.y;
        int i2;
        if (flag) {
            i2 = p_191821_1_ + this.x - this.width + 26 + 6;
        }
        else {
            i2 = p_191821_1_ + this.x;
        }
        final int j2 = 32 + this.description.size() * this.minecraft.fontRenderer.FONT_HEIGHT;
        if (!this.description.isEmpty()) {
            if (flag2) {
                this.render9Sprite(i2, l + 26 - j2, this.width, j2, 10, 200, 26, 0, 52);
            }
            else {
                this.render9Sprite(i2, l, this.width, j2, 10, 200, 26, 0, 52);
            }
        }
        this.drawTexturedModalRect(i2, l, 0, advancementstate.getId() * 26, j, 26);
        this.drawTexturedModalRect(i2 + j, l, 200 - k, advancementstate2.getId() * 26, k, 26);
        this.drawTexturedModalRect(p_191821_1_ + this.x + 3, p_191821_2_ + this.y, this.displayInfo.getFrame().getIcon(), 128 + advancementstate3.getId() * 26, 26, 26);
        if (flag) {
            this.minecraft.fontRenderer.drawString(this.title, (float)(i2 + 5), (float)(p_191821_2_ + this.y + 9), -1, true);
            if (s != null) {
                this.minecraft.fontRenderer.drawString(s, (float)(p_191821_1_ + this.x - i), (float)(p_191821_2_ + this.y + 9), -1, true);
            }
        }
        else {
            this.minecraft.fontRenderer.drawString(this.title, (float)(p_191821_1_ + this.x + 32), (float)(p_191821_2_ + this.y + 9), -1, true);
            if (s != null) {
                this.minecraft.fontRenderer.drawString(s, (float)(p_191821_1_ + this.x + this.width - i - 5), (float)(p_191821_2_ + this.y + 9), -1, true);
            }
        }
        if (flag2) {
            for (int k2 = 0; k2 < this.description.size(); ++k2) {
                this.minecraft.fontRenderer.drawString(this.description.get(k2), (float)(i2 + 5), (float)(l + 26 - j2 + 7 + k2 * this.minecraft.fontRenderer.FONT_HEIGHT), -5592406, false);
            }
        }
        else {
            for (int l2 = 0; l2 < this.description.size(); ++l2) {
                this.minecraft.fontRenderer.drawString(this.description.get(l2), (float)(i2 + 5), (float)(p_191821_2_ + this.y + 9 + 17 + l2 * this.minecraft.fontRenderer.FONT_HEIGHT), -5592406, false);
            }
        }
        RenderHelper.enableGUIStandardItemLighting();
        this.minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, this.displayInfo.getIcon(), p_191821_1_ + this.x + 8, p_191821_2_ + this.y + 5);
    }
    
    protected void render9Sprite(final int p_192994_1_, final int p_192994_2_, final int p_192994_3_, final int p_192994_4_, final int p_192994_5_, final int p_192994_6_, final int p_192994_7_, final int p_192994_8_, final int p_192994_9_) {
        this.drawTexturedModalRect(p_192994_1_, p_192994_2_, p_192994_8_, p_192994_9_, p_192994_5_, p_192994_5_);
        this.renderRepeating(p_192994_1_ + p_192994_5_, p_192994_2_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
        this.drawTexturedModalRect(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_, p_192994_5_, p_192994_5_);
        this.drawTexturedModalRect(p_192994_1_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
        this.renderRepeating(p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
        this.drawTexturedModalRect(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
        this.renderRepeating(p_192994_1_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
        this.renderRepeating(p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_ - p_192994_5_ - p_192994_5_);
        this.renderRepeating(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
    }
    
    protected void renderRepeating(final int p_192993_1_, final int p_192993_2_, final int p_192993_3_, final int p_192993_4_, final int p_192993_5_, final int p_192993_6_, final int p_192993_7_, final int p_192993_8_) {
        for (int i = 0; i < p_192993_3_; i += p_192993_7_) {
            final int j = p_192993_1_ + i;
            final int k = Math.min(p_192993_7_, p_192993_3_ - i);
            for (int l = 0; l < p_192993_4_; l += p_192993_8_) {
                final int i2 = p_192993_2_ + l;
                final int j2 = Math.min(p_192993_8_, p_192993_4_ - l);
                this.drawTexturedModalRect(j, i2, p_192993_5_, p_192993_6_, k, j2);
            }
        }
    }
    
    public boolean isMouseOver(final int p_191816_1_, final int p_191816_2_, final int p_191816_3_, final int p_191816_4_) {
        if (!this.displayInfo.isHidden() || (this.advancementProgress != null && this.advancementProgress.isDone())) {
            final int i = p_191816_1_ + this.x;
            final int j = i + 26;
            final int k = p_191816_2_ + this.y;
            final int l = k + 26;
            return p_191816_3_ >= i && p_191816_3_ <= j && p_191816_4_ >= k && p_191816_4_ <= l;
        }
        return false;
    }
    
    public void attachToParent() {
        if (this.parent == null && this.advancement.getParent() != null) {
            this.parent = this.getFirstVisibleParent(this.advancement);
            if (this.parent != null) {
                this.parent.addGuiAdvancement(this);
            }
        }
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getX() {
        return this.x;
    }
    
    static {
        WIDGETS = new ResourceLocation("textures/gui/advancements/widgets.png");
        PATTERN = Pattern.compile("(.+) \\S+");
    }
}
