package info.sigmaclient.sigma.modules.player;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NoFall extends Module {
    public ModeValue type = new ModeValue("Type", "Delay", new String[]{"Delay", "Vulcan", "Spoof"});
    public NoFall() {
        super("NoFall", Category.Player, "No fall damage.");
     registerValue(type);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(mc.player.fallDistance >= 2){
                switch (type.getValue()){
                    case "Spoof":
                        event.onGround = true;
                        break;
                    case "Delay":
                        if(mc.player.ticksExisted % 5 == 0)
                            event.onGround = true;
                        break;
                    case "Vulcan":
                        if(mc.player.ticksExisted % 2 == 0) {
                            double y = mc.player.getPosY();
                            y = y - (y % (0.015625));
                            event.y = y;
                            event.onGround = true;
                        }
                        break;
                }
            }
        }
        super.onUpdateEvent(event);
    }
}
