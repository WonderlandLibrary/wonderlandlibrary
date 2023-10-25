package tech.drainwalk.client.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.events.Packer.EventReceivePacket;
import tech.drainwalk.events.Player.EventUpdate;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.utility.BooleanSetting;
import tech.drainwalk.utility.ListSetting;

public class VelocityModule extends Module {
    public static BooleanSetting cancelOtherDamage;
    public static ListSetting velocityMode;
    private final MultiOption widgets = new MultiOption("Velocity", new MultiOptionValue("RW", false));

    public VelocityModule() {
        super("Velocity", Category.COMBAT);
        register(widgets);
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (widgets.isSelected("RW")) {
            if ((updateEvent.getPacket() instanceof SPacketEntityVelocity || updateEvent.getPacket() instanceof SPacketExplosion) &&
                    ((SPacketEntityVelocity) updateEvent.getPacket()).getEntityID() == mc.player.getEntityId()) {
                updateEvent.setOnGround(true);
            }
        }
    }
}
