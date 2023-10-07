package viaversion.viaversion.api.type.types.minecraft;

import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.type.Type;

public abstract class BaseItemType extends Type<Item> {
    public BaseItemType() {
        super("Item", Item.class);
    }

    public BaseItemType(String typeName) {
        super(typeName, Item.class);
    }

    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseItemType.class;
    }
}
