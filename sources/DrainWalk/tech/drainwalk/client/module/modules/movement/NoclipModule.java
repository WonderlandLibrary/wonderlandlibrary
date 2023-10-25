package tech.drainwalk.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.events.Packer.EventReceivePacket;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.utility.BooleanSetting;
import tech.drainwalk.utility.NumberSetting;
import tech.drainwalk.utility.movement.MovementUtils;

public class NoclipModule extends Module {

    private final MultiOption widgets = new MultiOption("Noclip Mode", new MultiOptionValue("Noclip Mode", false));

    public NoclipModule() {
        super("NoclipModule", Category.MOVEMENT);
        register(widgets);
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (widgets.isSelected("Noclip Mode")) {
            if (mc.player != null) {
                mc.player.noClip = true;
                mc.player.motionY = 0.00001;
                }
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.motionY = 0.4;
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY = -0.4;
                }
            }
        }
}
