// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import ru.tuskevich.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.MouseEvent;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "PearlThrowBlock", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.MISC)
public class PearlThrowBlock extends Module
{
    @EventTarget
    public void onMouse(final MouseEvent event) {
        if (event.button == 1) {
            final Minecraft mc = PearlThrowBlock.mc;
            if (Minecraft.player.getHeldItemMainhand().getItem() == Items.ENDER_PEARL) {
                final Minecraft mc2 = PearlThrowBlock.mc;
                Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }
    }
}
