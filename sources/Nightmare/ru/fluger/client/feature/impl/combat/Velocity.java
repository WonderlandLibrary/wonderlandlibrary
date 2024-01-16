// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class Velocity extends Feature
{
    public static BooleanSetting cancelOtherDamage;
    public static ListSetting velocityMode;
    
    public Velocity() {
        super("Velocity", "\u0412\u044b \u043d\u0435 \u0431\u0443\u0434\u0435\u0442\u0435 \u043e\u0442\u043a\u0438\u0434\u044b\u0432\u0430\u0442\u044c\u0441\u044f", Type.Combat);
        Velocity.velocityMode = new ListSetting("Velocity Mode", "Packet", () -> true, new String[] { "Packet", "Matrix" });
        Velocity.cancelOtherDamage = new BooleanSetting("Cancel Other Damage", true, () -> true);
        this.addSettings(Velocity.velocityMode, Velocity.cancelOtherDamage);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        final String mode = Velocity.velocityMode.getOptions();
        this.setSuffix("" + mode);
        if (Velocity.cancelOtherDamage.getCurrentValue() && Velocity.mc.h.ay > 0 && event.getPacket() instanceof kf && !Velocity.mc.h.a(vb.l) && (Velocity.mc.h.a(vb.s) || Velocity.mc.h.a(vb.t) || Velocity.mc.h.aR())) {
            event.setCancelled(true);
        }
        if (mode.equalsIgnoreCase("Packet")) {
            final kf velocity;
            if ((event.getPacket() instanceof kf || event.getPacket() instanceof ja) && (velocity = (kf)event.getPacket()).a() == Velocity.mc.h.S()) {
                event.setCancelled(true);
            }
        }
        else if (mode.equals("Matrix") && Velocity.mc.h.ay > 8) {
            Velocity.mc.h.z = true;
        }
    }
}
