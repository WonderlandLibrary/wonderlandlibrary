package Velo.api.Util.Other;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RaycastUtil {
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	public static BlockPos rayCast(double range, float yaw, float pitch) {
        double d0 = range;
        double d1 = d0;
		Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0f);
		
        boolean flag = false;
        boolean flag1 = true;
        
        if (d0 > 3.0D)
        {
            flag = true;
        }
        
        Vec3 vec31 = getVectorForRotation(pitch, yaw);
        Vec3 vec33 = null;
        double d2 = d1;
        
        Vec3 vec34 = mc.thePlayer.getLookVec();
        
        if(vec31 == vec34) {
        	vec33 = vec34;
        	
        	BlockPos pos = new BlockPos(vec33.xCoord*range,vec33.yCoord*range,vec33.zCoord*range);
        	return pos;
        }
        return null;
	}
	
	public static Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
}
