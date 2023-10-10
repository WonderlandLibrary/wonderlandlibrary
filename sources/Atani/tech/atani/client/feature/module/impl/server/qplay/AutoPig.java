package tech.atani.client.feature.module.impl.server.qplay;

import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.StringUtils;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.math.time.TimeHelper;

@ModuleData(name = "AutoPig", identifier = "mc.qplay.cz AutoPig", description = "Automatically uses golden pigs", category = Category.SERVER, supportedIPs = {"mc.qplay.cz"})
public class AutoPig extends Module {

    public SliderValue<Float> health = new SliderValue<>("Health", "At what health will it use pigs?", this, 3f, 0f, 20f, 1);
    public SliderValue<Integer> delay = new SliderValue<>("Delay", "What will be the delay between uses?", this, 500, 0, 10000, 0);

    private TimeHelper delayTimer = new TimeHelper();

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        final float health = this.health.getValue();
        if(mc.thePlayer.getHealth() < health) {
            for (int slot = 36; slot < 45; slot++) {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if(stack != null && (StringUtils.stripControlCodes(stack.getDisplayName()).toLowerCase().contains("golden pig")) && delayTimer.hasReached(delay.getValue())) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slot - 36));
                    mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer.getSlot(slot).getStack()));
                    mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    delayTimer.reset();
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}