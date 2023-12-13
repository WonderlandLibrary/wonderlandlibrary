// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import com.elementars.eclient.command.CommandManager;
import com.elementars.eclient.command.Command;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import com.elementars.eclient.util.RainbowUtils;
import com.elementars.eclient.util.TargetPlayers;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.elementars.eclient.util.XuluTessellator;
import org.lwjgl.opengl.GL11;
import com.elementars.eclient.module.ModuleManager;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import com.elementars.eclient.util.Wrapper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.Xulu;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import com.elementars.eclient.util.Helper;

public class KeyRegistry implements Helper
{
    @SubscribeEvent
    public void onKeyPress(final InputEvent.KeyInputEvent event) {
        for (final Module module : Xulu.MODULE_MANAGER.getModules()) {
            if (module.keybind != null && module.keybind.isPressed()) {
                module.toggle();
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (Wrapper.getMinecraft().world != null && event.getEntity().getEntityWorld().isRemote && event.getEntityLiving().equals((Object)Wrapper.getPlayer())) {
            final LocalPlayerUpdateEvent ev = new LocalPlayerUpdateEvent(event.getEntityLiving());
            ev.call();
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }
        RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;
        if (!Wrapper.getPlayer().isCreative() && Wrapper.getPlayer().getRidingEntity() instanceof AbstractHorse) {
            target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
        }
        if (event.getType() == target) {
            final ModuleManager module_MANAGER = Xulu.MODULE_MANAGER;
            ModuleManager.onRender();
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            XuluTessellator.releaseGL();
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (KeyRegistry.mc.player == null) {
            return;
        }
        final ModuleManager module_MANAGER = Xulu.MODULE_MANAGER;
        ModuleManager.onUpdate();
        TargetPlayers.onUpdate();
        RainbowUtils.updateRainbow();
    }
    
    @SubscribeEvent
    public void onAttack(final AttackEntityEvent event) {
        TargetPlayers.onAttack(event);
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        final ModuleManager module_MANAGER = Xulu.MODULE_MANAGER;
        ModuleManager.onWorldRender(event);
    }
    
    @SubscribeEvent
    public void onChatMessage(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getPrefix())) {
            final String message = event.getMessage();
            event.setCanceled(true);
            CommandManager.runCommand(message.substring(Command.getPrefix().length()));
        }
    }
    
    @SubscribeEvent
    public void onRenderPre(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && Xulu.MODULE_MANAGER.getModuleByName("BossStack").isToggled()) {
            event.setCanceled(true);
        }
    }
}
