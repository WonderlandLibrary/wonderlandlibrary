package sudo.module.combat;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import sudo.module.Mod;
import sudo.utils.world.FindItemResult;
import sudo.utils.world.InventoryUtils;

public class AutoTotem extends Mod {

	List<Entity> entities;
	
	public AutoTotem() {
		super("AutoTotem", "Automatically puts a totem in your offhand", Category.COMBAT, 0);
	}
	@Override
	public void onTick() {
		nullCheck();
		if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            FindItemResult iTotem = InventoryUtils.find(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING, 0, 35);
            if (iTotem.found()) {
            	InventoryUtils.move().from(iTotem.getSlot()).toOffhand();
            } else return;
		}
		super.onTick();
	}
}