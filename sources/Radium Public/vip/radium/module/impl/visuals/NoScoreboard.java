package vip.radium.module.impl.visuals;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.event.CancellableEvent;
import vip.radium.event.impl.render.overlay.RenderScoreboardEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;

@ModuleInfo(label = "No Scoreboard", category = ModuleCategory.VISUALS)
public final class NoScoreboard extends Module {

    @EventLink
    public final Listener<RenderScoreboardEvent> onRenderScoreboardEvent = CancellableEvent::setCancelled;
}
