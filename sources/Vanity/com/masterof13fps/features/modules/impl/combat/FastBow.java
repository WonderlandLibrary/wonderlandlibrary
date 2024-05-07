package com.masterof13fps.features.modules.impl.combat;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "FastBow", category = Category.COMBAT, description = "You shoot your arrows like ratatatatam")
public class FastBow extends Module {

    public Setting mode = new Setting("Mode", this, "Vanilla", new String[]{"Vanilla", "NCP"});

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (mc.thePlayer.inventory.getCurrentItem() != null) {
                if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
                    switch (mode.getCurrentMode()) {
                        case "Vanilla": {
                            doVanilla();
                            break;
                        }
                        case "NCP": {
                            doNCP();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void doNCP() {
        if (getPlayer().getItemInUseDuration() >= 15) {
            for (int loop = 0; loop < 25; loop++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
            }
            mc.playerController.onStoppedUsingItem(mc.thePlayer);
        }
    }

    private void doVanilla() {
        getPlayerController().sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
        Item item = getPlayer().inventory.getCurrentItem().getItem();
        ItemStack itemStack = getPlayer().inventory.getCurrentItem();
        item.onItemRightClick(itemStack, mc.theWorld, mc.thePlayer);
        for (int i = 0; i < 20; ++i) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
        }
        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
        item = mc.thePlayer.inventory.getCurrentItem().getItem();
        itemStack = mc.thePlayer.inventory.getCurrentItem();
        item.onPlayerStoppedUsing(itemStack, mc.theWorld, mc.thePlayer, 10);
    }
}
