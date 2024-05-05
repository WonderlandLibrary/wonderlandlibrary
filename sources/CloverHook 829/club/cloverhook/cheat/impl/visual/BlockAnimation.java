package club.cloverhook.cheat.impl.visual;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
 
public class BlockAnimation extends Cheat {

    public StringsProperty mode = new StringsProperty("Mode", "How this cheat will function.", null,
            false, true, new String[] {"Cloverhook", "Ejaculation", "Sigma", "Matt"}, new Boolean[] {false, true, false, false});
    public BlockAnimation() {
        super("Animations", "Different Sword block animations", CheatCategory.VISUAL);
        registerProperties(mode); 
    }

}
