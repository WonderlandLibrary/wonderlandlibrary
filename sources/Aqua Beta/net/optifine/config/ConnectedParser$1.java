package net.optifine.config;

import net.optifine.config.INameGetter;

static final class ConnectedParser.1
implements INameGetter<Enum> {
    ConnectedParser.1() {
    }

    public String getName(Enum en) {
        return en.name();
    }
}
