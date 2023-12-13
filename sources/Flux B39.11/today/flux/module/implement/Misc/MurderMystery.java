package today.flux.module.implement.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMap;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.event.RespawnEvent;
import today.flux.event.TickEvent;
import today.flux.event.WorldInitEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.ChatUtils;
import today.flux.utility.WorldUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 2017/04/26.
 */
public class MurderMystery extends Module {
	public static final String MurderPrefix = "Murder";
	private List<String> alartedPlayers = new ArrayList<>();
	private static EntityPlayer murder;

	public MurderMystery() {
		super("MurderMystery", Category.Misc, false);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		if (this.alartedPlayers != null) {
			this.alartedPlayers.clear();
		}
	}
	
	@EventTarget
	public void onRespawn(RespawnEvent event) {
		if (this.alartedPlayers != null) {
			this.alartedPlayers.clear();
		}
	}
	
	@EventTarget
	public void onWorldInit(WorldInitEvent event) {
		if (this.alartedPlayers != null) {
			this.alartedPlayers.clear();
		}
	}

	@EventTarget
	public void onTick(TickEvent event) {
		if (this.mc.theWorld == null || this.alartedPlayers == null)
			return;

		try {
			for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
				if (this.alartedPlayers.contains(player.getName()))
					continue;

				if (player.getCurrentEquippedItem() != null) {
					if (CheckItem(player.getCurrentEquippedItem().getItem())) {
						ChatUtils.sendMessageToPlayer(EnumChatFormatting.GOLD + player.getName() + EnumChatFormatting.RESET
								+ " is the murderer!!!");

						this.alartedPlayers.add(player.getName());
						murder = player;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isMurder(EntityPlayer player) {
		if (player == null || murder == null)
			return false;

		if (player.isDead || player.isInvisible())
			return false;

		if (!player.equals(murder))
			return false;

		return true;
	}
	
	
    public boolean CheckItem(Item item){
    	if(item instanceof ItemMap || item.getUnlocalizedName().equalsIgnoreCase("item.ingotGold") ||
    			item instanceof ItemBow || item.getUnlocalizedName().equalsIgnoreCase("item.arrow") ||
    			item.getUnlocalizedName().equalsIgnoreCase("item.potion") ||
    			item.getUnlocalizedName().equalsIgnoreCase("item.paper") ||
    			item.getUnlocalizedName().equalsIgnoreCase("tile.tnt") || 
    			item.getUnlocalizedName().equalsIgnoreCase("item.web") ||
    			item.getUnlocalizedName().equalsIgnoreCase("item.bed") || 
    			item.getUnlocalizedName().equalsIgnoreCase("item.compass") || 
    			item.getUnlocalizedName().equalsIgnoreCase("item.comparator") ||
    			item.getUnlocalizedName().equalsIgnoreCase("item.shovelWood")){
    		return false;
    	}
    	return true;
    }
}
