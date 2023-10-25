package fun.expensive.client.feature.impl.combat;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventSendPacket;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class SuperItems
        extends Feature {
    private BooleanSetting bow;
    private BooleanSetting eggs;
    private BooleanSetting pearls;
    private BooleanSetting snowballs;
    private NumberSetting shotPower = new NumberSetting("Shot Power", 70.0f, 1.0f, 100.0f, 5.0f, () -> true);

    public SuperItems() {
        super("SuperItems", "Убивает противников выстрелом из лука, эндер перла, снежка и т.д с 1 попадания", FeatureCategory.Combat);
        bow = new BooleanSetting("Bow", "Убивает противников выстрелом из лука с 1 попадания", true, () -> true);
        eggs = new BooleanSetting("Eggs", "Убивает противников выстрелом из яйца с 1 попадания", false, () -> true);
        pearls = new BooleanSetting("Pearls", "Убивает противников выстрелом эндерперлом с 1 попадания", false, () -> true);
        snowballs = new BooleanSetting("Snowballs", "Убивает противников выстрелом снежком с 1 попадания", false, () -> true);
        addSettings(shotPower, bow, eggs, pearls, snowballs);
    }

    private void doShot() {
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
        int index = 0;
        while ((float) index < shotPower.getNumberValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0E-10, mc.player.posZ, false));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1.0E-10, mc.player.posZ, true));
            ++index;
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        ItemStack handStack;
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            ItemStack handStack2;
            CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && !(handStack2 = mc.player.getHeldItem(EnumHand.MAIN_HAND)).func_190926_b() && handStack2.getItem() instanceof ItemBow && bow.getBoolValue()) {
                doShot();
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && ((CPacketPlayerTryUseItem) event.getPacket()).getHand() == EnumHand.MAIN_HAND && !(handStack = mc.player.getHeldItem(EnumHand.MAIN_HAND)).func_190926_b()) {
            if (handStack.getItem() instanceof ItemEgg && eggs.getBoolValue()) {
                doShot();
            } else if (handStack.getItem() instanceof ItemEnderPearl && pearls.getBoolValue()) {
                doShot();
            } else if (handStack.getItem() instanceof ItemSnowball && snowballs.getBoolValue()) {
                doShot();
            }
        }
    }
}