package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.Event3D;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.people.EnemyManager;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.people.FriendManager.Friend;
import cow.milkgod.cheese.utils.R2DUtils;
import cow.milkgod.cheese.utils.R3DUtils;
import cow.milkgod.cheese.utils.Wrapper;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
   public static boolean health = true;
   public static boolean armour = true;
   public static boolean bars = false;

   public Nametags() {
      super("Nametags", 0, Category.RENDER, 13261338, true, "Better TTF Chat", new String[]{"tag", "ntags", "tags"});
   }

   @EventTarget(4)
   public void onRender3D(Event3D render) {
      List list = Wrapper.mc.theWorld.playerEntities;
      Iterator var4 = Wrapper.mc.theWorld.playerEntities.iterator();

      while(var4.hasNext()) {
         Object player = var4.next();
         if(((EntityPlayer)player).isEntityAlive() && !(player instanceof EntityPlayerSP)) {
            double x = ((EntityPlayer)player).lastTickPosX + (((EntityPlayer)player).posX - ((EntityPlayer)player).lastTickPosX) * (double)Event3D.getPartialTicks() - RenderManager.renderPosX;
            double y = ((EntityPlayer)player).lastTickPosY + (((EntityPlayer)player).posY - ((EntityPlayer)player).lastTickPosY) * (double)Event3D.getPartialTicks() - RenderManager.renderPosY;
            double z = ((EntityPlayer)player).lastTickPosZ + (((EntityPlayer)player).posZ - ((EntityPlayer)player).lastTickPosZ) * (double)Event3D.getPartialTicks() - RenderManager.renderPosZ;
            this.renderNametag((EntityPlayer)player, x, y, z);
         }
      }

   }

   public void renderNametag(EntityPlayer player, double x, double y, double z) {
      double size = (double)this.getSize(player) * -0.0225D;
      FontRenderer var13 = Wrapper.fr;
      GL11.glPushMatrix();
      R3DUtils.start3DOGLConstants();
      GL11.glTranslated((double)((float)x), (double)((float)y + player.height) + 0.5D, (double)((float)z));
      GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glScaled(size, size, size);
      Tessellator var14 = Tessellator.getInstance();
      WorldRenderer var15 = var14.getWorldRenderer();
      int var16 = health?var13.getStringWidth(String.valueOf(this.getPlayerName(player)) + " " + this.getHealth(player)) / 2:var13.getStringWidth(this.getPlayerName(player)) / 2;
      int bordercolor = 1879048192;
      int maincolor = 1879048192;
      if(FriendManager.isFriend(player.getName())) {
         bordercolor = 806202623;
      } else if(EnemyManager.isEnemy(player.getName())) {
         bordercolor = 820529726;
      }

      R2DUtils.drawBorderedRect((float)(-var16 - 2), (float)(-(Wrapper.mc.fontRendererObj.FONT_HEIGHT - 6)), (float)(var16 + 2), (float)((double)Wrapper.mc.fontRendererObj.FONT_HEIGHT + 0.5D), 1.0F, -1879048192, bordercolor);
      GL11.glDisable(2929);
      if(!health) {
         var13.drawStringWithShadow(this.getPlayerName(player), (float)var16, 0.0F, 15790320);
      } else if(health) {
         var13.drawStringWithShadow(this.getPlayerName(player), (float)(-var13.getStringWidth(String.valueOf(this.getPlayerName(player)) + " " + this.getHealth(player)) / 2), 0.0F, 15790320);
         var13.drawStringWithShadow(this.getHealth(player), (float)((var13.getStringWidth(String.valueOf(this.getPlayerName(player)) + " " + this.getHealth(player)) - var13.getStringWidth(this.getHealth(player)) * 2) / 2), 0.0F, this.getHealthColorHEX(player));
      }

      GL11.glEnable(2929);
      if(armour) {
         this.renderArmor(player);
      }

      R3DUtils.finish3DOGLConstants();
      GL11.glPopMatrix();
   }

   public void renderArmor(EntityPlayer player) {
      int xOffset = 0;
      ItemStack[] renderStack2 = player.inventory.armorInventory;
      int armourStack = player.inventory.armorInventory.length;

      ItemStack renderStack;
      int index;
      for(index = 0; index < armourStack; ++index) {
         renderStack = renderStack2[index];
         if(renderStack != null) {
            xOffset -= 8;
         }
      }

      if(player.getHeldItem() != null) {
         xOffset -= 8;
         renderStack = player.getHeldItem().copy();
         if(renderStack.hasEffect() && (renderStack.getItem() instanceof ItemTool || renderStack.getItem() instanceof ItemArmor)) {
            renderStack.stackSize = 1;
         }

         R3DUtils.renderItemStack(renderStack, xOffset, -19);
         xOffset += 16;
      }

      ItemStack[] var7 = player.inventory.armorInventory;

      for(index = 3; index >= 0; --index) {
         ItemStack var8 = var7[index];
         if(var8 != null) {
            R3DUtils.renderItemStack(var8, xOffset, -19);
            xOffset += 16;
         }
      }

   }

   private int getHealthColorHEX(EntityPlayer e) {
      int health = Math.round(20.0F * (e.getHealth() / e.getMaxHealth()));
      int color = -1;
      if(health >= 20) {
         color = 5030935;
      } else if(health >= 18) {
         color = 9108247;
      } else if(health >= 16) {
         color = 10026904;
      } else if(health >= 14) {
         color = 12844472;
      } else if(health >= 12) {
         color = 16633879;
      } else if(health >= 10) {
         color = 15313687;
      } else if(health >= 8) {
         color = 16285719;
      } else if(health >= 6) {
         color = 16286040;
      } else if(health >= 4) {
         color = 15031100;
      } else if(health >= 2) {
         color = 16711680;
      } else if(health >= 0) {
         color = 16190746;
      }

      return color;
   }

   private String getHealth(EntityPlayer e) {
      String hp = "";
      DecimalFormat numberFormat = new DecimalFormat("#.0");
      double abs = (double)(2.0F * (e.getAbsorptionAmount() / 4.0F));
      double health = (10.0D + abs) * (double)(e.getHealth() / e.getMaxHealth());
      health = Double.valueOf(numberFormat.format(health)).doubleValue();
      int ihealth = (int)health;
      if(health % 1.0D != 0.0D) {
         hp = String.valueOf(health);
      } else {
         hp = String.valueOf(ihealth);
      }

      return hp;
   }

   private String getPlayerName(EntityPlayer player) {
      String name = "";
      name = player.getDisplayName().getFormattedText();
      Iterator var4 = FriendManager.friends.iterator();

      while(var4.hasNext()) {
         Friend f = (Friend)var4.next();
         if(f.getName().equalsIgnoreCase(player.getName()) && FriendManager.isFriend(player.getName())) {
            name = "§b" + f.getAlias();
         }
      }

      if(EnemyManager.isEnemy(player.getName())) {
         name = "§c" + player.getName();
      }

      return name;
   }

   private float getSize(EntityPlayer player) {
      EntityPlayerSP ent = Wrapper.mc.thePlayer;
      boolean angle = isFacingAtEntity(player, 22.0D);
      float dist = ent.getDistanceToEntity(player) / 6.0F;
      float size = dist <= 2.0F?1.3F:dist;
      return size;
   }

   public static boolean isFacingAtEntity(Entity cunt, double angleHowClose) {
      EntityPlayerSP ent = Wrapper.mc.thePlayer;
      float[] yawPitch = getYawAndPitch(cunt);
      angleHowClose /= 4.5D;
      float yaw = yawPitch[0];
      float pitch = yawPitch[1];
      return (double)AngleDistance(ent.rotationYaw, yaw) < angleHowClose && (double)AngleDistance(ent.rotationPitch, pitch) < angleHowClose;
   }

   public static float[] getYawAndPitch(Entity target) {
      EntityPlayerSP ent = Wrapper.mc.thePlayer;
      double x = target.posX - ent.posX;
      double z = target.posZ - ent.posZ;
      double y = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0D - Wrapper.mc.thePlayer.posY;
      double helper = (double)MathHelper.sqrt_double(x * x + z * z);
      float newYaw = (float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F;
      float newPitch = (float)(Math.atan2(y * 1.0D, helper) * 180.0D / 3.141592653589793D);
      return new float[]{newYaw, newPitch};
   }

   private static float AngleDistance(float par1, float par2) {
      float angle = Math.abs(par1 - par2) % 360.0F;
      if(angle > 180.0F) {
         angle = 360.0F - angle;
      }

      return angle;
   }
}
