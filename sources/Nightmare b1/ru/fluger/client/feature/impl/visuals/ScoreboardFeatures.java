// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.render.EventRenderScoreboard;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class ScoreboardFeatures extends Feature
{
    public static BooleanSetting noScore;
    public NumberSetting x;
    public NumberSetting y;
    
    public ScoreboardFeatures() {
        super("Scoreboard", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0441\u043a\u043e\u0440\u0431\u043e\u0440\u0434 \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0435", Type.Visuals);
        ScoreboardFeatures.noScore = new BooleanSetting("No Scoreboard", false, () -> true);
        this.x = new NumberSetting("Scoreboard X", 0.0f, -1000.0f, 1000.0f, 1.0f, () -> !ScoreboardFeatures.noScore.getCurrentValue());
        this.y = new NumberSetting("Scoreboard Y", 0.0f, -500.0f, 500.0f, 1.0f, () -> !ScoreboardFeatures.noScore.getCurrentValue());
        this.addSettings(ScoreboardFeatures.noScore, this.x, this.y);
    }
    
    @EventTarget
    public void onRenderScoreboard(final EventRenderScoreboard event) {
        if (event.isPre()) {
            bus.c(-this.x.getCurrentValue(), this.y.getCurrentValue(), 12.0f);
        }
        else {
            bus.c(this.x.getCurrentValue(), -this.y.getCurrentValue(), 12.0f);
        }
    }
}
