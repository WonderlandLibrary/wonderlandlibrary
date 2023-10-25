package ru.smertnix.celestial.feature.impl.misc;

import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class Optimizer extends Feature {

    public static BooleanSetting q;
    public static BooleanSetting w;
    public static BooleanSetting e;
    public static BooleanSetting z;
    public static NumberSetting c;
    public static BooleanSetting x;

    public Optimizer() {
        super("Minecraft Optimizer", "Оптимизирует майнкрафт", FeatureCategory.Util);
        q = new BooleanSetting("Remove Entities", true, () -> true);
        w = new BooleanSetting("Memory Clean", true, () -> true);
        e = new BooleanSetting("Disable Sky", false, () -> true);
        z = new BooleanSetting("Disable Particles", false, () -> true);
        x = new BooleanSetting("Auto Optifine", true, () -> true);
        c = new NumberSetting("Render Distance", 32, 1, 64, 1,() -> true);
        addSettings(q, w, e ,z, x, c);
    }
    
    public void onUpdate(EventUpdate e) {
    	 long i = Runtime.getRuntime().maxMemory();
         long j = Runtime.getRuntime().totalMemory();
         long k = Runtime.getRuntime().freeMemory();
         long l = j - k;
         
         if (w.getBoolValue()) {
        	 System.gc();
         }
         
         if (this.e.getBoolValue()) {
        	 mc.gameSettings.ofSky = false;
        	 mc.gameSettings.ofCustomSky = false;
         }
         
         if (this.z.getBoolValue()) {
        	 mc.gameSettings.ofVoidParticles = false;
         }
    }
}