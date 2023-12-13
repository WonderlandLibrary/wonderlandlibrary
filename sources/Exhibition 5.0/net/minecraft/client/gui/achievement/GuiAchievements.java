// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.achievement;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.stats.Achievement;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.GuiScreen;

public class GuiAchievements extends GuiScreen implements IProgressMeter
{
    private static final int field_146572_y;
    private static final int field_146571_z;
    private static final int field_146559_A;
    private static final int field_146560_B;
    private static final ResourceLocation field_146561_C;
    protected GuiScreen parentScreen;
    protected int field_146555_f;
    protected int field_146557_g;
    protected int field_146563_h;
    protected int field_146564_i;
    protected float field_146570_r;
    protected double field_146569_s;
    protected double field_146568_t;
    protected double field_146567_u;
    protected double field_146566_v;
    protected double field_146565_w;
    protected double field_146573_x;
    private int field_146554_D;
    private StatFileWriter statFileWriter;
    private boolean loadingAchievements;
    private static final String __OBFID = "CL_00000722";
    
    public GuiAchievements(final GuiScreen p_i45026_1_, final StatFileWriter p_i45026_2_) {
        this.field_146555_f = 256;
        this.field_146557_g = 202;
        this.field_146570_r = 1.0f;
        this.loadingAchievements = true;
        this.parentScreen = p_i45026_1_;
        this.statFileWriter = p_i45026_2_;
        final short var3 = 141;
        final short var4 = 141;
        final double field_146569_s = AchievementList.openInventory.displayColumn * 24 - var3 / 2 - 12;
        this.field_146565_w = field_146569_s;
        this.field_146567_u = field_146569_s;
        this.field_146569_s = field_146569_s;
        final double field_146568_t = AchievementList.openInventory.displayRow * 24 - var4 / 2;
        this.field_146573_x = field_146568_t;
        this.field_146566_v = field_146568_t;
        this.field_146568_t = field_146568_t;
    }
    
    @Override
    public void initGui() {
        this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
        this.buttonList.clear();
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (!this.loadingAchievements && button.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.loadingAchievements) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), this.width / 2, this.height / 2, 16777215);
            this.drawCenteredString(this.fontRendererObj, GuiAchievements.lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % GuiAchievements.lanSearchStates.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
        }
        else {
            if (Mouse.isButtonDown(0)) {
                final int var4 = (this.width - this.field_146555_f) / 2;
                final int var5 = (this.height - this.field_146557_g) / 2;
                final int var6 = var4 + 8;
                final int var7 = var5 + 17;
                if ((this.field_146554_D == 0 || this.field_146554_D == 1) && mouseX >= var6 && mouseX < var6 + 224 && mouseY >= var7 && mouseY < var7 + 155) {
                    if (this.field_146554_D == 0) {
                        this.field_146554_D = 1;
                    }
                    else {
                        this.field_146567_u -= (mouseX - this.field_146563_h) * this.field_146570_r;
                        this.field_146566_v -= (mouseY - this.field_146564_i) * this.field_146570_r;
                        final double field_146567_u = this.field_146567_u;
                        this.field_146569_s = field_146567_u;
                        this.field_146565_w = field_146567_u;
                        final double field_146566_v = this.field_146566_v;
                        this.field_146568_t = field_146566_v;
                        this.field_146573_x = field_146566_v;
                    }
                    this.field_146563_h = mouseX;
                    this.field_146564_i = mouseY;
                }
            }
            else {
                this.field_146554_D = 0;
            }
            final int var4 = Mouse.getDWheel();
            final float var8 = this.field_146570_r;
            if (var4 < 0) {
                this.field_146570_r += 0.25f;
            }
            else if (var4 > 0) {
                this.field_146570_r -= 0.25f;
            }
            this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0f, 2.0f);
            if (this.field_146570_r != var8) {
                final float var9 = var8 - this.field_146570_r;
                final float var10 = var8 * this.field_146555_f;
                final float var11 = var8 * this.field_146557_g;
                final float var12 = this.field_146570_r * this.field_146555_f;
                final float var13 = this.field_146570_r * this.field_146557_g;
                this.field_146567_u -= (var12 - var10) * 0.5f;
                this.field_146566_v -= (var13 - var11) * 0.5f;
                final double field_146567_u2 = this.field_146567_u;
                this.field_146569_s = field_146567_u2;
                this.field_146565_w = field_146567_u2;
                final double field_146566_v2 = this.field_146566_v;
                this.field_146568_t = field_146566_v2;
                this.field_146573_x = field_146566_v2;
            }
            if (this.field_146565_w < GuiAchievements.field_146572_y) {
                this.field_146565_w = GuiAchievements.field_146572_y;
            }
            if (this.field_146573_x < GuiAchievements.field_146571_z) {
                this.field_146573_x = GuiAchievements.field_146571_z;
            }
            if (this.field_146565_w >= GuiAchievements.field_146559_A) {
                this.field_146565_w = GuiAchievements.field_146559_A - 1;
            }
            if (this.field_146573_x >= GuiAchievements.field_146560_B) {
                this.field_146573_x = GuiAchievements.field_146560_B - 1;
            }
            this.drawDefaultBackground();
            this.drawAchievementScreen(mouseX, mouseY, partialTicks);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.drawTitle();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }
    
    @Override
    public void doneLoading() {
        if (this.loadingAchievements) {
            this.loadingAchievements = false;
        }
    }
    
    @Override
    public void updateScreen() {
        if (!this.loadingAchievements) {
            this.field_146569_s = this.field_146567_u;
            this.field_146568_t = this.field_146566_v;
            final double var1 = this.field_146565_w - this.field_146567_u;
            final double var2 = this.field_146573_x - this.field_146566_v;
            if (var1 * var1 + var2 * var2 < 4.0) {
                this.field_146567_u += var1;
                this.field_146566_v += var2;
            }
            else {
                this.field_146567_u += var1 * 0.85;
                this.field_146566_v += var2 * 0.85;
            }
        }
    }
    
    protected void drawTitle() {
        final int var1 = (this.width - this.field_146555_f) / 2;
        final int var2 = (this.height - this.field_146557_g) / 2;
        this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), var1 + 15, var2 + 5, 4210752);
    }
    
    protected void drawAchievementScreen(final int p_146552_1_, final int p_146552_2_, final float p_146552_3_) {
        int var4 = MathHelper.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * p_146552_3_);
        int var5 = MathHelper.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * p_146552_3_);
        if (var4 < GuiAchievements.field_146572_y) {
            var4 = GuiAchievements.field_146572_y;
        }
        if (var5 < GuiAchievements.field_146571_z) {
            var5 = GuiAchievements.field_146571_z;
        }
        if (var4 >= GuiAchievements.field_146559_A) {
            var4 = GuiAchievements.field_146559_A - 1;
        }
        if (var5 >= GuiAchievements.field_146560_B) {
            var5 = GuiAchievements.field_146560_B - 1;
        }
        final int var6 = (this.width - this.field_146555_f) / 2;
        final int var7 = (this.height - this.field_146557_g) / 2;
        final int var8 = var6 + 16;
        final int var9 = var7 + 17;
        this.zLevel = 0.0f;
        GlStateManager.depthFunc(518);
        GlStateManager.pushMatrix();
        GlStateManager.translate(var8, var9, -200.0f);
        GlStateManager.scale(1.0f / this.field_146570_r, 1.0f / this.field_146570_r, 0.0f);
        GlStateManager.enableTextures();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        final int var10 = var4 + 288 >> 4;
        final int var11 = var5 + 288 >> 4;
        final int var12 = (var4 + 288) % 16;
        final int var13 = (var5 + 288) % 16;
        final boolean var14 = true;
        final boolean var15 = true;
        final boolean var16 = true;
        final boolean var17 = true;
        final boolean var18 = true;
        final Random var19 = new Random();
        final float var20 = 16.0f / this.field_146570_r;
        final float var21 = 16.0f / this.field_146570_r;
        for (int var22 = 0; var22 * var20 - var13 < 155.0f; ++var22) {
            final float var23 = 0.6f - (var11 + var22) / 25.0f * 0.3f;
            GlStateManager.color(var23, var23, var23, 1.0f);
            for (int var24 = 0; var24 * var21 - var12 < 224.0f; ++var24) {
                var19.setSeed(this.mc.getSession().getPlayerID().hashCode() + var10 + var24 + (var11 + var22) * 16);
                final int var25 = var19.nextInt(1 + var11 + var22) + (var11 + var22) / 2;
                TextureAtlasSprite var26 = this.func_175371_a(Blocks.sand);
                if (var25 <= 37 && var11 + var22 != 35) {
                    if (var25 == 22) {
                        if (var19.nextInt(2) == 0) {
                            var26 = this.func_175371_a(Blocks.diamond_ore);
                        }
                        else {
                            var26 = this.func_175371_a(Blocks.redstone_ore);
                        }
                    }
                    else if (var25 == 10) {
                        var26 = this.func_175371_a(Blocks.iron_ore);
                    }
                    else if (var25 == 8) {
                        var26 = this.func_175371_a(Blocks.coal_ore);
                    }
                    else if (var25 > 4) {
                        var26 = this.func_175371_a(Blocks.stone);
                    }
                    else if (var25 > 0) {
                        var26 = this.func_175371_a(Blocks.dirt);
                    }
                }
                else {
                    final Block var27 = Blocks.bedrock;
                    var26 = this.func_175371_a(var27);
                }
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.func_175175_a(var24 * 16 - var12, var22 * 16 - var13, var26, 16, 16);
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        this.mc.getTextureManager().bindTexture(GuiAchievements.field_146561_C);
        for (int var22 = 0; var22 < AchievementList.achievementList.size(); ++var22) {
            final Achievement var28 = AchievementList.achievementList.get(var22);
            if (var28.parentAchievement != null) {
                final int var24 = var28.displayColumn * 24 - var4 + 11;
                final int var25 = var28.displayRow * 24 - var5 + 11;
                final int var29 = var28.parentAchievement.displayColumn * 24 - var4 + 11;
                final int var30 = var28.parentAchievement.displayRow * 24 - var5 + 11;
                final boolean var31 = this.statFileWriter.hasAchievementUnlocked(var28);
                final boolean var32 = this.statFileWriter.canUnlockAchievement(var28);
                final int var33 = this.statFileWriter.func_150874_c(var28);
                if (var33 <= 4) {
                    int var34 = -16777216;
                    if (var31) {
                        var34 = -6250336;
                    }
                    else if (var32) {
                        var34 = -16711936;
                    }
                    this.drawHorizontalLine(var24, var29, var25, var34);
                    Gui.drawVerticalLine(var29, var25, var30, var34);
                    if (var24 > var29) {
                        this.drawTexturedModalRect(var24 - 11 - 7, var25 - 5, 114, 234, 7, 11);
                    }
                    else if (var24 < var29) {
                        this.drawTexturedModalRect(var24 + 11, var25 - 5, 107, 234, 7, 11);
                    }
                    else if (var25 > var30) {
                        this.drawTexturedModalRect(var24 - 5, var25 - 11 - 7, 96, 234, 11, 7);
                    }
                    else if (var25 < var30) {
                        this.drawTexturedModalRect(var24 - 5, var25 + 11, 96, 241, 11, 7);
                    }
                }
            }
        }
        Achievement var35 = null;
        final float var23 = (p_146552_1_ - var8) * this.field_146570_r;
        final float var36 = (p_146552_2_ - var9) * this.field_146570_r;
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        for (int var25 = 0; var25 < AchievementList.achievementList.size(); ++var25) {
            final Achievement var37 = AchievementList.achievementList.get(var25);
            final int var30 = var37.displayColumn * 24 - var4;
            final int var38 = var37.displayRow * 24 - var5;
            if (var30 >= -24 && var38 >= -24 && var30 <= 224.0f * this.field_146570_r && var38 <= 155.0f * this.field_146570_r) {
                final int var39 = this.statFileWriter.func_150874_c(var37);
                if (this.statFileWriter.hasAchievementUnlocked(var37)) {
                    final float var40 = 0.75f;
                    GlStateManager.color(var40, var40, var40, 1.0f);
                }
                else if (this.statFileWriter.canUnlockAchievement(var37)) {
                    final float var40 = 1.0f;
                    GlStateManager.color(var40, var40, var40, 1.0f);
                }
                else if (var39 < 3) {
                    final float var40 = 0.3f;
                    GlStateManager.color(var40, var40, var40, 1.0f);
                }
                else if (var39 == 3) {
                    final float var40 = 0.2f;
                    GlStateManager.color(var40, var40, var40, 1.0f);
                }
                else {
                    if (var39 != 4) {
                        continue;
                    }
                    final float var40 = 0.1f;
                    GlStateManager.color(var40, var40, var40, 1.0f);
                }
                this.mc.getTextureManager().bindTexture(GuiAchievements.field_146561_C);
                if (var37.getSpecial()) {
                    this.drawTexturedModalRect(var30 - 2, var38 - 2, 26, 202, 26, 26);
                }
                else {
                    this.drawTexturedModalRect(var30 - 2, var38 - 2, 0, 202, 26, 26);
                }
                if (!this.statFileWriter.canUnlockAchievement(var37)) {
                    final float var40 = 0.1f;
                    GlStateManager.color(var40, var40, var40, 1.0f);
                    this.itemRender.func_175039_a(false);
                }
                GlStateManager.enableLighting();
                GlStateManager.enableCull();
                this.itemRender.func_180450_b(var37.theItemStack, var30 + 3, var38 + 3);
                GlStateManager.blendFunc(770, 771);
                GlStateManager.disableLighting();
                if (!this.statFileWriter.canUnlockAchievement(var37)) {
                    this.itemRender.func_175039_a(true);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                if (var23 >= var30 && var23 <= var30 + 22 && var36 >= var38 && var36 <= var38 + 22) {
                    var35 = var37;
                }
            }
        }
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiAchievements.field_146561_C);
        this.drawTexturedModalRect(var6, var7, 0, 0, this.field_146555_f, this.field_146557_g);
        this.zLevel = 0.0f;
        GlStateManager.depthFunc(515);
        GlStateManager.disableDepth();
        GlStateManager.enableTextures();
        super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);
        if (var35 != null) {
            String var41 = var35.getStatName().getUnformattedText();
            final String var42 = var35.getDescription();
            final int var30 = p_146552_1_ + 12;
            final int var38 = p_146552_2_ - 4;
            final int var39 = this.statFileWriter.func_150874_c(var35);
            if (this.statFileWriter.canUnlockAchievement(var35)) {
                final int var33 = Math.max(this.fontRendererObj.getStringWidth(var41), 120);
                int var34 = this.fontRendererObj.splitStringWidth(var42, var33);
                if (this.statFileWriter.hasAchievementUnlocked(var35)) {
                    var34 += 12;
                }
                this.drawGradientRect(var30 - 3, var38 - 3, var30 + var33 + 3, var38 + var34 + 3 + 12, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(var42, var30, var38 + 12, var33, -6250336);
                if (this.statFileWriter.hasAchievementUnlocked(var35)) {
                    this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken", new Object[0]), var30, var38 + var34 + 4, -7302913);
                }
            }
            else if (var39 == 3) {
                var41 = I18n.format("achievement.unknown", new Object[0]);
                final int var33 = Math.max(this.fontRendererObj.getStringWidth(var41), 120);
                final String var43 = new ChatComponentTranslation("achievement.requires", new Object[] { var35.parentAchievement.getStatName() }).getUnformattedText();
                final int var44 = this.fontRendererObj.splitStringWidth(var43, var33);
                this.drawGradientRect(var30 - 3, var38 - 3, var30 + var33 + 3, var38 + var44 + 12 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(var43, var30, var38 + 12, var33, -9416624);
            }
            else if (var39 < 3) {
                final int var33 = Math.max(this.fontRendererObj.getStringWidth(var41), 120);
                final String var43 = new ChatComponentTranslation("achievement.requires", new Object[] { var35.parentAchievement.getStatName() }).getUnformattedText();
                final int var44 = this.fontRendererObj.splitStringWidth(var43, var33);
                this.drawGradientRect(var30 - 3, var38 - 3, var30 + var33 + 3, var38 + var44 + 12 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(var43, var30, var38 + 12, var33, -9416624);
            }
            else {
                var41 = null;
            }
            if (var41 != null) {
                this.fontRendererObj.drawStringWithShadow(var41, var30, var38, this.statFileWriter.canUnlockAchievement(var35) ? (var35.getSpecial() ? -128 : -1) : (var35.getSpecial() ? -8355776 : -8355712));
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        RenderHelper.disableStandardItemLighting();
    }
    
    private TextureAtlasSprite func_175371_a(final Block p_175371_1_) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().func_175023_a().func_178122_a(p_175371_1_.getDefaultState());
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return !this.loadingAchievements;
    }
    
    static {
        field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
        field_146571_z = AchievementList.minDisplayRow * 24 - 112;
        field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
        field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
        field_146561_C = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    }
}
