package net.minecraft.client.renderer.entity;

import cc.novoline.Novoline;
import cc.novoline.events.EventManager;
import cc.novoline.events.events.RenderEntityEvent;
import cc.novoline.events.events.RenderNameTagEvent;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.move.Scaffold;
import cc.novoline.modules.visual.Chams;
import cc.novoline.modules.visual.ESP;
import cc.novoline.utils.OutlineUtils;
import cc.novoline.utils.RenderUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.optifine.Config;
import net.optifine.Reflector;
import net.shadersmod.client.Shaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.List;

public abstract class RendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final DynamicTexture field_177096_e = new DynamicTexture(16, 16);
    public static float NAME_TAG_RANGE = 64.0F;
    public static float NAME_TAG_RANGE_SNEAK = 32.0F;

    static {
        final int[] aint = field_177096_e.getTextureData();

        for (int i = 0; i < 256; ++i) {
            aint[i] = -1;
        }

        field_177096_e.updateDynamicTexture();
    }

    protected ModelBase mainModel;
    protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
    protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
    protected boolean renderOutlines = false;

    public RendererLivingEntity(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
        this.mainModel = modelBaseIn;
        this.shadowSize = shadowSizeIn;
    }

    @SuppressWarnings("unchecked")
    public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
        return this.layerRenderers.add((LayerRenderer<T>) layer);
    }

    protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(U layer) {
        return this.layerRenderers.remove(layer);
    }

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    protected float interpolateRotation(float par1, float par2, float par3) {
        float f;

        for (f = par2 - par1; f < -180.0F; f += 360.0F) {
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return par1 + par3 * f;
    }

    public void transformHeldFull3DItemLayer() {
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

        if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, entity, this, x, y, z)) {

            EventManager.call(new RenderEntityEvent(entity, RenderEntityEvent.State.PRE));

            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = getSwingProgress(entity, partialTicks);
            this.mainModel.isRiding = entity.isRiding();

            if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
                this.mainModel.isRiding = entity.isRiding() && entity.ridingEntity != null && Reflector.callBoolean(entity.ridingEntity, Reflector.ForgeEntity_shouldRiderSit);
            }

            this.mainModel.isChild = entity.isChild();
            try {
                float f = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                float f1 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f8 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float f2 = f1 - f;

                if (this.mainModel.isRiding && entity.ridingEntity instanceof EntityLivingBase) {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity.ridingEntity;

                    f = this.interpolateRotation(entityLivingBase.prevRenderYawOffset, entityLivingBase.renderYawOffset, partialTicks);
                    f2 = f1 - f;
                    float f3 = MathHelper.wrapAngleTo180_float(f2);

                    if (f3 < -85.0F) f3 = -85.0F;
                    if (f3 >= 85.0F) f3 = 85.0F;

                    f = f1 - f3;

                    if (f3 * f3 > 2_500.0F) f += f3 * 0.2F;
                }


                if (entity instanceof EntityPlayerSP) {
                    if (Novoline.getInstance().getModuleManager().getModule(Scaffold.class).isEnabled()) {
                        Scaffold scaffold = Novoline.getInstance().getModuleManager().getModule(Scaffold.class);

                        if (scaffold.renderRotations()) {
                            if (((EntityPlayerSP) entity).isMoving() || Keyboard.isKeyDown(Minecraft.getInstance().gameSettings.keyBindJump.getKeyCode())) {
                                f = this.interpolateRotation(scaffold.getYaw(), scaffold.getYaw(), partialTicks);
                                float renderYaw = this.interpolateRotation(scaffold.getYaw(), scaffold.getYaw(), partialTicks) - f;
                                float renderPitch = this.interpolateRotation(scaffold.getPitch(), scaffold.getPitch(), partialTicks);
                                f2 = renderYaw;
                                f8 = renderPitch;
                            }
                        }

                    } else if (Novoline.getInstance().getModuleManager().getModule(KillAura.class).isEnabled()) {
                        KillAura killAura = Novoline.getInstance().getModuleManager().getModule(KillAura.class);

                        if (killAura.shouldAttack()) {
                            f = this.interpolateRotation(killAura.getIYaw(), killAura.getIYaw(), partialTicks);
                            float renderYaw = this.interpolateRotation(killAura.getPrevIYaw(), killAura.getIYaw(), partialTicks) - f;
                            float renderPitch = this.interpolateRotation(killAura.getPrevIPitch(), killAura.getIPitch(), partialTicks);
                            f2 = renderYaw;
                            f8 = renderPitch;
                        }
                    }
                }


                renderLivingAt(entity, x, y, z);
                float f7 = this.handleRotationFloat(entity, partialTicks);
                rotateCorpse(entity, f7, f, partialTicks);
                GlStateManager.enableRescaleNormal();
                GlStateManager.scale(-1.0F, -1.0F, 1.0F);
                preRenderCallback(entity, partialTicks);
                GlStateManager.translate(0.0F, -1.5078125F, 0.0F);

                float f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                float f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
                if (entity.isChild()) f6 *= 3.0F;

                if (f5 > 1.0F) f5 = 1.0F;

                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
                this.mainModel.setRotationAngles(f6, f5, f7, f2, f8, 0.0625F, entity);

                if (this.renderOutlines) {
                    boolean flag1 = setScoreTeamColor(entity);
                    renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);

                    if (flag1) unsetScoreTeamColor();
                } else {
                    boolean flag = setDoRenderBrightness(entity, partialTicks);
                    renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);

                    if (flag) unsetBrightness();

                    GlStateManager.depthMask(true);

                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                        final boolean flag_2 = this.setBrightness(entity, partialTicks, true);
                        renderLayers(entity, f6, f5, partialTicks, f7, f2, f8, 0.0625F);
                        if (flag_2) {
                            this.unsetBrightness();
                        }
                    }
                }

                GlStateManager.disableRescaleNormal();
            } catch (Exception exception) {
                LOGGER.error("Couldn't render entity", exception);
            }

            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();

            if (!this.renderOutlines) super.doRender(entity, x, y, z, entityYaw, partialTicks);
            if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, entity, this, x, y, z);
            }
        }
        EventManager.call(new RenderEntityEvent(entity, RenderEntityEvent.State.POST));
    }

    protected boolean setScoreTeamColor(EntityLivingBase entityLivingBaseIn) {
        int i = 16777215;

        if (entityLivingBaseIn instanceof EntityPlayer) {
            final ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) entityLivingBaseIn.getTeam();

            if (scoreplayerteam != null) {
                final String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());

                if (s.length() >= 2) {
                    i = this.getFontRendererFromRenderManager().getColorCode(s.charAt(1));
                }
            }
        }

        final float f1 = (float) (i >> 16 & 255) / 255.0F;
        final float f2 = (float) (i >> 8 & 255) / 255.0F;
        final float f = (float) (i & 255) / 255.0F;

        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(f1, f2, f, 1.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        return true;
    }

    protected void unsetScoreTeamColor() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(T entityLivingBase, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
        final Novoline novoline = Novoline.getInstance();
        final ModuleManager moduleManager = novoline.getModuleManager();
        final ESP esp = moduleManager.getModule(ESP.class);
        final Chams chams = moduleManager.getModule(Chams.class);
        final float[] rgba = RenderUtils.getRGBAs(RenderUtils.getRainbow(6_000, -15));

        final Minecraft mc = Minecraft.getInstance();

        final boolean flag = !entityLivingBase.isInvisible(), //
                flag1 = !flag && !entityLivingBase.isInvisibleToPlayer(mc.player);

        if (flag || flag1) {
            if (!bindEntityTexture(entityLivingBase)) return;

            if (flag1) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }

            if (esp.isEnabled()) {
                GL11.glPushMatrix();
                GlStateManager.depthMask(true);

                if (esp.isValid(entityLivingBase) && mc.world != null) {
                    final Color espColor = esp.color.getAwtColor();
                    final String playerColor = esp.outlineColor.get(), // @off
                            formattedText = entityLivingBase.getDisplayName().getFormattedText();
                    final boolean b0 = !playerColor.equalsIgnoreCase("Team"),
                            b1 = playerColor.equalsIgnoreCase("Rainbow");
                    final float red = 0.003921568627451F * espColor.getRed(),
                            blue = 0.003921568627451F * espColor.getBlue(),
                            green = 0.003921568627451F * espColor.getGreen(),
                            alpha = 0.003921568627451F * esp.alpha.get(); // @on

                    setupColor(entityLivingBase, rgba, red, blue, green, alpha, b0, b1, formattedText);

                    if (!chams.isValid(entityLivingBase)) {
                        this.mainModel.render(entityLivingBase, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
                    }

                    OutlineUtils.renderOne();

                    setupColor(entityLivingBase, rgba, red, blue, green, alpha, b0, b1, formattedText);

                    this.mainModel.render(entityLivingBase, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

                    OutlineUtils.renderTwo();

                    setupColor(entityLivingBase, rgba, red, blue, green, alpha, b0, b1, formattedText);

                    this.mainModel.render(entityLivingBase, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

                    setupColor(entityLivingBase, rgba, red, blue, green, alpha, b0, b1, formattedText);

                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour();

                    setupColor(entityLivingBase, rgba, red, blue, green, alpha, b0, b1, formattedText);

                    this.mainModel.render(entityLivingBase, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
                    OutlineUtils.renderFive();
                }

                GL11.glColor4f(1, 1, 1, 1);
                GL11.glPopMatrix();
            }

            if (chams.isEnabled() && chams.isValid(entityLivingBase) && chams.isColored().get()) {
                int sexyHidden = chams.getHidden().getAwtColor().getRGB();

                GL11.glPushAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
                GlStateManager.enableBlend();
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting(!chams.isMaterial().get());
                GL11.glColor4f((sexyHidden >> 16 & 0xFF) / 255.0F, (sexyHidden >> 8 & 0xFF) / 255.0F,
                        (sexyHidden & 0xFF) / 255.0F, Math.max(30 / 255F, chams.getVisibleAlpha() / 255F));

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
                GlStateManager.disableDepth();
                this.mainModel.render(entityLivingBase, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
                GlStateManager.enableDepth();
                sexyHidden = chams.getVisible().getAwtColor().getRGB();
                GL11.glColor4f((sexyHidden >> 16 & 0xFF) / 255.0F, (sexyHidden >> 8 & 0xFF) / 255.0F,
                        (sexyHidden & 0xFF) / 255.0F, Math.max(30 / 255F, chams.getVisibleAlpha() / 255F));
                this.mainModel.render(entityLivingBase, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

                GlStateManager.enableLighting(!chams.isMaterial().get());
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GL11.glPopAttrib();
            } else {
                this.mainModel.render(entityLivingBase, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            }


            if (flag1) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }

    private void setupColorChams(float vRed, float vBlue, float vGreen, float cAlpha, float[] rgba, boolean chamsRainbow, boolean friend) {
        if (friend) {
            GL11.glColor4f(0.12F, 0.5F, 1, cAlpha);
        } else if (chamsRainbow) {
            GL11.glColor4f(rgba[0], rgba[1], rgba[2], cAlpha);
        } else {
            GL11.glColor4f(vRed, vGreen, vBlue, cAlpha);
        }
    }

    private void setupColor(T entityLivingBase, float[] rgba, float red, float blue, float green, float alpha, boolean b0, boolean b1, String formattedText) {
        final boolean friend = Novoline.getInstance().getPlayerManager().hasType(entityLivingBase.getName(), PlayerManager.EnumPlayerType.FRIEND);

        if (b0) {
            setupColorChams(red, blue, green, alpha, rgba, b1, friend);
        } else if (friend) {
            GL11.glColor4f(0, 1, 1, 1);
        } else if (formattedText.startsWith("§c")) {
            GL11.glColor4f(1, 0, 0, 1);
        } else if (formattedText.startsWith("§8")) {
            GL11.glColor4f(0.4F, 0.4F, 0.4F, 1);
        } else if (formattedText.startsWith("§b")) {
            GL11.glColor4f(0.4F, 1, 1, 1);
        } else if (formattedText.startsWith("§6")) {
            GL11.glColor4f(1, 0.78F, 0, 1);
        } else if (formattedText.startsWith("§a")) {
            GL11.glColor4f(0.35F, 1, 0.35F, 1);
        } else if (formattedText.startsWith("§0")) {
            GL11.glColor4f(0, 0, 0, 1);
        } else if (formattedText.startsWith("§1")) {
            GL11.glColor4f(0, 0, 0.78F, 1);
        } else if (formattedText.startsWith("§7")) {
            GL11.glColor4f(0.78F, 0.78F, 0.78F, 1);
        } else if (formattedText.startsWith("§e")) {
            GL11.glColor4f(1, 1, 0.35F, 1);
        } else if (formattedText.startsWith("§f")) {
            GL11.glColor4f(1, 1, 1, 1);
        } else if (formattedText.startsWith("§d")) {
            GL11.glColor4f(1, 0.35F, 1, 1);
        }
    }

    private boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
        return this.setBrightness(entityLivingBaseIn, partialTicks, true);
    }

    private boolean setBrightness(T entityLivingBase, float partialTicks, boolean combineTextures) {
        final float f = entityLivingBase.getBrightness(partialTicks);
        final int i = this.getColorMultiplier(entityLivingBase, f, partialTicks);
        final boolean flag = (i >> 24 & 255) > 0;
        final boolean flag1 = entityLivingBase.hurtTime > 0 || entityLivingBase.deathTime > 0;

        if (!flag && !flag1) {
            return false;
        } else if (!flag && !combineTextures) {
            return false;
        } else {
            startBrightness(GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND2_RGB, GL11.GL_SRC_ALPHA);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            this.brightnessBuffer.position(0);

            if (flag1) {
                this.brightnessBuffer.put(1.0F);
                this.brightnessBuffer.put(0.0F);
                this.brightnessBuffer.put(0.0F);
                this.brightnessBuffer.put(0.3F);

                if (Config.isShaders()) {
                    Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F);
                }
            } else {
                final float f1 = (float) (i >> 24 & 255) / 255.0F;
                final float f2 = (float) (i >> 16 & 255) / 255.0F;
                final float f3 = (float) (i >> 8 & 255) / 255.0F;
                final float f4 = (float) (i & 255) / 255.0F;

                this.brightnessBuffer.put(f2);
                this.brightnessBuffer.put(f3);
                this.brightnessBuffer.put(f4);
                this.brightnessBuffer.put(1.0F - f1);

                if (Config.isShaders()) Shaders.setEntityColor(f2, f3, f4, 1.0F - f1);
            }

            this.brightnessBuffer.flip();
            GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, this.brightnessBuffer);
            GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(field_177096_e.getGlTextureId());
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            return true;
        }
    }

    private void unsetBrightness() {
        startBrightness(GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_ALPHA, GL11.GL_SRC_ALPHA);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        unsetBrightness0();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.disableTexture2D();
        GlStateManager.bindTexture(0);
        unsetBrightness0();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    private void startBrightness(int glReplace) {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, glReplace);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
    }

    private void unsetBrightness0() {
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
        GlStateManager.translate((float) x, (float) y, (float) z);
    }

    protected void rotateCorpse(T bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

        if (bat.deathTime > 0) {
            float f = ((float) bat.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt_float(f);

            if (f > 1.0F) {
                f = 1.0F;
            }

            GlStateManager.rotate(f * this.getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
        } else {
            final String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());

            if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0F, bat.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    /**
     * Returns where in the swing animation the living entity is (from 0 to 1).  Args : entity, partialTickTime
     */
    protected float getSwingProgress(T livingBase, float partialTickTime) {
        return livingBase.getSwingProgress(partialTickTime);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(T livingBase, float partialTicks) {
        return (float) livingBase.ticksExisted + partialTicks;
    }

    protected void renderLayers(T entityLivingBase,
                                float p_177093_2_,
                                float p_177093_3_,
                                float partialTicks,
                                float p_177093_5_,
                                float p_177093_6_,
                                float p_177093_7_,
                                float p_177093_8_) {
        for (LayerRenderer<T> layerRenderer : this.layerRenderers) {
            final boolean flag = this.setBrightness(entityLivingBase, partialTicks, layerRenderer.shouldCombineTextures());
            layerRenderer.doRenderLayer(entityLivingBase, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);

            if (flag) unsetBrightness();
        }
    }

    protected float getDeathMaxRotation(T entityLivingBase) {
        return 90.0F;
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(T entityLivingBase, float lightBrightness, float partialTickTime) {
        return 0;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(T entityLivingBase, float partialTickTime) {
    }

    public void renderName(T entity, double x, double y, double z) {
        final RenderNameTagEvent renderNameTagEvent = new RenderNameTagEvent(entity);
        EventManager.call(renderNameTagEvent);

        if (renderNameTagEvent.isCancelled()) return;

        if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, entity, this, x, y, z)) {
            if (canRenderName(entity)) {
                final double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
                final float f = entity.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

                if (d0 < (double) (f * f)) {
                    final String s = entity.getDisplayName().getFormattedText();
                    GlStateManager.alphaFunc(516, 0.1F);

                    if (entity.isSneaking()) {
                        final FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((float) x, (float) y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float) z);
                        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                        GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
                        GlStateManager.translate(0.0F, 9.374999F, 0.0F);
                        GlStateManager.disableLighting();
                        GlStateManager.depthMask(false);
                        GlStateManager.enableBlend();
                        GlStateManager.disableTexture2D();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        final int i = fontrenderer.getStringWidth(s) / 2;
                        final Tessellator tessellator = Tessellator.getInstance();
                        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                        worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                        worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                        worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                        worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                        tessellator.draw();
                        GlStateManager.enableTexture2D();
                        GlStateManager.depthMask(true);
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                        GlStateManager.enableLighting();
                        GlStateManager.disableBlend();
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        GlStateManager.popMatrix();
                    } else {
                        this.renderOffsetLivingLabel(entity, x, y - (entity.isChild() ? (double) (entity.height / 2.0F) : 0.0D), z, s, 0.02666667F, d0);
                    }
                }
            }

            if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, entity, this, x, y, z);
            }
        }
    }

    protected boolean canRenderName(T entity) {
        final EntityPlayerSP thePlayer = Minecraft.getInstance().player;

        if (entity instanceof EntityPlayer && entity != thePlayer) {
            final Team team = entity.getTeam();

            if (team != null) {
                final Team.EnumVisible visibility = team.getNameTagVisibility();
                final Team team1 = thePlayer.getTeam();

                switch (visibility.ordinal()) { // @off
                    case 0:
                        return true;
                    case 1:
                        return false;
                    case 2:
                        return team1 == null || team.isSameTeam(team1);
                    case 3:
                        return team1 == null || !team.isSameTeam(team1);
                    default:
                        return true;
                } // @on
            }
        }

        return Minecraft.isGuiEnabled() && entity != this.renderManager.livingPlayer && !entity.isInvisibleToPlayer(thePlayer) && entity.riddenByEntity == null;
    }

    public void setRenderOutlines(boolean renderOutlinesIn) {
        this.renderOutlines = renderOutlinesIn;
    }

    public ModelBase getMainModel() {
        return this.mainModel;
    }

}
