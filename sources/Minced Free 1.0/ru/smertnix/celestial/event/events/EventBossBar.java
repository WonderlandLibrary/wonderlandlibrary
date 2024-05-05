package ru.smertnix.celestial.event.events;


public class EventBossBar implements Event {
    public String bossName;
    public int x;
    public int y;
    public int z;
    public EventBossBar(String bossName) {
        this.bossName = bossName;
    }
    public EventBossBar() {
    }
    
}