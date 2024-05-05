package tech.drainwalk.client.scripting;


import com.darkmagician6.eventapi.EventTarget;
import org.openjdk.nashorn.api.scripting.JSObject;
import org.openjdk.nashorn.api.scripting.ScriptUtils;
import tech.drainwalk.events.EventRender2D;
import tech.drainwalk.events.EventRender3D;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.client.option.Option;

public class ScriptModule extends Module {
    private final Script script;

    public ScriptModule(Script script) {
        super(script.getName(), Category.SCRIPTS);
        this.script = script;
        if(((JSObject)script.getEngine().get("script")).hasMember("options")) {
            super.register((Option<?>[]) ScriptUtils.convert(((JSObject) script.getEngine().get("script")).getMember("options"), Option[].class));
        }

        if(((JSObject)script.getEngine().get("script")).hasMember("type")) {
            super.addType((Type) ScriptUtils.convert(((JSObject) script.getEngine().get("script")).getMember("type"), Type.class));
        }
        if(((JSObject)script.getEngine().get("script")).hasMember("key")) {
            super.addKey((Integer) ScriptUtils.convert(((JSObject) script.getEngine().get("script")).getMember("key"), Integer.class));
        }
        if(((JSObject)script.getEngine().get("script")).hasMember("description")) {
            super.addDescription((String) ScriptUtils.convert(((JSObject) script.getEngine().get("script")).getMember("description"), String.class));
        }
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Category getCategory() {
        return super.getCategory();
    }

    @Override
    public int getKey() {
        return super.getKey();
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        script.invokeMethod("onEnable");
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        script.invokeMethod("onRender2D", event);
    }
    @EventTarget
    public void onRender3D(EventRender3D event) {
        script.invokeMethod("onRender3D", event);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        script.invokeMethod("onUpdate", event);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        script.invokeMethod("onDisable");
    }
}
