package dev.rise.util.player;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;

@UtilityClass
public class PositionUtil {

    private final Minecraft mc = Minecraft.getMinecraft();

    public void setY(final double y) {
        setXYZ(getX(), y, getZ());
    }

    public void setXZ(final double x, final double z) {
        setXYZ(x, getY(), z);
    }

    public void setXYZ(final double x, final double y, final double z) {
        mc.thePlayer.setPosition(x, y, z);
    }

    public double getX() {
        return mc.thePlayer.posX;
    }

    public double getY() {
        return mc.thePlayer.posY;
    }

    public double getZ() {
        return mc.thePlayer.posZ;
    }
}
