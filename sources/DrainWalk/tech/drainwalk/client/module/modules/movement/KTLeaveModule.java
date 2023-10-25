package tech.drainwalk.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.events.Player.EventPreMotion;
import tech.drainwalk.events.Player.EventUpdate;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.utility.ChatUtils;

public class KTLeaveModule extends Module {
    private final MultiOption widgets = new MultiOption("Widgets", new MultiOptionValue("Leave", false));

    public KTLeaveModule() {
        super("KTLeaveModule", Category.MOVEMENT);
        register(widgets);
    }


    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (widgets.isSelected("Leave")) {
            float endX = 15900;
            float endZ = -1000;
            float endY = 70;
            if (mc.player.isSneaking() && mc.player.ticksExisted % 8 == 0) {
                ChatUtils.addChatMessage("  Мобилизирую на заданные мне координаты " + endX + " " + endY + " " + endZ);
                if (mc.player.posX != endX && mc.player.posZ != endZ) {
                    mc.player.motionY = 0.05f;
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY, endZ - 0.5, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY + 109, endZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY, endZ - 0.5, true));
                }
            }
        }
    }
}



