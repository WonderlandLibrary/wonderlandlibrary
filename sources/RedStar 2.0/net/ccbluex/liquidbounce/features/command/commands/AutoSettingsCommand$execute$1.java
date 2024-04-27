package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.commands.AutoSettingsCommand;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.SettingsUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "invoke"})
final class AutoSettingsCommand$execute$1
extends Lambda
implements Function0<Unit> {
    final AutoSettingsCommand this$0;
    final String $url;

    @Override
    public final void invoke() {
        try {
            String settings = HttpUtils.get(this.$url);
            this.this$0.chat("Applying settings...");
            SettingsUtils.INSTANCE.executeScript(settings);
            this.this$0.chat("§6Settings applied successfully");
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Notification", "Updated Settings", NotifyType.INFO, 0, 0, 24, null));
            this.this$0.playEdit();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.this$0.chat("Failed to fetch auto settings.");
        }
    }

    AutoSettingsCommand$execute$1(AutoSettingsCommand autoSettingsCommand, String string) {
        this.this$0 = autoSettingsCommand;
        this.$url = string;
        super(0);
    }
}
