package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.configurations.property.object.StringProperty;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Mouse;

public class ClickTeleport extends AbstractModule {

    @Property("mouse-button")
    private final StringProperty mouse_button = PropertyFactory.createString("Side Front").acceptableValues("Left", "Right", "Middle", "Side Front", "Side Back");

    public ClickTeleport(@NonNull ModuleManager novoline) {
        super(novoline, EnumModuleType.PLAYER, "ClickTeleport", "Click Teleport");
        Manager.put(new Setting("CT_MOUSE_BUTTON", "Mouse", SettingType.COMBOBOX, this, mouse_button));
    }

    private boolean down;

    private int button() {
        if (mouse_button.equals("Left")) {
            return 0;
        } else if (mouse_button.equals("Right")) {
            return 1;
        } else if (mouse_button.equals("Middle")) {
            return 2;
        } else if (mouse_button.equals("Side Back")) {
            return 3;
        } else if (mouse_button.equals("Side Front")) {
            return 4;
        } else {
            return -1;
        }
    }

    @EventTarget
    private void onMotions(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (mc.currentScreen == null) {
                if (Mouse.isButtonDown(button())) {
                    if (down) {
                        if (rayTrace() != null) {
                            int xNew = rayTrace().getBlockPos().getX();
                            int yNew = rayTrace().getBlockPos().getY();
                            int zNew = rayTrace().getBlockPos().getZ();
                            mc.player.sendChatMessage(".tp " + xNew + " " + yNew + " " + zNew);
                        }

                        down = false;
                    }

                } else {
                    down = true;
                }
            }
        }
    }

/*    public void tpToPos(double xNew, double yNew, double zNew) {
        double distance = mc.player.getDistance(xNew, yNew, zNew);

        for (double dist = 0.0D; dist < distance; dist += 1.0D) {
            double xPos = mc.player.posX + (xNew - mc.player.getHorizontalFacing().getFrontOffsetX() - mc.player.posX) * dist / distance;
            double yPos = mc.player.posY + (yNew - mc.player.posY) * dist / distance;
            double zPos = mc.player.posZ + (zNew - mc.player.getHorizontalFacing().getFrontOffsetZ() - mc.player.posZ) * dist / distance;
            setPos(xPos, yPos, zPos);
        }

        checkModule(Flight.class);
        setPos(xNew, yNew, zNew);
    }*/

    public MovingObjectPosition rayTrace() {
        Vec3 posVec = new Vec3(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
        Vec3 rotVec = getVectorForRotation(mc.player.rotationPitch, mc.player.rotationYaw);
        Vec3 endVec = posVec.addVector(rotVec.xCoord * 500, rotVec.yCoord * 500, rotVec.zCoord * 500);
        return mc.world.rayTraceBlocks(posVec, endVec, false, false, false);
    }

    protected final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(Math.toRadians(-yaw) - (float) Math.PI);
        float f1 = MathHelper.sin(Math.toRadians(-yaw) - (float) Math.PI);
        float f2 = -MathHelper.cos(Math.toRadians(-pitch));
        float f3 = MathHelper.sin(Math.toRadians(-pitch));
        return new Vec3(f1 * f2, f3, f * f2);
    }

/*    public void setPos(double x, double y, double z) {
        if (mc.isSingleplayer() || ServerUtils.isHypixel()) {
            sendPacket(new C04PacketPlayerPosition(x, y, z, true));
        } else {
            sendPacket(new C04PacketPlayerPosition(x, y, z, false));
        }

        //    mc.player.setPositionAndUpdate(x, y, z);
    }*/
}
