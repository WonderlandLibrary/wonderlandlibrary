/*
package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.utils.ServerUtils;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.checkerframework.checker.nullness.qual.NonNull;
import viaversion.viafabric.ViaFabric;

public class FastUse extends AbstractModule {

    @Property("tick")
    private final IntProperty tick = PropertyFactory.createInt(4).maximum(20).minimum(1);

    public FastUse(@NonNull ModuleManager novoline) {
        super(novoline, EnumModuleType.PLAYER, "FastUse", "Fast Use");
        Manager.put(new Setting("FS_TICK", "Tick", SettingType.SLIDER, this, tick, 1));
    }

    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.POST)) {
            if (mc.player.isUsingItem() && (mc.player.getItemInUse().getItem() instanceof ItemFood
                    || mc.player.getItemInUse().getItem() instanceof ItemPotion
                    || mc.player.getItemInUse().getItem() instanceof ItemBucketMilk)) {
                if (tick.get() == 1 || mc.player.getItemInUseDuration() == tick.get()) {
                    if (ServerUtils.isHypixel()) {
                        if (ViaFabric.clientSideVersion == novoline.viaVersion()) {
                            for (int i = 0; i < 30; i++) {
                                sendPacketNoEvent(new C03PacketPlayer(false));
                                sendPacketNoEvent(new C09PacketHeldItemChange(mc.player.inventory.currentItem));
                            }
                        }

                    } else {
                        for (int i = 0; i < 20; i++) {
                            sendPacketNoEvent(new C03PacketPlayer(false));
                        }
                    }
                }
            }
        }
    }
}
*/
