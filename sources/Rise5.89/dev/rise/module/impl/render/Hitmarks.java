package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Hitmarkers", description = "Shows a marker when you hit someone", category = Category.RENDER)
public class Hitmarks extends Module {
    private int lastEntity;
    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil attackTimer = new TimeUtil();

    private final NumberSetting volume = new NumberSetting("Volume", this, 1, 0, 2, 0.1);
    private final NumberSetting pitch = new NumberSetting("Pitch", this, 1, 0, 2, 0.1);
    private final BooleanSetting randomPitch = new BooleanSetting("Random Pitch", this, false);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.theWorld.getEntityByID(lastEntity) != null && mc.thePlayer.getDistanceToEntity(mc.theWorld.getEntityByID(lastEntity)) < 8 && mc.theWorld.getEntityByID(lastEntity).hurtResistantTime == 19 && !attackTimer.hasReached(2000)) {
            timer.reset();
            mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation("rise.hitmarker"), (float) volume.getValue(), randomPitch.isEnabled() ? (float) Math.random() * 2 : (float) pitch.getValue()));
        }

        if (attackTimer.hasReached(3000)) {
            lastEntity = -1;
        }
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        final Entity e = event.getTarget();
        if (e != null) {
            lastEntity = e.getEntityId();
            attackTimer.reset();
        }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {

        if (!timer.hasReached(500)) {
            final ScaledResolution sr = new ScaledResolution(mc);
            RenderUtil.color(Color.WHITE);
            RenderUtil.imageCentered(new ResourceLocation("rise/hitmarker.png"), sr.getScaledWidth() / 2f + 0.1f, sr.getScaledHeight() / 2f + 0.1f, 280 / 20f, 280 / 20f);
        }
        if (timer.hasReached(500) && !timer.hasReached(755)) {
            final ScaledResolution sr = new ScaledResolution(mc);
            RenderUtil.color(new Color(255, 255, 255, (int) Math.abs(((System.nanoTime() / 1000000L) - timer.lastMS) - 755)));
            RenderUtil.imageCentered(new ResourceLocation("rise/hitmarker.png"), sr.getScaledWidth() / 2f + 0.1f, sr.getScaledHeight() / 2f + 0.1f, 280 / 20f, 280 / 20f);
        }
    }
}
