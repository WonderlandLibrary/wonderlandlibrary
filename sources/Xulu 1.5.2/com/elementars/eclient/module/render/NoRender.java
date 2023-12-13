// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import com.elementars.eclient.event.events.EventReceivePacket;
import net.minecraftforge.common.MinecraftForge;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class NoRender extends Module
{
    private final Value<Boolean> mob;
    private final Value<Boolean> gentity;
    public final Value<Boolean> armor;
    public final Value<Boolean> armorTrans;
    public final Value<Integer> alpha;
    private final Value<Boolean> object;
    private final Value<Boolean> xp;
    private final Value<Boolean> paint;
    private final Value<Boolean> fire;
    
    public NoRender() {
        super("NoRender", "Prevents rendering of certain things", 0, Category.RENDER, true);
        this.mob = this.register(new Value<Boolean>("Mob", this, false));
        this.gentity = this.register(new Value<Boolean>("GEntity", this, false));
        this.armor = this.register(new Value<Boolean>("Armor", this, false));
        this.armorTrans = this.register(new Value<Boolean>("Armor Transparency", this, false));
        this.alpha = this.register(new Value<Integer>("Transparency", this, 255, 0, 255));
        this.object = this.register(new Value<Boolean>("Object", this, false));
        this.xp = this.register(new Value<Boolean>("XP", this, false));
        this.paint = this.register(new Value<Boolean>("Paintings", this, false));
        this.fire = this.register(new Value<Boolean>("Fire", this, true));
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        final Packet packet = event.getPacket();
        if ((packet instanceof SPacketSpawnMob && this.mob.getValue()) || (packet instanceof SPacketSpawnGlobalEntity && this.gentity.getValue()) || (packet instanceof SPacketSpawnObject && this.object.getValue()) || (packet instanceof SPacketSpawnExperienceOrb && this.xp.getValue()) || (packet instanceof SPacketSpawnPainting && this.paint.getValue())) {
            event.setCancelled(true);
        }
    }
    
    @SubscribeEvent
    public void onBlockOverlay(final RenderBlockOverlayEvent event) {
        if (this.fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
            event.setCanceled(true);
        }
    }
}
