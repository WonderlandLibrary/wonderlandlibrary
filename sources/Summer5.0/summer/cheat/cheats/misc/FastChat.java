package summer.cheat.cheats.misc;

import summer.Summer;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.cheat.guiutil.Setting;

public class FastChat extends Cheats{

	public static Setting noShadow;

	public FastChat() {
		super("FastChat", "Removes the background in chat", Selection.RENDER);

		Summer.INSTANCE.settingsManager.Property(noShadow = new Setting("No Shadow", this, false));

	}

}
