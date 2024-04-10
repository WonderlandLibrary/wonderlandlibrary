package info.sigmaclient.sigma.modules.gui;

import java.util.Iterator;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import info.sigmaclient.sigma.utils.render.blurs.GradientRoundRectShader;
import info.sigmaclient.sigma.utils.render.blurs.GradientGlowing;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.blurs.RoundRectShader;
import java.util.Collection;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import net.minecraft.client.gui.AbstractGui;
import info.sigmaclient.sigma.sigma5.utils.JelloSwapBlur;
import java.awt.Color;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import info.sigmaclient.sigma.sigma5.utils.SomeAnim;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.gui.screen.ChatScreen;
import info.sigmaclient.sigma.modules.combat.Killaura;
import net.minecraft.entity.player.PlayerEntity;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.config.values.Value;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import net.minecraft.entity.LivingEntity;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.modules.RenderModule;

public class TargetHUD extends RenderModule
    {
        public NumberValue x;
        public NumberValue y;
        static BooleanValue healthBypass;
        static ModeValue animation;
        LivingEntity target;
        PartialTicksAnim scale;
        PartialTicksAnim alpha;
        PartialTicksAnim health;
        PartialTicksAnim mhealth;

        @Override
    public void setX(final float v) {
            this.x.setValue(v);
        }

    @Override
    public void setY(final float v) {
            this.y.setValue(v);
        }

    @Override
    public float getX() {
            return this.x.getValue().floatValue();
        }

    @Override
    public float getY() {
            return this.y.getValue().floatValue();
        }

    public TargetHUD() {
            super("TargetHud", Category.Gui, "Target HUD");
            this.x = new NumberValue("X", (Number)0, (Number)0, (Number)10000, NumberValue.NUMBER_TYPE.INT) {
                @Override
            public boolean isHidden() {
                    return true;
                }
            };
            this.y = new NumberValue("Y", (Number)0, (Number)0, (Number)10000, NumberValue.NUMBER_TYPE.INT) {
                @Override
            public boolean isHidden() {
                    return true;
                }
            };
            this.target = null;
            this.scale = new PartialTicksAnim(0.0f);
            this.alpha = new PartialTicksAnim(0.0f);
            this.health = new PartialTicksAnim(0.0f);
            this.mhealth = new PartialTicksAnim(0.0f);
            this.registerValue(this.x);
            this.registerValue(this.y);
            this.registerValue(TargetHUD.healthBypass);
            this.registerValue(TargetHUD.animation);
        }

    @Override
    public void onWindowUpdateEvent(final WindowUpdateEvent event) {
            boolean s = false;
            if (Killaura.attackTarget instanceof PlayerEntity) {
                this.target = Killaura.attackTarget;
            }
        else if (TargetHUD.mc.currentScreen instanceof ChatScreen) {
            this.target = TargetHUD.mc.player;
        }
        else {
            s = true;
            if (this.scale.getValue() == 0.0f) {
                this.target = null;
            }
        }
        this.alpha.interpolate(s ? 0.0f : 10.0f, -1.0);
        this.scale.interpolate(s ? 0.0f : 10.0f, -0.6);
        if (this.target != null) {
            final float health = this.getHealth();
            this.health.interpolate(Math.max(Math.min(health / this.target.getMaxHealth(), 1.0f), 0.0f), 3.0);
            this.mhealth.interpolate(Math.max(Math.min(this.target.getAbsorptionAmount() / this.target.getMaxHealth(), 1.0f), 0.0f), 3.0);
        }
        super.onWindowUpdateEvent(event);
        }

    private float getHealth() {
            float health = this.target.getHealth();
            if (TargetHUD.healthBypass.isEnable()) {
                final LivingEntity target = this.target;
                if (target instanceof final PlayerEntity player) {
                    final Scoreboard scoreboard = player.getWorldScoreboard();
                    final ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
                    if (scoreobjective != null) {
                        final Score score = scoreboard.getOrCreateScore(player.getScoreboardName(), scoreobjective);
                        health = (float)score.getScorePoints();
                    }
                }
            }
        return health;
        }

    public boolean isHover(final double mx, final double my) {
            final double x = this.x.getValue().floatValue();
            final double y = this.y.getValue().floatValue();
            final double width = 60.0;
            final double height = 40.0;
            return ClickUtils.isClickable(x - width, y, x + width, y + height, mx, my);
        }

    @Override
    public void onRenderEvent(final RenderEvent event) {
            if (this.target != null) {
                final float health = this.getHealth();
                double scale = this.scale.getValue() / 10.0;
                final double alpha = this.alpha.getValue() / 10.0;
                final String s = TargetHUD.animation.getValue();
                switch (s) {
                    case "Jello": {
                        scale = SomeAnim.欫좯콵甐鶲㥇((float)scale, 0.17, 1.0, 0.51, 1.0);
                        scale *= 1.1799999475479126;
                        if (scale > 1.090000033378601) {
                            scale = 1.18 - scale + 1.0;
                            break;
                        }
                        break;
                    }
                    case "Ease": {
                        scale = SomeAnim.欫좯콵甐鶲㥇((float)scale, 0.17, 1.0, 0.51, 1.0);
                        break;
                    }
                }
                final double x = this.x.getValue().floatValue();
                final double y = this.y.getValue().floatValue();
                final double width = 60.0;
                final double height = 40.0;
                GL14.glPushMatrix();
                GlStateManager.translate(x, y + height / 2.0, 0.0);
                GlStateManager.scale(scale, scale, 1.0);
                GlStateManager.translate(-x, -(y + height / 2.0), 0.0);
                RenderUtils.sigma_drawShadow((float)(x - width), (float)y, (float)(width * 2.0), (float)height, 7.0f, 0.5f);
                if (Shader.isEnable()) {
                    StencilUtil.initStencilToWrite();
                    RenderUtils.drawRect((float)(x - width), (float)y, (float)(x - width) + (float)width * 2.0f, (float)y + height, new Color(255, 255, 255, (int)(255.0 * alpha)).getRGB());
                    StencilUtil.readStencilBuffer(1);
                    JelloSwapBlur.蓳瀧藸䖼錌();
                    StencilUtil.uninitStencilBuffer();
                }
                RenderUtils.drawRect((float)(x - width), (float)y, (float)(x - width) + (float)width * 2.0f, (float)y + height, new Color(0, 0, 0, (int)(10.0 * alpha)).getRGB());
                final NetworkPlayerInfo info = TargetHUD.mc.getConnection().getPlayerInfo(this.target.getUniqueID());
                if (info != null) {
                    RenderUtils.resetColor();
                    GL14.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    TargetHUD.mc.getTextureManager().bindTexture(info.getLocationSkin());
                    StencilUtil.initStencilToWrite();
                    RenderUtils.drawRoundedRect((float)(x - width + 4.0), (float)(y + 4.0), (float)(x - width + 4.0) + 32.0f, (float)(y + 4.0) + 32.0f, 4.0f, new Color(255, 255, 255, (int)(255.0 * alpha)).getRGB());
                    StencilUtil.readStencilBuffer(1);
                    AbstractGui.drawScaledCustomSizeModalRect((float)(x - width + 4.0), (float)(y + 4.0), 8.0f, 8.0f, 8.0f, 8.0f, 32.0f, 32.0f, 64.0f, 64.0f);
                    StencilUtil.uninitStencilBuffer();
                }
                String n = this.target.getName().getUnformattedComponentText();
                if (n.length() > 10) {
                    n = n.substring(0, 10) + "...";
                }
                JelloFontUtil.jelloFontBold18.drawString(n, (float)(x - width + 5.0 + 32.0 + 5.0), (float)y + 7.0f - 1.0f, new Color(230, 230, 230, (int)(255.0 * alpha)).getRGB());
                GL14.glPushMatrix();
                int index = 0;
                final float offX = -2.0f;
                final ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
                stackList.add(this.target.getHeldItemMainhand());
                stackList.add(this.target.getHeldItemOffhand());
                stackList.addAll((Collection)this.target.getArmorInventoryList());
                for (final ItemStack a : stackList) {
                    RoundRectShader.drawRoundRect((int)(x - width + 5.0 + 32.0 + 5.0) + index * 9 + 2 + offX, (float)((int)y + 16), 8.0f, 8.0f, 3.0f, new Color(255, 255, 255, (int)(50.0 * alpha)));
                    GlStateManager.color(1.0f, 1.0f, 1.0f, (float)(alpha * 1.0));
                    TargetHUD.mc.ingameGUI.renderHotbarItemCustom((int)((int)(x - width + 5.0 + 32.0 + 5.0) + index * 9 + offX), (int)y + 16 - 2, event.renderTime, TargetHUD.mc.player, a, 0.5f);
                    ++index;
                }
                GL14.glPopMatrix();
                final double dd = (width * 2.0 - 5.0 - 32.0 - 5.0) * this.health.getValue() * 0.8999999761581421;
                final double dd2 = (width * 2.0 - 5.0 - 32.0 - 5.0) * 0.8999999761581421;
                final double dd3 = (width * 2.0 - 5.0 - 32.0 - 5.0) * this.mhealth.getValue() * 0.8999999761581421;
                RoundRectShader.drawRoundRect((float)(x - width + 5.0 + 32.0 + 5.0), (float)y + 7.0f + 11.0f + 8.0f + 2.0f, (float)dd2, 4.0f, 4.0f, new Color(30, 30, 30, (int)(255.0 * alpha * 0.800000011920929)));
                GradientGlowing.applyGradientCornerRL((float)(x - width + 5.0 + 32.0 + 5.0), (float)y + 7.0f + 11.0f + 8.0f + 2.0f, (float)dd, 4.0f, (float)Math.sqrt(alpha), ColorUtils.reAlpha(ColorChanger.getColor(0, 10), (int)(255.0 * Math.sqrt(alpha))), ColorUtils.reAlpha(ColorChanger.getColor(100, 10), (int)(255.0 * Math.sqrt(alpha))), 4.0f, 3.0f);
                GradientRoundRectShader.drawRoundRect((float)(x - width + 5.0 + 32.0 + 5.0), (float)y + 7.0f + 11.0f + 8.0f + 2.0f, (float)dd, 4.0f, 4.0f, ColorUtils.reAlpha(ColorChanger.getColor(0, 10), (int)(255.0 * Math.sqrt(alpha))), ColorUtils.reAlpha(ColorChanger.getColor(100, 10), (int)(255.0 * Math.sqrt(alpha))));
                if (dd3 != 0.0) {
                    RoundRectShader.drawRoundRect((float)(x - width + 5.0 + 32.0 + 5.0), (float)y + 7.0f + 11.0f + 8.0f + 2.0f, (float)dd3, 4.0f, 4.0f, ColorUtils.reAlpha(ColorUtils.blend(ColorChanger.getColor(0, 10), new Color(255, 255, 255), 0.5), (int)(255.0 * Math.sqrt(alpha))));
                }
                JelloFontUtil.jelloFontBold14.drawCenteredString("" + Math.ceil(health), (float)((float)(x - width + 5.0 + 32.0 + 5.0) + dd), (float)y + 7.0f + 11.0f + 17.0f, new Color(255, 255, 255, (int)(255.0 * alpha)).getRGB());
                GL14.glPopMatrix();
                RenderUtils.resetColor();
            }
        super.onRenderEvent(event);
        }

    static {
            TargetHUD.healthBypass = new BooleanValue("Health Bypass", false);
            TargetHUD.animation = new ModeValue("Animation", "Jello", new String[] { "Jello", "Smooth", "Ease" });
        }
    }
