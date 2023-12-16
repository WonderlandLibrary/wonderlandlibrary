package wtf.expensive.util.misc;

import lombok.Getter;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SEntityStatusPacket;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.potion.Effects;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventDamage;
import wtf.expensive.util.IMinecraft;

/**
 * @author dedinside
 * @since 22.07.2023
 */
public class DamageUtil implements IMinecraft {
    private final TimerUtil timeTracker = new TimerUtil();
    @Getter
    private boolean normalDamage;
    private boolean fallDamage;
    private boolean explosionDamage;
    private boolean arrowDamage;
    private boolean pearlDamage;

    public void onPacketEvent(EventPacket eventPacket) {
        boolean isDamage = this.fallDamage || this.arrowDamage || this.explosionDamage || this.pearlDamage;

        if (this.isBadEffects()) {
            return;
        }
        if (eventPacket.getPacket() instanceof SExplosionPacket) {
            this.explosionDamage = true;
        }
        if (!isDamage) {
            IPacket<?> packet = eventPacket.getPacket();
            if (packet instanceof SEntityStatusPacket statusPacket) {
                if (statusPacket.getOpCode() == 2 && statusPacket.getEntity(mc.world) == IMinecraft.mc.player) {
                    this.normalDamage = true;
                }
            }
        } else if (mc.player.hurtTime > 0) {
            this.normalDamage = false;
            this.reset();
        }
    }

    public boolean time(long time) {
        if (this.normalDamage) {
            if (this.timeTracker.hasTimeElapsed(time)) {
                this.normalDamage = false;
                this.timeTracker.reset();
                return true;
            }
        } else {
            this.timeTracker.reset();
        }
        return false;
    }

    public void processDamage(EventDamage damageEvent) {
        switch (damageEvent.getDamageType()) {
            case FALL -> this.fallDamage = true;
            case ARROW -> this.arrowDamage = true;
            case ENDER_PEARL -> this.pearlDamage = true;
        }
        this.normalDamage = false;
    }

    public void resetDamages() {
        this.normalDamage = false;
        this.reset();
        this.timeTracker.reset();
    }

    private void reset() {
        this.fallDamage = false;
        this.explosionDamage = false;
        this.arrowDamage = false;
        this.pearlDamage = false;
    }

    private boolean isBadEffects() {
        return mc.player.isPotionActive(Effects.POISON) || mc.player.isPotionActive(Effects.WITHER) || mc.player.isPotionActive(Effects.INSTANT_DAMAGE);
    }

}
