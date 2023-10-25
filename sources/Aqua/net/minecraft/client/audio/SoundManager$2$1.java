package net.minecraft.client.audio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import net.minecraft.client.Minecraft;

class SoundManager.1
extends URLConnection {
    SoundManager.1(URL x0) {
        super(x0);
    }

    public void connect() throws IOException {
    }

    public InputStream getInputStream() throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(val$p_148612_0_).getInputStream();
    }
}
