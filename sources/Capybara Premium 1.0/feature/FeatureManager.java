package fun.expensive.client.feature;

import fun.expensive.client.feature.impl.FeatureCategory;
import fun.expensive.client.feature.impl.combat.*;
import fun.expensive.client.feature.impl.hud.*;
import fun.expensive.client.feature.impl.misc.*;
import fun.expensive.client.feature.impl.movement.*;
import fun.expensive.client.feature.impl.player.*;
import fun.expensive.client.feature.impl.visual.*;
import fun.expensive.client.ui.notification.Notification;
import fun.expensive.client.ui.notification.Notification;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeatureManager {

    public CopyOnWriteArrayList<Feature> features = new CopyOnWriteArrayList<>();

    public FeatureManager() {

        /* HUD */
        features.add(new FeatureList());
        features.add(new Notifications());
        features.add(new TargetHUD());
        features.add(new NoOverlay());
        features.add(new ClickGUI());
        features.add(new Hud());
        /* MISC */
        features.add(new MiddleClickFriend());
        features.add(new ModuleSoundAlert());
        features.add(new KillMessage());
        features.add(new PlayerTracker());
        features.add(new DeathSound());
        features.add(new ChatHistory());
        features.add(new NameProtect());
        features.add(new BetterChat());
        features.add(new Disabler());
        /* COMBAT */
        features.add(new AppleGoldenTimer());
        features.add(new TargetStrafe());
        features.add(new NoFriendDamage());
        features.add(new TriggerBot());
        features.add(new PushAttack());

        //scam
        features.add(new AntiCrystal());

        features.add(new AutoPotion());
        features.add(new HitSounds());
        features.add(new AutoArmor());
        features.add(new SuperItems());
        features.add(new FastBow());
        features.add(new AutoGapple());
        features.add(new AutoTotem());
        features.add(new KillAura());
        features.add(new KeepSprint());


        features.add(new Velocity());
        features.add(new AntiBot());
        features.add(new HitBox());
        /* MOVEMENT */
        features.add(new AutoParkour());
        features.add(new DamageFly());
        //  features.add(new ElytraStrafe());
        features.add(new Phase());
        features.add(new WaterSpeed());
        features.add(new HighJump());
        features.add(new Spider());
        features.add(new AirJump());
        features.add(new Flight());
        features.add(new Sprint());
        features.add(new Speed());
        features.add(new Jesus());
        features.add(new Strafe());
        features.add(new Timer());
        features.add(new NoWeb());
        features.add(new ElytraFly());


        //scam

        features.add(new ElytraFix());
        features.add(new XCarry());
        features.add(new FastWorldLoading());
        features.add(new ElytraHighJump());
        features.add(new TeleportBack());
        features.add(new AutoCrystal());
        features.add(new Criticals());
        features.add(new GlideFly());
        features.add(new FastPlace());

        /* PLAYER */
        features.add(new NoServerRotations());
        features.add(new MiddleClickPearl());
        features.add(new DeathCoordinates());
        features.add(new ItemScroller());
        features.add(new BedrockClip());
        features.add(new AntiAFK());
        features.add(new AutoFarm());
        features.add(new GPS());
        features.add(new AutoTPAccept());
        features.add(new NoJumpDelay());
        features.add(new NoInteract());
        features.add(new NoSlowDown());
        features.add(new AntiAim());
        features.add(new StaffAlert());
        features.add(new FreeCam());
        features.add(new AutoTool());
        features.add(new GuiWalk());
        features.add(new NoPush());
        features.add(new NoClip());
        features.add(new NoFall());
        /* VISUALS */
        features.add(new Cosmetics());
        features.add(new ScoreboardFeatures());
        features.add(new DamageParticles());
        features.add(new SwingAnimations());
        features.add(new WorldFeatures());
        features.add(new JumpCircle());
        features.add(new EntityESP());
        features.add(new Chams());
        features.add(new FullBright());
        features.add(new CustomModel());
        features.add(new ItemPhysics());
        features.add(new TargetESP());
        features.add(new FogColor());

        //scam
        features.add(new Crosshair());


        features.add(new ChinaHat());
        features.add(new PearlESP());
        features.add(new NameTags());
        features.add(new Trails());
        features.add(new ItemESP());
        features.add(new BlockESP());
        features.add(new Tracers());
        features.add(new ViewModel());
        features.add(new XRay());
        features.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().rubik_17.getStringWidth(((Feature) m).getLabel())).reversed());

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
            if (feature.getLabel().equals(name)) {
                return feature;
            }
        }
        return null;
    }

    public List<Feature> getFeatureList() {
        return this.features;
    }
}