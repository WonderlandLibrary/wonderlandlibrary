package net.minecraft.server.integrated;

import java.util.concurrent.Callable;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;

class IntegratedServer.2
implements Callable<String> {
    IntegratedServer.2() {
    }

    public String call() throws Exception {
        String s = ClientBrandRetriever.getClientModName();
        if (!s.equals((Object)"vanilla")) {
            return "Definitely; Client brand changed to '" + s + "'";
        }
        s = IntegratedServer.this.getServerModName();
        return !s.equals((Object)"vanilla") ? "Definitely; Server brand changed to '" + s + "'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.");
    }
}
