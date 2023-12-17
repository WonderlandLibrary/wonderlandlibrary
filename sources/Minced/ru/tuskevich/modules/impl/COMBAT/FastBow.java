// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "FastBow", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.COMBAT)
public class FastBow extends Module
{
    private final SliderSetting ticks;
    
    public FastBow() {
        this.ticks = new SliderSetting("Delay", 3.0f, 1.0f, 10.0f, 0.5f);
        this.add(this.ticks);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        final Minecraft mc = FastBow.mc;
        if (Minecraft.player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
            final Minecraft mc2 = FastBow.mc;
            if (Minecraft.player.isHandActive()) {
                final Minecraft mc3 = FastBow.mc;
                if (Minecraft.player.getItemInUseMaxCount() >= this.ticks.getFloatValue()) {
                    final Minecraft mc4 = FastBow.mc;
                    final NetHandlerPlayClient connection = Minecraft.player.connection;
                    final CPacketPlayerDigging.Action release_USE_ITEM = CPacketPlayerDigging.Action.RELEASE_USE_ITEM;
                    final BlockPos origin = BlockPos.ORIGIN;
                    final Minecraft mc5 = FastBow.mc;
                    connection.sendPacket(new CPacketPlayerDigging(release_USE_ITEM, origin, Minecraft.player.getHorizontalFacing()));
                    final Minecraft mc6 = FastBow.mc;
                    Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    final Minecraft mc7 = FastBow.mc;
                    Minecraft.player.stopActiveHand();
                }
            }
        }
    }
}
