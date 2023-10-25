package intent.AquaDev.aqua.modules.combat;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.combat.Killaura;
import intent.AquaDev.aqua.utils.TimeUtil;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.MathHelper;

public class AntiFireball
extends Module {
    public static EntityFireball target = null;
    public TimeUtil timeUtil = new TimeUtil();

    public AntiFireball() {
        super("AntiFireball", "AntiFireball", 0, Category.Combat);
        Aqua.setmgr.register(new Setting("Range", (Module)this, 6.0, 3.0, 6.0, false));
        Aqua.setmgr.register(new Setting("minCPS", (Module)this, 17.0, 1.0, 20.0, false));
        Aqua.setmgr.register(new Setting("maxCPS", (Module)this, 19.0, 1.0, 20.0, false));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        this.timeUtil.reset();
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            float minCPS = (float)Aqua.setmgr.getSetting("AntiFireballminCPS").getCurrentNumber();
            float maxCPS = (float)Aqua.setmgr.getSetting("AntiFireballmaxCPS").getCurrentNumber();
            float CPS = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)minCPS, (double)maxCPS);
            target = this.searchTargets();
            if (target != null && Killaura.target == null && this.timeUtil.hasReached((long)(1000.0f / CPS))) {
                AntiFireball.mc.thePlayer.swingItem();
                AntiFireball.mc.playerController.attackEntity((EntityPlayer)AntiFireball.mc.thePlayer, (Entity)target);
                this.timeUtil.reset();
            }
        }
    }

    public EntityFireball searchTargets() {
        float range = (float)Aqua.setmgr.getSetting("AntiFireballRange").getCurrentNumber();
        EntityFireball player = null;
        double closestDist = 100000.0;
        for (Entity o : AntiFireball.mc.theWorld.loadedEntityList) {
            double dist;
            if (o.getName().equals((Object)AntiFireball.mc.thePlayer.getName()) || !(o instanceof EntityFireball) || !(AntiFireball.mc.thePlayer.getDistanceToEntity(o) < range) || !((dist = (double)AntiFireball.mc.thePlayer.getDistanceToEntity(o)) < closestDist)) continue;
            closestDist = dist;
            player = (EntityFireball)o;
        }
        return player;
    }
}
