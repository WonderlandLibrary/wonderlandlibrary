package client.bot;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.WorldLoadEvent;
import lombok.Getter;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BotManager extends ArrayList<Entity> {

    @Getter
    private final List<String> friends = new ArrayList<>();

    public void init() {
        Client.INSTANCE.getEventBus().register(this);
    }

    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> clear();

    public boolean add(Entity entity) {
        if (!contains(entity)) super.add(entity);
        return false;
    }
}
