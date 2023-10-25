package net.minecraft.server.management;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;

static final class PlayerProfileCache.2
implements ProfileLookupCallback {
    final /* synthetic */ GameProfile[] val$agameprofile;

    PlayerProfileCache.2(GameProfile[] gameProfileArray) {
        this.val$agameprofile = gameProfileArray;
    }

    public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
        this.val$agameprofile[0] = p_onProfileLookupSucceeded_1_;
    }

    public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
        this.val$agameprofile[0] = null;
    }
}
