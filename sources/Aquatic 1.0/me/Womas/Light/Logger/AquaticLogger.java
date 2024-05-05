package me.Womas.Light.Logger;

import me.Womas.Light.Core.Aquatic;
import net.minecraft.block.Block;

public class AquaticLogger {

    public void logger(String s) {
        System.out.println("[" + Aquatic.getName() + "] " + s);
    }

    public void warn(String s) {
        System.out.print("[" + Aquatic.getName() + "] ");
        System.err.print("[WARNING] ");
        System.out.println(s);
    }

}
