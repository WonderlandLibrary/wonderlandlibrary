package com.enjoytheban.module.modules.render;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

public class Xray extends Module {

    public List<Integer> blocks = new ArrayList<>();

    public Xray() {
        super("Xray", new String[]{"xrai", "oreesp"}, ModuleType.Render);
        setColor(Color.GREEN.getRGB());
        blocks.add(16);
        blocks.add(56);
        blocks.add(14);
        blocks.add(15);
        blocks.add(129);
        blocks.add(73);
    }

    @Override
    public void onEnable() {
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
    }

    public List<Integer> getBlocks() {
        return blocks;
    }
}
