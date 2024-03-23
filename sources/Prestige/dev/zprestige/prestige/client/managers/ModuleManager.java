package dev.zprestige.prestige.client.managers;

import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.module.impl.combat.*;
import dev.zprestige.prestige.client.module.impl.misc.*;
import dev.zprestige.prestige.client.module.impl.movement.*;
import dev.zprestige.prestige.client.module.impl.visuals.*;
import dev.zprestige.prestige.client.ui.drawables.gui.buttons.MenuButton;
import java.util.ArrayList;
import java.util.Arrays;

public class ModuleManager {

    public MenuButton menu;
    public ArrayList<Module> modules = new ArrayList();

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public ModuleManager() {
        menu = new MenuButton();
        modules.addAll(Arrays.asList(new AimAssist(), new AnchorExploder(), new AnchorPlacer(), new AntiBot(), new AutoCrystal(), new AutoHitCrystal(), new AutoShieldBreaker(), new AutoTotem(), new GhostObby(), new Hitboxes(), new NoMissDelay(), new PredictDoubleHand(), new SilentAim(), new TotemHit(), new Triggerbot(), new AutoClicker(), new AutoLoot(), new AutoPickaxe(), new AutoPot(), new AutoTool(), new ChestStealer(), new Criticals(), new CrystalOptimizer(), new FakeGhost(), new FastEXP(), new FastPlace(), new KeyPearl(), new MiddleClickFriend(), new NameHider(), new PotionRefill(), new Reach(), new SelfDestruct(), new AutoJumpReset(), new FakeLag(), new FastBridge(), new Freecam(), new NoJumpDelay(), new ShortPearl(), new Sprint(), new Velocity(), new WTap(), new AntiResourcePack(), new AspectRatio(), new ESP(), new HealthIndicators(), new Hud(), new NameTags(), new NoRender(), new TotemAnimation()));
    }

    public MenuButton getMenu() {
        return this.menu;
    }
}
