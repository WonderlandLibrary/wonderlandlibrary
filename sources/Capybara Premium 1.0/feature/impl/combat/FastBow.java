package fun.expensive.client.feature.impl.combat;

import fun.expensive.client.event.EventTarget;
import fun.expensive.client.event.events.impl.player.EventUpdate;
import fun.expensive.client.feature.Feature;
import fun.expensive.client.feature.impl.FeatureCategory;
import fun.expensive.client.ui.settings.impl.NumberSetting;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class FastBow extends Feature {

    private final NumberSetting bowDelay;

    public FastBow() {
        super("FastBow", "При зажатии на ПКМ игрок быстро стреляет из лука", FeatureCategory.Combat);
        this.bowDelay = new NumberSetting("Bow Delay", 1.0f, 1.0f, 10, 0.5f, () -> true);
        addSettings(bowDelay);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isBowing() && mc.player.getItemInUseMaxCount() >= bowDelay.getNumberValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.stopActiveHand();
        }
    }
}
