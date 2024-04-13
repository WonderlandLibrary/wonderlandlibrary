//package dev.tenacity.module.impl.funny;
//
//import dev.tenacity.event.IEventListener;
//import dev.tenacity.event.impl.packet.PacketReceiveEvent;
//import dev.tenacity.module.Module;
//import dev.tenacity.module.ModuleCategory;
//import dev.tenacity.setting.impl.NumberSetting;
//import net.minecraft.network.play.server.S12PacketEntityVelocity;
//import net.minecraft.network.play.server.S27PacketExplosion;
//
//public final class KiteModule extends Module {
//
//    private final NumberSetting horizontalVelocity = new NumberSetting("Horizontal Velocity", -10, 0, -100, 0.1),
//            verticalVelocity = new NumberSetting("Vertical Velocity", -10, 0, -100, 0.1);
//
//
//    public KiteModule() {
//        super("Kite", "Makes you fly like a kite", ModuleCategory.MOVEMENT);
//        initializeSettings(horizontalVelocity, verticalVelocity);
//    }
//
//    private final IEventListener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
//
//        if (mc.thePlayer.hurtTime > 0) {
//            mc.thePlayer.motionX *= horizontalVelocity.getCurrentValue();
//             mc.thePlayer.motionY *= verticalVelocity.getCurrentValue();
//             mc.thePlayer.motionZ *= horizontalVelocity.getCurrentValue();
//    }
//
////        if(event.getPacket() instanceof S12PacketEntityVelocity) {
////            final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) event.getPacket();
////            if(s12.getEntityID() == mc.thePlayer.getEntityId()) {
////               mc.thePlayer.motionX *= horizontalVelocity.getCurrentValue();
////               mc.thePlayer.motionY *= verticalVelocity.getCurrentValue();
////               mc.thePlayer.motionZ *= horizontalVelocity.getCurrentValue();
////            }
////        }
//
////        if(event.getPacket() instanceof S12PacketEntityVelocity) {
////            final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) event.getPacket();
////            if(s12.getEntityID() == mc.thePlayer.getEntityId()) {
////                s12.motionX = (int) (horizontalVelocity.getCurrentValue());
////                s12.motionY = (int) (verticalVelocity.getCurrentValue());
////                s12.motionZ = (int) (horizontalVelocity.getCurrentValue());
////            }
////        }
////        if(event.getPacket() instanceof S27PacketExplosion) {
////            final S27PacketExplosion s27 = (S27PacketExplosion) event.getPacket();
////            if(horizontalVelocity.getCurrentValue() == 0 && verticalVelocity.getCurrentValue() == 0) {
////                return;
////            }
////            s27.motionX = (float) (horizontalVelocity.getCurrentValue());
////            s27.motionY = (float) (verticalVelocity.getCurrentValue());
////            s27.motionZ = (float) (horizontalVelocity.getCurrentValue());
////        }
//    };
//
//}
