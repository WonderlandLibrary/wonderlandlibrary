package today.flux.addon;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import today.flux.addon.FluxAPI;
import today.flux.addon.api.event.events.Event2DRender;
import today.flux.addon.api.event.events.Event3DRender;
import today.flux.addon.api.event.events.EventMove;
import today.flux.addon.api.event.events.EventPreUpdate;
import today.flux.addon.api.module.AddonModule;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.event.*;
import today.flux.utility.ChatUtils;

public class AddonEventManager {
    public AddonEventManager() {
        EventManager.register(this);
    }

    @EventTarget
    private void onPre(PreUpdateEvent e) {
        EventPreUpdate apiEvent = new EventPreUpdate();
        apiEvent.setX(e.getX());
        apiEvent.setY(e.getY());
        apiEvent.setZ(e.getZ());
        apiEvent.setYaw(e.getYaw());
        apiEvent.setPitch(e.getPitch());
        apiEvent.setOnGround(e.onGround);

        for (AddonModule module : FluxAPI.FLUX_API.getModuleManager().getAddonModules()) {
            try {
                if (module.getStage()) module.onPreUpdate(apiEvent);
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", module.getName(), exception.getMessage()));
            }
        }

        e.setX(apiEvent.getX());
        e.setY(apiEvent.getY());
        e.setZ(apiEvent.getZ());
        if (apiEvent.getYaw() != e.getYaw())
            e.setYaw(apiEvent.getYaw());
        if (apiEvent.getPitch() != e.getPitch())
            e.setPitch(apiEvent.getPitch());
        e.setOnGround(apiEvent.isOnGround());
    }

    @EventTarget
    private void onPost(PostUpdateEvent e) {
        for (AddonModule module : FluxAPI.FLUX_API.getModuleManager().getAddonModules()) {
            try {
                if (module.getStage()) module.onPostUpdate();
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", module.getName(), exception.getMessage()));
            }
        }
    }

    @EventTarget
    private void onMove(MoveEvent e) {
        EventMove apiEvent = new EventMove();
        apiEvent.setX(e.getX());
        apiEvent.setY(e.getY());
        apiEvent.setZ(e.getZ());
        apiEvent.setOnGround(apiEvent.isOnGround());
        apiEvent.setSafeWalk(apiEvent.isSafeWalk());

        for (AddonModule module : FluxAPI.FLUX_API.getModuleManager().getAddonModules()) {
            try {
                if (module.getStage()) module.onMove(apiEvent);
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", module.getName(), exception.getMessage()));
            }
        }

        e.setX(apiEvent.getX());
        e.setY(apiEvent.getY());
        e.setZ(apiEvent.getZ());
        e.setOnGround(apiEvent.isOnGround());
        e.setSafeWalk(apiEvent.isSafeWalk());
    }

    @EventTarget
    private void on2DRender(UIRenderEvent e) {
        Event2DRender apiEvent = new Event2DRender();
        apiEvent.setParticalTicks(e.getParticalTicks());
        
        for (AddonModule module : FluxAPI.FLUX_API.getModuleManager().getAddonModules()) {
            try {
                if (module.getStage()) module.on2DRender(apiEvent);
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", module.getName(), exception.getMessage()));
            }
        }
    }

    @EventTarget
    private void on3DRender(WorldRenderEvent e) {
        Event3DRender apiEvent = new Event3DRender();
        apiEvent.setPartialTicks(e.getPartialTicks());

        for (AddonModule module : FluxAPI.FLUX_API.getModuleManager().getAddonModules()) {
            try {
                if (module.getStage()) module.on3DRender(apiEvent);
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", module.getName(), exception.getMessage()));
            }
        }
    }

    @EventTarget
    private void onTicks(TickEvent e) {
        for (AddonModule module : FluxAPI.FLUX_API.getModuleManager().getAddonModules()) {
            try {
                if (module.getStage()) module.onTicks();
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", module.getName(), exception.getMessage()));
            }
        }
    }

    @EventTarget
    private void onPacket(PacketSendEvent e) {
        AddonPacket packet = AddonPacket.transformPacket(e.getPacket());
        for (AddonModule module : FluxAPI.FLUX_API.getModuleManager().getAddonModules()) {
            try {
                if (packet != null && module.getStage()) module.onPacket(packet);
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", module.getName(), exception.getMessage()));
            }
        }

        e.setCancelled(packet.isCancelled());
    }

    @EventTarget
    private void onPacket(PacketReceiveEvent e) {
        AddonPacket packet = AddonPacket.transformPacket(e.getPacket());
        for (AddonModule module : FluxAPI.FLUX_API.getModuleManager().getAddonModules()) {
            try {
                if (packet != null && module.getStage()) module.onPacket(packet);
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", module.getName(), exception.getMessage()));
            }
        }

        e.setCancelled(packet.isCancelled());
    }
}
