// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import ru.tuskevich.event.EventTarget;
import ru.tuskevich.util.chat.ChatUtility;
import ru.tuskevich.modules.impl.HUD.Notifications;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "DeathCoordinates", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class DeathCoords extends Module
{
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        final Minecraft mc = DeathCoords.mc;
        if (Minecraft.player.getHealth() < 1.0f && DeathCoords.mc.currentScreen instanceof GuiGameOver) {
            final Minecraft mc2 = DeathCoords.mc;
            final int x = Minecraft.player.getPosition().getX();
            final Minecraft mc3 = DeathCoords.mc;
            final int y = Minecraft.player.getPosition().getY();
            final Minecraft mc4 = DeathCoords.mc;
            final int z = Minecraft.player.getPosition().getZ();
            final Minecraft mc5 = DeathCoords.mc;
            if (Minecraft.player.deathTime < 1) {
                Notifications.notify("Death toggled", "X: " + x + " Y: " + y + " Z: " + z, Notifications.Notify.NotifyType.INFO, 10);
                ChatUtility.addChatMessage("Death Coordinates: X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}
