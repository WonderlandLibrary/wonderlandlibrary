package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecureTextureException;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;

/*
 * Exception performing whole class analysis ignored.
 */
class SkinManager.3
implements Runnable {
    final /* synthetic */ GameProfile val$profile;
    final /* synthetic */ boolean val$requireSecure;
    final /* synthetic */ SkinManager.SkinAvailableCallback val$skinAvailableCallback;

    SkinManager.3(GameProfile gameProfile, boolean bl, SkinManager.SkinAvailableCallback skinAvailableCallback) {
        this.val$profile = gameProfile;
        this.val$requireSecure = bl;
        this.val$skinAvailableCallback = skinAvailableCallback;
    }

    public void run() {
        HashMap map = Maps.newHashMap();
        try {
            map.putAll(SkinManager.access$000((SkinManager)SkinManager.this).getTextures(this.val$profile, this.val$requireSecure));
        }
        catch (InsecureTextureException insecureTextureException) {
            // empty catch block
        }
        if (map.isEmpty() && this.val$profile.getId().equals((Object)Minecraft.getMinecraft().getSession().getProfile().getId())) {
            this.val$profile.getProperties().clear();
            this.val$profile.getProperties().putAll((Multimap)Minecraft.getMinecraft().getProfileProperties());
            map.putAll(SkinManager.access$000((SkinManager)SkinManager.this).getTextures(this.val$profile, false));
        }
        Minecraft.getMinecraft().addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */);
    }
}
