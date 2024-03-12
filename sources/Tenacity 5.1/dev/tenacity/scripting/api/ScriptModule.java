package dev.tenacity.scripting.api;

import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.event.impl.game.WorldEvent;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.event.impl.player.*;
import dev.tenacity.event.impl.render.*;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.player.ChatUtil;
import jdk.nashorn.api.scripting.JSObject;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;

public class ScriptModule extends Module {

    private final HashMap<String, JSObject> eventMap;
    @Getter
    private final File file;
    @Getter
    @Setter
    private boolean reloadable = true;

    public ScriptModule(String name, String description, HashMap<String, JSObject> events, String author, File file) {
        super(name, Category.SCRIPTS, description);
        eventMap = events;
        this.file = file;
        setAuthor(author);
    }

    @Override
    public void onRender2DEvent(Render2DEvent event) {
        if (eventMap.containsKey("render")) {
            try {
                eventMap.get("render").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in render2D event");
                e.printStackTrace();
                //ChatUtil.print(false, e.getMessage());
                eventMap.remove("render");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Render2D event unloaded", 7);
            }
        }
    }

    @Override
    public void onShaderEvent(ShaderEvent shaderEvent) {
        if (eventMap.containsKey("shader")) {
            try {
                eventMap.get("shader").call(null, shaderEvent);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in shader event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("shader");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Shader event unloaded", 7);
            }
        }
    }

    @Override
    public void onChatReceivedEvent(ChatReceivedEvent event) {
        if (eventMap.containsKey("chat")) {
            try {
                eventMap.get("chat").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in chat event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("chat");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Chat event unloaded", 7);
            }
        }
    }

    @Override
    public void onSafeWalkEvent(SafeWalkEvent event) {
        if (eventMap.containsKey("safewalk")) {
            try {
                eventMap.get("safewalk").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in safewalk event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("safewalk");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "SafeWalk event unloaded", 7);
            }
        }
    }

    @Override
    public void onPlayerSendMessageEvent(PlayerSendMessageEvent event) {
        if (eventMap.containsKey("playerMessage")) {
            try {
                eventMap.get("playerMessage").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in playerMessage event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("playerMessage");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "PlayerSendMessage event unloaded", 7);
            }
        }
    }

    @Override
    public void onWorldEvent(WorldEvent event) {
        if(event instanceof WorldEvent.Load) {
            if (eventMap.containsKey("worldLoad")) {
                try {
                    eventMap.get("worldLoad").call(null, event);
                } catch (Exception e) {
                    ChatUtil.scriptError(this, "in worldLoad event");
                    ChatUtil.print(false, e.getMessage());
                    eventMap.remove("worldLoad");
                    NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "WorldLoad event unloaded", 7);
                }
            }
        }
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if (eventMap.containsKey("render3D")) {
            try {
                eventMap.get("render3D").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in render3D event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("render3D");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Render3D event unloaded", 7);
            }
        }
    }

    @Override
    public void onMotionEvent(MotionEvent event) {
        setSuffix(getAuthor());
        if (eventMap.containsKey("motion")) {
            try {
                eventMap.get("motion").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in motion event");
                ChatUtil.print(false, e.getMessage());
                System.out.println(e.getMessage());
                NotificationManager.post(NotificationType.WARNING,  "\"" + getName() + "\" Script", "Motion event unloaded", 7);
                eventMap.remove("motion");
            }
        }
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        if (eventMap.containsKey("move")) {
            try {
                eventMap.get("move").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in move event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("move");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Move event unloaded", 7);
            }
        }
    }

    @Override
    public void onTickEvent(TickEvent event) {
        if (eventMap.containsKey("tick")) {
            try {
                eventMap.get("tick").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in tick event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("tick");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Tick event unloaded", 7);
            }
        }
    }

    @Override
    public void onPacketSendEvent(PacketSendEvent event) {
    if (eventMap.containsKey("packetSend")) {
            try {
                eventMap.get("packetSend").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in packetSend event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("packetSend");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "PacketSend event unloaded", 7);
            }
        }
    }

    @Override
    public void onPacketReceiveEvent(PacketReceiveEvent event) {
        if (eventMap.containsKey("packetReceive")) {
            try {
                eventMap.get("packetReceive").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in packetReceive event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("packetReceive");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "PacketReceive event unloaded", 7);
            }
        }
    }


    @Override
    public void onAttackEvent(AttackEvent event) {
        if (eventMap.containsKey("attack")) {
            try {
                eventMap.get("attack").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in attack event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("attack");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Attack event unloaded", 7);
            }
        }
    }

    @Override
    public void onRenderModelEvent(RenderModelEvent event) {
        if (eventMap.containsKey("renderModel")) {
            try {
                eventMap.get("renderModel").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in renderModel event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("renderModel");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Render model event unloaded", 7);
            }
        }
    }

    @Override
    public void onCustomBlockRender(CustomBlockRenderEvent event) {
        if (eventMap.containsKey("customBlockRender")) {
            try {
                eventMap.get("customBlockRender").call(null, event);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in customBlockRender event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("customBlockRender");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Custom Block Render event unloaded", 7);
            }
        }
    }

    @Override
    public void onEnable() {
        if (eventMap.containsKey("enable")) {
            try {
                eventMap.get("enable").call(null);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in enable event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("enable");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Enable event unloaded", 7);
            }
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (eventMap.containsKey("disable")) {
            try {
                eventMap.get("disable").call(null);
            } catch (Exception e) {
                ChatUtil.scriptError(this, "in disable event");
                ChatUtil.print(false, e.getMessage());
                eventMap.remove("disable");
                NotificationManager.post(NotificationType.WARNING, "\"" + getName() + "\" Script", "Disable event unloaded", 7);
            }
        }
        super.onDisable();
    }
}
