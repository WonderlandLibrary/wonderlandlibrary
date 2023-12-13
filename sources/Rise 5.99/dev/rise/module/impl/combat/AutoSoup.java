package dev.rise.module.impl.combat;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;

@ModuleInfo(name = "AutoSoup", description = "Eats soups when your health is getting low", category = Category.COMBAT)
public class AutoSoup extends Module {

    private final NumberSetting health = new NumberSetting("Health", this, 15, 0, 20, 1);
    private final NumberSetting minDelay = new NumberSetting("Min Delay", this, 300, 0, 1000, 25);
    private final NumberSetting maxDelay = new NumberSetting("Max Delay", this, 500, 0, 1000, 25);
    private final BooleanSetting dropBowl = new BooleanSetting("Drop Bowl", this, true);

    private final TimeUtil timer = new TimeUtil();
    private boolean switchBack;
    private long decidedTimer;
    private int soup = -37;

    @Override
    protected void onDisable() {
        switchBack = false;
        soup = -37;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (switchBack) {
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            if (dropBowl.isEnabled())
                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            switchBack = false;
            return;
        }

        if (timer.hasReached(decidedTimer)) {
            if (mc.thePlayer.ticksExisted > 10 && mc.thePlayer.getHealth() < health.getValue()) {
                soup = PlayerUtil.findSoup() - 36;

                if (soup != -37) {
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(soup));
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(soup)));
                    switchBack = true;
                }

                final int delayFirst = (int) Math.floor(Math.min(minDelay.getValue(), maxDelay.getValue()));
                final int delaySecond = (int) Math.ceil(Math.max(minDelay.getValue(), maxDelay.getValue()));

                decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);
                timer.reset();
            }
        }
    }
}
