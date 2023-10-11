package com.initial.modules.impl.visual;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;

public class Animations extends Module
{
    public ModeSet mode;
    public static DoubleSet spinSpeed;
    public static DoubleSet swingSpeed;
    
    public Animations() {
        super("Animations", 0, Category.VISUAL);
        this.mode = new ModeSet("Mode", "1.7", new String[] { "1.7", "Old", "Spin", "Spinny", "Plain", "Slide", "Exhibition" });
        this.addSettings(this.mode, Animations.spinSpeed, Animations.swingSpeed);
    }
    
    static {
        Animations.spinSpeed = new DoubleSet("Spin", 2.0, 0.0, 20.0, 1.0);
        Animations.swingSpeed = new DoubleSet("Speed", 6.0, 0.0, 15.0, 1.0);
    }
}
