package me.crimp.claudius.mod.modules.misc;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.utils.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

public class EnemyAlert extends Module {
    public EnemyAlert() {
        super("EnemyAlert", "Alerts When Enemy/s Are In Range", Category.Misc, false, false);
    }

    int pluscount = 0;
    boolean soundplayed = false;
    Timer timer = new Timer();

    @Override
    public void onEnable() {
        soundplayed = false;
    }

    @Override
    public void onUpdate() {
        for (Object o : mc.world.loadedEntityList) {
            if (o instanceof EntityPlayer && o != mc.player) {
                EntityPlayer entity = (EntityPlayer) o;
                String name = entity.getGameProfile().getName();

                if (claudius.enemyManager.isEnemy(name) && !soundplayed) {
                    if (timer.passedMs(5L)) {
                        mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f + pluscount);
                        timer.reset();
                        pluscount = pluscount + pluscount ;
                    }
                    if (timer.passedMs(5L)) {
                        mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f + pluscount);
                        timer.reset();
                        pluscount = pluscount + pluscount ;
                    }
                    if (timer.passedMs(5L)) {
                        mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f + pluscount);
                        timer.reset();
                        pluscount = pluscount + pluscount ;
                    }
                    if (timer.passedMs(5L)) {
                        mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f + pluscount);
                        timer.reset();
                        pluscount = 0;
                    }
                    soundplayed = true;
                }
            }
        }
        if (soundplayed && timer.passedMs(5L * 1000)) {
            soundplayed = false;
            timer.reset();
        }
    }
}
