package com.initial.modules.impl.visual;

import java.util.*;
import net.minecraft.block.*;
import com.initial.modules.*;

public class XRay extends Module
{
    public static ArrayList<Block> xrayBlocks;
    
    public XRay() {
        super("XRay", 0, Category.VISUAL);
    }
    
    @Override
    public void onEnable() {
        this.mc.renderGlobal.loadRenderers();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.mc.renderGlobal.loadRenderers();
        super.onDisable();
    }
    
    static {
        XRay.xrayBlocks = new ArrayList<Block>();
    }
}
