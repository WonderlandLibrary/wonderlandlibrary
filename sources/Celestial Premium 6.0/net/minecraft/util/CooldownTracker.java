/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

public class CooldownTracker {
    private final Map<Item, Cooldown> cooldowns = Maps.newHashMap();
    private int ticks;

    public boolean hasCooldown(Item itemIn) {
        return this.getCooldown(itemIn, 0.0f) > 0.0f;
    }

    public float getCooldown(Item itemIn, float partialTicks) {
        Cooldown cooldowntracker$cooldown = this.cooldowns.get(itemIn);
        if (cooldowntracker$cooldown != null) {
            float f = cooldowntracker$cooldown.expireTicks - cooldowntracker$cooldown.createTicks;
            float f1 = (float)cooldowntracker$cooldown.expireTicks - ((float)this.ticks + partialTicks);
            return MathHelper.clamp(f1 / f, 0.0f, 1.0f);
        }
        return 0.0f;
    }

    public void tick() {
        ++this.ticks;
        if (!this.cooldowns.isEmpty()) {
            Iterator<Map.Entry<Item, Cooldown>> iterator = this.cooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Item, Cooldown> entry = iterator.next();
                if (entry.getValue().expireTicks > this.ticks) continue;
                iterator.remove();
                this.notifyOnRemove(entry.getKey());
            }
        }
    }

    public void setCooldown(Item itemIn, int ticksIn) {
        this.cooldowns.put(itemIn, new Cooldown(this.ticks, this.ticks + ticksIn));
        this.notifyOnSet(itemIn, ticksIn);
    }

    public void removeCooldown(Item itemIn) {
        this.cooldowns.remove(itemIn);
        this.notifyOnRemove(itemIn);
    }

    protected void notifyOnSet(Item itemIn, int ticksIn) {
    }

    protected void notifyOnRemove(Item itemIn) {
    }

    class Cooldown {
        final int createTicks;
        final int expireTicks;

        private Cooldown(int createTicksIn, int expireTicksIn) {
            this.createTicks = createTicksIn;
            this.expireTicks = expireTicksIn;
        }
    }
}

