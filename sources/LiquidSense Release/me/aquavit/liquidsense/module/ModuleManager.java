package me.aquavit.liquidsense.module;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.blatant.*;
import me.aquavit.liquidsense.module.modules.client.*;
import me.aquavit.liquidsense.module.modules.exploit.*;
import me.aquavit.liquidsense.module.modules.hud.*;
import me.aquavit.liquidsense.module.modules.misc.*;
import me.aquavit.liquidsense.module.modules.movement.*;
import me.aquavit.liquidsense.module.modules.player.*;
import me.aquavit.liquidsense.module.modules.ghost.*;
import me.aquavit.liquidsense.module.modules.render.*;
import me.aquavit.liquidsense.module.modules.world.*;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.KeyEvent;
import me.aquavit.liquidsense.event.Listenable;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.value.Value;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

@SideOnly(Side.CLIENT)
public final class ModuleManager implements Listenable {

    private static final TreeSet<Module> modules = new TreeSet(new Comparator<Module>() {
        @Override
        public int compare(Module module1, Module module2) {
            return module1.getName().compareTo(module2.getName());
        }
    });

    public TreeSet<Module> getModules() {
        return modules;
    }

    private final HashMap<Class<?>, Module> moduleClassMap = new HashMap<>();

    public ModuleManager() {
        LiquidSense.eventManager.registerListener(this);
    }

    /**
     * 注册所有功能
     */
    public void registerModules() {
        ClientUtils.getLogger().info("[" + LiquidSense.CLIENT_NAME + "]" + " Loading modules...");
        registerModules(
                Armor.class,
                Effects.class,
                HeadLogo.class,
                Hotbar.class,
                ScoreboardElement.class,
                SpeedGraph.class,

                Target.class,
                Aimbot.class,
                AutoArmor.class,
                AutoPot.class,
                AutoSoup.class,
                Aura.class,
                Fly.class,
                ClickGUI.class,
                HighJump.class,
                InvMove.class,
                NoSlow.class,
                LiquidWalk.class,
                Sprint.class,
                AntiBot.class,
                ChestStealer.class,
                Scaffold.class,
                Tower.class,
                ESP.class,
                Speed.class,
                FastUse.class,
                Teleport.class,
                Phase.class,
                Fullbright.class,
                ItemESP.class,
                StorageESP.class,
                PingSpoof.class,
                FastClimb.class,
                Spammer.class,
                IceSpeed.class,
                NoFall.class,
                Breadcrumbs.class,
                Blink.class,
                NameProtect.class,
                MidClick.class,
                XRay.class,
                FreeCam.class,
                Plugins.class,
                LongJump.class,
                AutoClicker.class,
                BlockESP.class,
                ServerCrasher.class,
                FastStairs.class,
                InvClean.class,
                BufferSpeed.class,
                ProphuntESP.class,
                KeepContainer.class,
                BugUp.class,
                HUD.class,
                ChestAura.class,
                AntiObsidian.class,

                WTap.class,
                CaveFinder.class,
                Step.class,
                Criticals.class,
                SuperKnockback.class,
                Teams.class,
                Projectiles.class,
                AbortBreaking.class,
                SwingAnimation.class,
                FastPlace.class,
                BowAimbot.class,
                Compass.class,
                Zoot.class,
                RenderChanger.class,
                Rotations.class,
                Regen.class,
                JumpCircle.class,
                ConsoleSpammer.class,
                Damage.class,
                SkinDerp.class,
                Derp.class,
                EventLogger.class,
                Velocity.class,
                FastBow.class,
                NameTags.class,
                GameSpeed.class,
                BlockOverlay.class,
                Clip.class,
                Ghost.class,
                Tracers.class,
                AutoBow.class,
                GhostHand.class,
                Kick.class,
                HYTBypass.class,
                Fucker.class,
                AntiAFK.class,
                Reach.class,
                NoClip.class,
                NoJumpDelay.class,
                NoWeb.class,
                SafeWalk.class,
                NoRotateSet.class,
                ComponentOnHover.class,
                AutoWeapon.class,
                AntiBlind.class,
                NoBob.class,
                NoFOV.class,
                NoHurtCam.class,
                NoSwing.class,
                TrueSight.class,
                Chams.class,
                Particle.class,
                EveryThingBlock.class,
                FastBreak.class,
                ItemPhysic.class,
                Ambience.class,
                EnchantEffect.class,
                AutoFish.class,
                AutoWalk.class,
                Eagle.class,
                AutoSpawn.class,
                HitBox.class,
                Freeze.class,
                Strafe.class,
                NoFriends.class,
                Animations.class,
                ItemRotate.class,
                LagBack.class,
                MemoryFixer.class,
                PointerESP.class,
                Disabler.class,
                Skeltal.class,
                TargetStrafe.class,
                AutoADL.class,
                CameraView.class,
                KillESP.class,
                Jesus.class,
                Cape.class,
                AutoApple.class,
                AntiSpam.class,
                AutoTool.class
                );

        ClientUtils.getLogger().info("[" + LiquidSense.CLIENT_NAME + "]" + " Loaded " + modules.size() + " modules.");
    }

    /**
     * 注册功能
     */
    public void registerModule(final Module module) {
        modules.add(module);
        moduleClassMap.put(module.getClass(), module);

        generateCommand(module);
        LiquidSense.eventManager.registerListener(module);
    }

    /**
     * 注册功能的类
     */
    private void registerModule(Class<? extends Module> moduleClass) {
        try {
            registerModule(moduleClass.newInstance());
        } catch (Throwable e) {
            ClientUtils.getLogger().error("Failed to load module: " + moduleClass.getName() + " (" + e.getClass().getName() + ": " + e.getMessage() + ")");
        }
    }

    /**
     * 注册功能列表
     */
    @SafeVarargs
    public final void registerModules(Class<? extends Module>... modules) {
        for (Class<? extends Module> module : modules) {
            registerModule(module);
        }
    }

    /**
     * 注销功能
     */
    public void unregisterModule(final Module module) {
        modules.remove(module);
        moduleClassMap.remove(module.getClass());
        LiquidSense.eventManager.unregisterListener(module);
    }

    /**
     * 为功能生成命令
     */
    private void generateCommand(Module module) {
        List<Value<?>> values = module.getValues();
        if (values.isEmpty()) {
            return;
        }
        LiquidSense.commandManager.registerCommand(new ModuleCommand(module, values));
    }

    /**
     * 通过功能的类获取模块
     */
    public Module getModule(Class<?> moduleClass) {
        return moduleClassMap.get(moduleClass);
    }

    public Module get(Class<?> clazz) {
        return getModule(clazz);
    }

    /**
     * 通过功能名称获取模块
     */
    public Module getModule(String moduleName) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(moduleName)) {
                return module;
            }
        }
        return null;
    }

    /**
     * 处理传入的按键
     */
    @EventTarget
    private void onKey(final KeyEvent event) {
        modules.stream().filter(module -> module.getKeyBind() == event.getKey()).forEach(Module::toggle);
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

