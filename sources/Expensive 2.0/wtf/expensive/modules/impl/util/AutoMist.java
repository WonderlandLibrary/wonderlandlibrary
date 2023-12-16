package wtf.expensive.modules.impl.util;

import net.minecraft.util.text.ITextComponent;
import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ButtonSetting;
import wtf.expensive.ui.automyst.Window;

@FunctionAnnotation(name = "Auto Buy", type = Type.Util)
public class AutoMist extends Function {

    public ButtonSetting buttonSetting = new ButtonSetting("Открыть панель", () -> {
        mc.displayGuiScreen(new Window(ITextComponent.getTextComponentOrEmpty("")));
    });

    public AutoMist() {
        super();
        addSettings(buttonSetting);
    }

    @Override
    public void onEvent(Event event) {

    }
}
