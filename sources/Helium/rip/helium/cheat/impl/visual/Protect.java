package rip.helium.cheat.impl.visual;

import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.utils.property.impl.BooleanProperty;

public class Protect extends Cheat {

    public static BooleanProperty friendprotect = new BooleanProperty("Friend Protect", "Sets your friends name to a randomly generated string!", null, true);
    public static BooleanProperty nameprotect = new BooleanProperty("Name Protect", "Sets your name to the name you specified we call you", null, true);

    public Protect() {
        super("Protector", "Protects friend names & your name.", CheatCategory.VISUAL);
        registerProperties(friendprotect, nameprotect);
    }


}
