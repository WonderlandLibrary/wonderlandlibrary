package today.flux.module.implement.Command;

import today.flux.Flux;
import today.flux.module.Command;

/**
 * Created by John on 2017/05/02.
 */
@Command.Info(name = "resetgui", syntax = { "" }, help = "Reset the position of all windows")
public class ResetGuiCmd extends Command {
    @Override
    public void execute(String[] args) throws Error {
        Flux.INSTANCE.getClickGUI().rePositionWindows();
    }
}
