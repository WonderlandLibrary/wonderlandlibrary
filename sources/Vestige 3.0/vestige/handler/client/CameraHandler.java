package vestige.handler.client;

import lombok.Setter;
import org.lwjgl.opengl.Display;
import vestige.Vestige;
import vestige.util.IMinecraft;

public class CameraHandler implements IMinecraft {

    private float cameraYaw, cameraPitch;

    @Setter
    private boolean freelooking;

    public void overrideMouse() {
        if(mc.inGameHasFocus && Display.isActive()) {
            mc.mouseHelper.mouseXYChange();
            float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f2 = f1 * f1 * f1 * 8.0F;
            float f3 = (float) mc.mouseHelper.deltaX * f2;
            float f4 = (float) mc.mouseHelper.deltaY * f2;

            cameraYaw += f3 * 0.15F;
            cameraPitch -= f4 * 0.15F;

            cameraPitch = Math.max(-90, Math.min(90, cameraPitch));
        }
    }

    public boolean isFreelooking() {
        if(freelooking) {
            return true;
        }

        cameraYaw = mc.thePlayer.rotationYaw;
        cameraPitch = mc.thePlayer.rotationPitch;

        return false;
    }

    public float getYaw() {
        return freelooking && !Vestige.instance.isDestructed() ? cameraYaw : mc.thePlayer.rotationYaw;
    }

    public float getPitch() {
        return freelooking && !Vestige.instance.isDestructed() ? cameraPitch : mc.thePlayer.rotationPitch;
    }

    public float getPrevYaw() {
        return freelooking && !Vestige.instance.isDestructed() ? cameraYaw : mc.thePlayer.prevRotationYaw;
    }

    public float getPrevPitch() {
        return freelooking && !Vestige.instance.isDestructed() ? cameraPitch : mc.thePlayer.rotationPitch;
    }

}
