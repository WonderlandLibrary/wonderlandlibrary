package net.minecraft.client.audio;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import net.minecraft.util.ResourceLocation;

static final class SoundManager.2
extends URLStreamHandler {
    final /* synthetic */ ResourceLocation val$p_148612_0_;

    SoundManager.2(ResourceLocation resourceLocation) {
        this.val$p_148612_0_ = resourceLocation;
    }

    protected URLConnection openConnection(URL p_openConnection_1_) {
        return new /* Unavailable Anonymous Inner Class!! */;
    }
}
