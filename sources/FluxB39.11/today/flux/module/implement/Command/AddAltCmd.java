package today.flux.module.implement.Command;

import today.flux.Flux;
import today.flux.gui.altMgr.Alt;
import today.flux.gui.altMgr.GuiAltMgr;
import today.flux.module.Command;

@Command.Info(name = "addalt", syntax = { "" }, help = "Add alt currently logged into Alt Manager")
public class AddAltCmd extends Command {
	@Override
	public void execute(String[] args) throws Error {
		if(Flux.currentAlt != null){
			GuiAltMgr.alts.add(0, new Alt(Flux.currentAlt[0], Flux.currentAlt[1], mc.getSession().getUsername()));
		}
	}
}
