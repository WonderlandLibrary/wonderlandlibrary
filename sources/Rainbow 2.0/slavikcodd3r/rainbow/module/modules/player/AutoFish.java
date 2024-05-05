package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "AutoFish")
public class AutoFish extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (ClientUtils.player().getCurrentEquippedItem() != null && Item.getIdFromItem(ClientUtils.player().getCurrentEquippedItem().getItem()) == 346 && ClientUtils.player().fishEntity != null && ClientUtils.player().fishEntity.isInWater() && ClientUtils.player().fishEntity.ticksCatchable > 0) {
            ClientUtils.playerController().sendUseItem(ClientUtils.player(), ClientUtils.world(), ClientUtils.mc().thePlayer.inventory.getCurrentItem());
        }
    }
}