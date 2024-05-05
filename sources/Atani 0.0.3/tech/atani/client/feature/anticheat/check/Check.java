package tech.atani.client.feature.anticheat.check;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import tech.atani.client.feature.anticheat.data.PlayerData;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.time.TimeHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Check implements Methods {

    private final String name;
    protected final PlayerData data;
    private final TimeHelper delay;

    private double buffer;

    public Check(PlayerData data) {
        this.name = getClass().getAnnotation(Info.class).name();
        this.data = data;
        this.delay = new TimeHelper();
    }

    public abstract void handle(Packet<? extends INetHandler> event);

    public double increaseBuffer() {
        buffer++;
        return buffer;
    }

    public double increaseBufferBy(double increase) {
        buffer += increase;
        return buffer;
    }

    public double reduceBuffer(double amount) {
        if(buffer > 0) {
            buffer -= amount;
        }
        return buffer;
    }

    public double resetBuffer() {
        buffer = 0;
        return buffer;
    }

    protected void flag() {
        if(delay.hasReached(5000)) {
            sendMessage("§c" + data.getPlayer().getCommandSenderName() + " §7failed §c" + name + "§7!");
            delay.reset();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
    }

    public String getName() {
        return name;
    }

    public PlayerData getData() {
        return data;
    }

    public TimeHelper getDelay() {
        return delay;
    }

    public double getBuffer() {
        return buffer;
    }
}
