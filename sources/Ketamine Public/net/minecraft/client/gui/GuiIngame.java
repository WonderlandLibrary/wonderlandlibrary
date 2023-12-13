package net.minecraft.client.gui;

import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.util.render.BlurUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

public class GuiIngame extends Gui {
    private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    private final Random rand = new Random();
    private final Minecraft mc;
    private final RenderItem itemRenderer;

    /**
     * ChatGUI instance that retains all previous chat data
     */
    private final GuiNewChat persistantChatGUI;
    private int updateCounter;

    /**
     * Remaining ticks the item highlight should be visible
     */
    private int remainingHighlightTicks;

    /**
     * The ItemStack that is currently being highlighted
     */
    private ItemStack highlightingItemStack;
    private final GuiOverlayDebug overlayDebug;

    /**
     * The spectator GUI for this in-game GUI instance
     */
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private int field_175195_w;
    private String field_175201_x = "";
    public String field_175200_y = "";
    private int field_175199_z;
    private int field_175192_A;
    private int field_175193_B;
    private int playerHealth = 0;
    private int lastPlayerHealth = 0;

    /**
     * The last recorded system time
     */
    private long lastSystemTime = 0L;

    /**
     * Used with updateCounter to make the heart bar flash
     */
    private long healthUpdateCounter = 0L;

    public GuiIngame(Minecraft mcIn) {
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.func_175177_a();
    }

    public void func_175177_a() {
        this.field_175199_z = 10;
        this.field_175192_A = 70;
        this.field_175193_B = 20;
    }

    public void renderGameOverlay(ScaledResolution scaledresolution, float partialTicks) {
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();

        this.mc.entityRenderer.setupOverlayRendering(scaledresolution);

        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        DrawUtil.glDrawArcOutline(20, 20, 10, 20, 40, 2, 0xFFFFFFFF);

        // FIND ME :: Draw blur overlay
        BlurUtil.onRenderGameOverlay(this.mc.getFramebuffer(), scaledresolution);

        // FIND ME :: RenderOverlayEvent
        final RenderGameOverlayEvent event = new RenderGameOverlayEvent(partialTicks, scaledresolution);
        KetamineClient.getInstance().getEventBus().post(event);

        glEnable(GL_BLEND);
        glColor4f(1, 1, 1, 1);

        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        } else {
            this.renderTooltip(scaledresolution, partialTicks);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(icons);
        GL11.glEnable(GL11.GL_BLEND);

        GL14.glBlendFuncSeparate(775, 769, 1, 0);
        GlStateManager.enableAlpha();

        if (event.isRenderCrossHair() && this.showCrosshair()) {
            drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }

        GL14.glBlendFuncSeparate(770, 771, 1, 0);

        if (event.isRenderBossHealth()) {
            this.renderBossHealth();
        }

        if (this.mc.playerController.shouldDrawHUD()) {
            this.renderPlayerStats(scaledresolution);
        }

        GL11.glDisable(GL11.GL_BLEND);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k1 = i / 2 - 91;

        if (this.mc.thePlayer.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, k1);
        } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, k1);
        }

        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.func_181551_a(scaledresolution);
        } else if (this.mc.thePlayer.isSpectator()) {
            this.spectatorGui.func_175263_a(scaledresolution);
        }

        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }

        if (this.field_175195_w > 0) {
            float f3 = (float) this.field_175195_w - partialTicks;
            int i2 = 255;

            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                float f4 = (float) (this.field_175199_z + this.field_175192_A + this.field_175193_B) - f3;
                i2 = (int) (f4 * 255.0F / (float) this.field_175199_z);
            }

            if (this.field_175195_w <= this.field_175193_B) {
                i2 = (int) (f3 * 255.0F / (float) this.field_175193_B);
            }

            i2 = MathHelper.clamp_int(i2, 0, 255);

            if (i2 > 8) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) (i / 2), (float) (j / 2), 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL14.glBlendFuncSeparate(770, 771, 1, 0);

                final float scale = 4.0F;
                final float downscale = 2.0F / 4.0F;
                final float restore = 1.0F / downscale;
                glScalef(scale, scale, scale);
                int j2 = i2 << 24 & -16777216;
                this.getFontRenderer().drawString(this.field_175201_x, (float) (-this.getFontRenderer().getStringWidth(this.field_175201_x) / 2), -10.0F, 16777215 | j2, true);
                glScalef(downscale, downscale, downscale);
                this.getFontRenderer().drawString(this.field_175200_y, (float) (-this.getFontRenderer().getStringWidth(this.field_175200_y) / 2), 5.0F, 16777215 | j2, true);
                glScalef(restore, restore, restore);

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }

        final Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        final ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);

        if (objective != null) {
            this.renderScoreboard(objective, scaledresolution);
        }

        this.persistantChatGUI.drawChat(scaledresolution, this.updateCounter);

        if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && !this.mc.isIntegratedServerRunning()) {
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreboard.getObjectiveInDisplaySlot(0));
        }

        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glDisable(GL_LIGHTING);
        glEnable(GL_ALPHA_TEST);
    }

    protected void renderTooltip(ScaledResolution sr, float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            float f = this.zLevel;
            this.zLevel = -90.0F;
            drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            this.zLevel = f;
            GlStateManager.enableRescaleNormal();
            GL11.glEnable(GL11.GL_BLEND);
            GL14.glBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for (int j = 0; j < 9; ++j) {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    public void renderHorseJumpBar(ScaledResolution p_175186_1_, int p_175186_2_) {
        this.mc.getTextureManager().bindTexture(Gui.icons);
        float f = this.mc.thePlayer.getHorseJumpPower();
        int i = 182;
        int j = (int) (f * (float) (i + 1));
        int k = p_175186_1_.getScaledHeight() - 32 + 3;
        drawTexturedModalRect(p_175186_2_, k, 0, 84, i, 5);

        if (j > 0) {
            drawTexturedModalRect(p_175186_2_, k, 0, 89, j, 5);
        }
    }

    public void renderExpBar(ScaledResolution p_175176_1_, int p_175176_2_) {
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int i = this.mc.thePlayer.xpBarCap();

        if (i > 0) {
            int j = 182;
            int k = (int) (this.mc.thePlayer.experience * (float) (j + 1));
            int l = p_175176_1_.getScaledHeight() - 32 + 3;
            drawTexturedModalRect(p_175176_2_, l, 0, 64, j, 5);

            if (k > 0) {
                drawTexturedModalRect(p_175176_2_, l, 0, 69, k, 5);
            }
        }

        if (this.mc.thePlayer.experienceLevel > 0) {
            int k1 = 8453920;
            String s = "" + this.mc.thePlayer.experienceLevel;
            int l1 = (p_175176_1_.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int i1 = p_175176_1_.getScaledHeight() - 31 - 4;
            int j1 = 0;
            this.getFontRenderer().drawString(s, l1 + 1, i1, 0);
            this.getFontRenderer().drawString(s, l1 - 1, i1, 0);
            this.getFontRenderer().drawString(s, l1, i1 + 1, 0);
            this.getFontRenderer().drawString(s, l1, i1 - 1, 0);
            this.getFontRenderer().drawString(s, l1, i1, k1);
        }
    }

    public void func_181551_a(ScaledResolution p_181551_1_) {
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            String s = this.highlightingItemStack.getDisplayName();

            if (this.highlightingItemStack.hasDisplayName()) {
                s = EnumChatFormatting.ITALIC + s;
            }

            int i = (p_181551_1_.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int j = p_181551_1_.getScaledHeight() - 59;

            if (!this.mc.playerController.shouldDrawHUD()) {
                j += 14;
            }

            int k = (int) ((float) this.remainingHighlightTicks * 256.0F / 10.0F);

            if (k > 255) {
                k = 255;
            }

            if (k > 0) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL14.glBlendFuncSeparate(770, 771, 1, 0);
                this.getFontRenderer().drawStringWithShadow(s, (float) i, (float) j, 16777215 + (k << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

    protected boolean showCrosshair() {
        if (this.mc.gameSettings.showDebugInfo && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
            return false;
        } else if (this.mc.playerController.isSpectator()) {
            if (this.mc.pointedEntity != null) {
                return true;
            } else {
                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();

                    return this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory;
                }

                return false;
            }
        } else {
            return true;
        }
    }

    private void renderScoreboard(final ScoreObjective objective,
                                  final ScaledResolution resolution) {
        final Scoreboard scoreboard = objective.getScoreboard();
        final List<Score> list = scoreboard.getSortedScores(objective).stream()
            .filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#"))
            .limit(15)
            .collect(Collectors.toList());

        final int length = list.size();

        final FontRenderer fontRenderer = this.mc.fontRendererObj;

        double width = fontRenderer.getWidth(objective.getDisplayName()) + 4;

        final String[] formattedPlayerNames = new String[length];

        for (int i = 0; i < length; i++) {
            final Score score = list.get(i);

            final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            final String playerName = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName());
            formattedPlayerNames[i] = playerName;

            width = Math.max(width, fontRenderer.getWidth(playerName) + 4);
        }

        final int height = 11 + list.size() * this.getFontRenderer().FONT_HEIGHT;

        final double xBuffer = 4;
        final int yBuffer = (int) (4 + resolution.getScaledHeight() / 2.0 - height / 2.0);

        BlurUtil.blurArea(xBuffer, yBuffer, width, height);
        DrawUtil.glDrawFilledQuad(xBuffer, yBuffer, width, height, 0x80000000);

        for (int i = 0; i < length; i++) {
            fontRenderer.drawStringWithShadow(formattedPlayerNames[i], xBuffer + 2, yBuffer + height - (i + 1) * 9, 0xFFFFFFFF);
        }

        fontRenderer.drawStringWithShadow(objective.getDisplayName(),
                                          xBuffer + width / 2.0F - fontRenderer.getWidth(objective.getDisplayName()) / 2.0F,
                                          yBuffer + 2, 0xFFFFFFFF);
    }

    private void renderPlayerStats(ScaledResolution p_180477_1_) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
            int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean flag = this.healthUpdateCounter > (long) this.updateCounter && (this.healthUpdateCounter - (long) this.updateCounter) / 3L % 2L == 1L;

            if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            } else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }

            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = i;
                this.lastPlayerHealth = i;
                this.lastSystemTime = Minecraft.getSystemTime();
            }

            this.playerHealth = i;
            int j = this.lastPlayerHealth;
            this.rand.setSeed(this.updateCounter * 312871);
            boolean flag1 = false;
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            int l = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = p_180477_1_.getScaledWidth() / 2 - 91;
            int j1 = p_180477_1_.getScaledWidth() / 2 + 91;
            int k1 = p_180477_1_.getScaledHeight() - 39;
            float f = (float) iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = k1 - (l1 - 1) * i2 - 10;
            float f2 = f1;
            int k2 = entityplayer.getTotalArmorValue();
            int l2 = -1;

            if (entityplayer.isPotionActive(Potion.regeneration)) {
                l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
            }

            for (int i3 = 0; i3 < 10; ++i3) {
                if (k2 > 0) {
                    int j3 = i1 + i3 * 8;

                    if (i3 * 2 + 1 < k2) {
                        drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 == k2) {
                        drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 > k2) {
                        drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
                    }
                }
            }

            for (int i6 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; i6 >= 0; --i6) {
                int j6 = 16;

                if (entityplayer.isPotionActive(Potion.poison)) {
                    j6 += 36;
                } else if (entityplayer.isPotionActive(Potion.wither)) {
                    j6 += 72;
                }

                int k3 = 0;

                if (flag) {
                    k3 = 1;
                }

                int l3 = MathHelper.ceiling_float_int((float) (i6 + 1) / 10.0F) - 1;
                int i4 = i1 + i6 % 10 * 8;
                int j4 = k1 - l3 * i2;

                if (i <= 4) {
                    j4 += this.rand.nextInt(2);
                }

                if (i6 == l2) {
                    j4 -= 2;
                }

                int k4 = 0;

                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    k4 = 5;
                }

                drawTexturedModalRect(i4, j4, 16 + k3 * 9, 9 * k4, 9, 9);

                if (flag) {
                    if (i6 * 2 + 1 < j) {
                        drawTexturedModalRect(i4, j4, j6 + 54, 9 * k4, 9, 9);
                    }

                    if (i6 * 2 + 1 == j) {
                        drawTexturedModalRect(i4, j4, j6 + 63, 9 * k4, 9, 9);
                    }
                }

                if (f2 > 0.0F) {
                    if (f2 == f1 && f1 % 2.0F == 1.0F) {
                        drawTexturedModalRect(i4, j4, j6 + 153, 9 * k4, 9, 9);
                    } else {
                        drawTexturedModalRect(i4, j4, j6 + 144, 9 * k4, 9, 9);
                    }

                    f2 -= 2.0F;
                } else {
                    if (i6 * 2 + 1 < i) {
                        drawTexturedModalRect(i4, j4, j6 + 36, 9 * k4, 9, 9);
                    }

                    if (i6 * 2 + 1 == i) {
                        drawTexturedModalRect(i4, j4, j6 + 45, 9 * k4, 9, 9);
                    }
                }
            }

            Entity entity = entityplayer.ridingEntity;

            if (entity == null) {
                for (int k6 = 0; k6 < 10; ++k6) {
                    int i7 = k1;
                    int l7 = 16;
                    int j8 = 0;

                    if (entityplayer.isPotionActive(Potion.hunger)) {
                        l7 += 36;
                        j8 = 13;
                    }

                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (k * 3 + 1) == 0) {
                        i7 = k1 + (this.rand.nextInt(3) - 1);
                    }

                    if (flag1) {
                        j8 = 1;
                    }

                    int i9 = j1 - k6 * 8 - 9;
                    drawTexturedModalRect(i9, i7, 16 + j8 * 9, 27, 9, 9);

                    if (flag1) {
                        if (k6 * 2 + 1 < l) {
                            drawTexturedModalRect(i9, i7, l7 + 54, 27, 9, 9);
                        }

                        if (k6 * 2 + 1 == l) {
                            drawTexturedModalRect(i9, i7, l7 + 63, 27, 9, 9);
                        }
                    }

                    if (k6 * 2 + 1 < k) {
                        drawTexturedModalRect(i9, i7, l7 + 36, 27, 9, 9);
                    }

                    if (k6 * 2 + 1 == k) {
                        drawTexturedModalRect(i9, i7, l7 + 45, 27, 9, 9);
                    }
                }
            } else if (entity instanceof EntityLivingBase) {
                EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
                int j7 = (int) Math.ceil(entitylivingbase.getHealth());
                float f3 = entitylivingbase.getMaxHealth();
                int k8 = (int) (f3 + 0.5F) / 2;

                if (k8 > 30) {
                    k8 = 30;
                }

                int j9 = k1;

                for (int k9 = 0; k8 > 0; k9 += 20) {
                    int l4 = Math.min(k8, 10);
                    k8 -= l4;

                    for (int i5 = 0; i5 < l4; ++i5) {
                        int j5 = 52;
                        int k5 = 0;

                        if (flag1) {
                            k5 = 1;
                        }

                        int l5 = j1 - i5 * 8 - 9;
                        drawTexturedModalRect(l5, j9, j5 + k5 * 9, 9, 9, 9);

                        if (i5 * 2 + 1 + k9 < j7) {
                            drawTexturedModalRect(l5, j9, j5 + 36, 9, 9, 9);
                        }

                        if (i5 * 2 + 1 + k9 == j7) {
                            drawTexturedModalRect(l5, j9, j5 + 45, 9, 9, 9);
                        }
                    }

                    j9 -= 10;
                }
            }

            if (entityplayer.isInsideOfMaterial(Material.water)) {
                int l6 = this.mc.thePlayer.getAir();
                int k7 = MathHelper.ceiling_double_int((double) (l6 - 2) * 10.0D / 300.0D);
                int i8 = MathHelper.ceiling_double_int((double) l6 * 10.0D / 300.0D) - k7;

                for (int l8 = 0; l8 < k7 + i8; ++l8) {
                    if (l8 < k7) {
                        drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 16, 18, 9, 9);
                    } else {
                        drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 25, 18, 9, 9);
                    }
                }
            }
        }
    }

    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            FontRenderer fontrenderer = this.mc.fontRendererObj;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i = scaledresolution.getScaledWidth();
            int j = 182;
            int k = i / 2 - j / 2;
            int l = (int) (BossStatus.healthScale * (float) (j + 1));
            int i1 = 12;
            drawTexturedModalRect(k, i1, 0, 74, j, 5);
            drawTexturedModalRect(k, i1, 0, 74, j, 5);

            if (l > 0) {
                drawTexturedModalRect(k, i1, 0, 79, l, 5);
            }

            String s = BossStatus.bossName;
            this.getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - this.getFontRenderer().getStringWidth(s) / 2), (float) (i1 - 10), 16777215);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }

    private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_) {
        ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];

        if (itemstack != null) {
            float f = (float) itemstack.animationsToGo - partialTicks;

            if (f > 0.0F) {
                GL11.glPushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GL11.glTranslatef((float) (xPos + 8), (float) (yPos + 12), 0.0F);
                GL11.glScalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float) (-(xPos + 8)), (float) (-(yPos + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);

            if (f > 0.0F) {
                GL11.glPopMatrix();
            }

            itemRenderer.renderItemOverlays(this.getFontRenderer(), itemstack, xPos, yPos);
        }
    }

    /**
     * The update tick for the ingame UI
     */
    public void updateTick() {
        if (this.field_175195_w > 0) {
            --this.field_175195_w;

            if (this.field_175195_w <= 0) {
                this.field_175201_x = "";
                this.field_175200_y = "";
            }
        }

        ++this.updateCounter;

        if (this.mc.thePlayer != null) {
            ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();

            if (itemstack == null) {
                this.remainingHighlightTicks = 0;
            } else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            } else {
                this.remainingHighlightTicks = 40;
            }

            this.highlightingItemStack = itemstack;
        }
    }

    public void displayTitle(String title, String subtitle, int fadeInTime, int displayTime, int fadeOutTime) {
        if (title == null && subtitle == null && fadeInTime < 0 && displayTime < 0 && fadeOutTime < 0) {
            this.field_175201_x = "";
            this.field_175200_y = "";
            this.field_175195_w = 0;
        } else if (title != null) {
            this.field_175201_x = title;
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
        } else if (subtitle != null) {
            this.field_175200_y = subtitle;
        } else {
            if (fadeInTime >= 0) {
                this.field_175199_z = fadeInTime;
            }

            if (displayTime >= 0) {
                this.field_175192_A = displayTime;
            }

            if (fadeOutTime >= 0) {
                this.field_175193_B = fadeOutTime;
            }

            if (this.field_175195_w > 0) {
                this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
            }
        }
    }

    /**
     * returns a pointer to the persistant Chat GUI, containing all previous chat messages and such
     */
    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }

    public FontRenderer getFontRenderer() {
        return this.mc.fontRendererObj;
    }

    public GuiSpectator getSpectatorGui() {
        return this.spectatorGui;
    }

    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }

    public void func_181029_i() {
        this.overlayPlayerList.func_181030_a();
    }
}
