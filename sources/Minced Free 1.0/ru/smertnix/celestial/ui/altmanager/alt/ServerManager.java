package ru.smertnix.celestial.ui.altmanager.alt;

import java.util.ArrayList;

public class ServerManager {

    public static Server lastServer;
    public static ArrayList<Server> registry = new ArrayList<>();

    public static ArrayList<Server> getRegistry() {
        return registry;
    }

    public void setLastServer(Server Server) {
        lastServer = Server;
    }
    
    public static boolean inRegistry(String friend) {
        return registry.stream().anyMatch(isFriend -> isFriend.getName().equals(friend));
    }
}
