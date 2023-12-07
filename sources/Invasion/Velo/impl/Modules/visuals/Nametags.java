package Velo.impl.Modules.visuals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Velo.api.Module.Module;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.Util.fontRenderer.Utils.Tff;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventRenderNameTag;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;


@SuppressWarnings("rawtypes")
public class Nametags extends Module {


    BooleanSetting healthBar = new BooleanSetting("Health Bar", true),
            armor = new BooleanSetting("Armor", true),
            background = new BooleanSetting("Background", true);
	public Fonts font1 = new Fonts();
	
    NumberSetting scale = new NumberSetting("Scale", 5, 3, 8, 0.2);
    public Nametags() {
        super("Nametags", "Nametags", Keyboard.KEY_NONE, Category.VISUALS);
        this.loadSettings(background, scale, healthBar, armor);
    }

    
    @Override
    public void onEnable() {
    	Velo.api.Util.fontRenderer.Utils.DrawFontUtil.loadFonts();
		Fonts.loadFonts();
    }
    public static void renderItem(ItemStack stack, int x, int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = -100.0f;
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y + 8);
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0f;
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }

    
    @Override
    public void onRenderNametag(EventRenderNameTag event) {
    	
    	event.isCancelled = true;
    	// TODO Auto-generated method stub
    	super.onRenderNametag(event);
    }
    
    @Override
    public void onRender3DUpdate(EventRender3D e) {
    	  for (EntityPlayer entity : mc.theWorld.playerEntities) {

              if (entity.isInvisible() || entity == mc.thePlayer)
                  continue;

              GL11.glPushMatrix();
Tff fr = font1.nametags;

              double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
              double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
              double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
              //float distance = mc.thePlayer.getDistanceToEntity(entity);


              GL11.glTranslated(x, y + entity.getEyeHeight() + 1.7, z);
            //  GL11.glNormal3f(0, 1, 0);
              if (mc.gameSettings.thirdPersonView == 2) {
                  GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
                  GlStateManager.rotate(-mc.getRenderManager().playerViewX, 1, 0, 0);
              } else {
                  GlStateManager.rotate(-mc.thePlayer.rotationYaw, 0, 1, 0);
                  GlStateManager.rotate(mc.thePlayer.rotationPitch, 1, 0, 0);
              }
              float distance = mc.thePlayer.getDistanceToEntity(entity),
                      scaleConst_1 = 0.02672f, scaleConst_2 = 0.10f;
              double maxDist = 7.0;


              float scaleFactor = (float) (distance <= maxDist ? maxDist * scaleConst_2 : (double) (distance * scaleConst_2));
              scaleConst_1 *= scaleFactor;

              float scaleBet = (float) (scale.getValue() * 10E-3);
              scaleConst_1 = Math.min(scaleBet, scaleConst_1);


              GL11.glScalef(-scaleConst_1, -scaleConst_1, .2f);

              GlStateManager.disableLighting();
              GlStateManager.depthMask(false);
              GL11.glDisable(GL11.GL_DEPTH_TEST);


              String colorCode = entity.getHealth() > 15 ? "\247a" : entity.getHealth() > 10 ? "\247e" : entity.getHealth() > 7 ? "\2476" : "\247c";
              int colorrectCode = entity.getHealth() > 15 ? 0xff4DF75B : entity.getHealth() > 10 ? 0xffF1F74D : entity.getHealth() > 7 ? 0xffF7854D : 0xffF7524D;
              String thing = entity.getName() + " " + colorCode + (int) entity.getHealth();
              float namewidth = (float) Minecraft.getMinecraft().fontRendererObj.getStringWidth(thing);


              Gui.drawRect(-namewidth / 2 - 2, 42, namewidth / 2 + 2, 40, 0x90080808);


              if (healthBar.isEnabled())
                  Gui.drawRect(-namewidth / 2 - 15, 42, namewidth / 2 + 15 - (1 - (entity.getHealth() / entity.getMaxHealth())) * (namewidth + 4), 40, colorrectCode);

              if (background.isEnabled())
                  Gui.drawRect(-namewidth / 2 - 15, 20, namewidth / 2 + 15, 40, 0x90202020);


             Minecraft.getMinecraft().fontRendererObj.drawCenteredString(entity.getName(), -20, 23, -1);
           //   fr.drawCenteredString(colorCode + (int) entity.getHealth(), namewidth / 2, 23, -1);

              GlStateManager.disableBlend();
              GlStateManager.depthMask(true);
              GL11.glEnable(GL11.GL_DEPTH_TEST);


              double movingArmor = 1.2;

              if (namewidth <= 65) {
                  movingArmor = 2;
              }
              if (namewidth <= 85) {
                  movingArmor = 1.2;
              }

              if (namewidth <= 100) {
                  movingArmor = 1.1;
              }

              if (armor.isEnabled()) {
                  for (int index = 0; index < 5; index++) {

                      if (entity.getEquipmentInSlot(index) == null)
                          continue;


                     renderItem(entity.getEquipmentInSlot(index), (int) (index * 19 / movingArmor) - 30, -10);


                  }
              }

              GL11.glPopMatrix();

          }
      

  
    	// TODO Auto-generated method stub
    	super.onRender3DUpdate(e);
    }
 
      

}

