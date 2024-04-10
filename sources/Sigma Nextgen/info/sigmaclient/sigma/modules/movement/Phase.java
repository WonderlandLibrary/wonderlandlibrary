package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.network.play.client.CPlayerPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Phase extends Module {
    public ModeValue type = new ModeValue("Type", "Vanilla", new String[]{"Vanilla", "Clip", "ONCP", "NClip"});
    public Phase() {
        super("Phase", Category.Movement, "Move into a block");
     registerValue(type);
    }
    @Override
    public void onEnable() {
        super.onEnable();
    }

//    public static Pair<Direction, Vector3d> 쿨뎫郝곻聛(final double n) {
//        final AxisAlignedBB 䢿鷏낛缰䩜꿩 = mc.player.getBoundingBox();
//        for (final Direction 훔睬蕃㠠쬷柿 : new Direction[] {
//                Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
//        }) {
//            if (mc.world.getCollisionShapes(
//                    mc.player,
//                    䢿鷏낛缰䩜꿩.쥡㮃붛竬鏟䬾(n * 훔睬蕃㠠쬷柿.getXOffset(), 0.0, n * 훔睬蕃㠠쬷柿.getZOffset())
//            ).iterator().hasNext()) {
//                return new Pair<>(
//                        훔睬蕃㠠쬷柿, mc.player.getPositionVec().add(훔睬蕃㠠쬷柿.getXOffset(), 0.0, 훔睬蕃㠠쬷柿.getZOffset())
//                );
//            }
//        }
//        return null;
//    }
    @Override
    public void onMoveEvent(MoveEvent event) {
        switch (type.getValue()) {
            case "NClip":
                mc.player.noClip = true;
                break;
            case "ONCP":
                if(mc.player.collidedHorizontally) {
                    if(MovementUtils.isMoving()){
                        final double yaw = Math.toRadians(mc.player.rotationYaw);
                        final double x = -Math.sin(yaw) * 1.56250014364675;
                        final double z = Math.cos(yaw) * 1.56250014364675;
                        mc.player.setPosition(mc.player.getPosX() + x, mc.player.getPosY(), mc.player.getPosZ() + z);
                        mc.getConnection().sendPacketNOEvent(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), true));
                    }
//                    final Pair<Direction, Vector3d> 쿨뎫郝곻聛 = 쿨뎫郝곻聛(1.0E-4);
//                    if(쿨뎫郝곻聛 == null) return;
//                    boolean hyp = false;
//                    final double n = hyp ? 1.0E-6 : 0.0625;
//                    if (쿨뎫郝곻聛.getKey().getAxis() != Direction.Axis.X) {
//                        event.setZ(Math.round((쿨뎫郝곻聛.getValue().z + 1.1921022E-8) * 10000.0) / 10000.0 + 쿨뎫郝곻聛.getKey().getZOffset() * n);
//                    } else {
//                        event.setX(Math.round((쿨뎫郝곻聛.getValue().x + 1.1921022E-8) * 10000.0) / 10000.0 + 쿨뎫郝곻聛.getKey().getXOffset() * n);
//                    }
                }
                break;
        }
        super.onMoveEvent(event);
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            switch (type.getValue()){
                case "Clip":
                    if(mc.player.collidedHorizontally) {
                        if(MovementUtils.isMoving()){
                            final double yaw = Math.toRadians(mc.player.rotationYaw);
                            final double x = -Math.sin(yaw) * 0.08;
                            final double z = Math.cos(yaw) * 0.08;
                            mc.player.setPosition(mc.player.getPosX() + x, mc.player.getPosY(), mc.player.getPosZ() + z);
//                            mc.getConnection().sendPacketNOEvent(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), true));
                        }
                    }
                    break;
                case "Vanilla":
                    if(mc.player.collidedHorizontally) {
                        if(MovementUtils.isMoving()){
                            final double yaw = Math.toRadians(mc.player.rotationYaw);
                            final double x = -Math.sin(yaw) * 2;
                            final double z = Math.cos(yaw) * 2;
                            mc.player.setPosition(mc.player.getPosX() + x, mc.player.getPosY(), mc.player.getPosZ() + z);
                            mc.getConnection().sendPacketNOEvent(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), true));
                        }
                    }
                    break;
            }
        }
        super.onUpdateEvent(event);
    }
}
