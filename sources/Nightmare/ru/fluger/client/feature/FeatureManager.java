// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature;

import java.util.Iterator;
import ru.fluger.client.feature.impl.Type;
import java.util.List;
import java.util.Comparator;
import ru.fluger.client.feature.impl.visuals.ViewModel;
import ru.fluger.client.feature.impl.visuals.Tracers;
import ru.fluger.client.feature.impl.visuals.TargetESP;
import ru.fluger.client.feature.impl.visuals.PearlESP;
import ru.fluger.client.feature.impl.visuals.EntityESP;
import ru.fluger.client.feature.impl.visuals.XRay;
import ru.fluger.client.feature.impl.visuals.Trails;
import ru.fluger.client.feature.impl.visuals.BlockESP;
import ru.fluger.client.feature.impl.player.NoDelay;
import ru.fluger.client.feature.impl.visuals.ShulkerViewer;
import ru.fluger.client.feature.impl.visuals.NightMode;
import ru.fluger.client.feature.impl.visuals.Ambience;
import ru.fluger.client.feature.impl.visuals.WorldColor;
import ru.fluger.client.feature.impl.visuals.FullBright;
import ru.fluger.client.feature.impl.visuals.CustomModel;
import ru.fluger.client.feature.impl.visuals.FogColor;
import ru.fluger.client.feature.impl.visuals.ScoreboardFeatures;
import ru.fluger.client.feature.impl.visuals.Chams;
import ru.fluger.client.feature.impl.visuals.DamageParticles;
import ru.fluger.client.feature.impl.visuals.JumpCircle;
import ru.fluger.client.feature.impl.visuals.SwingAnimations;
import ru.fluger.client.feature.impl.visuals.NameTags;
import ru.fluger.client.feature.impl.player.NoPush;
import ru.fluger.client.feature.impl.player.NoClip;
import ru.fluger.client.feature.impl.player.FreeCam;
import ru.fluger.client.feature.impl.player.GuiWalk;
import ru.fluger.client.feature.impl.player.AutoTool;
import ru.fluger.client.feature.impl.player.NoInteract;
import ru.fluger.client.feature.impl.player.ItemScroller;
import ru.fluger.client.feature.impl.player.SpeedMine;
import ru.fluger.client.feature.impl.player.AutoTPAccept;
import ru.fluger.client.feature.impl.player.DeathCoordinates;
import ru.fluger.client.feature.impl.player.NoServerRotations;
import ru.fluger.client.feature.impl.movement.Timer;
import ru.fluger.client.feature.impl.movement.Jesus;
import ru.fluger.client.feature.impl.movement.BedrockClip;
import ru.fluger.client.feature.impl.movement.Strafe;
import ru.fluger.client.feature.impl.movement.Spider;
import ru.fluger.client.feature.impl.movement.GlideFly;
import ru.fluger.client.feature.impl.movement.WaterSpeed;
import ru.fluger.client.feature.impl.movement.NoWeb;
import ru.fluger.client.feature.impl.movement.DamageFly;
import ru.fluger.client.feature.impl.movement.Flight;
import ru.fluger.client.feature.impl.movement.Speed;
import ru.fluger.client.feature.impl.movement.NoSlowDown;
import ru.fluger.client.feature.impl.movement.AutoSprint;
import ru.fluger.client.feature.impl.combat.Velocity;
import ru.fluger.client.feature.impl.combat.TargetStrafe;
import ru.fluger.client.feature.impl.combat.FastBow;
import ru.fluger.client.feature.impl.combat.Reach;
import ru.fluger.client.feature.impl.combat.AutoTotem;
import ru.fluger.client.feature.impl.combat.HitBox;
import ru.fluger.client.feature.impl.combat.AutoGapple;
import ru.fluger.client.feature.impl.combat.AutoPotion;
import ru.fluger.client.feature.impl.combat.AutoArmor;
import ru.fluger.client.feature.impl.combat.AntiBot;
import ru.fluger.client.feature.impl.combat.KillAura;
import ru.fluger.client.feature.impl.combat.KeepSprint;
import ru.fluger.client.feature.impl.combat.AppleGoldenTimer;
import ru.fluger.client.feature.impl.misc.StaffAlert;
import ru.fluger.client.feature.impl.misc.NameProtect;
import ru.fluger.client.feature.impl.misc.MiddleClickPearl;
import ru.fluger.client.feature.impl.misc.ModuleSoundAlert;
import ru.fluger.client.feature.impl.misc.MiddleClickFriend;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.feature.impl.hud.ArrayList;
import ru.fluger.client.feature.impl.hud.TargetHUD;
import ru.fluger.client.feature.impl.hud.Notifications;
import ru.fluger.client.feature.impl.hud.ClientFont;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.feature.impl.hud.NoOverlay;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeatureManager
{
    public CopyOnWriteArrayList<Feature> features;
    
    public FeatureManager() {
        (this.features = new CopyOnWriteArrayList<Feature>()).add(new NoOverlay());
        this.features.add(new ClickGui());
        this.features.add(new ClientFont());
        this.features.add(new Notifications());
        this.features.add(new TargetHUD());
        this.features.add(new ArrayList());
        this.features.add(new HUD());
        this.features.add(new MiddleClickFriend());
        this.features.add(new ModuleSoundAlert());
        this.features.add(new MiddleClickPearl());
        this.features.add(new NameProtect());
        this.features.add(new StaffAlert());
        this.features.add(new AppleGoldenTimer());
        this.features.add(new KeepSprint());
        this.features.add(new KillAura());
        this.features.add(new AntiBot());
        this.features.add(new AutoArmor());
        this.features.add(new AutoPotion());
        this.features.add(new AutoGapple());
        this.features.add(new HitBox());
        this.features.add(new AutoTotem());
        this.features.add(new Reach());
        this.features.add(new FastBow());
        this.features.add(new TargetStrafe());
        this.features.add(new Velocity());
        this.features.add(new AutoSprint());
        this.features.add(new NoSlowDown());
        this.features.add(new Speed());
        this.features.add(new Flight());
        this.features.add(new DamageFly());
        this.features.add(new NoWeb());
        this.features.add(new WaterSpeed());
        this.features.add(new GlideFly());
        this.features.add(new Spider());
        this.features.add(new Strafe());
        this.features.add(new BedrockClip());
        this.features.add(new Jesus());
        this.features.add(new Timer());
        this.features.add(new NoServerRotations());
        this.features.add(new DeathCoordinates());
        this.features.add(new AutoTPAccept());
        this.features.add(new SpeedMine());
        this.features.add(new ItemScroller());
        this.features.add(new NoInteract());
        this.features.add(new AutoTool());
        this.features.add(new GuiWalk());
        this.features.add(new FreeCam());
        this.features.add(new NoClip());
        this.features.add(new NoPush());
        this.features.add(new NameTags());
        this.features.add(new SwingAnimations());
        this.features.add(new JumpCircle());
        this.features.add(new DamageParticles());
        this.features.add(new Chams());
        this.features.add(new ScoreboardFeatures());
        this.features.add(new FogColor());
        this.features.add(new CustomModel());
        this.features.add(new FullBright());
        this.features.add(new WorldColor());
        this.features.add(new Ambience());
        this.features.add(new NightMode());
        this.features.add(new ShulkerViewer());
        this.features.add(new NoDelay());
        this.features.add(new BlockESP());
        this.features.add(new Trails());
        this.features.add(new XRay());
        this.features.add(new EntityESP());
        this.features.add(new PearlESP());
        this.features.add(new TargetESP());
        this.features.add(new Tracers());
        this.features.add(new ViewModel());
        this.features.sort(Comparator.comparingInt(m -> bib.z().robotoRegularFontRender.getStringWidth(m.getLabel())).reversed());
    }
    
    public List<Feature> getFeatureList() {
        return this.features;
    }
    
    public List<Feature> getFeaturesForCategory(final Type category) {
        final java.util.ArrayList<Feature> featureList = new java.util.ArrayList<Feature>();
        for (final Feature feature : this.getFeatureList()) {
            if (feature.getType() != category) {
                continue;
            }
            featureList.add(feature);
        }
        return featureList;
    }
    
    public Feature getFeatureByClass(final Class<? extends Feature> classModule) {
        for (final Feature feature : this.getFeatureList()) {
            if (feature != null) {
                if (feature.getClass() != classModule) {
                    continue;
                }
                return feature;
            }
        }
        return null;
    }
    
    public Feature getFeatureByLabel(final String name) {
        for (final Feature feature : this.getFeatureList()) {
            if (!feature.getLabel().equalsIgnoreCase(name)) {
                continue;
            }
            return feature;
        }
        return null;
    }
}
