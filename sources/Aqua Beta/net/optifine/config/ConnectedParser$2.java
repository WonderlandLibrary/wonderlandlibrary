package net.optifine.config;

import net.minecraft.item.EnumDyeColor;
import net.optifine.config.INameGetter;

static final class ConnectedParser.2
implements INameGetter<EnumDyeColor> {
    ConnectedParser.2() {
    }

    public String getName(EnumDyeColor col) {
        return col.getName();
    }
}
