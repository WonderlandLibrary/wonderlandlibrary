package me.aquavit.liquidsense.module.modules.ghost;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "AutoBow", description = "Automatically shoots an arrow whenever your bow is fully loaded.", category = ModuleCategory.GHOST)
public class AutoBow extends Module {

    private BoolValue waitForBowAimbot = new BoolValue("WaitForBowAimbot", true);
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        BowAimbot bowAimbot = (BowAimbot) LiquidSense.moduleManager.getModule(BowAimbot.class);
        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() == Items.bow &&
                mc.thePlayer.isUsingItem() && mc.thePlayer.getItemInUseDuration() > 20 && (!waitForBowAimbot.get() || !bowAimbot.getState() || bowAimbot.hasTarget())) {
            mc.thePlayer.stopUsingItem();
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
    
}
