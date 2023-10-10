package Reality.Realii.anticheat;
import Reality.Realii.utils.cheats.player.Helper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class StrafeA {
	double lastPosX, lastPosY, lastPosZ;
	long lastTime;
	public static final double MAX_HORIZONTAL_SPEED = 90.0D; // example value, adjust as needed
	public static final double MAX_VERTICAL_SPEED = 90.0D; // example
	 private final double MAX_STRAFE_SPEED = 10.0D;

public void FukcingStarfe () {
	for (Object obj : Helper.mc.theWorld.playerEntities) {
        EntityPlayer player = (EntityPlayer) obj;
        if(Helper.mc.thePlayer == null)
            return;
        
       
        
        double posX = player.posX;
        double posY = player.posY;
        double posZ = player.posZ;
        double horizontalDistance = Math.sqrt((posX - lastPosX) * (posX - lastPosX) + (posZ - lastPosZ) * (posZ - lastPosZ));
        double verticalDistance = posY - lastPosY;
        
        long currentTime = System.currentTimeMillis();
        double timeDiff = (double) (currentTime - lastTime) / 1000;
        
        double horizontalSpeed = horizontalDistance / timeDiff;
        double verticalSpeed = verticalDistance / timeDiff;
        
        if (horizontalSpeed > MAX_HORIZONTAL_SPEED || verticalSpeed > MAX_VERTICAL_SPEED) {
            Helper.sendMessage(EnumChatFormatting.RED + "Player " + player.getName() + " is moving too fast!");
        }
        
        // Strafe check
        if (player.moveStrafing != 0) {
            double strafeDistance = Math.sqrt((posX - lastPosX) * (posX - lastPosX) + (posZ - lastPosZ) * (posZ - lastPosZ));
            double strafeSpeed = strafeDistance / timeDiff;
            
            if (strafeSpeed > MAX_STRAFE_SPEED) {
                Helper.sendMessage(EnumChatFormatting.RED + "Player " + player.getName() + " is strafing too fast!");
            }
        }
        
        lastPosX = posX;
        lastPosY = posY;
        lastPosZ = posZ;
        lastTime = currentTime;
    
	}
}
}
