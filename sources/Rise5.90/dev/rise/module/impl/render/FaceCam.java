/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.math.TimeUtil;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "FaceCam", description = "Adds your facecam to the screen", category = Category.RENDER)
public final class FaceCam extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Dawson Hessler", "Dawson Hessler", "Xavier");

    ArrayList<BufferedImage> currentFrames = new ArrayList<>();
    String previousMode = "";
    int frameRate, currentFrame, maxFrames;
    TimeUtil stopWatch = new TimeUtil();

    @Override
    public void onRender2DEvent(Render2DEvent event) {
        if (!Objects.equals(previousMode, mode.getMode())) {
            this.updateFrame();
        }

        if (currentFrames.isEmpty()) return;

        if (stopWatch.hasReached(1000 / frameRate)) {
            this.updateFrame();
        }

        // Drawing frame


        previousMode = mode.getMode();
    }

    @Override
    public void onPreMotion(PreMotionEvent event) {
        switch (mode.getMode()) {
            case "Dawson Hessler":
                frameRate = 60;
                break;

            case "Xavier":
                frameRate = 30;
                break;
        }
    }

    public void updateFrame() {
        if (currentFrame + 1 > currentFrames.size()) {
            currentFrame = 0;
        } else {
            currentFrame++;
        }
    }
}
