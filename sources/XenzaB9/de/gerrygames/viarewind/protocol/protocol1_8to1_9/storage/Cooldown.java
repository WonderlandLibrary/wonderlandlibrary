// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import com.viaversion.viaversion.util.Pair;
import java.util.ArrayList;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import de.gerrygames.viarewind.ViaRewind;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.UUID;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.utils.Tickable;
import com.viaversion.viaversion.api.connection.StoredObject;

public class Cooldown extends StoredObject implements Tickable
{
    private double attackSpeed;
    private long lastHit;
    private final ViaRewindConfig.CooldownIndicator cooldownIndicator;
    private UUID bossUUID;
    private boolean lastSend;
    private static final int max = 10;
    
    public Cooldown(final UserConnection user) {
        super(user);
        this.attackSpeed = 4.0;
        this.lastHit = 0L;
        ViaRewindConfig.CooldownIndicator indicator;
        try {
            indicator = ViaRewind.getConfig().getCooldownIndicator();
        }
        catch (final IllegalArgumentException e) {
            ViaRewind.getPlatform().getLogger().warning("Invalid cooldown-indicator setting");
            indicator = ViaRewindConfig.CooldownIndicator.DISABLED;
        }
        this.cooldownIndicator = indicator;
    }
    
    @Override
    public void tick() {
        if (!this.hasCooldown()) {
            if (this.lastSend) {
                this.hide();
                this.lastSend = false;
            }
            return;
        }
        final BlockPlaceDestroyTracker tracker = this.getUser().get(BlockPlaceDestroyTracker.class);
        if (tracker.isMining()) {
            this.lastHit = 0L;
            if (this.lastSend) {
                this.hide();
                this.lastSend = false;
            }
            return;
        }
        this.showCooldown();
        this.lastSend = true;
    }
    
    private void showCooldown() {
        if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.TITLE) {
            this.sendTitle("", this.getTitle(), 0, 2, 5);
        }
        else if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.ACTION_BAR) {
            this.sendActionBar(this.getTitle());
        }
        else if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.BOSS_BAR) {
            this.sendBossBar((float)this.getCooldown());
        }
    }
    
    private void hide() {
        if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.ACTION_BAR) {
            this.sendActionBar("§r");
        }
        else if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.TITLE) {
            this.hideTitle();
        }
        else if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.BOSS_BAR) {
            this.hideBossBar();
        }
    }
    
    private void hideBossBar() {
        if (this.bossUUID == null) {
            return;
        }
        final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.BOSSBAR, null, this.getUser());
        wrapper.write(Type.UUID, this.bossUUID);
        wrapper.write(Type.VAR_INT, 1);
        PacketUtil.sendPacket(wrapper, Protocol1_8TO1_9.class, false, true);
        this.bossUUID = null;
    }
    
    private void sendBossBar(final float cooldown) {
        final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.BOSSBAR, this.getUser());
        if (this.bossUUID == null) {
            this.bossUUID = UUID.randomUUID();
            wrapper.write(Type.UUID, this.bossUUID);
            wrapper.write(Type.VAR_INT, 0);
            wrapper.write((Type<JsonPrimitive>)Type.COMPONENT, new JsonPrimitive(" "));
            wrapper.write(Type.FLOAT, cooldown);
            wrapper.write(Type.VAR_INT, 0);
            wrapper.write(Type.VAR_INT, 0);
            wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
        }
        else {
            wrapper.write(Type.UUID, this.bossUUID);
            wrapper.write(Type.VAR_INT, 2);
            wrapper.write(Type.FLOAT, cooldown);
        }
        PacketUtil.sendPacket(wrapper, Protocol1_8TO1_9.class, false, true);
    }
    
    private void hideTitle() {
        final PacketWrapper hide = PacketWrapper.create(ClientboundPackets1_8.TITLE, null, this.getUser());
        hide.write(Type.VAR_INT, 3);
        PacketUtil.sendPacket(hide, Protocol1_8TO1_9.class);
    }
    
    private void sendTitle(final String title, final String subTitle, final int fadeIn, final int stay, final int fadeOut) {
        final PacketWrapper timePacket = PacketWrapper.create(ClientboundPackets1_8.TITLE, null, this.getUser());
        timePacket.write(Type.VAR_INT, 2);
        timePacket.write(Type.INT, fadeIn);
        timePacket.write(Type.INT, stay);
        timePacket.write(Type.INT, fadeOut);
        final PacketWrapper titlePacket = PacketWrapper.create(ClientboundPackets1_8.TITLE, this.getUser());
        titlePacket.write(Type.VAR_INT, 0);
        titlePacket.write((Type<JsonPrimitive>)Type.COMPONENT, new JsonPrimitive(title));
        final PacketWrapper subtitlePacket = PacketWrapper.create(ClientboundPackets1_8.TITLE, this.getUser());
        subtitlePacket.write(Type.VAR_INT, 1);
        subtitlePacket.write((Type<JsonPrimitive>)Type.COMPONENT, new JsonPrimitive(subTitle));
        PacketUtil.sendPacket(titlePacket, Protocol1_8TO1_9.class);
        PacketUtil.sendPacket(subtitlePacket, Protocol1_8TO1_9.class);
        PacketUtil.sendPacket(timePacket, Protocol1_8TO1_9.class);
    }
    
    private void sendActionBar(final String bar) {
        final PacketWrapper actionBarPacket = PacketWrapper.create(ClientboundPackets1_8.CHAT_MESSAGE, this.getUser());
        actionBarPacket.write((Type<JsonPrimitive>)Type.COMPONENT, new JsonPrimitive(bar));
        actionBarPacket.write(Type.BYTE, (Byte)2);
        PacketUtil.sendPacket(actionBarPacket, Protocol1_8TO1_9.class);
    }
    
    public boolean hasCooldown() {
        final long time = System.currentTimeMillis() - this.lastHit;
        final double cooldown = this.restrain(time * this.attackSpeed / 1000.0, 0.0, 1.5);
        return cooldown > 0.1 && cooldown < 1.1;
    }
    
    public double getCooldown() {
        final long time = System.currentTimeMillis() - this.lastHit;
        return this.restrain(time * this.attackSpeed / 1000.0, 0.0, 1.0);
    }
    
    private double restrain(final double x, final double a, final double b) {
        if (x < a) {
            return a;
        }
        return Math.min(x, b);
    }
    
    private String getTitle() {
        final String symbol = (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.ACTION_BAR) ? "\u25a0" : "\u02d9";
        final double cooldown = this.getCooldown();
        int green = (int)Math.floor(10.0 * cooldown);
        int grey = 10 - green;
        final StringBuilder builder = new StringBuilder("§8");
        while (green-- > 0) {
            builder.append(symbol);
        }
        builder.append("§7");
        while (grey-- > 0) {
            builder.append(symbol);
        }
        return builder.toString();
    }
    
    public double getAttackSpeed() {
        return this.attackSpeed;
    }
    
    public void setAttackSpeed(final double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
    
    public void setAttackSpeed(final double base, final ArrayList<Pair<Byte, Double>> modifiers) {
        this.attackSpeed = base;
        for (int j = 0; j < modifiers.size(); ++j) {
            if ((byte)modifiers.get(j).getKey() == 0) {
                this.attackSpeed += (double)modifiers.get(j).getValue();
                modifiers.remove(j--);
            }
        }
        for (int j = 0; j < modifiers.size(); ++j) {
            if ((byte)modifiers.get(j).getKey() == 1) {
                this.attackSpeed += base * (double)modifiers.get(j).getValue();
                modifiers.remove(j--);
            }
        }
        for (int j = 0; j < modifiers.size(); ++j) {
            if ((byte)modifiers.get(j).getKey() == 2) {
                this.attackSpeed *= 1.0 + (double)modifiers.get(j).getValue();
                modifiers.remove(j--);
            }
        }
    }
    
    public void hit() {
        this.lastHit = System.currentTimeMillis();
    }
    
    public void setLastHit(final long lastHit) {
        this.lastHit = lastHit;
    }
}
