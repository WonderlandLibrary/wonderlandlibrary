package com.enjoytheban.ui.login;

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
    public Alt getLastAlt()
    {
        return this.lastAlt;
    }

    public void setLastAlt(Alt alt)
    {
        this.lastAlt = alt;
    }

    public static void setupAlts()
    {
        AltManager.alts = new ArrayList();
    }

    public static List<Alt> getAlts()
    {
        return AltManager.alts;
    }
}
