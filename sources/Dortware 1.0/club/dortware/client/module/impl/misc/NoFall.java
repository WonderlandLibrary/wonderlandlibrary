package club.dortware.client.module.impl.misc;

import club.dortware.client.Client;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.StringProperty;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.util.impl.networking.PacketUtil;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleData(name = "No Fall", description = "Prevents you from taking fall damage", category = ModuleCategory.MISC)
public class NoFall extends Module {

    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new StringProperty<>("Mode", this, "Edit", "Packet", "Verus"));
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
            String mode = (String) propertyManager.getProperty(this, "Mode").getValue();
            switch (mode) {
                case "Edit": {
                    if (mc.thePlayer.fallDistance > 2F) {
                        event.setOnGround(true);
                    }
                    break;
                }
                case "Packet": {
                    if (mc.thePlayer.fallDistance > 2F) {
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                        mc.thePlayer.fallDistance = 0.5F;
                    }
                    break;
                }
                case "Verus": {
                    if (mc.thePlayer.fallDistance > 1.25F || mc.thePlayer.ticksExisted % 26 != 0) {
                        mc.thePlayer.motionY = 0;
                        mc.thePlayer.setPosition(event.getPosX(), (int) event.getPosY(), event.getPosZ());
                        event.setOnGround(true);
                        mc.thePlayer.fallDistance = 0;
                    }
                    break;
                }
            }
        }
    }
}