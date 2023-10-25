package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.nio.FloatBuffer;
import java.util.List;
import me.rhys.base.Lite;
import me.rhys.base.event.Event;
import me.rhys.base.event.impl.render.RenderEntityEvent;
import me.rhys.base.event.impl.render.RenderNameTagEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import optifine.Config;
import optifine.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

public abstract class RendererLivingEntity<T extends EntityLivingBase> extends Render<T> {
  private static final Logger logger = LogManager.getLogger();
  
  private static final DynamicTexture field_177096_e = new DynamicTexture(16, 16);
  
  protected ModelBase mainModel;
  
  protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
  
  protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
  
  protected boolean renderOutlines = false;
  
  private static final String __OBFID = "CL_00001012";
  
  public static float NAME_TAG_RANGE = 64.0F;
  
  public static float NAME_TAG_RANGE_SNEAK = 32.0F;
  
  public RendererLivingEntity(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
    super(renderManagerIn);
    this.mainModel = modelBaseIn;
    this.shadowSize = shadowSizeIn;
  }
  
  public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
    return this.layerRenderers.add((LayerRenderer<T>)layer);
  }
  
  protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(U layer) {
    return this.layerRenderers.remove(layer);
  }
  
  public ModelBase getMainModel() {
    return this.mainModel;
  }
  
  protected float interpolateRotation(float par1, float par2, float par3) {
    float f;
    for (f = par2 - par1; f < -180.0F; f += 360.0F);
    while (f >= 180.0F)
      f -= 360.0F; 
    return par1 + par3 * f;
  }
  
  public void transformHeldFull3DItemLayer() {}
  
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) })) {
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();
      this.mainModel.swingProgress = getSwingProgress(entity, partialTicks);
      this.mainModel.isRiding = entity.isRiding();
      if (Reflector.ForgeEntity_shouldRiderSit.exists())
        this.mainModel.isRiding = (entity.isRiding() && ((EntityLivingBase)entity).ridingEntity != null && Reflector.callBoolean(((EntityLivingBase)entity).ridingEntity, Reflector.ForgeEntity_shouldRiderSit, new Object[0])); 
      this.mainModel.isChild = entity.isChild();
      try {
        float f = interpolateRotation(((EntityLivingBase)entity).prevRenderYawOffset, ((EntityLivingBase)entity).renderYawOffset, partialTicks);
        float f1 = interpolateRotation(((EntityLivingBase)entity).prevRotationYawHead, ((EntityLivingBase)entity).rotationYawHead, partialTicks);
        float f2 = f1 - f;
        if (this.mainModel.isRiding && ((EntityLivingBase)entity).ridingEntity instanceof EntityLivingBase) {
          EntityLivingBase entitylivingbase = (EntityLivingBase)((EntityLivingBase)entity).ridingEntity;
          f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
          f2 = f1 - f;
          float f3 = MathHelper.wrapAngleTo180_float(f2);
          if (f3 < -85.0F)
            f3 = -85.0F; 
          if (f3 >= 85.0F)
            f3 = 85.0F; 
          f = f1 - f3;
          if (f3 * f3 > 2500.0F)
            f += f3 * 0.2F; 
        } 
        float f8 = ((EntityLivingBase)entity).prevRotationPitch + (((EntityLivingBase)entity).rotationPitch - ((EntityLivingBase)entity).prevRotationPitch) * partialTicks;
        renderLivingAt(entity, x, y, z);
        float f7 = handleRotationFloat(entity, partialTicks);
        rotateCorpse(entity, f7, f, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        preRenderCallback(entity, partialTicks);
        float f4 = 0.0625F;
        GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
        float f5 = ((EntityLivingBase)entity).prevLimbSwingAmount + (((EntityLivingBase)entity).limbSwingAmount - ((EntityLivingBase)entity).prevLimbSwingAmount) * partialTicks;
        float f6 = ((EntityLivingBase)entity).limbSwing - ((EntityLivingBase)entity).limbSwingAmount * (1.0F - partialTicks);
        if (entity == (Minecraft.getMinecraft()).thePlayer) {
          f8 = ((EntityLivingBase)entity).prevRenderPitchRotation + (((EntityLivingBase)entity).renderPitchRotation - ((EntityLivingBase)entity).prevRenderPitchRotation) * partialTicks;
        } else {
          f8 = ((EntityLivingBase)entity).prevRotationPitch + (((EntityLivingBase)entity).rotationPitch - ((EntityLivingBase)entity).prevRotationPitch) * partialTicks;
        } 
        if (entity.isChild())
          f6 *= 3.0F; 
        if (f5 > 1.0F)
          f5 = 1.0F; 
        GlStateManager.enableAlpha();
        this.mainModel.setLivingAnimations((EntityLivingBase)entity, f6, f5, partialTicks);
        this.mainModel.setRotationAngles(f6, f5, f7, f2, f8, 0.0625F, (Entity)entity);
        if (this.renderOutlines) {
          boolean flag1 = setScoreTeamColor((EntityLivingBase)entity);
          renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
          if (flag1)
            unsetScoreTeamColor(); 
        } else {
          boolean flag = setDoRenderBrightness(entity, partialTicks);
          renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
          if (flag)
            unsetBrightness(); 
          GlStateManager.depthMask(true);
          if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator())
            renderLayers(entity, f6, f5, partialTicks, f7, f2, f8, 0.0625F); 
        } 
        GlStateManager.disableRescaleNormal();
      } catch (Exception exception) {
        logger.error("Couldn't render entity", exception);
      } 
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableCull();
      GlStateManager.popMatrix();
      if (!this.renderOutlines)
        super.doRender(entity, x, y, z, entityYaw, partialTicks); 
      if (!Reflector.RenderLivingEvent_Post_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) }));
    } 
  }
  
  protected boolean setScoreTeamColor(EntityLivingBase entityLivingBaseIn) {
    int i = 16777215;
    if (entityLivingBaseIn instanceof EntityPlayer) {
      ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entityLivingBaseIn.getTeam();
      if (scoreplayerteam != null) {
        String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
        if (s.length() >= 2)
          i = getFontRendererFromRenderManager().getColorCode(s.charAt(1)); 
      } 
    } 
    float f1 = (i >> 16 & 0xFF) / 255.0F;
    float f2 = (i >> 8 & 0xFF) / 255.0F;
    float f = (i & 0xFF) / 255.0F;
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
  
  protected void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
    boolean flag = !entitylivingbaseIn.isInvisible();
    boolean flag1 = (!flag && !entitylivingbaseIn.isInvisibleToPlayer((EntityPlayer)(Minecraft.getMinecraft()).thePlayer));
    if (flag || flag1) {
      if (!bindEntityTexture(entitylivingbaseIn))
        return; 
      if (flag1) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569F);
      } 
      Lite.EVENT_BUS.call((new RenderEntityEvent(p_77036_2_, p_77036_3_, p_77036_4_, (EntityLivingBase)entitylivingbaseIn, false)).setType(Event.Type.PRE));
      this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
      Lite.EVENT_BUS.call((new RenderEntityEvent(p_77036_2_, p_77036_3_, p_77036_4_, (EntityLivingBase)entitylivingbaseIn, false)).setType(Event.Type.POST));
      if (flag1) {
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
      } 
    } 
  }
  
  protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
    return setBrightness(entityLivingBaseIn, partialTicks, true);
  }
  
  protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
    float f = entitylivingbaseIn.getBrightness(partialTicks);
    int i = getColorMultiplier(entitylivingbaseIn, f, partialTicks);
    boolean flag = ((i >> 24 & 0xFF) > 0);
    boolean flag1 = (((EntityLivingBase)entitylivingbaseIn).hurtTime > 0 || ((EntityLivingBase)entitylivingbaseIn).deathTime > 0);
    if (!flag && !flag1)
      return false; 
    if (!flag && !combineTextures)
      return false; 
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.enableTexture2D();
    GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.enableTexture2D();
    GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    this.brightnessBuffer.position(0);
    if (flag1) {
      this.brightnessBuffer.put(1.0F);
      this.brightnessBuffer.put(0.0F);
      this.brightnessBuffer.put(0.0F);
      this.brightnessBuffer.put(0.3F);
      if (Config.isShaders())
        Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F); 
    } else {
      float f1 = (i >> 24 & 0xFF) / 255.0F;
      float f2 = (i >> 16 & 0xFF) / 255.0F;
      float f3 = (i >> 8 & 0xFF) / 255.0F;
      float f4 = (i & 0xFF) / 255.0F;
      this.brightnessBuffer.put(f2);
      this.brightnessBuffer.put(f3);
      this.brightnessBuffer.put(f4);
      this.brightnessBuffer.put(1.0F - f1);
      if (Config.isShaders())
        Shaders.setEntityColor(f2, f3, f4, 1.0F - f1); 
    } 
    this.brightnessBuffer.flip();
    GL11.glTexEnv(8960, 8705, this.brightnessBuffer);
    GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
    GlStateManager.enableTexture2D();
    GlStateManager.bindTexture(field_177096_e.getGlTextureId());
    GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    return true;
  }
  
  protected void unsetBrightness() {
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.enableTexture2D();
    GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
    GlStateManager.disableTexture2D();
    GlStateManager.bindTexture(0);
    GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    if (Config.isShaders())
      Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F); 
  }
  
  protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
    GlStateManager.translate((float)x, (float)y, (float)z);
  }
  
  protected void rotateCorpse(T bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
    GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
    if (((EntityLivingBase)bat).deathTime > 0) {
      float f = (((EntityLivingBase)bat).deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
      f = MathHelper.sqrt_float(f);
      if (f > 1.0F)
        f = 1.0F; 
      GlStateManager.rotate(f * getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
    } else {
      String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());
      if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer)bat).isWearing(EnumPlayerModelParts.CAPE))) {
        GlStateManager.translate(0.0F, ((EntityLivingBase)bat).height + 0.1F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      } 
    } 
  }
  
  protected float getSwingProgress(T livingBase, float partialTickTime) {
    return livingBase.getSwingProgress(partialTickTime);
  }
  
  protected float handleRotationFloat(T livingBase, float partialTicks) {
    return ((EntityLivingBase)livingBase).ticksExisted + partialTicks;
  }
  
  protected void renderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
    Lite.EVENT_BUS.call((new RenderEntityEvent(p_177093_2_, p_177093_3_, p_177093_5_, (EntityLivingBase)entitylivingbaseIn, true)).setType(Event.Type.PRE));
    for (LayerRenderer<T> layerrenderer : this.layerRenderers) {
      boolean flag = setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
      layerrenderer.doRenderLayer((EntityLivingBase)entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
      if (flag)
        unsetBrightness(); 
    } 
    Lite.EVENT_BUS.call((new RenderEntityEvent(p_177093_2_, p_177093_3_, p_177093_5_, (EntityLivingBase)entitylivingbaseIn, true)).setType(Event.Type.POST));
  }
  
  protected float getDeathMaxRotation(T entityLivingBaseIn) {
    return 90.0F;
  }
  
  protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
    return 0;
  }
  
  protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {}
  
  public void renderName(T entity, double x, double y, double z) {
    if ((!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) })) && !((Event)Lite.EVENT_BUS.call((Event)new RenderNameTagEvent((EntityLivingBase)entity, entity.getDisplayName().getFormattedText(), x, y, z, 10))).isCancelled()) {
      if (canRenderName(entity)) {
        double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
        float f = entity.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
        if (d0 < (f * f)) {
          String s = entity.getDisplayName().getFormattedText();
          float f1 = 0.02666667F;
          GlStateManager.alphaFunc(516, 0.1F);
          if (entity.isSneaking()) {
            FontRenderer fontrenderer = getFontRendererFromRenderManager();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y + ((EntityLivingBase)entity).height + 0.5F - (entity.isChild() ? (((EntityLivingBase)entity).height / 2.0F) : 0.0F), (float)z);
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
            int i = fontrenderer.getStringWidth(s) / 2;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2), 0.0F, 553648127);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
          } else {
            renderOffsetLivingLabel(entity, x, y - (entity.isChild() ? (((EntityLivingBase)entity).height / 2.0F) : 0.0D), z, s, 0.02666667F, d0);
          } 
        } 
      } 
      if (!Reflector.RenderLivingEvent_Specials_Post_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) }));
    } 
  }
  
  protected boolean canRenderName(T entity) {
    EntityPlayerSP entityplayersp = (Minecraft.getMinecraft()).thePlayer;
    if (entity instanceof EntityPlayer && entity != entityplayersp) {
      Team team = entity.getTeam();
      Team team1 = entityplayersp.getTeam();
      if (team != null) {
        Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
        switch (RendererLivingEntity$1.field_178679_a[team$enumvisible.ordinal()]) {
          case 1:
            return true;
          case 2:
            return false;
          case 3:
            return (team1 == null || team.isSameTeam(team1));
          case 4:
            return (team1 == null || !team.isSameTeam(team1));
        } 
        return true;
      } 
    } 
    return (Minecraft.isGuiEnabled() && entity != this.renderManager.livingPlayer && !entity.isInvisibleToPlayer((EntityPlayer)entityplayersp) && ((EntityLivingBase)entity).riddenByEntity == null);
  }
  
  public void setRenderOutlines(boolean renderOutlinesIn) {
    this.renderOutlines = renderOutlinesIn;
  }
  
  static {
    int[] aint = field_177096_e.getTextureData();
    for (int i = 0; i < 256; i++)
      aint[i] = -1; 
    field_177096_e.updateDynamicTexture();
  }
  
  static final class RendererLivingEntity$1 {
    static final int[] field_178679_a = new int[(Team.EnumVisible.values()).length];
    
    private static final String __OBFID = "CL_00002435";
    
    static {
      try {
        field_178679_a[Team.EnumVisible.ALWAYS.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_178679_a[Team.EnumVisible.NEVER.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_178679_a[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_178679_a[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {}
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RendererLivingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */