/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.feature.impl.misc.TeleportExploit;
import org.celestial.client.feature.impl.movement.NoClip;
import org.celestial.client.feature.impl.movement.Scaffold;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;

public class AutoSprint
extends Feature {
    public static ListSetting sprintMode;
    public static BooleanSetting matrixMulti;

    public AutoSprint() {
        super("AutoSprint", "\u0417\u0430\u0436\u0438\u043c\u0430\u0435\u0442 CTRL \u0437\u0430 \u0432\u0430\u0441, \u0447\u0442\u043e \u0431\u044b \u0431\u044b\u0441\u0442\u0440\u043e \u0431\u0435\u0436\u0430\u0442\u044c", Type.Movement);
        sprintMode = new ListSetting("Sprint Mode", "Default", "Default", "Rage");
        matrixMulti = new BooleanSetting("Matrix Multi", false, () -> AutoSprint.sprintMode.currentMode.equals("Rage"));
        this.addSettings(sprintMode, matrixMulti);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (!matrixMulti.getCurrentValue()) {
            return;
        }
        if (AutoSprint.sprintMode.currentMode.equals("Rage")) {
            CPacketEntityAction packet;
            if (Celestial.instance.featureManager.getFeatureByClass(NoClip.class).getState() && (NoClip.noClipMode.currentMode.equals("Lorent Craft") || NoClip.noClipMode.currentMode.equals("Lorent Fast"))) {
                return;
            }
            if (event.getPacket() instanceof CPacketEntityAction && ((packet = (CPacketEntityAction)event.getPacket()).getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING)) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(sprintMode.getCurrentMode());
        if (!(Celestial.instance.featureManager.getFeatureByClass(TeleportExploit.class).getState() || Celestial.instance.featureManager.getFeatureByClass(Scaffold.class).getState() && Scaffold.sprintoff.getCurrentValue() || Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.sprinting.getCurrentValue() && KillAura.target != null)) {
            if (AutoSprint.sprintMode.currentMode.equals("Default")) {
                AutoSprint.mc.player.setSprinting(AutoSprint.mc.player.movementInput.moveForward > 0.0f);
            } else if (AutoSprint.sprintMode.currentMode.equals("Rage")) {
                AutoSprint.mc.player.setSprinting(MovementHelper.isMoving());
            }
        }
    }
}

