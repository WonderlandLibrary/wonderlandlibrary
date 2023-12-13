package today.flux.module.implement.Player;

import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.*;
import today.flux.module.implement.Movement.*;
import today.flux.module.implement.World.*;
import today.flux.utility.PlayerUtils;
import today.flux.utility.ServerUtils;

public class AutoBypass extends Module {
    public AutoBypass() {
        super("我觉得你真的好像一个傻逼", Category.Player, false);
    }

    @EventTarget
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void onTick(TickEvent e) {
        if (ServerUtils.getServer() == ServerUtils.Server.Hypixel) {

            for (Module module : ModuleManager.getModList()) {
                Class moduleClass = module.getClass();

                if (moduleClass == AntiBots.class && !module.isEnabled()) {
                    module.setEnabled(true);
                }

                if (moduleClass == Criticals.class || moduleClass == Speed.class || moduleClass == Fly.class || moduleClass == NoFall.class ||
                        moduleClass == LongJump.class || moduleClass == NoSlow.class || moduleClass == Scaffold.class || moduleClass == AntiBots.class) {
                    if (module.isEnabled() && !module.getMode().isCurrentMode("Hypixel")) {
                        module.getMode().setValue("Hypixel");
                        PlayerUtils.tellPlayer("[Auto Bypass] Set " + module.getName() + " mode to Hypixel.");
                    }
                    break;
                }

                if (
                 // Combat
                 moduleClass == AutoPot.class || moduleClass == BetterSword.class || moduleClass == BowAimbot.class ||
                 moduleClass == HitBox.class || moduleClass == KillAura.class || moduleClass == Reach.class || moduleClass == TargetStrafe.class || moduleClass == Velocity.class ||

                 // Movement
                 moduleClass == AntiVoid.class || moduleClass == InvMove.class || moduleClass == SafeWalk.class || moduleClass == Sprint.class || moduleClass == Strafe.class || moduleClass == Step.class ||

                 // Player
                 moduleClass == NoRotate.class || moduleClass == Wings.class || moduleClass == ItemPhysics.class || moduleClass == InvCleaner.class || moduleClass == ChatBypass.class || moduleClass == AutoTools.class ||

                 // World
                 moduleClass == AutoGG.class || moduleClass == AutoL.class || moduleClass == ChestStealer.class || moduleClass == FastBreak.class ||

                 // Misc & Ghost
                 module.getCategory() == Category.Misc || module.getCategory() == Category.Ghost
                ) {
                    continue;
                }

                module.setEnabled(false);
                PlayerUtils.tellPlayer("[Auto Bypass] Disabled " + module.getName() + " for Hypixel.");
            }
        }
    }
}
