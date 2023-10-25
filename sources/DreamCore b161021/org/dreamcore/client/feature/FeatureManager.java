package org.dreamcore.client.feature;

import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.combat.*;
import org.dreamcore.client.feature.impl.ghost.*;
import org.dreamcore.client.feature.impl.hud.*;
import org.dreamcore.client.feature.impl.misc.*;
import org.dreamcore.client.feature.impl.movement.*;
import org.dreamcore.client.feature.impl.player.*;
import org.dreamcore.client.feature.impl.visual.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeatureManager {

    public CopyOnWriteArrayList<Feature> features = new CopyOnWriteArrayList<>();

    public FeatureManager() {

        /* HUD */

        features.add(new ClickGui());
        features.add(new HUD());
        features.add(new FeatureList());
        features.add(new Notifications());
        features.add(new Keystrokes());
        features.add(new WaterMark());
        features.add(new ClientSounds());
        features.add(new ChinaHat());
        features.add(new EnderPearlESP());
        features.add(new WorldColor());
        features.add(new AutoClicker());
        features.add(new Eagle());
        features.add(new ChunkAnimator());
        features.add(new MLG());
        features.add(new SelfDamage());
        features.add(new HurtClip());
        features.add(new WTap());
        features.add(new FastBow());
        features.add(new AirStuck());
        features.add(new BetterChat());
        features.add(new Trajectories());
        features.add(new HitColor());
        features.add(new PortalFeatures());
        features.add(new CameraNoClip());

        /* MISC */
        features.add(new FreeCam());
        features.add(new Disabler());
        features.add(new StreamerMode());
        features.add(new FakeHack());
        features.add(new ItemScroller());
        features.add(new Fucker());
        features.add(new XRay());
        features.add(new Nuker());
        features.add(new Spammer());
        features.add(new FlagDetector());
        features.add(new MiddleClickFriend());
        features.add(new DeathCoordinates());
        features.add(new AntiVanish());
        features.add(new EntityFeatures());
        features.add(new Ambience());
        features.add(new AutoBypass());
        features.add(new AntiFreeze());
        features.add(new ServerCrasher());
        features.add(new Secret());

        /* COMBAT */
        features.add(new KillAura());
        features.add(new AntiBot());
        features.add(new TargetStrafe());
        features.add(new TriggerBot());
        features.add(new AutoShift());
        features.add(new AutoTotem());
        features.add(new PushAttack());
        features.add(new AutoArmor());
        features.add(new AimAssist());
        features.add(new Velocity());
        features.add(new KeepSprint());
        features.add(new AntiAim());
        features.add(new AntiCrystal());
        features.add(new AutoPotion());
        features.add(new Criticals());
        features.add(new HitBoxes());
        features.add(new HitParticles());
        features.add(new AutoGapple());
        features.add(new Reach());
        features.add(new NoFriendDamage());

        /* MOVEMENT */
        features.add(new Scaffold());
        features.add(new AutoSprint());
        features.add(new FastClimb());
        features.add(new AntiLevitation());
        features.add(new LiquidWalk());
        features.add(new WaterLeave());
        features.add(new LongJump());
        features.add(new Speed());
        features.add(new Timer());
        features.add(new Step());
        features.add(new Flight());
        features.add(new ElytraFlight());
        features.add(new WallClimb());
        features.add(new AirJump());
        features.add(new WebTP());
        features.add(new TeleportExploit());
        features.add(new WaterSpeed());
        features.add(new ParkourHelper());
        features.add(new FakeLags());
        features.add(new HighJump());
        features.add(new Strafe());

        /* PLAYER */
        features.add(new GuiWalk());
        features.add(new AutoRespawn());
        features.add(new AntiPush());
        features.add(new MiddleClickPearl());
        features.add(new NoClip());
        features.add(new NoDelay());
        features.add(new ChestStealer());
        features.add(new InventoryManager());
        features.add(new NoFall());
        features.add(new AntiVoid());
        features.add(new NoSlowDown());
        features.add(new NoRotate());
        features.add(new AntiAFK());
        features.add(new NoInteract());
        features.add(new AutoFarm());
        features.add(new FastEat());
        features.add(new XCarry());
        features.add(new ClipHelper());
        features.add(new SafeWalk());
        features.add(new AutoFish());
        features.add(new AutoTool());
        features.add(new SpeedMine());
        features.add(new NoWeb());
        features.add(new AutoAuth());
        features.add(new SolidWeb());
        features.add(new PearlLogger());
        features.add(new BullingBot());
        features.add(new ChatAppend());

        /* VISUALS */
        features.add(new EntityESP());
        features.add(new BlockESP());
        features.add(new NameTags());
        features.add(new FullBright());
        features.add(new Animations());
        features.add(new NoRender());
        features.add(new ViewModel());
        features.add(new ItemPhysics());
        features.add(new Scoreboard());
        features.add(new PersonViewer());
        features.add(new Chams());
        features.add(new FogColor());
        features.add(new Crosshair());
        features.add(new Radar());
        features.add(new Trails());
        features.add(new EnchantmentColor());
        features.add(new ItemESP());
        features.add(new MobESP());
        features.add(new BlockOverlay());
        features.add(new Tracers());
        features.add(new ChestESP());
    }

    public List<Feature> getFeatureList() {
        return this.features;
    }

    public List<Feature> getFeaturesForCategory(Type category) {
        List<Feature> featureList = new ArrayList<>();
        for (Feature feature : getFeatureList()) {
            if (feature.getType() == category) {
                featureList.add(feature);
            }
        }
        return featureList;
    }

    public Feature getFeatureByClass(Class<? extends Feature> classFeature) {
        for (Feature feature : getFeatureList()) {
            if (feature != null) {
                if (feature.getClass() == classFeature) {
                    return feature;
                }
            }
        }
        return null;
    }

    public Feature getFeatureByLabel(String name) {
        for (Feature feature : getFeatureList()) {
            if (feature.getLabel().equals(name)) {
                return feature;
            }
        }
        return null;
    }
}
