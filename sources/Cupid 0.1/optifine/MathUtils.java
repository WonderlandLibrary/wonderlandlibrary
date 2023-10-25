package optifine;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class MathUtils {
  public static int getAverage(int[] p_getAverage_0_) {
    if (p_getAverage_0_.length <= 0)
      return 0; 
    int i = 0;
    for (int j = 0; j < p_getAverage_0_.length; j++) {
      int k = p_getAverage_0_[j];
      i += k;
    } 
    int l = i / p_getAverage_0_.length;
    return l;
  }
  
  public static float[] constrainAngle(float[] vector) {
    vector[0] = vector[0] % 360.0F;
    vector[1] = vector[1] % 360.0F;
    while (vector[0] <= -180.0F)
      vector[0] = vector[0] + 360.0F; 
    while (vector[1] <= -180.0F)
      vector[1] = vector[1] + 360.0F; 
    while (vector[0] > 180.0F)
      vector[0] = vector[0] - 360.0F; 
    while (vector[1] > 180.0F)
      vector[1] = vector[1] - 360.0F; 
    return vector;
  }
  
  public static double getRandomInRange(double min, double max) {
    Random random = new Random();
    double range = max - min;
    double scaled = random.nextDouble() * range;
    if (scaled > max)
      scaled = max; 
    double shifted = scaled + min;
    if (shifted > max)
      shifted = max; 
    return shifted;
  }
  
  public static double yawDist(EntityLivingBase e) {
    if (e != null) {
      Vec3 difference = e.getPositionVector().addVector(0.0D, (e.getEyeHeight() / 2.0F), 0.0D).subtract((Minecraft.getMinecraft()).thePlayer.getPositionVector().addVector(0.0D, (Minecraft.getMinecraft()).thePlayer.getEyeHeight(), 0.0D));
      double d = Math.abs((Minecraft.getMinecraft()).thePlayer.rotationYaw - Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0D) % 360.0D;
      return (d > 180.0D) ? (360.0D - d) : d;
    } 
    return 0.0D;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\MathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */