/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package net.minecraft.client.gui.achievement;

import java.io.IOException;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiAchievements
extends GuiScreen
implements IProgressMeter {
    private static final int field_146571_z;
    protected int field_146555_f = 256;
    protected int field_146564_i;
    protected double field_146567_u;
    protected double field_146573_x;
    protected float field_146570_r = 1.0f;
    protected double field_146569_s;
    private int field_146554_D;
    private boolean loadingAchievements = true;
    protected double field_146568_t;
    private static final int field_146560_B;
    private static final int field_146572_y;
    protected GuiScreen parentScreen;
    protected double field_146566_v;
    protected double field_146565_w;
    private static final int field_146559_A;
    private StatFileWriter statFileWriter;
    protected int field_146563_h;
    private static final ResourceLocation ACHIEVEMENT_BACKGROUND;
    protected int field_146557_g = 202;

    @Override
    public void initGui() {
        this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
        this.buttonList.clear();
        this.buttonList.add(new GuiOptionButton(1, width / 2 + 24, height / 2 + 74, 80, 20, I18n.format("gui.done", new Object[0])));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return !this.loadingAchievements;
    }

    protected void drawAchievementScreen(int n, int n2, float f) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        float f2;
        int n9 = MathHelper.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * (double)f);
        int n10 = MathHelper.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * (double)f);
        if (n9 < field_146572_y) {
            n9 = field_146572_y;
        }
        if (n10 < field_146571_z) {
            n10 = field_146571_z;
        }
        if (n9 >= field_146559_A) {
            n9 = field_146559_A - 1;
        }
        if (n10 >= field_146560_B) {
            n10 = field_146560_B - 1;
        }
        int n11 = (width - this.field_146555_f) / 2;
        int n12 = (height - this.field_146557_g) / 2;
        int n13 = n11 + 16;
        int n14 = n12 + 17;
        zLevel = 0.0f;
        GlStateManager.depthFunc(518);
        GlStateManager.pushMatrix();
        GlStateManager.translate(n13, n14, -200.0f);
        GlStateManager.scale(1.0f / this.field_146570_r, 1.0f / this.field_146570_r, 0.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        int n15 = n9 + 288 >> 4;
        int n16 = n10 + 288 >> 4;
        int n17 = (n9 + 288) % 16;
        int n18 = (n10 + 288) % 16;
        int n19 = 4;
        int n20 = 8;
        int n21 = 10;
        int n22 = 22;
        int n23 = 37;
        Random random = new Random();
        float f3 = 16.0f / this.field_146570_r;
        float f4 = 16.0f / this.field_146570_r;
        int n24 = 0;
        while ((float)n24 * f3 - (float)n18 < 155.0f) {
            f2 = 0.6f - (float)(n16 + n24) / 25.0f * 0.3f;
            GlStateManager.color(f2, f2, f2, 1.0f);
            n8 = 0;
            while ((float)n8 * f4 - (float)n17 < 224.0f) {
                random.setSeed(this.mc.getSession().getPlayerID().hashCode() + n15 + n8 + (n16 + n24) * 16);
                n7 = random.nextInt(1 + n16 + n24) + (n16 + n24) / 2;
                TextureAtlasSprite textureAtlasSprite = this.func_175371_a(Blocks.sand);
                if (n7 <= 37 && n16 + n24 != 35) {
                    if (n7 == 22) {
                        textureAtlasSprite = random.nextInt(2) == 0 ? this.func_175371_a(Blocks.diamond_ore) : this.func_175371_a(Blocks.redstone_ore);
                    } else if (n7 == 10) {
                        textureAtlasSprite = this.func_175371_a(Blocks.iron_ore);
                    } else if (n7 == 8) {
                        textureAtlasSprite = this.func_175371_a(Blocks.coal_ore);
                    } else if (n7 > 4) {
                        textureAtlasSprite = this.func_175371_a(Blocks.stone);
                    } else if (n7 > 0) {
                        textureAtlasSprite = this.func_175371_a(Blocks.dirt);
                    }
                } else {
                    Block block = Blocks.bedrock;
                    textureAtlasSprite = this.func_175371_a(block);
                }
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.drawTexturedModalRect(n8 * 16 - n17, n24 * 16 - n18, textureAtlasSprite, 16, 16);
                ++n8;
            }
            ++n24;
        }
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
        n24 = 0;
        while (n24 < AchievementList.achievementList.size()) {
            Achievement achievement = AchievementList.achievementList.get(n24);
            if (achievement.parentAchievement != null) {
                n8 = achievement.displayColumn * 24 - n9 + 11;
                n7 = achievement.displayRow * 24 - n10 + 11;
                int n25 = achievement.parentAchievement.displayColumn * 24 - n9 + 11;
                int n26 = achievement.parentAchievement.displayRow * 24 - n10 + 11;
                n6 = this.statFileWriter.hasAchievementUnlocked(achievement) ? 1 : 0;
                n5 = this.statFileWriter.canUnlockAchievement(achievement);
                n4 = this.statFileWriter.func_150874_c(achievement);
                if (n4 <= 4) {
                    n3 = -16777216;
                    if (n6 != 0) {
                        n3 = -6250336;
                    } else if (n5 != 0) {
                        n3 = -16711936;
                    }
                    GuiAchievements.drawHorizontalLine(n8, n25, n7, n3);
                    this.drawVerticalLine(n25, n7, n26, n3);
                    if (n8 > n25) {
                        this.drawTexturedModalRect(n8 - 11 - 7, n7 - 5, 114, 234, 7, 11);
                    } else if (n8 < n25) {
                        this.drawTexturedModalRect(n8 + 11, n7 - 5, 107, 234, 7, 11);
                    } else if (n7 > n26) {
                        this.drawTexturedModalRect(n8 - 5, n7 - 11 - 7, 96, 234, 11, 7);
                    } else if (n7 < n26) {
                        this.drawTexturedModalRect(n8 - 5, n7 + 11, 96, 241, 11, 7);
                    }
                }
            }
            ++n24;
        }
        Achievement achievement = null;
        f2 = (float)(n - n13) * this.field_146570_r;
        float f5 = (float)(n2 - n14) * this.field_146570_r;
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        n7 = 0;
        while (n7 < AchievementList.achievementList.size()) {
            block50: {
                float f6;
                int n27;
                Achievement achievement2;
                block52: {
                    block55: {
                        block54: {
                            block53: {
                                block51: {
                                    achievement2 = AchievementList.achievementList.get(n7);
                                    n27 = achievement2.displayColumn * 24 - n9;
                                    n6 = achievement2.displayRow * 24 - n10;
                                    if (n27 < -24 || n6 < -24 || !((float)n27 <= 224.0f * this.field_146570_r) || !((float)n6 <= 155.0f * this.field_146570_r)) break block50;
                                    n5 = this.statFileWriter.func_150874_c(achievement2);
                                    if (!this.statFileWriter.hasAchievementUnlocked(achievement2)) break block51;
                                    f6 = 0.75f;
                                    GlStateManager.color(f6, f6, f6, 1.0f);
                                    break block52;
                                }
                                if (!this.statFileWriter.canUnlockAchievement(achievement2)) break block53;
                                f6 = 1.0f;
                                GlStateManager.color(f6, f6, f6, 1.0f);
                                break block52;
                            }
                            if (n5 >= 3) break block54;
                            f6 = 0.3f;
                            GlStateManager.color(f6, f6, f6, 1.0f);
                            break block52;
                        }
                        if (n5 != 3) break block55;
                        f6 = 0.2f;
                        GlStateManager.color(f6, f6, f6, 1.0f);
                        break block52;
                    }
                    if (n5 != 4) break block50;
                    f6 = 0.1f;
                    GlStateManager.color(f6, f6, f6, 1.0f);
                }
                this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
                if (achievement2.getSpecial()) {
                    this.drawTexturedModalRect(n27 - 2, n6 - 2, 26, 202, 26, 26);
                } else {
                    this.drawTexturedModalRect(n27 - 2, n6 - 2, 0, 202, 26, 26);
                }
                if (!this.statFileWriter.canUnlockAchievement(achievement2)) {
                    f6 = 0.1f;
                    GlStateManager.color(f6, f6, f6, 1.0f);
                    this.itemRender.func_175039_a(false);
                }
                GlStateManager.enableLighting();
                GlStateManager.enableCull();
                this.itemRender.renderItemAndEffectIntoGUI(achievement2.theItemStack, n27 + 3, n6 + 3);
                GlStateManager.blendFunc(770, 771);
                GlStateManager.disableLighting();
                if (!this.statFileWriter.canUnlockAchievement(achievement2)) {
                    this.itemRender.func_175039_a(true);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                if (f2 >= (float)n27 && f2 <= (float)(n27 + 22) && f5 >= (float)n6 && f5 <= (float)(n6 + 22)) {
                    achievement = achievement2;
                }
            }
            ++n7;
        }
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
        this.drawTexturedModalRect(n11, n12, 0, 0, this.field_146555_f, this.field_146557_g);
        zLevel = 0.0f;
        GlStateManager.depthFunc(515);
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        super.drawScreen(n, n2, f);
        if (achievement != null) {
            String string = achievement.getStatName().getUnformattedText();
            String string2 = achievement.getDescription();
            int n28 = n + 12;
            n6 = n2 - 4;
            n5 = this.statFileWriter.func_150874_c(achievement);
            if (this.statFileWriter.canUnlockAchievement(achievement)) {
                n4 = Math.max(this.fontRendererObj.getStringWidth(string), 120);
                n3 = this.fontRendererObj.splitStringWidth(string2, n4);
                if (this.statFileWriter.hasAchievementUnlocked(achievement)) {
                    n3 += 12;
                }
                GuiAchievements.drawGradientRect(n28 - 3, n6 - 3, n28 + n4 + 3, n6 + n3 + 3 + 12, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(string2, n28, n6 + 12, n4, -6250336);
                if (this.statFileWriter.hasAchievementUnlocked(achievement)) {
                    this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken", new Object[0]), n28, n6 + n3 + 4, -7302913);
                }
            } else if (n5 == 3) {
                string = I18n.format("achievement.unknown", new Object[0]);
                n4 = Math.max(this.fontRendererObj.getStringWidth(string), 120);
                String string3 = new ChatComponentTranslation("achievement.requires", achievement.parentAchievement.getStatName()).getUnformattedText();
                int n29 = this.fontRendererObj.splitStringWidth(string3, n4);
                GuiAchievements.drawGradientRect(n28 - 3, n6 - 3, n28 + n4 + 3, n6 + n29 + 12 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(string3, n28, n6 + 12, n4, -9416624);
            } else if (n5 < 3) {
                n4 = Math.max(this.fontRendererObj.getStringWidth(string), 120);
                String string4 = new ChatComponentTranslation("achievement.requires", achievement.parentAchievement.getStatName()).getUnformattedText();
                int n30 = this.fontRendererObj.splitStringWidth(string4, n4);
                GuiAchievements.drawGradientRect(n28 - 3, n6 - 3, n28 + n4 + 3, n6 + n30 + 12 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(string4, n28, n6 + 12, n4, -9416624);
            } else {
                string = null;
            }
            if (string != null) {
                this.fontRendererObj.drawStringWithShadow(string, n28, n6, this.statFileWriter.canUnlockAchievement(achievement) ? (achievement.getSpecial() ? -128 : -1) : (achievement.getSpecial() ? -8355776 : -8355712));
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public void doneLoading() {
        if (this.loadingAchievements) {
            this.loadingAchievements = false;
        }
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        if (n == Minecraft.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        } else {
            super.keyTyped(c, n);
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (this.loadingAchievements) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), width / 2, height / 2, 0xFFFFFF);
            this.drawCenteredString(this.fontRendererObj, lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % (long)lanSearchStates.length)], width / 2, height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 0xFFFFFF);
        } else {
            int n3;
            if (Mouse.isButtonDown((int)0)) {
                n3 = (width - this.field_146555_f) / 2;
                int n4 = (height - this.field_146557_g) / 2;
                int n5 = n3 + 8;
                int n6 = n4 + 17;
                if ((this.field_146554_D == 0 || this.field_146554_D == 1) && n >= n5 && n < n5 + 224 && n2 >= n6 && n2 < n6 + 155) {
                    if (this.field_146554_D == 0) {
                        this.field_146554_D = 1;
                    } else {
                        this.field_146567_u -= (double)((float)(n - this.field_146563_h) * this.field_146570_r);
                        this.field_146566_v -= (double)((float)(n2 - this.field_146564_i) * this.field_146570_r);
                        this.field_146565_w = this.field_146569_s = this.field_146567_u;
                        this.field_146573_x = this.field_146568_t = this.field_146566_v;
                    }
                    this.field_146563_h = n;
                    this.field_146564_i = n2;
                }
            } else {
                this.field_146554_D = 0;
            }
            n3 = Mouse.getDWheel();
            float f2 = this.field_146570_r;
            if (n3 < 0) {
                this.field_146570_r += 0.25f;
            } else if (n3 > 0) {
                this.field_146570_r -= 0.25f;
            }
            this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0f, 2.0f);
            if (this.field_146570_r != f2) {
                float f3 = f2 - this.field_146570_r;
                float f4 = f2 * (float)this.field_146555_f;
                float f5 = f2 * (float)this.field_146557_g;
                float f6 = this.field_146570_r * (float)this.field_146555_f;
                float f7 = this.field_146570_r * (float)this.field_146557_g;
                this.field_146567_u -= (double)((f6 - f4) * 0.5f);
                this.field_146566_v -= (double)((f7 - f5) * 0.5f);
                this.field_146565_w = this.field_146569_s = this.field_146567_u;
                this.field_146573_x = this.field_146568_t = this.field_146566_v;
            }
            if (this.field_146565_w < (double)field_146572_y) {
                this.field_146565_w = field_146572_y;
            }
            if (this.field_146573_x < (double)field_146571_z) {
                this.field_146573_x = field_146571_z;
            }
            if (this.field_146565_w >= (double)field_146559_A) {
                this.field_146565_w = field_146559_A - 1;
            }
            if (this.field_146573_x >= (double)field_146560_B) {
                this.field_146573_x = field_146560_B - 1;
            }
            this.drawDefaultBackground();
            this.drawAchievementScreen(n, n2, f);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.drawTitle();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }

    protected void drawTitle() {
        int n = (width - this.field_146555_f) / 2;
        int n2 = (height - this.field_146557_g) / 2;
        this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), n + 15, n2 + 5, 0x404040);
    }

    static {
        field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
        field_146571_z = AchievementList.minDisplayRow * 24 - 112;
        field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
        field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
        ACHIEVEMENT_BACKGROUND = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    }

    private TextureAtlasSprite func_175371_a(Block block) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getDefaultState());
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (!this.loadingAchievements && guiButton.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void updateScreen() {
        if (!this.loadingAchievements) {
            this.field_146569_s = this.field_146567_u;
            this.field_146568_t = this.field_146566_v;
            double d = this.field_146565_w - this.field_146567_u;
            double d2 = this.field_146573_x - this.field_146566_v;
            if (d * d + d2 * d2 < 4.0) {
                this.field_146567_u += d;
                this.field_146566_v += d2;
            } else {
                this.field_146567_u += d * 0.85;
                this.field_146566_v += d2 * 0.85;
            }
        }
    }

    public GuiAchievements(GuiScreen guiScreen, StatFileWriter statFileWriter) {
        this.parentScreen = guiScreen;
        this.statFileWriter = statFileWriter;
        int n = 141;
        int n2 = 141;
        this.field_146567_u = this.field_146565_w = (double)(AchievementList.openInventory.displayColumn * 24 - n / 2 - 12);
        this.field_146569_s = this.field_146565_w;
        this.field_146566_v = this.field_146573_x = (double)(AchievementList.openInventory.displayRow * 24 - n2 / 2);
        this.field_146568_t = this.field_146573_x;
    }
}

