package net.ccbluex.liquidbounce.features.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.features.command.commands.AutoSettingsCommand;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "invoke"})
final class AutoSettingsCommand$loadSettings$thread$1
extends Lambda
implements Function0<Unit> {
    final AutoSettingsCommand this$0;
    final boolean $useCached;
    final Function1 $callback;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void invoke() {
        Object object = this.this$0.loadingLock;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (object) {
            boolean bl3 = false;
            if (this.$useCached && this.this$0.autoSettingFiles != null) {
                List list = this.this$0.autoSettingFiles;
                if (list == null) {
                    Intrinsics.throwNpe();
                }
                this.$callback.invoke(list);
                return;
            }
            try {
                JsonElement json = new JsonParser().parse(HttpUtils.get("https://api.github.com/repos/CCBlueX/LiquidCloud/contents/LiquidBounce/settings"));
                boolean bl4 = false;
                List autoSettings = new ArrayList();
                if (json instanceof JsonArray) {
                    Iterator iterator = ((JsonArray)json).iterator();
                    while (iterator.hasNext()) {
                        JsonElement setting;
                        JsonElement jsonElement = setting = (JsonElement)iterator.next();
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "setting");
                        JsonElement jsonElement2 = jsonElement.getAsJsonObject().get("name");
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "setting.asJsonObject[\"name\"]");
                        String string = jsonElement2.getAsString();
                        Intrinsics.checkExpressionValueIsNotNull(string, "setting.asJsonObject[\"name\"].asString");
                        autoSettings.add(string);
                    }
                }
                this.$callback.invoke(autoSettings);
                this.this$0.autoSettingFiles = autoSettings;
            }
            catch (Exception e) {
                this.this$0.chat("Failed to fetch auto settings list.");
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    AutoSettingsCommand$loadSettings$thread$1(AutoSettingsCommand autoSettingsCommand, boolean bl, Function1 function1) {
        this.this$0 = autoSettingsCommand;
        this.$useCached = bl;
        this.$callback = function1;
        super(0);
    }
}
