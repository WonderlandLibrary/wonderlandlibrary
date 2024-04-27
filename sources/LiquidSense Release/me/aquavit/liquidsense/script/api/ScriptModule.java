package me.aquavit.liquidsense.script.api;

import java.util.*;

import jdk.nashorn.api.scripting.JSObject;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.*;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.Value;

@ModuleInfo(name = "ScriptModule", description = "Empty", category = ModuleCategory.MISC)
public class ScriptModule extends Module {
    private final JSObject moduleObject;
    private final HashMap<String, JSObject> events = new HashMap<>();
    private final LinkedHashMap<String, Value<?>> _values = new LinkedHashMap<>();
    private String _tag;

    /**
     * Allows the user to access values by typing module.settings.<valuename>
     */
    public final LinkedHashMap<String, Value<?>> settings = _values;

    public ScriptModule(JSObject moduleObject) {
        this.moduleObject = moduleObject;
        name = moduleObject.getMember("name").toString();
        description = moduleObject.getMember("description").toString();
        category = ModuleCategory.SCRIPTS;

        if (moduleObject.hasMember("settings")) {
            JSObject settings = (JSObject) moduleObject.getMember("settings");

            for (String settingName : settings.keySet()) {
                _values.put(settingName, (Value<?>) settings.getMember(settingName));
            }
        }

        if (moduleObject.hasMember("tag")) {
            _tag = moduleObject.getMember("tag").toString();
        }
    }

    @Override
    public List<Value<?>> getValues() {
        return new ArrayList<>(_values.values());
    }

    @Override
    public String getTag() {
        return _tag;
    }

    /**
     * Called from inside the script to register a new event handler.
     * @param eventName Name of the event.
     * @param handler JavaScript function used to handle the event.
     */
    public void on(String eventName, JSObject handler) {
        events.put(eventName, handler);
    }

    @Override
    public void onEnable() {
        callEvent("enable", null);
    }

    @Override
    public void onDisable() {
        callEvent("disable", null);
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        callEvent("update", null);
    }

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        callEvent("render2D", render2DEvent);
    }

    @EventTarget
    public void onRender3D(Render3DEvent render3DEvent) {
        callEvent("render3D", render3DEvent);
    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        callEvent("packet", packetEvent);
    }

    @EventTarget
    public void onJump(JumpEvent jumpEvent) {
        callEvent("jump", jumpEvent);
    }

    @EventTarget
    public void onAttack(AttackEvent attackEvent) {
        callEvent("attack", attackEvent);
    }

    @EventTarget
    public void onKey(KeyEvent keyEvent) {
        callEvent("key", keyEvent);
    }

    @EventTarget
    public void onMove(MoveEvent moveEvent) {
        callEvent("move", moveEvent);
    }

    @EventTarget
    public void onStep(StepEvent stepEvent) {
        callEvent("step", stepEvent);
    }

    @EventTarget
    public void onStepConfirm(StepConfirmEvent stepConfirmEvent) {
        callEvent("stepConfirm", null);
    }

    @EventTarget
    public void onWorld(WorldEvent worldEvent) {
        callEvent("world", worldEvent);
    }

    @EventTarget
    public void onSession(SessionEvent sessionEvent) {
        callEvent("session", null);
    }

    @EventTarget
    public void onClickBlock(ClickBlockEvent clickBlockEvent) {
        callEvent("clickBlock", clickBlockEvent);
    }

    @EventTarget
    public void onStrafe(StrafeEvent strafeEvent) {
        callEvent("strafe", strafeEvent);
    }

    @EventTarget
    public void onSlowDown(SlowDownEvent slowDownEvent) {
        callEvent("slowDown", slowDownEvent);
    }

    @EventTarget
    public void onShutdown(ClientShutdownEvent shutdownEvent) {
        callEvent("shutdown", null);
    }



    /**
     * Calls the handler of a registered event.
     * @param eventName Name of the event to be called.
     * @param payload Event data passed to the handler function.
     */
    private void callEvent(String eventName, Object payload) {
        try {
            JSObject eventHandler = events.get(eventName);
            if (eventHandler != null) {
                eventHandler.call(moduleObject, payload);
            }
        } catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in module '" + name + "'!", throwable);
        }
    }

}
