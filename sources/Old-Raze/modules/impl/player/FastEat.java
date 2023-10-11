package markgg.modules.impl.player;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "FastEat", category = Module.Category.PLAYER)
public class FastEat extends Module {


	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			if(mc.thePlayer.isUsingItem()) {
				Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
				if(item instanceof ItemFood || item instanceof ItemPotion || item instanceof ItemBucketMilk) {
					for(int i=0;i<20;i++) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
					}
					mc.thePlayer.stopUsingItem();
				}
			}
		}
	};

}
