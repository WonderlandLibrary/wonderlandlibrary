package net.minecraft.client.gui.achievement;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.StatFileWriter;

public class GuiStats
extends GuiScreen
implements IProgressMeter {
    protected GuiScreen parentScreen;
    protected String screenTitle = "Select world";
    private StatsGeneral generalStats;
    private StatsItem itemStats;
    private StatsBlock blockStats;
    private StatsMobsList mobStats;
    private StatFileWriter field_146546_t;
    private GuiSlot displaySlot;
    private boolean doesGuiPauseGame = true;

    public GuiStats(GuiScreen p_i1071_1_, StatFileWriter p_i1071_2_) {
        this.parentScreen = p_i1071_1_;
        this.field_146546_t = p_i1071_2_;
    }

    public void initGui() {
        this.screenTitle = I18n.format((String)"gui.stats", (Object[])new Object[0]);
        this.doesGuiPauseGame = true;
        this.mc.getNetHandler().addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.displaySlot != null) {
            this.displaySlot.handleMouseInput();
        }
    }

    public void func_175366_f() {
        this.generalStats = new StatsGeneral(this, this.mc);
        this.generalStats.registerScrollButtons(1, 1);
        this.itemStats = new StatsItem(this, this.mc);
        this.itemStats.registerScrollButtons(1, 1);
        this.blockStats = new StatsBlock(this, this.mc);
        this.blockStats.registerScrollButtons(1, 1);
        this.mobStats = new StatsMobsList(this, this.mc);
        this.mobStats.registerScrollButtons(1, 1);
    }

    public void createButtons() {
        this.buttonList.add((Object)new GuiButton(0, width / 2 + 4, height - 28, 150, 20, I18n.format((String)"gui.done", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(1, width / 2 - 160, height - 52, 80, 20, I18n.format((String)"stat.generalButton", (Object[])new Object[0])));
        GuiButton guibutton = new GuiButton(2, width / 2 - 80, height - 52, 80, 20, I18n.format((String)"stat.blocksButton", (Object[])new Object[0]));
        this.buttonList.add((Object)guibutton);
        GuiButton guibutton1 = new GuiButton(3, width / 2, height - 52, 80, 20, I18n.format((String)"stat.itemsButton", (Object[])new Object[0]));
        this.buttonList.add((Object)guibutton1);
        GuiButton guibutton2 = new GuiButton(4, width / 2 + 80, height - 52, 80, 20, I18n.format((String)"stat.mobsButton", (Object[])new Object[0]));
        this.buttonList.add((Object)guibutton2);
        if (this.blockStats.getSize() == 0) {
            guibutton.enabled = false;
        }
        if (this.itemStats.getSize() == 0) {
            guibutton1.enabled = false;
        }
        if (this.mobStats.getSize() == 0) {
            guibutton2.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button.id == 1) {
                this.displaySlot = this.generalStats;
            } else if (button.id == 3) {
                this.displaySlot = this.itemStats;
            } else if (button.id == 2) {
                this.displaySlot = this.blockStats;
            } else if (button.id == 4) {
                this.displaySlot = this.mobStats;
            } else {
                this.displaySlot.actionPerformed(button);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.doesGuiPauseGame) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format((String)"multiplayer.downloadingStats", (Object[])new Object[0]), width / 2, height / 2, 0xFFFFFF);
            this.drawCenteredString(this.fontRendererObj, lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % (long)lanSearchStates.length)], width / 2, height / 2 + FontRenderer.FONT_HEIGHT * 2, 0xFFFFFF);
        } else {
            this.displaySlot.drawScreen(mouseX, mouseY, partialTicks);
            this.drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, 20, 0xFFFFFF);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    public void doneLoading() {
        if (this.doesGuiPauseGame) {
            this.func_175366_f();
            this.createButtons();
            this.displaySlot = this.generalStats;
            this.doesGuiPauseGame = false;
        }
    }

    public boolean doesGuiPauseGame() {
        return !this.doesGuiPauseGame;
    }

    private void drawStatsScreen(int p_146521_1_, int p_146521_2_, Item p_146521_3_) {
        this.drawButtonBackground(p_146521_1_ + 1, p_146521_2_ + 1);
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.renderItemIntoGUI(new ItemStack(p_146521_3_, 1, 0), p_146521_1_ + 2, p_146521_2_ + 2);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }

    private void drawButtonBackground(int p_146531_1_, int p_146531_2_) {
        this.drawSprite(p_146531_1_, p_146531_2_, 0, 0);
    }

    private void drawSprite(int p_146527_1_, int p_146527_2_, int p_146527_3_, int p_146527_4_) {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(statIcons);
        float f = 0.0078125f;
        float f1 = 0.0078125f;
        int i = 18;
        int j = 18;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(p_146527_1_ + 0), (double)(p_146527_2_ + 18), (double)zLevel).tex((double)((float)(p_146527_3_ + 0) * 0.0078125f), (double)((float)(p_146527_4_ + 18) * 0.0078125f)).endVertex();
        worldrenderer.pos((double)(p_146527_1_ + 18), (double)(p_146527_2_ + 18), (double)zLevel).tex((double)((float)(p_146527_3_ + 18) * 0.0078125f), (double)((float)(p_146527_4_ + 18) * 0.0078125f)).endVertex();
        worldrenderer.pos((double)(p_146527_1_ + 18), (double)(p_146527_2_ + 0), (double)zLevel).tex((double)((float)(p_146527_3_ + 18) * 0.0078125f), (double)((float)(p_146527_4_ + 0) * 0.0078125f)).endVertex();
        worldrenderer.pos((double)(p_146527_1_ + 0), (double)(p_146527_2_ + 0), (double)zLevel).tex((double)((float)(p_146527_3_ + 0) * 0.0078125f), (double)((float)(p_146527_4_ + 0) * 0.0078125f)).endVertex();
        tessellator.draw();
    }

    static /* synthetic */ void access$000(GuiStats x0, int x1, int x2, int x3, int x4) {
        x0.drawSprite(x1, x2, x3, x4);
    }

    static /* synthetic */ StatFileWriter access$100(GuiStats x0) {
        return x0.field_146546_t;
    }

    static /* synthetic */ FontRenderer access$200(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$300(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$400(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$500(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$600(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ void access$700(GuiStats x0, int x1, int x2, int x3, int x4, int x5, int x6) {
        x0.drawGradientRect(x1, x2, x3, x4, x5, x6);
    }

    static /* synthetic */ FontRenderer access$800(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$900(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ void access$1000(GuiStats x0, int x1, int x2, int x3, int x4, int x5, int x6) {
        x0.drawGradientRect(x1, x2, x3, x4, x5, x6);
    }

    static /* synthetic */ FontRenderer access$1100(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ void access$1200(GuiStats x0, int x1, int x2, Item x3) {
        x0.drawStatsScreen(x1, x2, x3);
    }

    static /* synthetic */ FontRenderer access$1300(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$1400(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$1500(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$1600(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$1700(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$1800(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$1900(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$2000(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$2100(GuiStats x0) {
        return x0.fontRendererObj;
    }

    static /* synthetic */ FontRenderer access$2200(GuiStats x0) {
        return x0.fontRendererObj;
    }
}
