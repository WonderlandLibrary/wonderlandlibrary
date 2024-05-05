package net.minecraft.server.management;

import com.mojang.authlib.GameProfile;
import java.util.Date;

class PlayerProfileCache.ProfileEntry {
    private final GameProfile gameProfile;
    private final Date expirationDate;

    private PlayerProfileCache.ProfileEntry(GameProfile gameProfileIn, Date expirationDateIn) {
        this.gameProfile = gameProfileIn;
        this.expirationDate = expirationDateIn;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    static /* synthetic */ Date access$200(PlayerProfileCache.ProfileEntry x0) {
        return x0.expirationDate;
    }
}
