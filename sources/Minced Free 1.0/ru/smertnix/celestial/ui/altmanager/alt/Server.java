package ru.smertnix.celestial.ui.altmanager.alt;

import net.minecraft.util.text.TextFormatting;

public class Server {

    private final String name;
    private String ip;


    public Server(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return this.name;
    }
    
    public String getIp() {
        return this.ip;
    }
}