/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jdk.nashorn.api.scripting.JSObject
 *  kotlin.Lazy
 *  kotlin.LazyKt
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.script.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jdk.nashorn.api.scripting.JSObject;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.ClickBlockEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.event.SlowDownEvent;
import net.ccbluex.liquidbounce.event.StepConfirmEvent;
import net.ccbluex.liquidbounce.event.StepEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="ScriptModule", description="Empty", category=ModuleCategory.MISC)
public final class ScriptModule
extends Module {
    private final LinkedHashMap _values;
    private final HashMap events;
    private final JSObject moduleObject;
    private String _tag;
    private final Lazy settings$delegate;

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        this.callEvent("motion", motionEvent);
    }

    private final void callEvent(String string, Object object) {
        try {
            JSObject jSObject = (JSObject)this.events.get(string);
            if (jSObject != null) {
                jSObject.call((Object)this.moduleObject, new Object[]{object});
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in module '" + this.getName() + "'!", throwable);
        }
    }

    @EventTarget
    public final void onKey(KeyEvent keyEvent) {
        this.callEvent("key", keyEvent);
    }

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        this.callEvent("move", moveEvent);
    }

    public final void on(String string, JSObject jSObject) {
        ((Map)this.events).put(string, jSObject);
    }

    @EventTarget
    public final void onRender2D(Render2DEvent render2DEvent) {
        this.callEvent("render2D", render2DEvent);
    }

    public final LinkedHashMap getSettings() {
        Lazy lazy = this.settings$delegate;
        ScriptModule scriptModule = this;
        Object var3_3 = null;
        boolean bl = false;
        return (LinkedHashMap)lazy.getValue();
    }

    @Override
    public List getValues() {
        return CollectionsKt.toList((Iterable)this._values.values());
    }

    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        this.callEvent("jump", jumpEvent);
    }

    static void callEvent$default(ScriptModule scriptModule, String string, Object object, int n, Object object2) {
        if ((n & 2) != 0) {
            object = null;
        }
        scriptModule.callEvent(string, object);
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        this.callEvent("attack", attackEvent);
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        this.callEvent("render3D", render3DEvent);
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        ScriptModule.callEvent$default(this, "update", null, 2, null);
    }

    @Override
    public void onDisable() {
        ScriptModule.callEvent$default(this, "disable", null, 2, null);
    }

    @EventTarget
    public final void onStepConfirm(StepConfirmEvent stepConfirmEvent) {
        ScriptModule.callEvent$default(this, "stepConfirm", null, 2, null);
    }

    public ScriptModule(JSObject jSObject) {
        this.moduleObject = jSObject;
        this.events = new HashMap();
        this._values = new LinkedHashMap();
        this.settings$delegate = LazyKt.lazy((Function0)new Function0(this){
            final ScriptModule this$0;

            public Object invoke() {
                return this.invoke();
            }
            {
                this.this$0 = scriptModule;
                super(0);
            }

            public final LinkedHashMap invoke() {
                return ScriptModule.access$get_values$p(this.this$0);
            }

            static {
            }
        });
        Object object = this.moduleObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        this.setName((String)object);
        Object object2 = this.moduleObject.getMember("description");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        this.setDescription((String)object2);
        Object object3 = this.moduleObject.getMember("category");
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object3;
        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            if (!StringsKt.equals((String)string, (String)moduleCategory.getDisplayName(), (boolean)true)) continue;
            this.setCategory(moduleCategory);
        }
        if (this.moduleObject.hasMember("settings")) {
            ModuleCategory moduleCategory;
            Object object4 = this.moduleObject.getMember("settings");
            if (object4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type jdk.nashorn.api.scripting.JSObject");
            }
            moduleCategory = (JSObject)object4;
            for (String string2 : moduleCategory.keySet()) {
                Object object5 = moduleCategory.getMember(string2);
                if (object5 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.value.Value<*>");
                }
                ((Map)this._values).put(string2, (Value)object5);
            }
        }
        if (this.moduleObject.hasMember("tag")) {
            Object object6 = this.moduleObject.getMember("tag");
            if (object6 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
            }
            this._tag = (String)object6;
        }
    }

    @EventTarget
    public final void onSlowDown(SlowDownEvent slowDownEvent) {
        this.callEvent("slowDown", slowDownEvent);
    }

    @Override
    public String getTag() {
        return this._tag;
    }

    @EventTarget
    public final void onWorld(WorldEvent worldEvent) {
        this.callEvent("world", worldEvent);
    }

    @EventTarget
    public final void onSession(SessionEvent sessionEvent) {
        ScriptModule.callEvent$default(this, "session", null, 2, null);
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        this.callEvent("packet", packetEvent);
    }

    @EventTarget
    public final void onStrafe(StrafeEvent strafeEvent) {
        this.callEvent("strafe", strafeEvent);
    }

    @EventTarget
    public final void onClickBlock(ClickBlockEvent clickBlockEvent) {
        this.callEvent("clickBlock", clickBlockEvent);
    }

    public void setTag(@Nullable String string) {
        this._tag = string;
    }

    public static final LinkedHashMap access$get_values$p(ScriptModule scriptModule) {
        return scriptModule._values;
    }

    @Override
    public void onEnable() {
        ScriptModule.callEvent$default(this, "enable", null, 2, null);
    }

    @EventTarget
    public final void onStep(StepEvent stepEvent) {
        this.callEvent("step", stepEvent);
    }
}

