package cc.novoline.modules.misc;


import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Mouse;

public class MiddleClick extends AbstractModule {

    @Property("pearl")
    private final BooleanProperty pearl = PropertyFactory.booleanTrue();
    @Property("friend")
    private final BooleanProperty friend = PropertyFactory.booleanTrue();

    public MiddleClick(@NonNull ModuleManager novoline) {
        super(novoline, EnumModuleType.MISC, "MiddleClick", "Middle Click");
        Manager.put(new Setting("MC_FRIEND", "Friend", SettingType.CHECKBOX, this, friend));
        Manager.put(new Setting("MC_PEARL", "Pearl", SettingType.CHECKBOX, this, pearl));
    }

    private boolean down = true;

    private int pearlSlot() {
        return mc.player.getSlotByItem(Items.ender_pearl);
    }

    private void sendHeldItemChange(int slot) {
        if (mc.player.inventory.currentItem != pearlSlot()) {
            sendPacketNoEvent(new C09PacketHeldItemChange(slot));
        }
    }

    @EventTarget
    public void onPre(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (Mouse.isButtonDown(2)) {
                if (down) {
                    if (mc.objectMouseOver.entityHit == null || !(mc.objectMouseOver.entityHit instanceof EntityPlayer) || !friend.get()) {
                        if (pearlSlot() != -1 && pearl.get()) {
                            sendHeldItemChange(pearlSlot());
                            sendPacket(new C08PacketPlayerBlockPlacement(mc.player.getHeldItem()));
                            sendHeldItemChange(mc.player.inventory.currentItem);
                        }
                    } else {
                        EntityPlayer player = (EntityPlayer) mc.objectMouseOver.entityHit;
                        if (novoline.getPlayerManager().hasType(player.getName(), PlayerManager.EnumPlayerType.FRIEND)) {
                            mc.player.sendChatMessage(".f remove " + player.getName());
                        } else {
                            mc.player.sendChatMessage(".f add " + player.getName());
                        }
                    }
                    down = false;
                }

            } else {
                down = true;
            }
        }
    }
}
