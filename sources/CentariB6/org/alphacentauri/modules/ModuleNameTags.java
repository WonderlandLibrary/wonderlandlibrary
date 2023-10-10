package org.alphacentauri.modules;

import java.util.ArrayList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.MathUtils;
import org.lwjgl.opengl.GL11;

public class ModuleNameTags extends Module implements EventListener {
   private Property ShowHealth = new Property(this, "ShowHealth", Boolean.valueOf(true));
   private Property ShowPercent = new Property(this, "ShowPercent", Boolean.valueOf(false));
   private Property drawOutline = new Property(this, "DrawOutline", Boolean.valueOf(true));
   private Property showArmor = new Property(this, "ShowArmor", Boolean.valueOf(true));
   private Property inv = new Property(this, "Invisibles", Boolean.valueOf(false));
   private Property scale = new Property(this, "Scale", Float.valueOf(2.0F));
   private Property alpha = new Property(this, "Alpha", Float.valueOf(0.25F));
   private ArrayList rendered = new ArrayList();

   public ModuleNameTags() {
      super("NameTags", "Better Nametags", new String[]{"nametags"}, Module.Category.Render, 6954124);
   }

   public static void renderItem(Entity entityIn, ItemStack item, int x, int z) {
      GL11.glPushMatrix();
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      IBakedModel ibakedmodel = AC.getMC().getRenderItem().getItemModelMesher().getItemModel(item);
      AC.getMC().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      GL11.glTranslatef((float)x, -20.0F, (float)z);
      GlStateManager.translate(8.0F, 8.0F, 0.0F);
      GlStateManager.scale(0.5F, 0.5F, 0.0F);
      GlStateManager.scale(64.0F, 64.0F, 64.0F);
      GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.disableLighting();
      ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
      GlStateManager.pushMatrix();
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      if(ibakedmodel.isBuiltInRenderer()) {
         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(-0.5F, -0.5F, -0.5F);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableRescaleNormal();
         TileEntityItemStackRenderer.instance.renderByItem(item);
      } else {
         GlStateManager.translate(-0.5F, -0.5F, -0.5F);
         AC.getMC().getRenderItem().renderModel(ibakedmodel, item);
      }

      GlStateManager.popMatrix();
      GlStateManager.enableAlpha();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableLighting();
      GlStateManager.popMatrix();
   }

   public static String getHealthColor(float percent) {
      return (double)percent >= 0.9D?"§a":((double)percent >= 0.75D?"§2":((double)percent >= 0.5D?"§e":((double)percent >= 0.25D?"§6":((double)percent >= 0.15D?"§c":"§4"))));
   }

   public void renderNameTag(EntityPlayer entityIn, String name, double x, double y, double z) {
      if(!this.rendered.contains(name)) {
         this.rendered.add(name);
         if(!entityIn.isInvisible() || ((Boolean)this.inv.value).booleanValue()) {
            FontRenderer fontrenderer = AC.getMC().fontRendererObj;
            RenderManager renderManager = AC.getMC().getRenderManager();
            double dist = entityIn.getDistanceSqToEntity(renderManager.livingPlayer);
            float f = (float)Math.sqrt((double)((Float)this.scale.value).floatValue() * Math.sqrt(dist));
            float f1 = 0.016666668F * f;
            float r = 0.0F;
            float g = 0.0F;
            float b = 0.0F;
            if(entityIn.isSneaking()) {
               if(AC.getFriendManager().isFriend(entityIn.getName())) {
                  g = 1.0F;
                  b = 1.0F;
               }

               name = (AC.getFriendManager().isFriend(entityIn.getName())?"§4":"§5") + name;
            } else if(AC.getFriendManager().isFriend(entityIn.getName())) {
               g = 1.0F;
               b = 1.0F;
               name = "§a" + name;
            }

            if(((Boolean)this.ShowHealth.value).booleanValue()) {
               name = name + " " + getHealthColor(entityIn.getHealth() / entityIn.getMaxHealth()) + (((Boolean)this.ShowPercent.value).booleanValue()?(int)((entityIn.getHealth() + entityIn.getAbsorptionAmount()) / entityIn.getMaxHealth() * 100.0F) + "%":MathUtils.round((double)((entityIn.getHealth() + entityIn.getAbsorptionAmount()) / 2.0F), 1) + "❤");
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0F, (float)y + entityIn.height + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = fontrenderer.getStringWidth(name) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double)(-i - 1), -1.0D, 0.0D).color(r, g, b, ((Float)this.alpha.value).floatValue()).endVertex();
            worldrenderer.pos((double)(-i - 1), 8.0D, 0.0D).color(r, g, b, ((Float)this.alpha.value).floatValue()).endVertex();
            worldrenderer.pos((double)(i + 1), 8.0D, 0.0D).color(r, g, b, ((Float)this.alpha.value).floatValue()).endVertex();
            worldrenderer.pos((double)(i + 1), -1.0D, 0.0D).color(r, g, b, ((Float)this.alpha.value).floatValue()).endVertex();
            tessellator.draw();
            if(((Boolean)this.drawOutline.value).booleanValue()) {
               worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
               worldrenderer.pos((double)(-i) - 1.5D, -1.5D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)(-i) - 1.5D, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)i + 1.5D, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)i + 1.5D, -1.5D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               tessellator.draw();
               worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
               worldrenderer.pos((double)(-i) - 1.5D, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)(-i) - 1.5D, 8.5D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)i + 1.5D, 8.5D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)i + 1.5D, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               tessellator.draw();
               worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
               worldrenderer.pos((double)(-i) - 1.5D, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)(-i) - 1.5D, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)(-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)(-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               tessellator.draw();
               worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
               worldrenderer.pos((double)(i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)(i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)i + 1.5D, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               worldrenderer.pos((double)i + 1.5D, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
               tessellator.draw();
            }

            GlStateManager.enableTexture2D();
            fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, 0, -1);
            if(((Boolean)this.showArmor.value).booleanValue()) {
               for(int a = 0; a < 4; ++a) {
                  if(entityIn.getCurrentArmor(a) != null) {
                     renderItem(entityIn, entityIn.getCurrentArmor(a), 24 - a * 16, 0);
                  }
               }

               if(entityIn.getCurrentEquippedItem() != null) {
                  renderItem(entityIn, entityIn.getCurrentEquippedItem(), -40, 0);
               }
            }

            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
         }
      }
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         this.rendered.clear();
      }

   }
}
