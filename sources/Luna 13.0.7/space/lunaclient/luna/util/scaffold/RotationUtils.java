package space.lunaclient.luna.util.scaffold;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.util.MathHelper;
import space.lunaclient.luna.util.MathUtils;

public class RotationUtils
{
  private static boolean fakeRotation;
  private static float serverYaw;
  private static float serverPitch;
  
  public RotationUtils() {}
  
  private static Vec3d getEyesPos()
  {
    return new Vec3d(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + Minecraft.thePlayer
      .getEyeHeight(), Minecraft.thePlayer.posZ);
  }
  
  private static Vec3d getServerLookVec()
  {
    float f = MathHelper.cos(-serverYaw * 0.017453292F - 3.1415927F);
    float f1 = MathHelper.sin(-serverYaw * 0.017453292F - 3.1415927F);
    float f2 = -MathHelper.cos(-serverPitch * 0.017453292F);
    float f3 = MathHelper.sin(-serverPitch * 0.017453292F);
    return new Vec3d(f1 * f2, f3, f * f2);
  }
  
  private static float[] getNeededRotations(Vec3d vec)
  {
    Vec3d eyesPos = getEyesPos();
    
    double diffX = vec.xCoord - eyesPos.xCoord;
    double diffY = vec.yCoord - eyesPos.yCoord;
    double diffZ = vec.zCoord - eyesPos.zCoord;
    
    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
    
    float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
    float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
    
    return new float[] { MathUtils.wrapDegrees(yaw), MathUtils.wrapDegrees(pitch) };
  }
  
  public static float[] getNeededRotations2(Vec3d vec)
  {
    Vec3d eyesPos = getEyesPos();
    
    double diffX = vec.xCoord - eyesPos.xCoord;
    double diffY = vec.yCoord - eyesPos.yCoord;
    double diffZ = vec.zCoord - eyesPos.zCoord;
    
    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
    float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
    float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
    
    return new float[] {Minecraft.thePlayer.rotationYaw + 
    
      MathUtils.wrapDegrees(yaw - Minecraft.thePlayer.rotationYaw), Minecraft.thePlayer.rotationPitch + 
      
      MathUtils.wrapDegrees(pitch - Minecraft.thePlayer.rotationPitch) };
  }
  
  private static float limitAngleChange(float current, float intended, float maxChange)
  {
    float change = MathUtils.wrapDegrees(intended - current);
    
    change = MathHelper.clamp_float(change, -maxChange, maxChange);
    
    return MathUtils.wrapDegrees(current + change);
  }
  
  private static boolean faceVectorPacket(Vec3d vec)
  {
    fakeRotation = true;
    
    float[] rotations = getNeededRotations(vec);
    
    serverYaw = limitAngleChange(serverYaw, rotations[0], 30.0F);
    serverPitch = rotations[1];
    
    return Math.abs(serverYaw - rotations[0]) < 1.0F;
  }
  
  public static void faceVectorPacketInstant(Vec3d vec)
  {
    float[] rotations = getNeededRotations2(vec);
    
    Minecraft.thePlayer.rotationYawHead = rotations[0];
    
    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Minecraft.thePlayer.rotationYawHead, Minecraft.thePlayer.rotationPitch, Minecraft.thePlayer.onGround));
    
    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], Minecraft.thePlayer.onGround));
  }
  
  private static boolean faceVectorClient(Vec3d vec)
  {
    float[] rotations = getNeededRotations(vec);
    
    float oldYaw = Minecraft.thePlayer.prevRotationYaw;
    float oldPitch = Minecraft.thePlayer.prevRotationPitch;
    
    Minecraft.thePlayer.rotationYaw = limitAngleChange(oldYaw, rotations[0], 30.0F);
    Minecraft.thePlayer.rotationPitch = rotations[1];
    
    return Math.abs(oldYaw - rotations[0]) + 
      Math.abs(oldPitch - rotations[1]) < 1.0F;
  }
  
  public static boolean faceVectorForWalking(Vec3d vec)
  {
    float[] rotations = getNeededRotations(vec);
    
    float oldYaw = Minecraft.thePlayer.prevRotationYaw;
    
    Minecraft.thePlayer.rotationYaw = limitAngleChange(oldYaw, rotations[0], 30.0F);
    Minecraft.thePlayer.rotationPitch = 0.0F;
    
    return Math.abs(oldYaw - rotations[0]) < 1.0F;
  }
  
  public static float getAngleToClientRotation(Vec3d vec)
  {
    float[] needed = getNeededRotations(vec);
    
    float diffYaw = MathUtils.wrapDegrees(Minecraft.thePlayer.rotationYaw) - needed[0];
    
    float diffPitch = MathUtils.wrapDegrees(Minecraft.thePlayer.rotationPitch) - needed[1];
    
    float angle = (float)Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    
    return angle;
  }
  
  public static float getHorizontalAngleToClientRotation(Vec3d vec)
  {
    float[] needed = getNeededRotations(vec);
    
    float angle = MathUtils.wrapDegrees(Minecraft.thePlayer.rotationYaw) - needed[0];
    
    return angle;
  }
  
  public static float getAngleToServerRotation(Vec3d vec)
  {
    float[] needed = getNeededRotations(vec);
    
    float diffYaw = serverYaw - needed[0];
    float diffPitch = serverPitch - needed[1];
    
    float angle = (float)Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    
    return angle;
  }
  
  public static void updateServerRotation()
  {
    if (fakeRotation)
    {
      fakeRotation = false;
      return;
    }
    serverYaw = limitAngleChange(serverYaw, Minecraft.thePlayer.rotationYaw, 30.0F);
    serverPitch = Minecraft.thePlayer.rotationPitch;
  }
  
  public static float getServerYaw()
  {
    return serverYaw;
  }
  
  public static float getServerPitch()
  {
    return serverPitch;
  }
}
