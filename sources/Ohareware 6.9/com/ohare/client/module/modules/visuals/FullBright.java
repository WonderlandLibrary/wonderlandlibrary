package com.ohare.client.module.modules.visuals;

import java.awt.Color;

import com.ohare.client.module.Module;

/**
 * made by Xen for OhareWare
 *
 * @since 6/11/2019
 **/
public class FullBright extends Module {
    private float old;

    public FullBright() {
        super("FullBright", Category.VISUALS, new Color(0xFFE879).getRGB());
        setDescription("Bright boys");
        setRenderlabel("Full Bright");
    }

    @Override
    public void onEnable()
    {
        old = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1.5999999E7F;
    }

    @Override
    public void onDisable()
    {
        mc.gameSettings.gammaSetting = old;
    }

    @Override
    public boolean hasSubscribers() {
        return false;
    }
}
