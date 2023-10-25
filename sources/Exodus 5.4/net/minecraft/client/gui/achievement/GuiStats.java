/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 */
package net.minecraft.client.gui.achievement;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiStats
extends GuiScreen
implements IProgressMeter {
    protected String screenTitle = "Select world";
    private GuiSlot displaySlot;
    private StatFileWriter field_146546_t;
    private StatsItem itemStats;
    private StatsMobsList mobStats;
    protected GuiScreen parentScreen;
    private StatsGeneral generalStats;
    private boolean doesGuiPauseGame = true;
    private StatsBlock blockStats;

    private void drawStatsScreen(int n, int n2, Item item) {
        this.drawButtonBackground(n + 1, n2 + 1);
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.renderItemIntoGUI(new ItemStack(item, 1, 0), n + 2, n2 + 2);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }

    public void func_175366_f() {
        this.generalStats = new StatsGeneral(this.mc);
        this.generalStats.registerScrollButtons(1, 1);
        this.itemStats = new StatsItem(this.mc);
        this.itemStats.registerScrollButtons(1, 1);
        this.blockStats = new StatsBlock(this.mc);
        this.blockStats.registerScrollButtons(1, 1);
        this.mobStats = new StatsMobsList(this.mc);
        this.mobStats.registerScrollButtons(1, 1);
    }

    public void createButtons() {
        this.buttonList.add(new GuiButton(0, width / 2 + 4, height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(1, width / 2 - 160, height - 52, 80, 20, I18n.format("stat.generalButton", new Object[0])));
        GuiButton guiButton = new GuiButton(2, width / 2 - 80, height - 52, 80, 20, I18n.format("stat.blocksButton", new Object[0]));
        this.buttonList.add(guiButton);
        GuiButton guiButton2 = new GuiButton(3, width / 2, height - 52, 80, 20, I18n.format("stat.itemsButton", new Object[0]));
        this.buttonList.add(guiButton2);
        GuiButton guiButton3 = new GuiButton(4, width / 2 + 80, height - 52, 80, 20, I18n.format("stat.mobsButton", new Object[0]));
        this.buttonList.add(guiButton3);
        if (this.blockStats.getSize() == 0) {
            guiButton.enabled = false;
        }
        if (this.itemStats.getSize() == 0) {
            guiButton2.enabled = false;
        }
        if (this.mobStats.getSize() == 0) {
            guiButton3.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (guiButton.id == 1) {
                this.displaySlot = this.generalStats;
            } else if (guiButton.id == 3) {
                this.displaySlot = this.itemStats;
            } else if (guiButton.id == 2) {
                this.displaySlot = this.blockStats;
            } else if (guiButton.id == 4) {
                this.displaySlot = this.mobStats;
            } else {
                this.displaySlot.actionPerformed(guiButton);
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.displaySlot != null) {
            this.displaySlot.handleMouseInput();
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (this.doesGuiPauseGame) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), width / 2, height / 2, 0xFFFFFF);
            this.drawCenteredString(this.fontRendererObj, lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % (long)lanSearchStates.length)], width / 2, height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 0xFFFFFF);
        } else {
            this.displaySlot.drawScreen(n, n2, f);
            this.drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, 20, 0xFFFFFF);
            super.drawScreen(n, n2, f);
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

    private void drawSprite(int n, int n2, int n3, int n4) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(statIcons);
        float f = 0.0078125f;
        float f2 = 0.0078125f;
        int n5 = 18;
        int n6 = 18;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(n + 0, n2 + 18, zLevel).tex((float)(n3 + 0) * 0.0078125f, (float)(n4 + 18) * 0.0078125f).endVertex();
        worldRenderer.pos(n + 18, n2 + 18, zLevel).tex((float)(n3 + 18) * 0.0078125f, (float)(n4 + 18) * 0.0078125f).endVertex();
        worldRenderer.pos(n + 18, n2 + 0, zLevel).tex((float)(n3 + 18) * 0.0078125f, (float)(n4 + 0) * 0.0078125f).endVertex();
        worldRenderer.pos(n + 0, n2 + 0, zLevel).tex((float)(n3 + 0) * 0.0078125f, (float)(n4 + 0) * 0.0078125f).endVertex();
        tessellator.draw();
    }

    public GuiStats(GuiScreen guiScreen, StatFileWriter statFileWriter) {
        this.parentScreen = guiScreen;
        this.field_146546_t = statFileWriter;
    }

    private void drawButtonBackground(int n, int n2) {
        this.drawSprite(n, n2, 0, 0);
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.format("gui.stats", new Object[0]);
        this.doesGuiPauseGame = true;
        this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
    }

    class StatsMobsList
    extends GuiSlot {
        private final List<EntityList.EntityEggInfo> field_148222_l;

        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }

        @Override
        protected boolean isSelected(int n) {
            return false;
        }

        @Override
        protected int getSize() {
            return this.field_148222_l.size();
        }

        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            EntityList.EntityEggInfo entityEggInfo = this.field_148222_l.get(n);
            String string = I18n.format("entity." + EntityList.getStringFromID(entityEggInfo.spawnedID) + ".name", new Object[0]);
            int n7 = GuiStats.this.field_146546_t.readStat(entityEggInfo.field_151512_d);
            int n8 = GuiStats.this.field_146546_t.readStat(entityEggInfo.field_151513_e);
            String string2 = I18n.format("stat.entityKills", n7, string);
            String string3 = I18n.format("stat.entityKilledBy", string, n8);
            if (n7 == 0) {
                string2 = I18n.format("stat.entityKills.none", string);
            }
            if (n8 == 0) {
                string3 = I18n.format("stat.entityKilledBy.none", string);
            }
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, string, n2 + 2 - 10, n3 + 1, 0xFFFFFF);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, string2, n2 + 2, n3 + 1 + ((GuiStats)GuiStats.this).fontRendererObj.FONT_HEIGHT, n7 == 0 ? 0x606060 : 0x909090);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, string3, n2 + 2, n3 + 1 + ((GuiStats)GuiStats.this).fontRendererObj.FONT_HEIGHT * 2, n8 == 0 ? 0x606060 : 0x909090);
        }

        @Override
        protected int getContentHeight() {
            return this.getSize() * ((GuiStats)GuiStats.this).fontRendererObj.FONT_HEIGHT * 4;
        }

        public StatsMobsList(Minecraft minecraft) {
            super(minecraft, width, height, 32, height - 64, ((GuiStats)GuiStats.this).fontRendererObj.FONT_HEIGHT * 4);
            this.field_148222_l = Lists.newArrayList();
            this.setShowSelectionBox(false);
            for (EntityList.EntityEggInfo entityEggInfo : EntityList.entityEggs.values()) {
                if (GuiStats.this.field_146546_t.readStat(entityEggInfo.field_151512_d) <= 0 && GuiStats.this.field_146546_t.readStat(entityEggInfo.field_151513_e) <= 0) continue;
                this.field_148222_l.add(entityEggInfo);
            }
        }

        @Override
        protected void elementClicked(int n, boolean bl, int n2, int n3) {
        }
    }

    class StatsItem
    extends Stats {
        @Override
        protected void drawListHeader(int n, int n2, Tessellator tessellator) {
            super.drawListHeader(n, n2, tessellator);
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(n + 115 - 18 + 1, n2 + 1 + 1, 72, 18);
            } else {
                GuiStats.this.drawSprite(n + 115 - 18, n2 + 1, 72, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(n + 165 - 18 + 1, n2 + 1 + 1, 18, 18);
            } else {
                GuiStats.this.drawSprite(n + 165 - 18, n2 + 1, 18, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(n + 215 - 18 + 1, n2 + 1 + 1, 36, 18);
            } else {
                GuiStats.this.drawSprite(n + 215 - 18, n2 + 1, 36, 18);
            }
        }

        @Override
        protected String func_148210_b(int n) {
            return n == 1 ? "stat.crafted" : (n == 2 ? "stat.used" : "stat.depleted");
        }

        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            StatCrafting statCrafting = this.func_148211_c(n);
            Item item = statCrafting.func_150959_a();
            GuiStats.this.drawStatsScreen(n2 + 40, n3, item);
            int n7 = Item.getIdFromItem(item);
            this.func_148209_a(StatList.objectBreakStats[n7], n2 + 115, n3, n % 2 == 0);
            this.func_148209_a(StatList.objectCraftStats[n7], n2 + 165, n3, n % 2 == 0);
            this.func_148209_a(statCrafting, n2 + 215, n3, n % 2 == 0);
        }

        public StatsItem(Minecraft minecraft) {
            super(minecraft);
            this.statsHolder = Lists.newArrayList();
            for (StatCrafting statCrafting : StatList.itemStats) {
                boolean bl = false;
                int n = Item.getIdFromItem(statCrafting.func_150959_a());
                if (GuiStats.this.field_146546_t.readStat(statCrafting) > 0) {
                    bl = true;
                } else if (StatList.objectBreakStats[n] != null && GuiStats.this.field_146546_t.readStat(StatList.objectBreakStats[n]) > 0) {
                    bl = true;
                } else if (StatList.objectCraftStats[n] != null && GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[n]) > 0) {
                    bl = true;
                }
                if (!bl) continue;
                this.statsHolder.add(statCrafting);
            }
            this.statSorter = new Comparator<StatCrafting>(){

                @Override
                public int compare(StatCrafting statCrafting, StatCrafting statCrafting2) {
                    int n = Item.getIdFromItem(statCrafting.func_150959_a());
                    int n2 = Item.getIdFromItem(statCrafting2.func_150959_a());
                    StatBase statBase = null;
                    StatBase statBase2 = null;
                    if (StatsItem.this.field_148217_o == 0) {
                        statBase = StatList.objectBreakStats[n];
                        statBase2 = StatList.objectBreakStats[n2];
                    } else if (StatsItem.this.field_148217_o == 1) {
                        statBase = StatList.objectCraftStats[n];
                        statBase2 = StatList.objectCraftStats[n2];
                    } else if (StatsItem.this.field_148217_o == 2) {
                        statBase = StatList.objectUseStats[n];
                        statBase2 = StatList.objectUseStats[n2];
                    }
                    if (statBase != null || statBase2 != null) {
                        int n3;
                        if (statBase == null) {
                            return 1;
                        }
                        if (statBase2 == null) {
                            return -1;
                        }
                        int n4 = GuiStats.this.field_146546_t.readStat(statBase);
                        if (n4 != (n3 = GuiStats.this.field_146546_t.readStat(statBase2))) {
                            return (n4 - n3) * StatsItem.this.field_148215_p;
                        }
                    }
                    return n - n2;
                }
            };
        }
    }

    class StatsBlock
    extends Stats {
        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            StatCrafting statCrafting = this.func_148211_c(n);
            Item item = statCrafting.func_150959_a();
            GuiStats.this.drawStatsScreen(n2 + 40, n3, item);
            int n7 = Item.getIdFromItem(item);
            this.func_148209_a(StatList.objectCraftStats[n7], n2 + 115, n3, n % 2 == 0);
            this.func_148209_a(StatList.objectUseStats[n7], n2 + 165, n3, n % 2 == 0);
            this.func_148209_a(statCrafting, n2 + 215, n3, n % 2 == 0);
        }

        @Override
        protected String func_148210_b(int n) {
            return n == 0 ? "stat.crafted" : (n == 1 ? "stat.used" : "stat.mined");
        }

        @Override
        protected void drawListHeader(int n, int n2, Tessellator tessellator) {
            super.drawListHeader(n, n2, tessellator);
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(n + 115 - 18 + 1, n2 + 1 + 1, 18, 18);
            } else {
                GuiStats.this.drawSprite(n + 115 - 18, n2 + 1, 18, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(n + 165 - 18 + 1, n2 + 1 + 1, 36, 18);
            } else {
                GuiStats.this.drawSprite(n + 165 - 18, n2 + 1, 36, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(n + 215 - 18 + 1, n2 + 1 + 1, 54, 18);
            } else {
                GuiStats.this.drawSprite(n + 215 - 18, n2 + 1, 54, 18);
            }
        }

        public StatsBlock(Minecraft minecraft) {
            super(minecraft);
            this.statsHolder = Lists.newArrayList();
            for (StatCrafting statCrafting : StatList.objectMineStats) {
                boolean bl = false;
                int n = Item.getIdFromItem(statCrafting.func_150959_a());
                if (GuiStats.this.field_146546_t.readStat(statCrafting) > 0) {
                    bl = true;
                } else if (StatList.objectUseStats[n] != null && GuiStats.this.field_146546_t.readStat(StatList.objectUseStats[n]) > 0) {
                    bl = true;
                } else if (StatList.objectCraftStats[n] != null && GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[n]) > 0) {
                    bl = true;
                }
                if (!bl) continue;
                this.statsHolder.add(statCrafting);
            }
            this.statSorter = new Comparator<StatCrafting>(){

                @Override
                public int compare(StatCrafting statCrafting, StatCrafting statCrafting2) {
                    int n = Item.getIdFromItem(statCrafting.func_150959_a());
                    int n2 = Item.getIdFromItem(statCrafting2.func_150959_a());
                    StatBase statBase = null;
                    StatBase statBase2 = null;
                    if (StatsBlock.this.field_148217_o == 2) {
                        statBase = StatList.mineBlockStatArray[n];
                        statBase2 = StatList.mineBlockStatArray[n2];
                    } else if (StatsBlock.this.field_148217_o == 0) {
                        statBase = StatList.objectCraftStats[n];
                        statBase2 = StatList.objectCraftStats[n2];
                    } else if (StatsBlock.this.field_148217_o == 1) {
                        statBase = StatList.objectUseStats[n];
                        statBase2 = StatList.objectUseStats[n2];
                    }
                    if (statBase != null || statBase2 != null) {
                        int n3;
                        if (statBase == null) {
                            return 1;
                        }
                        if (statBase2 == null) {
                            return -1;
                        }
                        int n4 = GuiStats.this.field_146546_t.readStat(statBase);
                        if (n4 != (n3 = GuiStats.this.field_146546_t.readStat(statBase2))) {
                            return (n4 - n3) * StatsBlock.this.field_148215_p;
                        }
                    }
                    return n - n2;
                }
            };
        }
    }

    abstract class Stats
    extends GuiSlot {
        protected List<StatCrafting> statsHolder;
        protected int field_148215_p;
        protected Comparator<StatCrafting> statSorter;
        protected int field_148218_l;
        protected int field_148217_o;

        protected final StatCrafting func_148211_c(int n) {
            return this.statsHolder.get(n);
        }

        protected Stats(Minecraft minecraft) {
            super(minecraft, width, height, 32, height - 64, 20);
            this.field_148218_l = -1;
            this.field_148217_o = -1;
            this.setShowSelectionBox(false);
            this.setHasListHeader(true, 20);
        }

        protected abstract String func_148210_b(int var1);

        @Override
        protected void drawListHeader(int n, int n2, Tessellator tessellator) {
            if (!Mouse.isButtonDown((int)0)) {
                this.field_148218_l = -1;
            }
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(n + 115 - 18, n2 + 1, 0, 0);
            } else {
                GuiStats.this.drawSprite(n + 115 - 18, n2 + 1, 0, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(n + 165 - 18, n2 + 1, 0, 0);
            } else {
                GuiStats.this.drawSprite(n + 165 - 18, n2 + 1, 0, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(n + 215 - 18, n2 + 1, 0, 0);
            } else {
                GuiStats.this.drawSprite(n + 215 - 18, n2 + 1, 0, 18);
            }
            if (this.field_148217_o != -1) {
                int n3 = 79;
                int n4 = 18;
                if (this.field_148217_o == 1) {
                    n3 = 129;
                } else if (this.field_148217_o == 2) {
                    n3 = 179;
                }
                if (this.field_148215_p == 1) {
                    n4 = 36;
                }
                GuiStats.this.drawSprite(n + n3, n2 + 1, n4, 0);
            }
        }

        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }

        protected void func_148213_a(StatCrafting statCrafting, int n, int n2) {
            Item item;
            ItemStack itemStack;
            String string;
            String string2;
            if (statCrafting != null && (string2 = I18n.format(String.valueOf(string = (itemStack = new ItemStack(item = statCrafting.func_150959_a())).getUnlocalizedName()) + ".name", new Object[0]).trim()).length() > 0) {
                int n3 = n + 12;
                int n4 = n2 - 12;
                int n5 = GuiStats.this.fontRendererObj.getStringWidth(string2);
                GuiStats.drawGradientRect(n3 - 3, n4 - 3, n3 + n5 + 3, n4 + 8 + 3, -1073741824, -1073741824);
                GuiStats.this.fontRendererObj.drawStringWithShadow(string2, n3, n4, -1);
            }
        }

        protected void func_148212_h(int n) {
            if (n != this.field_148217_o) {
                this.field_148217_o = n;
                this.field_148215_p = -1;
            } else if (this.field_148215_p == -1) {
                this.field_148215_p = 1;
            } else {
                this.field_148217_o = -1;
                this.field_148215_p = 0;
            }
            Collections.sort(this.statsHolder, this.statSorter);
        }

        @Override
        protected void func_148142_b(int n, int n2) {
            if (n2 >= this.top && n2 <= this.bottom) {
                int n3 = this.getSlotIndexFromScreenCoords(n, n2);
                int n4 = this.width / 2 - 92 - 16;
                if (n3 >= 0) {
                    if (n < n4 + 40 || n > n4 + 40 + 20) {
                        return;
                    }
                    StatCrafting statCrafting = this.func_148211_c(n3);
                    this.func_148213_a(statCrafting, n, n2);
                } else {
                    String string = "";
                    if (n >= n4 + 115 - 18 && n <= n4 + 115) {
                        string = this.func_148210_b(0);
                    } else if (n >= n4 + 165 - 18 && n <= n4 + 165) {
                        string = this.func_148210_b(1);
                    } else {
                        if (n < n4 + 215 - 18 || n > n4 + 215) {
                            return;
                        }
                        string = this.func_148210_b(2);
                    }
                    string = I18n.format(string, new Object[0]).trim();
                    if (string.length() > 0) {
                        int n5 = n + 12;
                        int n6 = n2 - 12;
                        int n7 = GuiStats.this.fontRendererObj.getStringWidth(string);
                        GuiStats.drawGradientRect(n5 - 3, n6 - 3, n5 + n7 + 3, n6 + 8 + 3, -1073741824, -1073741824);
                        GuiStats.this.fontRendererObj.drawStringWithShadow(string, n5, n6, -1);
                    }
                }
            }
        }

        @Override
        protected boolean isSelected(int n) {
            return false;
        }

        protected void func_148209_a(StatBase statBase, int n, int n2, boolean bl) {
            if (statBase != null) {
                String string = statBase.format(GuiStats.this.field_146546_t.readStat(statBase));
                GuiStats.this.drawString(GuiStats.this.fontRendererObj, string, n - GuiStats.this.fontRendererObj.getStringWidth(string), n2 + 5, bl ? 0xFFFFFF : 0x909090);
            } else {
                String string = "-";
                GuiStats.this.drawString(GuiStats.this.fontRendererObj, string, n - GuiStats.this.fontRendererObj.getStringWidth(string), n2 + 5, bl ? 0xFFFFFF : 0x909090);
            }
        }

        @Override
        protected void func_148132_a(int n, int n2) {
            this.field_148218_l = -1;
            if (n >= 79 && n < 115) {
                this.field_148218_l = 0;
            } else if (n >= 129 && n < 165) {
                this.field_148218_l = 1;
            } else if (n >= 179 && n < 215) {
                this.field_148218_l = 2;
            }
            if (this.field_148218_l >= 0) {
                this.func_148212_h(this.field_148218_l);
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
            }
        }

        @Override
        protected final int getSize() {
            return this.statsHolder.size();
        }

        @Override
        protected void elementClicked(int n, boolean bl, int n2, int n3) {
        }
    }

    class StatsGeneral
    extends GuiSlot {
        @Override
        protected int getSize() {
            return StatList.generalStats.size();
        }

        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            StatBase statBase = StatList.generalStats.get(n);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, statBase.getStatName().getUnformattedText(), n2 + 2, n3 + 1, n % 2 == 0 ? 0xFFFFFF : 0x909090);
            String string = statBase.format(GuiStats.this.field_146546_t.readStat(statBase));
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, string, n2 + 2 + 213 - GuiStats.this.fontRendererObj.getStringWidth(string), n3 + 1, n % 2 == 0 ? 0xFFFFFF : 0x909090);
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
        protected boolean isSelected(int n) {
            return false;
        }

        public StatsGeneral(Minecraft minecraft) {
            super(minecraft, width, height, 32, height - 64, 10);
            this.setShowSelectionBox(false);
        }

        @Override
        protected void elementClicked(int n, boolean bl, int n2, int n3) {
        }
    }
}

