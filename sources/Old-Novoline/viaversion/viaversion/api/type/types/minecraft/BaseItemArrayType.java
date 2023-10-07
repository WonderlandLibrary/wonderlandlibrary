package viaversion.viaversion.api.type.types.minecraft;

import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.type.Type;

public abstract class BaseItemArrayType extends Type<Item[]> {
    public BaseItemArrayType() {
        super("Item[]", Item[].class);
    }

    public BaseItemArrayType(String typeName) {
        super(typeName, Item[].class);
    }

    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseItemArrayType.class;
    }
}
