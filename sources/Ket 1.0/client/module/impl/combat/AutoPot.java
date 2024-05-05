package client.module.impl.combat;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.movement.Flight;
import client.module.impl.other.Scaffold;
import client.value.impl.BooleanValue;
import client.value.impl.NumberValue;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(name = "Auto Potion", description = "", category = Category.COMBAT)
public class AutoPot extends Module {


}