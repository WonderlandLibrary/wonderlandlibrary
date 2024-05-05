package client.module.impl.other;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.WorldLoadEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.other.autoreport.MushAutoReport;
import client.value.impl.ModeValue;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Auto Report", description = "", category = Category.OTHER)
public class AutoReport extends Module {
    @Getter
    private final List<String> alreadyReported = new ArrayList<>();
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new MushAutoReport("Mush", this))
            .setDefault("Mush");
    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> alreadyReported.clear();
}
