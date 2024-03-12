package xyz.cucumber.base.utils.game;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.commands.cmds.FriendsCommand;
import xyz.cucumber.base.module.feat.other.FriendsModule;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.math.RotationUtils;

public class EntityUtils
{
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static int size = 0;
	
    public static Timer timer = new Timer();
    public static EntityLivingBase getTarget(double range, String targetMode, String attackMode, int switchTimer, boolean teams, boolean troughWalls, boolean dead, boolean invisible)
    {
        EntityLivingBase target = null;
        List<Entity> targets = (List<Entity>)(mc).theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
        targets = (List<Entity>)targets.stream().filter(entity -> (mc.thePlayer.getDistanceToEntity(entity) < range && entity != (mc).thePlayer) && !(entity instanceof EntityArmorStand)).collect(Collectors.toList());
        
        targets.removeIf(entity -> (!invisible && entity.isInvisible()));
        targets.removeIf(entity -> (!dead && ((((EntityLivingBase) entity).getHealth() <= 0) || entity.isDead)));
        
        if(Client.INSTANCE.getModuleManager().getModule(FriendsModule.class).isEnabled()) {
        	for(String friend : FriendsCommand.friends) {
        		targets.removeIf(entity -> entity.getName().equalsIgnoreCase(friend));
        	}
        }
        
        targets.removeIf(entity -> (teams && mc.thePlayer.isOnSameTeam((EntityLivingBase) entity)));
        try {
        	targets.removeIf(entity -> (teams && entity instanceof EntityPlayer && isInSameTeam((EntityPlayer) entity)));
        }catch(Exception ex) {
        	
        }
        targets.removeIf(entity -> (!troughWalls && !mc.thePlayer.canEntityBeSeen(entity)));
        
        targets.sort(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity)));

        switch ((targetMode).toLowerCase())
        {
            case "players":
                targets = (List<Entity>)targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
                break;
        }

        if (!targets.isEmpty())
        {
            switch ((attackMode).toLowerCase())
            {
                case "single":
                    target = (EntityLivingBase)targets.get(0);
                    break;

                case "switch":

                    if (timer.hasTimeElapsed(switchTimer, true))size++;
                    
                    if (targets.size() > 0 && size >= targets.size())size = 0;
                    
                    target = (EntityLivingBase)targets.get(size);

                    break;
            }
        }

        return target;
    }
    
    public static EntityLivingBase getTargetBox(double range, String targetMode, String attackMode, int switchTimer, boolean teams, boolean troughWalls, boolean dead, boolean invisible)
    {
    	    	
        EntityLivingBase target = null;
        List<Entity> targets = (List<Entity>)(mc).theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
        targets = (List<Entity>)targets.stream().filter(entity -> getDistanceToEntityBox(entity) < range && entity != (mc).thePlayer && !(entity instanceof EntityArmorStand)).collect(Collectors.toList());
        
        targets.removeIf(entity -> (!invisible && entity.isInvisible()));
        targets.removeIf(entity -> (!dead && ((((EntityLivingBase) entity).getHealth() <= 0) || entity.isDead)));
        
        if(Client.INSTANCE.getModuleManager().getModule(FriendsModule.class).isEnabled()) {
        	for(String friend : FriendsCommand.friends) {
        		targets.removeIf(entity -> entity.getName().equalsIgnoreCase(friend));
        	}
        }
        
        targets.removeIf(entity -> (teams && mc.thePlayer.isOnSameTeam((EntityLivingBase) entity)));
        try {
        	targets.removeIf(entity -> (teams && entity instanceof EntityPlayer && isInSameTeam((EntityPlayer) entity)));
        }catch(Exception ex) {
        	
        }
        targets.removeIf(entity -> (!troughWalls && !mc.thePlayer.canEntityBeSeen(entity)));
        
        targets.sort(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity)));

        switch ((targetMode).toLowerCase())
        {
            case "players":
                targets = (List<Entity>)targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
                break;
        }

        if (!targets.isEmpty())
        {
            switch ((attackMode).toLowerCase())
            {
                case "single":
                    target = (EntityLivingBase)targets.get(0);
                    break;

                case "switch":

                    if (timer.hasTimeElapsed(switchTimer, true))size++;
                    
                    if (targets.size() > 0 && size >= targets.size())size = 0;
                    
                    target = (EntityLivingBase)targets.get(size);

                    break;
            }
        }

        return target;
    }
    
    public static boolean isInSameTeam(EntityPlayer player) {
    	try {
	        String[] name = mc.thePlayer.getDisplayName().getUnformattedText().split("");
	        String[] parts = player.getDisplayName().getUnformattedText().split("");
	
	        boolean b = (Arrays.asList(name).contains("§") && Arrays.asList(parts).contains("§")) && (Arrays.asList(name).get(Arrays.asList(name).indexOf("§")+1).equals(Arrays.asList(parts).get(Arrays.asList(parts).indexOf("§")+1)));
	
	        return b;
    	}catch(Exception ex) {
    		
    	}
    	return false;
    }
    
    public static double getDistanceToEntityBox(Entity entity) {
        Vec3 eyes = mc.thePlayer.getPositionEyes(1f);
        Vec3 pos = RotationUtils.getBestHitVec(entity);
        double xDist = Math.abs(pos.xCoord - eyes.xCoord);
        double yDist = Math.abs(pos.yCoord - eyes.yCoord);
        double zDist = Math.abs(pos.zCoord - eyes.zCoord);
        return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2) + Math.pow(zDist, 2));
    }
    
    public static double getDistanceToPositionBox(Entity entity, double posX, double posY, double posZ) {
    	final Vec3 positionEyes = mc.thePlayer.getPositionEyes(1f);
        final float f11 = entity.getCollisionBorderSize();
        final AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand(f11, f11, f11).offset(posX - entity.posX, posY - entity.posY, posZ - entity.posZ);
        final double ex = MathHelper.clamp_double(positionEyes.xCoord, entityBoundingBox.minX, entityBoundingBox.maxX);
        final double ey = MathHelper.clamp_double(positionEyes.yCoord, entityBoundingBox.minY, entityBoundingBox.maxY);
        final double ez = MathHelper.clamp_double(positionEyes.zCoord, entityBoundingBox.minZ, entityBoundingBox.maxZ);
        
        Vec3 pos = new Vec3(ex, ey-0.4, ez);
    	
        double xDist = Math.abs(pos.xCoord - positionEyes.xCoord);
        double yDist = Math.abs(pos.yCoord - positionEyes.yCoord);
        double zDist = Math.abs(pos.zCoord - positionEyes.zCoord);
        return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2) + Math.pow(zDist, 2));
    }
}
