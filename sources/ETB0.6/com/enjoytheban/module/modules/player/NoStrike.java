package com.enjoytheban.module.modules.player;

import java.awt.*;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

/**
 * @author - Purity
 * STOPS VELT PEE VEE PEE FROM STRIKING KILLER CUSTOM KKKKK VIDS
 */

public class NoStrike extends Module {

    public NoStrike() {
        super("NoStrike", new String[] {"antistrike"}, ModuleType.Player);
        setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }
}
