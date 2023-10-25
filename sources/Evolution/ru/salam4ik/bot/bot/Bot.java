package ru.salam4ik.bot.bot;

import io.netty.util.internal.ConcurrentSet;
import ru.salam4ik.bot.bot.entity.BotController;
import ru.salam4ik.bot.bot.entity.BotPlayer;
import ru.salam4ik.bot.bot.network.BotNetwork;
import ru.salam4ik.bot.bot.network.BotPlayClient;
import ru.salam4ik.bot.bot.world.BotWorld;
import wtf.evolution.helpers.animation.Counter;

import java.util.Set;

public class Bot {
    private final BotController controller;
    private final BotNetwork netManager;
    private final BotPlayer bot;
    public static Set<Bot> bots;
    private final BotWorld world;

    public Counter c = new Counter();
    private final BotPlayClient connection;

    public long systemTime = System.currentTimeMillis();

    public BotController getController() {
        return this.controller;
    }

    public Bot(BotNetwork botNetwork, BotPlayClient botPlayClient, BotController botController, BotPlayer botPlayer, BotWorld botWorld) {
        this.netManager = botNetwork;
        this.connection = botPlayClient;
        this.controller = botController;
        this.bot = botPlayer;
        this.world = botWorld;
    }

    public long getTime() {
        return System.currentTimeMillis() - systemTime;
    }

    public BotPlayClient getConnection() {
        return this.connection;
    }

    public BotWorld getWorld() {
        return this.world;
    }

    public BotNetwork getNetManager() {
        return this.netManager;
    }

    public BotPlayer getBot() {
        return this.bot;
    }

    static {
        bots = new ConcurrentSet<>();
    }
}
 