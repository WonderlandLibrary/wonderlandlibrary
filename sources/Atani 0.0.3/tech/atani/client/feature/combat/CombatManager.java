package tech.atani.client.feature.combat;

import net.minecraft.entity.Entity;
import tech.atani.client.listener.event.minecraft.game.RunTickEvent;
import tech.atani.client.listener.handling.EventHandling;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.combat.interfaces.IgnoreList;

import java.util.ArrayList;
import java.util.List;

public class CombatManager implements Methods {

    private static CombatManager instance;
    private final List<Entity> ignored = new ArrayList<>();
    private final List<IgnoreList> ignoreLists = new ArrayList<>();

    public void init() {
        EventHandling.getInstance().registerListener(this);
    }

    @Listen
    public void onTick(RunTickEvent runTickEvent) {
        this.ignored.clear();

        if(mc.theWorld == null || mc.thePlayer == null)
            return;

        for(Entity entity : mc.theWorld.loadedEntityList) {
            for(IgnoreList ignoreList : ignoreLists) {
                if(ignoreList.shouldSkipEntity(entity)) {
                    this.ignored.add(entity);
                }
            }
        }
    }

    public void addIgnoreList(IgnoreList ignoreList) {
        this.ignoreLists.add(ignoreList);
    }

    public final boolean isIgnored(Entity entity) {
        return ignored.stream().filter(entity1 -> entity1.getEntityId() == entity.getEntityId()).findFirst().orElse(null) != null;
    }

    public final List<Entity> getIgnored() {
        return ignored;
    }

    public static CombatManager getInstance() {
        return instance;
    }

    public static void setInstance(CombatManager instance) {
        CombatManager.instance = instance;
    }
}
