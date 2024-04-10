package info.sigmaclient.sigma.scripts;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.ChatUtils;

public class ScriptModule extends Module {
    String script;
    ExpressRunner runner;
    DefaultContext<String, Object> context;
    public ScriptModule(String name, Category category, String desc, String script) {
        super(name, category, desc);
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        try {
            runner.execute("onRenderEvent", context, null, true, false, 1000);
        } catch (Exception e) {
            ChatUtils.sendMessage("Script Module: " + name + " bad happen: " + e.getMessage());
        }
        super.onRenderEvent(event);
    }

    @Override
    public void onWorldEvent(WorldEvent event) {
        try {
            runner.execute("onWorldEvent", context, null, true, false, 1000);
        } catch (Exception e) {
            ChatUtils.sendMessage("Script Module: " + name + " bad happen: " + e.getMessage());
        }
        super.onWorldEvent(event);
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        try {
            runner.execute("onRender3DEvent", context, null, true, false, 1000);
        } catch (Exception e) {
            ChatUtils.sendMessage("Script Module: " + name + " bad happen: " + e.getMessage());
        }
        super.onRender3DEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        try {
            runner.execute("onMoveEvent", context, null, true, false, 1000);
        } catch (Exception e) {
            ChatUtils.sendMessage("Script Module: " + name + " bad happen: " + e.getMessage());
        }
        super.onMoveEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        try {
            runner.execute("onUpdateEvent", context, null, true, false, 1000);
        } catch (Exception e) {
            ChatUtils.sendMessage("Script Module: " + name + " bad happen: " + e.getMessage());
        }
        super.onUpdateEvent(event);
    }
}
