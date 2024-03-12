// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api;

import dev.tenacity.event.impl.render.CustomBlockRenderEvent;
import dev.tenacity.event.impl.render.RenderModelEvent;
import dev.tenacity.event.impl.player.AttackEvent;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.event.impl.player.MoveEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.event.impl.game.WorldEvent;
import dev.tenacity.event.impl.player.PlayerSendMessageEvent;
import dev.tenacity.event.impl.player.SafeWalkEvent;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.player.ChatUtil;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.module.Category;
import java.io.File;
import jdk.nashorn.api.scripting.JSObject;
import java.util.HashMap;
import dev.tenacity.module.Module;

public class ScriptModule extends Module
{
    private final HashMap<String, JSObject> eventMap;
    private final File file;
    private boolean reloadable;
    
    public ScriptModule(final String name, final String spacedName, final String description, final HashMap<String, JSObject> events, final String author, final File file) {
        super(name, spacedName, Category.SCRIPTS, description);
        this.reloadable = true;
        this.eventMap = events;
        this.file = file;
        this.setAuthor(author);
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (this.eventMap.containsKey("render")) {
            try {
                this.eventMap.get("render").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in render2D event");
                e.printStackTrace();
                this.eventMap.remove("render");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Render2D event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent shaderEvent) {
        if (this.eventMap.containsKey("shader")) {
            try {
                this.eventMap.get("shader").call(null, shaderEvent);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in shader event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("shader");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Shader event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
        if (this.eventMap.containsKey("chat")) {
            try {
                this.eventMap.get("chat").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in chat event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("chat");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Chat event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onSafeWalkEvent(final SafeWalkEvent event) {
        if (this.eventMap.containsKey("safewalk")) {
            try {
                this.eventMap.get("safewalk").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in safewalk event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("safewalk");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "SafeWalk event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onPlayerSendMessageEvent(final PlayerSendMessageEvent event) {
        if (this.eventMap.containsKey("playerMessage")) {
            try {
                this.eventMap.get("playerMessage").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in playerMessage event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("playerMessage");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "PlayerSendMessage event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        if (event instanceof WorldEvent.Load && this.eventMap.containsKey("worldLoad")) {
            try {
                this.eventMap.get("worldLoad").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in worldLoad event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("worldLoad");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "WorldLoad event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (this.eventMap.containsKey("render3D")) {
            try {
                this.eventMap.get("render3D").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in render3D event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("render3D");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Render3D event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        this.setSuffix(this.getAuthor());
        if (this.eventMap.containsKey("motion")) {
            try {
                this.eventMap.get("motion").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in motion event");
                ChatUtil.print(false, e.getMessage());
                System.out.println(e.getMessage());
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Motion event unloaded", 7.0f);
                this.eventMap.remove("motion");
            }
        }
    }
    
    @Override
    public void onMoveEvent(final MoveEvent event) {
        if (this.eventMap.containsKey("move")) {
            try {
                this.eventMap.get("move").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in move event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("move");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Move event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onTickEvent(final TickEvent event) {
        if (this.eventMap.containsKey("tick")) {
            try {
                this.eventMap.get("tick").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in tick event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("tick");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Tick event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
        if (this.eventMap.containsKey("packetSend")) {
            try {
                this.eventMap.get("packetSend").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in packetSend event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("packetSend");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "PacketSend event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent event) {
        if (this.eventMap.containsKey("packetReceive")) {
            try {
                this.eventMap.get("packetReceive").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in packetReceive event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("packetReceive");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "PacketReceive event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (this.eventMap.containsKey("attack")) {
            try {
                this.eventMap.get("attack").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in attack event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("attack");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Attack event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onRenderModelEvent(final RenderModelEvent event) {
        if (this.eventMap.containsKey("renderModel")) {
            try {
                this.eventMap.get("renderModel").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in renderModel event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("renderModel");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Render model event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onCustomBlockRender(final CustomBlockRenderEvent event) {
        if (this.eventMap.containsKey("customBlockRender")) {
            try {
                this.eventMap.get("customBlockRender").call(null, event);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in customBlockRender event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("customBlockRender");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Custom Block Render event unloaded", 7.0f);
            }
        }
    }
    
    @Override
    public void onEnable() {
        if (this.eventMap.containsKey("enable")) {
            try {
                this.eventMap.get("enable").call(null, new Object[0]);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in enable event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("enable");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Enable event unloaded", 7.0f);
            }
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.eventMap.containsKey("disable")) {
            try {
                this.eventMap.get("disable").call(null, new Object[0]);
            }
            catch (Exception e) {
                ChatUtil.scriptError(this, "in disable event");
                ChatUtil.print(false, e.getMessage());
                this.eventMap.remove("disable");
                NotificationManager.post(NotificationType.WARNING, "\"" + this.getName() + "\" Script", "Disable event unloaded", 7.0f);
            }
        }
        super.onDisable();
    }
    
    public File getFile() {
        return this.file;
    }
    
    public boolean isReloadable() {
        return this.reloadable;
    }
    
    public void setReloadable(final boolean reloadable) {
        this.reloadable = reloadable;
    }
}
