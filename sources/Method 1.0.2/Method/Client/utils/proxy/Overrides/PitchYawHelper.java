package Method.Client.utils.proxy.Overrides;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;

public class PitchYawHelper extends MouseHelper {

    public  boolean Pitch = false;
    public  boolean Yaw = false;


    @Override
    public void mouseXYChange() {
        this.deltaX = Mouse.getDX();
        this.deltaY = Mouse.getDY();
        if (Pitch) {
            Minecraft.getMinecraft().mouseHelper.deltaY = 0;
        }
        if (Yaw) {
            Minecraft.getMinecraft().mouseHelper.deltaX = 0;
        }
    }
}
