package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;

@ModuleInfo(name = "RenderChanger", description = "Can change u body render", category = ModuleCategory.CLIENT)
public class RenderChanger extends Module {
    //public static final BoolValue itemToViewValue = new BoolValue("Item-To-View", false);
    public static final BoolValue bigHeadsValue = new BoolValue("Big-Heads", false);
    public static final BoolValue flipEntitiesValue = new BoolValue("Flip-Entities", false);
    public static final BoolValue littleEntitiesValue = new BoolValue("Little-Entities", false);

    public static final IntegerValue armorRed = new IntegerValue("Armor-Glint-Red", 225, 0, 255);
    public static final IntegerValue armorGreen = new IntegerValue("Armor-Glint-Green", 225, 0, 255);
    public static final IntegerValue armorBlue = new IntegerValue("Armor-Glint-Blue", 225, 0, 255);
    public static final IntegerValue armorAlpha = new IntegerValue("Armor-Alpha", 85, 0, 255);

    public static final BoolValue riderValue = new BoolValue("Rider", false);
    public static final BoolValue sleeperValue = new BoolValue("Sleeper", false);
    public static final BoolValue corpseValue = new BoolValue("Corpse", false);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (corpseValue.get()) {
            mc.thePlayer.deathTime = 10;
        }
    }
}
