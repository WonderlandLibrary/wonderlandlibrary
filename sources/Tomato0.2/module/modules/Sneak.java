/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPostMotionUpdates;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sneak
extends Module {
    public Sneak() {
        super("Sneak", 0, Category.EXPLOITS, 23552, true, "Makes you sneak server side.", new String[]{"snek", "snak", "seak"});
    }

    @EventTarget
    public void onEvent(EventPreMotionUpdates pre) {
        Wrapper.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        Wrapper.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }

    @EventTarget
    public void onPost(EventPostMotionUpdates post) {
        Wrapper.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        Wrapper.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
    }

    @Override
    public void toggleModule() {
        super.toggleModule();
        if (!this.getState()) {
            Wrapper.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}

