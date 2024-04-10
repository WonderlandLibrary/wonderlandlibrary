package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MouseClickEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ClickTP extends Module {
    public ModeValue type = new ModeValue("Type", "Spartan", new String[]{"Spartan", "OldNCP", "Vanilla", "Vulcan"});
    public ClickTP() {
        super("ClickTP", Category.Movement, "Left click to teleport to your wanna");
     registerValue(type);
    }
    boolean flag = false, wflag = false;

    @Override
    public void onEnable() {
        wflag = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.setTimerSpeed(1f);
        super.onDisable();
    }

    @Override
    public void onMouseClickEvent(MouseClickEvent event) {
        if(event.isAttack()){
            RayTraceResult result = mc.player.pick(20.0f, 1.0F);
            if(result.getType() == RayTraceResult.Type.BLOCK){
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
                double x_new = (double) blockRayTraceResult.getPos().getX() + 0.5;
                double y_new = blockRayTraceResult.getPos().getY() + 1;
                double z_new = (double) blockRayTraceResult.getPos().getZ() + 0.5;
                switch (type.getValue()) {
                    case "OldNCP":
                        double distance = mc.player.getDistance(x_new, y_new, z_new);
                        double d = 0.0;
                        while (d < distance) {
                            setPos(mc.player.getPosX() +
                                    (x_new - (double) mc.player.getHorizontalFacing().getXOffset() -
                                            mc.player.getPosX()) * d / distance, mc.player.getPosY() +
                                    (y_new - mc.player.getPosY()) * d / distance, mc.player.getPosZ() +
                                    (z_new - mc.player.getHorizontalFacing().getZOffset() - mc.player.getPosZ()) * d
                                            / distance);
                            d += MovementUtils.getBaseMoveSpeed();
                        }
                        break;
                    case "Vulcan":
                        if(!wflag) {
                            flag = false;
                            wflag = true;
                            setPos(mc.player.getPosX(), mc.player.getPosY() - 0.1, mc.player.getPosZ());
                        }else{
                            if(flag){
                                setPos(x_new, y_new, z_new);
                                flag = false;
                                wflag = false;
                            }
                        }
                        break;
                    case "Spartan":
                        if(!wflag) {
                            flag = false;
                            wflag = true;
                            setPos(mc.player.getPosX(), mc.player.getPosY() - 0.1, mc.player.getPosZ());
                            setPos(mc.player.getPosX(), mc.player.getPosY() - 1, mc.player.getPosZ());
                            mc.timer.setTimerSpeed(0.1f);
                        }else{
                            if(flag){
                                mc.timer.setTimerSpeed(1f);
                                setPos(x_new, y_new, z_new);
                                flag = false;
                                wflag = false;
                            }
                        }
                        break;
                    case "Vanilla":
                        setPos(x_new, y_new, z_new);
                        break;
                }
            }
        }
        super.onMouseClickEvent(event);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(wflag && event.packet instanceof SPlayerPositionLookPacket){
            flag = true;
        }
        super.onPacketEvent(event);
    }

    public void setPos(double x, double y, double z) {
        mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(x, y, z, false));
        mc.player.setPosition(x, y, z);
    }
}
