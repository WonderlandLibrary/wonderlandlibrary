package io.github.raze.utilities.collection.world;

import io.github.raze.utilities.system.Methods;

public class ServerUtil implements Methods {

    public static String getServerAddress() {
        return mc.getCurrentServerData().serverIP.toLowerCase();
    }

}
