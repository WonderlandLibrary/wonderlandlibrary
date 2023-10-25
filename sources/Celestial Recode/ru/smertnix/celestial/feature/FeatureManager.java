package ru.smertnix.celestial.feature;

import net.minecraft.client.Minecraft;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.combat.*;
import ru.smertnix.celestial.feature.impl.hud.*;
import ru.smertnix.celestial.feature.impl.misc.*;
import ru.smertnix.celestial.feature.impl.movement.*;
import ru.smertnix.celestial.feature.impl.player.*;
import ru.smertnix.celestial.feature.impl.visual.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeatureManager {

    public CopyOnWriteArrayList<Feature> features = new CopyOnWriteArrayList<>();

    public FeatureManager() {

        /* HUD */
        features.add(new Criticals());
        features.add(new FeatureList());
        features.add(new Notifications());
        features.add(new NoRender());
        features.add(new ClickGUI());
        features.add(new Hud());
        /* MISC */
        //features.add(new RWSafe());
        features.add(new AirStealer());
        features.add(new Optimizer());
        features.add(new GriefJoiner());
        features.add(new FastWorldLoad());
        features.add(new ElytraFix());
        features.add(new MiddleClickFriend());
        features.add(new ChatHistory());
        features.add(new PearlThrowBlock());
        features.add(new NameProtect());
        features.add(new SuperBow());
        /* COMBAT */
        features.add(new CrystalAura());
        features.add(new ItemFix());
        features.add(new ShieldBreaker());
        features.add(new AppleGoldenTimer());
        features.add(new AutoPotion());
        features.add(new BowSpam());
        features.add(new AutoGapple());
        features.add(new AutoTotem());
        features.add(new AttackAura());
        features.add(new Velocity());
        features.add(new HitBox());
        /* MOVEMENT */
        features.add(new WebLeave());
        features.add(new DamageSpeed());
        features.add(new WaterSpeed());
        features.add(new LongJump());
        features.add(new HighJump());
        features.add(new Spider());
        features.add(new AirJump());
        features.add(new Flight());
        features.add(new AutoSprint());
        features.add(new Speed());
        features.add(new Jesus());
        features.add(new Strafe());
        features.add(new Timer());
        features.add(new NoWeb());
        /* PLAYER */
        features.add(new AutoEat());
        features.add(new AutoRespawn());
        features.add(new AutoLeave());
        features.add(new AntiLevitation());
        features.add(new MiddleClickPearl());
        features.add(new DeathCoords());
        features.add(new ItemScroller());
        features.add(new AntiAFK());
        features.add(new AirDropWay());
        features.add(new AutoTPAccept());
        features.add(new NoJumpDelay());
        features.add(new NoInteract());
        features.add(new NoSlowDown());
        features.add(new StaffAlert());
        features.add(new FreeCamera());
        features.add(new AutoTool());
        features.add(new InventoryMove());
        features.add(new NoClip());
        /* VISUALS */
        features.add(new Particle());
        features.add(new Crosshair());
        features.add(new ChunkAnimator());
        features.add(new ShaderESP());
        features.add(new Prediction());
        features.add(new SwingAnimations());
        features.add(new JumpCircle());
        features.add(new ESP());
        features.add(new TimeChanger());
        features.add(new CustomModel());
        features.add(new CustomFog());
        features.add(new ChinaHat());
        features.add(new Trails());
        features.add(new BlockESP());
        features.add(new Tracers());
        features.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().mntsb_15.getStringWidth(((Feature) m).getLabel())).reversed());

    }

    public List<Feature> getAllFeatures() {
        return this.features;
    }

    public List<Feature> getFeaturesCategory(FeatureCategory category) {
        List<Feature> features = new ArrayList<>();
        for (Feature feature : getAllFeatures()) {
            if (feature.getCategory() == category) {
                features.add(feature);
            }
        }
        return features;
    }

    public Feature getFeature(Class<? extends Feature> classFeature) {
        for (Feature feature : getAllFeatures()) {
            if (feature != null) {
                if (feature.getClass() == classFeature) {
                    return feature;
                }
            }
        }
        return null;
    }

    public Feature getFeature(String name) {
        for (Feature feature : getAllFeatures()) {
            if (feature.getLabel().replaceAll(" ", "").equals(name.replaceAll(" ", ""))) {
                return feature;
            }
        }
        return null;
    }
}
