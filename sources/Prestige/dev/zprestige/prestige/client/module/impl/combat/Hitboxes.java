package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.EntityMarginEvent;
import dev.zprestige.prestige.client.event.impl.HitboxEvent;
import dev.zprestige.prestige.client.event.impl.RenderHitboxEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.setting.impl.MultipleSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;

public class Hitboxes extends Module {

    public FloatSetting expand;
    public MultipleSetting targets;
    public BooleanSetting render;
    public FloatSetting distance;
    public Entity entity;

    public Hitboxes() {
        super("Hitboxes", Category.Combat, "Expands entity hitboxes");
        expand = setting("Expand", 0.5f, 0, 5).description("How much to expand the hitboxes");
        targets = setting("Targets", new String[]{"Players", "Crystals"}, new Boolean[]{true, true}).description("What entities to expand the hitboxes of");
        render = setting("Render Hitboxes", true).description("Renders the hitboxes");
        distance = setting("Distance", 5, 0.1f, 20).description("How far away to render the hitboxes");
    }

    @EventListener
    public void event(EntityMarginEvent event) {
        if (event.equals(getMc().player) || !(event.getEntity() instanceof EndCrystalEntity) && !(event.getEntity() instanceof PlayerEntity)) {
            return;
        }
        if (event.getEntity() instanceof PlayerEntity && (!this.targets.getValue("Players") || !Prestige.Companion.getAntiBotManager().isNotBot(event.getEntity())) || event.getEntity() instanceof EndCrystalEntity && !this.targets.getValue("Crystals")) {
            return;
        }
        event.setMargin(event.getMargin() + this.expand.getObject() / 10);
        event.setCancelled();
    }

    @EventListener
    public void event(HitboxEvent event) {
        if (entity == event.getEntity() && render.getObject() && entity != null && entity.distanceTo(getMc().player) > distance.getObject()) {
            return;
        }
        if (event.getEntity() instanceof PlayerEntity && (!targets.getValue("Players") || !Prestige.Companion.getAntiBotManager().isNotBot(event.getEntity())) || event.getEntity() instanceof EndCrystalEntity && !targets.getValue("Crystals")) {
            return;
        }
        event.setBox(event.getBox().expand(expand.getObject() / 10));
        event.setCancelled();
    }

    @EventListener
    public void event(RenderHitboxEvent event) {
        entity = event.getEntity();
    }
}