package slavikcodd3r.rainbow.gui.alt;

import java.util.ArrayList;

public class AltManager
{
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;
    
    public AltManager() {
        super();
    }
    
    public ArrayList<Alt> getRegistry() {
        return AltManager.registry;
    }
    
    public void setLastAlt(final Alt alt) {
        AltManager.lastAlt = alt;
    }
    
    static {
        AltManager.registry = new ArrayList<Alt>();
    }
}
