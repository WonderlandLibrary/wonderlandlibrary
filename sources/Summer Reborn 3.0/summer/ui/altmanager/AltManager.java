package summer.ui.altmanager;

import java.util.ArrayList;
import java.util.List;

public class AltManager
{
    static List<Alt> alts;
    static Alt lastAlt;
    
    public static void init() {
        setupAlts();
        getAlts();
    }
    
    public Alt getLastAlt() {
        return AltManager.lastAlt;
    }
    
    public void setLastAlt(final Alt alt) {
        AltManager.lastAlt = alt;
    }
    
    public static void setupAlts() {
        AltManager.alts = new ArrayList<Alt>();
    }
    
    public static List<Alt> getAlts() {
        return AltManager.alts;
    }
}
