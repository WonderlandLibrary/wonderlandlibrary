package artemis.mods.Combat;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.events.Event;
import artemis.events.EventTarget;
import artemis.events.events.EventPostMotionUpdate;
import artemis.events.events.EventPreMotionUpdate;
import artemis.events.listeners.EventMotion;
import artemis.mods.Render.TargetHUD;
import artemis.settings.Setting;
import artemis.util.RenderUtils;
import artemis.util.Timer;
import artemis.util.Utils;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
public class Killaura extends module {
	public static EntityLivingBase target;
    private long current, last;
 //   private int delay = 8;
    
    public static Killaura instance = new Killaura();
    private float yaw, pitch;
    private boolean others;
    private double aps;
    private double range;
    private String aps2;
    private String range2;
    boolean blockingStatus;
    public Timer timer = new Timer();
    EventPreMotionUpdate event;
    public boolean enabled = false;
  public Killaura() {
    super("Aura", "Automatically hits entitys", Keyboard.KEY_P, 9, Category.COMBAT);
    
  }
  
  @Override
  public void setup() {
	  //TODO add modes switch, multi, single
	  //TODO add more rotation types
	  //TODO add more sliders
      Artemis.instance.settingsManager.rSetting(new Setting("Crack Size", this, 5, 0, 15, true));
      Artemis.instance.settingsManager.rSetting(new Setting("APS", this, 8, 0, 20, true));
      Artemis.instance.settingsManager.rSetting(new Setting("Random APS", this, true));
      Artemis.instance.settingsManager.rSetting(new Setting("Range", this, 4, 0, 6, false));
      Artemis.instance.settingsManager.rSetting(new Setting("Random Range", this, true));
      Artemis.instance.settingsManager.rSetting(new Setting("Existed", this, 30, 0, 500, true));
      Artemis.instance.settingsManager.rSetting(new Setting("FOV", this, 360, 0, 360, true));
      Artemis.instance.settingsManager.rSetting(new Setting("AutoBlock", this, true));
      Artemis.instance.settingsManager.rSetting(new Setting("Rotations", this, true));
      Artemis.instance.settingsManager.rSetting(new Setting("Invisibles", this, false));
      Artemis.instance.settingsManager.rSetting(new Setting("Players", this, true));
      Artemis.instance.settingsManager.rSetting(new Setting("Animals", this, true));
      Artemis.instance.settingsManager.rSetting(new Setting("Monsters", this, true));
      Artemis.instance.settingsManager.rSetting(new Setting("Villagers", this, false));
      Artemis.instance.settingsManager.rSetting(new Setting("Teams", this, false));
      Artemis.instance.settingsManager.rSetting(new Setting("AutoDisable", this, true));
  }
  //@EventTarget
  //public void onPre(EventPreMotionUpdate event) {
  
  
  
  public void updateSettings() {
	 aps2 = String.format("%.2f%%", Artemis.instance.settingsManager.getSettingByName("APS").getValDouble() + (Artemis.instance.settingsManager.getSettingByName("Random APS").getValBoolean() ? ThreadLocalRandom.current().nextDouble(-1, 1) : 0));
	 range2 = String.format("%.2f%%",Artemis.instance.settingsManager.getSettingByName("Range").getValDouble() + (Artemis.instance.settingsManager.getSettingByName("Random Range").getValBoolean() ? ThreadLocalRandom.current().nextDouble(-1, 1) : 0));
	  aps = Artemis.instance.settingsManager.getSettingByName("APS").getValDouble() + (Artemis.instance.settingsManager.getSettingByName("Random APS").getValBoolean() ? ThreadLocalRandom.current().nextDouble(-1, 1) : 0);
	  range = Artemis.instance.settingsManager.getSettingByName("Range").getValDouble() + (Artemis.instance.settingsManager.getSettingByName("Random Range").getValBoolean() ? ThreadLocalRandom.current().nextDouble(1, 2) : 0);
  }
  
  
@Override
public void onEvent(Event e){
	if(e instanceof EventMotion) {
		if(e.isPre()) {
			EventMotion event = (EventMotion)e;
			this.updateSettings();
		  this.setName("Aura §7Switch APS " + aps2 + " R " + range2);
		  
		  if (Artemis.instance.settingsManager.getSettingByName("AutoDisable").getValBoolean() && !mc.thePlayer.isEntityAlive()) {
				this.setToggled(false);
				return;
			}
		  
		  Artemis.instance.target = true;
		  //mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
		  if(Artemis.instance.settingsManager.getSettingByName("Rotations").getValBoolean()) {
              //this.faceEntity(target);
          }
		     //mc.thePlayer.rotationYaw = (this.getRotations2(target)[0]);
	        //  mc.thePlayer.rotationPitch = (this.getRotations2(target)[1]);
      target = getClosest(range);
      if(target == null)
          return;
      updateTime();
      yaw = mc.thePlayer.rotationYaw;
      pitch = mc.thePlayer.rotationPitch;
     
      event.setYaw(this.getRotations2(target)[0]);
      event.setPitch(this.getRotations2(target)[1]);
     boolean block = target != null && Artemis.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
      if(canBlock() && target.getDistanceToEntity(mc.thePlayer) < 8F) {
    	  mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71626);
    		this.updateSettings();
    	//  mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
          //mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
          if (Minecraft.getMinecraft().thePlayer.isBlocking()) {
             // Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging());
          }
      }
      
      if(target instanceof EntityPlayer) {
    	//  System.out.println("Playerstats works lol");
    	 // PlayerStats.instance.drawStats(target);
      }
  	this.updateSettings();
      this.enabled = true;
      if(current - last > 1000 / aps) {
    	 // this.faceEntity(target);
          attack(target);
      	this.updateSettings();
          event.setYaw(this.getRotations2(target)[0]);
          event.setPitch(this.getRotations2(target)[1]);
          mc.thePlayer.rotationYaw = (this.getRotations2(target)[0]);
          mc.thePlayer.rotationPitch = (this.getRotations2(target)[1]);
          if(Artemis.instance.settingsManager.getSettingByName("Rotations").getValBoolean()) {
             // this.faceEntity(target);
          }
       //this.faceEntity(target);
          float facing = Utils.getNeededRotationsYaw(target);
          float facing2 = Utils.getNeededRotationsPitch(target);

          
         // mc.thePlayer.cameraYaw = target.rotationYaw;
          //mc.thePlayer.cameraPitch = target.rotationPitch;
          
         // mc.thePlayer.cameraYaw = facing;
          //mc.thePlayer.cameraPitch = facing2;
		//event.setYaw(facing[0]);
          //event.setPitch(facing[1]);
          resetTime();
       }
      if(target == null)
          return;
      mc.thePlayer.rotationYaw = yaw;
      mc.thePlayer.rotationPitch = pitch;
	}
	}
//      /super.onUpdate();
  }

public boolean canBlock() {
	if(target != null && Artemis.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
		return true;
	}
	return false;
}
@Override
public void onDisable() {
	  this.enabled = false;
	  Artemis.instance.target = false;
}

@Override
public void onRender() {
	  if(this.isToggled()) {
		  if(enabled) {
			  
			//  this.drawRadius(Killaura.target, 30, 2);
			  
			  
			  if(this.target instanceof EntityMob) {
					mob(target);
					
				}
				
				if(this.target instanceof EntityAnimal) {
					animal(this.target);
				
				}
				
				if(target instanceof EntityPlayer) {
					if(target != mc.thePlayer) {
						player(target);
						
						//int x =  (int) ((int) (target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX);
						//int y =  (int) ((int) (target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY);
						//double z = (target.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
						//RenderUtils.drawCircle(x, y, 10, 20);
					}
	  }
		  }
	  }
}

public void mob(EntityLivingBase entity) {
	float red = 1F;
	float green = 0.5F;
	float blue = 0.5F;
	
	double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
	double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
	double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
	
	render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
}

public void animal(EntityLivingBase entity) {
	float red = 0.5F;
	float green = 1F;
	float blue = 0.5F;
	
	double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
	double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
	double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
	
	render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
}


public void player(EntityLivingBase entity) {
	float red = 0.5F;
	float green = 0.5F;
	float blue = 1F;
	
	double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
	double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
	double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
	
	render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
}


public void render(float red, float green, float blue, double x, double y, double z, float width, float height) {
	RenderUtils.drawEntityESP(x, y + 2.3, z, width, height - 1.6, red, green, blue, 0.45F, 0F, 0F, 0F, 1F, 1F);
}

  @EventTarget
  public void onPost(EventPostMotionUpdate event) {
      if(target == null)
          return;
      mc.thePlayer.rotationYaw = yaw;
      mc.thePlayer.rotationPitch = pitch;
  }	
  
  
  
  private void attack(Entity entity) {
	//   this.faceEntity(target);
      for(int i = 0; i < Artemis.instance.settingsManager.getSettingByName("Crack Size").getValDouble(); i++)
          mc.thePlayer.onCriticalHit(entity);
//    /this.faceEntity(target);
     mc.thePlayer.swingItem();
      mc.playerController.attackEntity(mc.thePlayer, entity);
  }

  private void updateTime() {
      current = (System.nanoTime() / 1000000L);
  }

  private void resetTime() {
      last = (System.nanoTime() / 1000000L);
  }

  private EntityLivingBase getClosest(double range) {
      double dist = range;
      EntityLivingBase target = null;
      for (Object object : mc.theWorld.loadedEntityList) {
          Entity entity = (Entity) object;
          if (entity instanceof EntityLivingBase) {
              EntityLivingBase player = (EntityLivingBase) entity;
              if (canAttack(player)) {
                  double currentDist = mc.thePlayer.getDistanceToEntity(player);
                  if (currentDist <= dist) {
                      dist = currentDist;
                      target = player;
                  }
              }
          }
      }
      return target;
  }
  public boolean canAttack(EntityLivingBase player) {
      if(player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
          if (player instanceof EntityPlayer && !Artemis.instance.settingsManager.getSettingByName("Players").getValBoolean())
              return false;
          if (player instanceof EntityAnimal && !Artemis.instance.settingsManager.getSettingByName("Animals").getValBoolean())
              return false;
          if (player instanceof EntityMob && !Artemis.instance.settingsManager.getSettingByName("Monsters").getValBoolean())
              return false;
          if (Artemis.f.isFriend(player.getName()))
              return false;
          if (player instanceof EntityVillager && !Artemis.instance.settingsManager.getSettingByName("Villagers").getValBoolean())
              return false;
      }
      if(player.isOnSameTeam(mc.thePlayer) && Artemis.instance.settingsManager.getSettingByName("Teams").getValBoolean())
          return false;
      if(player.isInvisible() && !Artemis.instance.settingsManager.getSettingByName("Invisibles").getValBoolean())
          return false;
      if(!isInFOV(player, Artemis.instance.settingsManager.getSettingByName("FOV").getValDouble()))
          return false;
      return player != mc.thePlayer && player.isEntityAlive() && mc.thePlayer.getDistanceToEntity(player) <= mc.playerController.getBlockReachDistance() && player.ticksExisted > Artemis.instance.settingsManager.getSettingByName("Existed").getValDouble();
  }

  private boolean isInFOV(EntityLivingBase entity, double angle) {
      angle *= .5D;
      double angleDiff = getAngleDifference(mc.thePlayer.rotationYaw, getRotations(entity.posX, entity.posY, entity.posZ)[0]);
      return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
  }

  private float getAngleDifference(float dir, float yaw) {
      float f = Math.abs(yaw - dir) % 360F;
      float dist = f > 180F ? 360F - f : f;
      return dist;
  }

  private float[] getRotations(double x, double y, double z) {
      double diffX = x + .5D - mc.thePlayer.posX;
      double diffY = (y + .5D) / 2D - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
      double diffZ = z + .5D - mc.thePlayer.posZ;

      double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
      float yaw = (float)(Math.atan2(diffZ, diffX) * 180D / Math.PI) - 90F;
      float pitch = (float)-(Math.atan2(diffY, dist) * 180D / Math.PI);

      return new float[] { yaw, pitch };
  }
  public static synchronized void faceEntity(EntityLivingBase entity) {
		final float[] rotations = getRotationsNeeded(entity);

		if (rotations != null) {
			Minecraft.getMinecraft().thePlayer.rotationYaw = rotations[0];
			Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1] + 1.0F;// 14
		}
	}

	public static float[] getRotationsNeeded(Entity entity) {
		if (entity == null) {
			return null;
		}

		final double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		final double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double diffY;

		if (entity instanceof EntityLivingBase) {
			final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		}

		final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		final float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] { Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}
	private float[] getRotations2(Entity e) {
	     double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
	    		 deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
	    		 deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
	             distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
	     
	     
	     float yaw = (float)Math.toDegrees(-Math.atan(deltaX / deltaZ)),
	    		 pitch = (float)-Math.toDegrees(Math.atan(deltaY / distance));
	     
	    if(deltaX < 0 && deltaZ < 0) {
	    	yaw = (float)(90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
	    }else if(deltaX > 0 && deltaZ < 0) {
	    	yaw = (float)(-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
	    }
	      return new float[] { yaw, pitch };
	  }
	

    
	//}
	


private void drawRadius(final Entity entity, final float partialTicks, final double rad) {
	float points = 90F;
	GlStateManager.enableDepth();
	for (double il = 0; il < 5; il += 0.001) {
    	GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(6.0f);
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;
        final double pix2 = 6.283185307179586;
        float speed = 5000f;
        float baseHue = System.currentTimeMillis() % (int)speed;
		while (baseHue > speed) {
			baseHue -= speed;
		}
		baseHue /= speed;
        for (int i = 0; i <= 90; ++i) {
        	float max = ((float) i + (float)(il * 8)) / points;
    		float hue = max + baseHue ;
    		while (hue > 1) {
    			hue -= 1;
    		}
            final float r = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getRed();
            final float g = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getGreen();
            final float b = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getBlue();
            GL11.glColor3f(r, g, b);
            GL11.glVertex3d(x + rad * Math.cos(i * pix2 / points), y + il, z + rad * Math.sin(i * pix2 / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GlStateManager.color(255, 255, 255);
	}
}

}
