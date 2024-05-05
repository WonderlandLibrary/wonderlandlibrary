package client.module.impl.movement;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.movement.inventorymove.VanillaInventoryMove;
import client.value.impl.ModeValue;

@ModuleInfo(name = "Inventory Move", description = "Allows you to move whilst in GUIs", category = Category.MOVEMENT)
public class InventoryMove extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaInventoryMove("Vanilla", this))
            .setDefault("Vanilla");
}
