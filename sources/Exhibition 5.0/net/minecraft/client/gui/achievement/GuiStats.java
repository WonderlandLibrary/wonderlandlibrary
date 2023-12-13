// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.achievement;

import net.minecraft.entity.EntityList;
import java.util.Iterator;
import net.minecraft.stats.StatList;
import com.google.common.collect.Lists;
import java.util.Collections;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.GuiScreen;

public class GuiStats extends GuiScreen implements IProgressMeter
{
    protected GuiScreen parentScreen;
    protected String screenTitle;
    private StatsGeneral generalStats;
    private StatsItem itemStats;
    private StatsBlock blockStats;
    private StatsMobsList mobStats;
    private StatFileWriter field_146546_t;
    private GuiSlot displaySlot;
    private boolean doesGuiPauseGame;
    private static final String __OBFID = "CL_00000723";
    
    public GuiStats(final GuiScreen p_i1071_1_, final StatFileWriter p_i1071_2_) {
        this.screenTitle = "Select world";
        this.doesGuiPauseGame = true;
        this.parentScreen = p_i1071_1_;
        this.field_146546_t = p_i1071_2_;
    }
    
    @Override
    public void initGui() {
        this.screenTitle = I18n.format("gui.stats", new Object[0]);
        this.doesGuiPauseGame = true;
        this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.displaySlot != null) {
            this.displaySlot.handleMouseScrolling();
        }
    }
    
    public void func_175366_f() {
        (this.generalStats = new StatsGeneral(this.mc)).registerScrollButtons(1, 1);
        (this.itemStats = new StatsItem(this.mc)).registerScrollButtons(1, 1);
        (this.blockStats = new StatsBlock(this.mc)).registerScrollButtons(1, 1);
        (this.mobStats = new StatsMobsList(this.mc)).registerScrollButtons(1, 1);
    }
    
    public void createButtons() {
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 160, this.height - 52, 80, 20, I18n.format("stat.generalButton", new Object[0])));
        final GuiButton var1;
        this.buttonList.add(var1 = new GuiButton(2, this.width / 2 - 80, this.height - 52, 80, 20, I18n.format("stat.blocksButton", new Object[0])));
        final GuiButton var2;
        this.buttonList.add(var2 = new GuiButton(3, this.width / 2, this.height - 52, 80, 20, I18n.format("stat.itemsButton", new Object[0])));
        final GuiButton var3;
        this.buttonList.add(var3 = new GuiButton(4, this.width / 2 + 80, this.height - 52, 80, 20, I18n.format("stat.mobsButton", new Object[0])));
        if (this.blockStats.getSize() == 0) {
            var1.enabled = false;
        }
        if (this.itemStats.getSize() == 0) {
            var2.enabled = false;
        }
        if (this.mobStats.getSize() == 0) {
            var3.enabled = false;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 1) {
                this.displaySlot = this.generalStats;
            }
            else if (button.id == 3) {
                this.displaySlot = this.itemStats;
            }
            else if (button.id == 2) {
                this.displaySlot = this.blockStats;
            }
            else if (button.id == 4) {
                this.displaySlot = this.mobStats;
            }
            else {
                this.displaySlot.actionPerformed(button);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.doesGuiPauseGame) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), this.width / 2, this.height / 2, 16777215);
            this.drawCenteredString(this.fontRendererObj, GuiStats.lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % GuiStats.lanSearchStates.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
        }
        else {
            this.displaySlot.drawScreen(mouseX, mouseY, partialTicks);
            this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 20, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    public void doneLoading() {
        if (this.doesGuiPauseGame) {
            this.func_175366_f();
            this.createButtons();
            this.displaySlot = this.generalStats;
            this.doesGuiPauseGame = false;
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return !this.doesGuiPauseGame;
    }
    
    private void drawStatsScreen(final int p_146521_1_, final int p_146521_2_, final Item p_146521_3_) {
        this.drawButtonBackground(p_146521_1_ + 1, p_146521_2_ + 1);
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.remderItemIntoGUI(new ItemStack(p_146521_3_, 1, 0), p_146521_1_ + 2, p_146521_2_ + 2);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }
    
    private void drawButtonBackground(final int p_146531_1_, final int p_146531_2_) {
        this.drawSprite(p_146531_1_, p_146531_2_, 0, 0);
    }
    
    private void drawSprite(final int p_146527_1_, final int p_146527_2_, final int p_146527_3_, final int p_146527_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiStats.statIcons);
        final float var5 = 0.0078125f;
        final float var6 = 0.0078125f;
        final boolean var7 = true;
        final boolean var8 = true;
        final Tessellator var9 = Tessellator.getInstance();
        final WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(p_146527_1_ + 0, p_146527_2_ + 18, this.zLevel, (p_146527_3_ + 0) * 0.0078125f, (p_146527_4_ + 18) * 0.0078125f);
        var10.addVertexWithUV(p_146527_1_ + 18, p_146527_2_ + 18, this.zLevel, (p_146527_3_ + 18) * 0.0078125f, (p_146527_4_ + 18) * 0.0078125f);
        var10.addVertexWithUV(p_146527_1_ + 18, p_146527_2_ + 0, this.zLevel, (p_146527_3_ + 18) * 0.0078125f, (p_146527_4_ + 0) * 0.0078125f);
        var10.addVertexWithUV(p_146527_1_ + 0, p_146527_2_ + 0, this.zLevel, (p_146527_3_ + 0) * 0.0078125f, (p_146527_4_ + 0) * 0.0078125f);
        var9.draw();
    }
    
    abstract class Stats extends GuiSlot
    {
        protected int field_148218_l;
        protected List statsHolder;
        protected Comparator statSorter;
        protected int field_148217_o;
        protected int field_148215_p;
        private static final String __OBFID = "CL_00000730";
        
        protected Stats(final Minecraft mcIn) {
            super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 20);
            this.field_148218_l = -1;
            this.field_148217_o = -1;
            this.setShowSelectionBox(false);
            this.setHasListHeader(true, 20);
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
            if (!Mouse.isButtonDown(0)) {
                this.field_148218_l = -1;
            }
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 0);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 0);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 0);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 18);
            }
            if (this.field_148217_o != -1) {
                short var4 = 79;
                byte var5 = 18;
                if (this.field_148217_o == 1) {
                    var4 = 129;
                }
                else if (this.field_148217_o == 2) {
                    var4 = 179;
                }
                if (this.field_148215_p == 1) {
                    var5 = 36;
                }
                GuiStats.this.drawSprite(p_148129_1_ + var4, p_148129_2_ + 1, var5, 0);
            }
        }
        
        @Override
        protected void func_148132_a(final int p_148132_1_, final int p_148132_2_) {
            this.field_148218_l = -1;
            if (p_148132_1_ >= 79 && p_148132_1_ < 115) {
                this.field_148218_l = 0;
            }
            else if (p_148132_1_ >= 129 && p_148132_1_ < 165) {
                this.field_148218_l = 1;
            }
            else if (p_148132_1_ >= 179 && p_148132_1_ < 215) {
                this.field_148218_l = 2;
            }
            if (this.field_148218_l >= 0) {
                this.func_148212_h(this.field_148218_l);
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            }
        }
        
        @Override
        protected final int getSize() {
            return this.statsHolder.size();
        }
        
        protected final StatCrafting func_148211_c(final int p_148211_1_) {
            return this.statsHolder.get(p_148211_1_);
        }
        
        protected abstract String func_148210_b(final int p0);
        
        protected void func_148209_a(final StatBase p_148209_1_, final int p_148209_2_, final int p_148209_3_, final boolean p_148209_4_) {
            if (p_148209_1_ != null) {
                final String var5 = p_148209_1_.func_75968_a(GuiStats.this.field_146546_t.writeStat(p_148209_1_));
                GuiStats.this.drawString(GuiStats.this.fontRendererObj, var5, p_148209_2_ - GuiStats.this.fontRendererObj.getStringWidth(var5), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
            }
            else {
                final String var5 = "-";
                GuiStats.this.drawString(GuiStats.this.fontRendererObj, var5, p_148209_2_ - GuiStats.this.fontRendererObj.getStringWidth(var5), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
            }
        }
        
        @Override
        protected void func_148142_b(final int p_148142_1_, final int p_148142_2_) {
            if (p_148142_2_ >= this.top && p_148142_2_ <= this.bottom) {
                final int var3 = this.getSlotIndexFromScreenCoords(p_148142_1_, p_148142_2_);
                final int var4 = this.width / 2 - 92 - 16;
                if (var3 >= 0) {
                    if (p_148142_1_ < var4 + 40 || p_148142_1_ > var4 + 40 + 20) {
                        return;
                    }
                    final StatCrafting var5 = this.func_148211_c(var3);
                    this.func_148213_a(var5, p_148142_1_, p_148142_2_);
                }
                else {
                    String var6 = "";
                    if (p_148142_1_ >= var4 + 115 - 18 && p_148142_1_ <= var4 + 115) {
                        var6 = this.func_148210_b(0);
                    }
                    else if (p_148142_1_ >= var4 + 165 - 18 && p_148142_1_ <= var4 + 165) {
                        var6 = this.func_148210_b(1);
                    }
                    else {
                        if (p_148142_1_ < var4 + 215 - 18 || p_148142_1_ > var4 + 215) {
                            return;
                        }
                        var6 = this.func_148210_b(2);
                    }
                    var6 = ("" + I18n.format(var6, new Object[0])).trim();
                    if (var6.length() > 0) {
                        final int var7 = p_148142_1_ + 12;
                        final int var8 = p_148142_2_ - 12;
                        final int var9 = GuiStats.this.fontRendererObj.getStringWidth(var6);
                        GuiStats.this.drawGradientRect(var7 - 3, var8 - 3, var7 + var9 + 3, var8 + 8 + 3, -1073741824, -1073741824);
                        GuiStats.this.fontRendererObj.drawStringWithShadow(var6, var7, var8, -1);
                    }
                }
            }
        }
        
        protected void func_148213_a(final StatCrafting p_148213_1_, final int p_148213_2_, final int p_148213_3_) {
            if (p_148213_1_ != null) {
                final Item var4 = p_148213_1_.func_150959_a();
                final ItemStack var5 = new ItemStack(var4);
                final String var6 = var5.getUnlocalizedName();
                final String var7 = ("" + I18n.format(var6 + ".name", new Object[0])).trim();
                if (var7.length() > 0) {
                    final int var8 = p_148213_2_ + 12;
                    final int var9 = p_148213_3_ - 12;
                    final int var10 = GuiStats.this.fontRendererObj.getStringWidth(var7);
                    GuiStats.this.drawGradientRect(var8 - 3, var9 - 3, var8 + var10 + 3, var9 + 8 + 3, -1073741824, -1073741824);
                    GuiStats.this.fontRendererObj.drawStringWithShadow(var7, var8, var9, -1);
                }
            }
        }
        
        protected void func_148212_h(final int p_148212_1_) {
            if (p_148212_1_ != this.field_148217_o) {
                this.field_148217_o = p_148212_1_;
                this.field_148215_p = -1;
            }
            else if (this.field_148215_p == -1) {
                this.field_148215_p = 1;
            }
            else {
                this.field_148217_o = -1;
                this.field_148215_p = 0;
            }
            Collections.sort((List<Object>)this.statsHolder, this.statSorter);
        }
    }
    
    class StatsBlock extends Stats
    {
        private static final String __OBFID = "CL_00000724";
        
        public StatsBlock(final Minecraft mcIn) {
            super(mcIn);
            this.statsHolder = Lists.newArrayList();
            for (final StatCrafting var4 : StatList.objectMineStats) {
                boolean var5 = false;
                final int var6 = Item.getIdFromItem(var4.func_150959_a());
                if (GuiStats.this.field_146546_t.writeStat(var4) > 0) {
                    var5 = true;
                }
                else if (StatList.objectUseStats[var6] != null && GuiStats.this.field_146546_t.writeStat(StatList.objectUseStats[var6]) > 0) {
                    var5 = true;
                }
                else if (StatList.objectCraftStats[var6] != null && GuiStats.this.field_146546_t.writeStat(StatList.objectCraftStats[var6]) > 0) {
                    var5 = true;
                }
                if (var5) {
                    this.statsHolder.add(var4);
                }
            }
            this.statSorter = new Comparator() {
                private static final String __OBFID = "CL_00000725";
                
                public int compare(final StatCrafting p_compare_1_, final StatCrafting p_compare_2_) {
                    final int var3 = Item.getIdFromItem(p_compare_1_.func_150959_a());
                    final int var4 = Item.getIdFromItem(p_compare_2_.func_150959_a());
                    StatBase var5 = null;
                    StatBase var6 = null;
                    if (StatsBlock.this.field_148217_o == 2) {
                        var5 = StatList.mineBlockStatArray[var3];
                        var6 = StatList.mineBlockStatArray[var4];
                    }
                    else if (StatsBlock.this.field_148217_o == 0) {
                        var5 = StatList.objectCraftStats[var3];
                        var6 = StatList.objectCraftStats[var4];
                    }
                    else if (StatsBlock.this.field_148217_o == 1) {
                        var5 = StatList.objectUseStats[var3];
                        var6 = StatList.objectUseStats[var4];
                    }
                    if (var5 != null || var6 != null) {
                        if (var5 == null) {
                            return 1;
                        }
                        if (var6 == null) {
                            return -1;
                        }
                        final int var7 = GuiStats.this.field_146546_t.writeStat(var5);
                        final int var8 = GuiStats.this.field_146546_t.writeStat(var6);
                        if (var7 != var8) {
                            return (var7 - var8) * StatsBlock.this.field_148215_p;
                        }
                    }
                    return var3 - var4;
                }
                
                @Override
                public int compare(final Object p_compare_1_, final Object p_compare_2_) {
                    return this.compare((StatCrafting)p_compare_1_, (StatCrafting)p_compare_2_);
                }
            };
        }
        
        @Override
        protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
            super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1, 18, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 18, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1, 36, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 36, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1, 54, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 54, 18);
            }
        }
        
        @Override
        protected void drawSlot(final int p_180791_1_, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int p_180791_5_, final int p_180791_6_) {
            final StatCrafting var7 = this.func_148211_c(p_180791_1_);
            final Item var8 = var7.func_150959_a();
            GuiStats.this.drawStatsScreen(p_180791_2_ + 40, p_180791_3_, var8);
            final int var9 = Item.getIdFromItem(var8);
            this.func_148209_a(StatList.objectCraftStats[var9], p_180791_2_ + 115, p_180791_3_, p_180791_1_ % 2 == 0);
            this.func_148209_a(StatList.objectUseStats[var9], p_180791_2_ + 165, p_180791_3_, p_180791_1_ % 2 == 0);
            this.func_148209_a(var7, p_180791_2_ + 215, p_180791_3_, p_180791_1_ % 2 == 0);
        }
        
        @Override
        protected String func_148210_b(final int p_148210_1_) {
            return (p_148210_1_ == 0) ? "stat.crafted" : ((p_148210_1_ == 1) ? "stat.used" : "stat.mined");
        }
    }
    
    class StatsGeneral extends GuiSlot
    {
        private static final String __OBFID = "CL_00000726";
        
        public StatsGeneral(final Minecraft mcIn) {
            super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 10);
            this.setShowSelectionBox(false);
        }
        
        @Override
        protected int getSize() {
            return StatList.generalStats.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected int getContentHeight() {
            return this.getSize() * 10;
        }
        
        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawSlot(final int p_180791_1_, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int p_180791_5_, final int p_180791_6_) {
            final StatBase var7 = StatList.generalStats.get(p_180791_1_);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, var7.getStatName().getUnformattedText(), p_180791_2_ + 2, p_180791_3_ + 1, (p_180791_1_ % 2 == 0) ? 16777215 : 9474192);
            final String var8 = var7.func_75968_a(GuiStats.this.field_146546_t.writeStat(var7));
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, var8, p_180791_2_ + 2 + 213 - GuiStats.this.fontRendererObj.getStringWidth(var8), p_180791_3_ + 1, (p_180791_1_ % 2 == 0) ? 16777215 : 9474192);
        }
    }
    
    class StatsItem extends Stats
    {
        private static final String __OBFID = "CL_00000727";
        
        public StatsItem(final Minecraft mcIn) {
            super(mcIn);
            this.statsHolder = Lists.newArrayList();
            for (final StatCrafting var4 : StatList.itemStats) {
                boolean var5 = false;
                final int var6 = Item.getIdFromItem(var4.func_150959_a());
                if (GuiStats.this.field_146546_t.writeStat(var4) > 0) {
                    var5 = true;
                }
                else if (StatList.objectBreakStats[var6] != null && GuiStats.this.field_146546_t.writeStat(StatList.objectBreakStats[var6]) > 0) {
                    var5 = true;
                }
                else if (StatList.objectCraftStats[var6] != null && GuiStats.this.field_146546_t.writeStat(StatList.objectCraftStats[var6]) > 0) {
                    var5 = true;
                }
                if (var5) {
                    this.statsHolder.add(var4);
                }
            }
            this.statSorter = new Comparator() {
                private static final String __OBFID = "CL_00000728";
                
                public int compare(final StatCrafting p_compare_1_, final StatCrafting p_compare_2_) {
                    final int var3 = Item.getIdFromItem(p_compare_1_.func_150959_a());
                    final int var4 = Item.getIdFromItem(p_compare_2_.func_150959_a());
                    StatBase var5 = null;
                    StatBase var6 = null;
                    if (StatsItem.this.field_148217_o == 0) {
                        var5 = StatList.objectBreakStats[var3];
                        var6 = StatList.objectBreakStats[var4];
                    }
                    else if (StatsItem.this.field_148217_o == 1) {
                        var5 = StatList.objectCraftStats[var3];
                        var6 = StatList.objectCraftStats[var4];
                    }
                    else if (StatsItem.this.field_148217_o == 2) {
                        var5 = StatList.objectUseStats[var3];
                        var6 = StatList.objectUseStats[var4];
                    }
                    if (var5 != null || var6 != null) {
                        if (var5 == null) {
                            return 1;
                        }
                        if (var6 == null) {
                            return -1;
                        }
                        final int var7 = GuiStats.this.field_146546_t.writeStat(var5);
                        final int var8 = GuiStats.this.field_146546_t.writeStat(var6);
                        if (var7 != var8) {
                            return (var7 - var8) * StatsItem.this.field_148215_p;
                        }
                    }
                    return var3 - var4;
                }
                
                @Override
                public int compare(final Object p_compare_1_, final Object p_compare_2_) {
                    return this.compare((StatCrafting)p_compare_1_, (StatCrafting)p_compare_2_);
                }
            };
        }
        
        @Override
        protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
            super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1, 72, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 72, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1, 18, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 18, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1, 36, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 36, 18);
            }
        }
        
        @Override
        protected void drawSlot(final int p_180791_1_, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int p_180791_5_, final int p_180791_6_) {
            final StatCrafting var7 = this.func_148211_c(p_180791_1_);
            final Item var8 = var7.func_150959_a();
            GuiStats.this.drawStatsScreen(p_180791_2_ + 40, p_180791_3_, var8);
            final int var9 = Item.getIdFromItem(var8);
            this.func_148209_a(StatList.objectBreakStats[var9], p_180791_2_ + 115, p_180791_3_, p_180791_1_ % 2 == 0);
            this.func_148209_a(StatList.objectCraftStats[var9], p_180791_2_ + 165, p_180791_3_, p_180791_1_ % 2 == 0);
            this.func_148209_a(var7, p_180791_2_ + 215, p_180791_3_, p_180791_1_ % 2 == 0);
        }
        
        @Override
        protected String func_148210_b(final int p_148210_1_) {
            return (p_148210_1_ == 1) ? "stat.crafted" : ((p_148210_1_ == 2) ? "stat.used" : "stat.depleted");
        }
    }
    
    class StatsMobsList extends GuiSlot
    {
        private final List field_148222_l;
        private static final String __OBFID = "CL_00000729";
        
        public StatsMobsList(final Minecraft mcIn) {
            super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, GuiStats.this.fontRendererObj.FONT_HEIGHT * 4);
            this.field_148222_l = Lists.newArrayList();
            this.setShowSelectionBox(false);
            for (final EntityList.EntityEggInfo var4 : EntityList.entityEggs.values()) {
                if (GuiStats.this.field_146546_t.writeStat(var4.field_151512_d) > 0 || GuiStats.this.field_146546_t.writeStat(var4.field_151513_e) > 0) {
                    this.field_148222_l.add(var4);
                }
            }
        }
        
        @Override
        protected int getSize() {
            return this.field_148222_l.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected int getContentHeight() {
            return this.getSize() * GuiStats.this.fontRendererObj.FONT_HEIGHT * 4;
        }
        
        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawSlot(final int p_180791_1_, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int p_180791_5_, final int p_180791_6_) {
            final EntityList.EntityEggInfo var7 = this.field_148222_l.get(p_180791_1_);
            final String var8 = I18n.format("entity." + EntityList.getStringFromID(var7.spawnedID) + ".name", new Object[0]);
            final int var9 = GuiStats.this.field_146546_t.writeStat(var7.field_151512_d);
            final int var10 = GuiStats.this.field_146546_t.writeStat(var7.field_151513_e);
            String var11 = I18n.format("stat.entityKills", var9, var8);
            String var12 = I18n.format("stat.entityKilledBy", var8, var10);
            if (var9 == 0) {
                var11 = I18n.format("stat.entityKills.none", var8);
            }
            if (var10 == 0) {
                var12 = I18n.format("stat.entityKilledBy.none", var8);
            }
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, var8, p_180791_2_ + 2 - 10, p_180791_3_ + 1, 16777215);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, var11, p_180791_2_ + 2, p_180791_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT, (var9 == 0) ? 6316128 : 9474192);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, var12, p_180791_2_ + 2, p_180791_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT * 2, (var10 == 0) ? 6316128 : 9474192);
        }
    }
}
