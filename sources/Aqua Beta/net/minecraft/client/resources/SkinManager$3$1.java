package net.minecraft.client.resources;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;

class SkinManager.1
implements Runnable {
    final /* synthetic */ Map val$map;

    SkinManager.1(Map map) {
        this.val$map = map;
    }

    public void run() {
        if (this.val$map.containsKey((Object)MinecraftProfileTexture.Type.SKIN)) {
            this$0.loadSkin((MinecraftProfileTexture)this.val$map.get((Object)MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, val$skinAvailableCallback);
        }
        if (this.val$map.containsKey((Object)MinecraftProfileTexture.Type.CAPE)) {
            this$0.loadSkin((MinecraftProfileTexture)this.val$map.get((Object)MinecraftProfileTexture.Type.CAPE), MinecraftProfileTexture.Type.CAPE, val$skinAvailableCallback);
        }
    }
}
