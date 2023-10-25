/* 3eLeHyy#0089 */

package org.dreamcore.client.feature.impl.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketKeepAlive;
import org.apache.commons.lang3.RandomUtils;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Disabler extends Feature {

    public long randomLong = 0L;

    public BooleanSetting MatrixDestruction = new BooleanSetting("MatrixDestruction", false, () -> true);
    public BooleanSetting MatrixFlagClear = new BooleanSetting("MatrixFlagClear", false, () -> true);

    public ListSetting mode = new ListSetting("Disabler Mode", "Matrix", () -> true, "Matrix");

    public Disabler() {
        super("Disabler", "Ослабляет воздействие античитов на вас", Type.Misc);
        addSettings(mode, MatrixDestruction, MatrixFlagClear);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(mode.currentMode);
        if (mode.currentMode.equals("Matrix")) {
            if (MatrixDestruction.getBoolValue()) {
                if (mc.player.ticksExisted % 6 == 0) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                }
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (mode.currentMode.equals("Matrix")) {
            if (MatrixFlagClear.getBoolValue()) {
                randomLong = RandomUtils.nextLong(0L, 10L);
                if (event.getPacket() instanceof SPacketKeepAlive)
                    try {
                        Thread.sleep(50L * randomLong);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}


