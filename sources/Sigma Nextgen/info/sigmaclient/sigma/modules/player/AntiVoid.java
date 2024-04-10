package info.sigmaclient.sigma.modules.player;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.block.AirBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AntiVoid extends Module {
    public ModeValue type = new ModeValue("Type", "Flag", new String[]{"Flag", "Vulcan", "KKCraft", "Polar"});
    public AntiVoid() {
        super("AntiVoid", Category.Player, "Anti fall to void.");
     registerValue(type);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }
    public boolean isOnVoid(){
        double y = mc.player.getPosY();
        while(y > 0){
            y -= 1.0;
            if(!(mc.world.getBlockState(new BlockPos(mc.player.getPosX(), y, mc.player.getPosZ())).getBlock() instanceof AirBlock)) return false;
        }
        return true;
    }
    boolean flag = false;
    int vulcantick = 0;
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(mc.player.fallDistance >= 2 && isOnVoid()){
                if(!flag) {
                    switch (type.getValue()) {
                        case "KKCraft":
                            mc.player.getMotion().x = 0;
                            mc.player.getMotion().y = 0;
                            mc.player.getMotion().z = 0;
                            event.yaw += new Random().nextInt(100);
                            event.yaw = MathHelper.wrapAngleTo180_float(event.yaw);
                            event.pitch += new Random().nextInt(100);
                            event.pitch = Math.max(Math.min(event.pitch, 90), -90);
                            event.dontRotation = true;
                            if(mc.player.hurtTime > 0){
                                flag = true;
                            }
                            break;
                        case "Flag":
                            mc.player.getMotion().y *= 0.2;
                            flag = true;
                            break;
                        case "Polar":
                            event.cancelable = true;

                            break;
                        case "Vulcan":
                            double y = mc.player.getPosY();
                            y = y - (y % (0.015625));
                            event.y = y;
                            event.onGround = true;
                            vulcantick ++;
                            if(vulcantick > 100){
                                vulcantick = 0;
                                flag = true;
                            }
                            break;
                    }
                }
            }else{
                flag = false;
                vulcantick = 0;
            }
        }
        super.onUpdateEvent(event);
    }
}
