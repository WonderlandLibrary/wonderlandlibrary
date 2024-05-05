package net.minecraft.server.management;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PreYggdrasilConverter;

/*
 * Exception performing whole class analysis ignored.
 */
static final class PreYggdrasilConverter.2
implements ProfileLookupCallback {
    final /* synthetic */ MinecraftServer val$minecraftserver;
    final /* synthetic */ List val$list;

    PreYggdrasilConverter.2(MinecraftServer minecraftServer, List list) {
        this.val$minecraftserver = minecraftServer;
        this.val$list = list;
    }

    public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
        this.val$minecraftserver.getPlayerProfileCache().addEntry(p_onProfileLookupSucceeded_1_);
        this.val$list.add((Object)p_onProfileLookupSucceeded_1_);
    }

    public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
        PreYggdrasilConverter.access$000().warn("Could not lookup user whitelist entry for " + p_onProfileLookupFailed_1_.getName(), (Throwable)p_onProfileLookupFailed_2_);
    }
}
