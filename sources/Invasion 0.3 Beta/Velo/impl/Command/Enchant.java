package Velo.impl.Command;

import org.lwjgl.input.Keyboard;

import Velo.api.Command.Command;
import Velo.api.Main.Main;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;
import Velo.impl.Modules.combat.Criticals;
import Velo.impl.Modules.combat.Killaura;
import Velo.impl.Modules.combat.Velocity;
import Velo.impl.Modules.exploit.Disabler;
import Velo.impl.Modules.movement.Flight;
import Velo.impl.Modules.movement.InventoryMove;
import Velo.impl.Modules.movement.Longjump;
import Velo.impl.Modules.movement.Scaffold;
import Velo.impl.Modules.movement.Speed;
import Velo.impl.Modules.movement.Sprint;
import Velo.impl.Modules.player.Breaker;
import Velo.impl.Modules.player.ChestStealer;
import Velo.impl.Modules.player.FastEat;
import Velo.impl.Modules.player.InventoryManager;
import Velo.impl.Modules.player.NoFall;
import Velo.impl.Modules.player.NoRotate;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class Enchant extends Command {
	
	
	
	Killaura ka = new Killaura();
	Speed speed = new Speed();
	Velocity velocity = new Velocity();
	Disabler disabler = new Disabler();
	Breaker breaker = new Breaker();
	FastEat fasteat = new FastEat();
	
	ChestStealer cheststealer = new ChestStealer();
	NoRotate norotate = new NoRotate();
	InventoryMove invmove = new InventoryMove();
	Criticals criticals = new Criticals();
	Longjump longjump = new Longjump();
	Sprint sprint = new Sprint();
	Scaffold scaffold = new Scaffold();
	Flight flight = new Flight();
	NoFall nofall = new NoFall();
	InventoryManager invmanager = new InventoryManager();
	
	
	public Enchant() {
		super("Enchant", "Enchant Items", "Enchant <Enchantment Name> <Level>", "ench");
	}

	@Override
	public void onCommand(String[] args, String command) {
		boolean foundConfig = false;
	       if (args.length > 2) {
	            if (mc.playerController.isNotCreative()) {
	                ChatUtil.addChatMessage("§c§lError: §3You need to be in creative mode.");
	                return;
	            }
	       
				// Fly
	            ItemStack item = mc.thePlayer.getHeldItem();
				
	            if (item.item == null) {
	            	ChatUtil.addChatMessage("§c§lError: §3You need to hold an item.");
	                return;
	            }
				
	            int enchantID = args[1].length();
	        
	            Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);

	                if (enchantment == null) {
	                	ChatUtil.addChatMessage("There is no enchantment with the name '${args[1]}'");
	                    return;
	                }
	       }

            
           
	       int enchantID = args[1].length();
	       Enchantment enchantment = Enchantment.getEnchantmentById(enchantID);

            if (enchantment == null) {
            	ChatUtil.addChatMessage("There is no enchantment with the ID '$enchantID'");
                return;
            }

            int level = args[2].length();
            ItemStack item = mc.thePlayer.getHeldItem();

            item.addEnchantment(enchantment, level);
            mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(36 + mc.thePlayer.inventory.currentItem, item));
            ChatUtil.addChatMessage("${enchantment.getTranslatedName(level)} added to ${item.displayName}.");
            return;
		
	}
	
}
	
	


