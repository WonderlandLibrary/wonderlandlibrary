package appu26j.mods.visuals;

import org.lwjgl.opengl.Display;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.utils.ServerUtil;
import net.minecraft.client.gui.GuiScreen;

@ModInterface(name = "Perspective", description = "Allows you to look around without moving your head.", category = Category.VISUALS)
public class Perspective extends Mod
{
    private static float cameraYaw = 0, cameraPitch = 0;
    private static boolean perspectiveEnabled = false;
    private static int previousPerspective = 0;
    
    @Subscribe
    public void onTick(EventTick e)
    {
        if (this.mc.gameSettings.freelookKey.isKeyDown())
        {
            if (!this.perspectiveEnabled && !ServerUtil.isPlayerOnHypixel())
            {
                this.cameraYaw = this.mc.thePlayer.rotationYaw;
                this.cameraPitch = this.mc.thePlayer.rotationPitch;
                this.previousPerspective = this.mc.gameSettings.thirdPersonView;
                this.mc.gameSettings.thirdPersonView = 1;
                this.perspectiveEnabled = true;
            }
        }
        
        else
        {
            if (this.perspectiveEnabled)
            {
                this.mc.gameSettings.thirdPersonView = this.previousPerspective;
                this.perspectiveEnabled = false;
            }
        }
    }
    
    public static float getRotationYaw()
    {
        return perspectiveEnabled ? cameraYaw : mc.thePlayer.rotationYaw;
    }
    
    public static float getRotationPitch()
    {
        return perspectiveEnabled ? cameraPitch : mc.thePlayer.rotationPitch;
    }
    
    public static float getPrevRotationYaw()
    {
        return perspectiveEnabled ? cameraYaw : mc.thePlayer.prevRotationYaw;
    }
    
    public static float getPrevRotationPitch()
    {
        return perspectiveEnabled ? cameraPitch : mc.thePlayer.prevRotationPitch;
    }
    
    public static boolean overrideMouse()
    {
        if (mc.inGameHasFocus && Display.isActive() && perspectiveEnabled)
        {
            mc.mouseHelper.mouseXYChange();
            float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            float f2 = (float) mc.mouseHelper.deltaX * f1;
            float f3 = (float) mc.mouseHelper.deltaY * f1;
            
            if (mc.gameSettings.invertMouse)
            {
                f3 = -f3;
            }
            
            cameraYaw += f2 * 0.15F;
            cameraPitch += f3 * 0.15F;
            
            if (cameraPitch > 90)
            {
                cameraPitch = 90;
            }
            
            if (cameraPitch < -90)
            {
                cameraPitch = -90;
            }
            
            return false;
        }
        
        return true;
    }
    
    public static boolean isActive()
    {
        return perspectiveEnabled;
    }
}
