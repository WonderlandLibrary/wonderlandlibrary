package io.github.nevalackin.client.module.misc.inventory;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.property.BooleanProperty;

public final class Inventory extends Module {

    private final BooleanProperty moveInsideGUIProperty = new BooleanProperty("Move Inside GUIs", true);
    // TODO :: More Inventory settings

    public Inventory() {
        super("Inventory+", Category.MISC, Category.SubCategory.MISC_INVENTORY);

        this.register(this.moveInsideGUIProperty);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
