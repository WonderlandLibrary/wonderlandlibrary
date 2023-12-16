package wtf.expensive.modules.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SEntityStatusPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.Initilization;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventWorldChange;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.math.AuraUtil;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.misc.TimerUtil;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Math.*;
import static net.minecraft.network.play.client.CUseEntityPacket.Action.ATTACK;
import static net.minecraft.network.play.server.SChangeGameStatePacket.field_241770_g_;
import static net.minecraft.util.math.MathHelper.wrapDegrees;

@SuppressWarnings("all")
@FunctionAnnotation(name = "HitSound", type = Type.Combat)
public class HitSound extends Function {

    private final ModeSetting sound = new ModeSetting("Звук",
            "bell",
            "bell", "metallic", "rust", "bubble", "bonk", "crime"
    );

    private final MultiBoxSetting triggers = new MultiBoxSetting("Тригеры",
            new BooleanOption("Удар", true),
            new BooleanOption("Выстрел", true)
    );

    SliderSetting volume = new SliderSetting("Громкость", 35, 5, 100, 5);

    Map<Entity, Long> targets = new HashMap<>();

    public HitSound() {
        addSettings(sound, triggers, volume);
    }

    @Override
    public void onEvent(Event event) {
        if (mc.player == null || mc.world == null) return;

        if (event instanceof EventWorldChange) {
            targets.clear();
        }

        if (event instanceof EventPacket e) {
            if (triggers.get(1) && e.getPacket() instanceof SChangeGameStatePacket p) {
                if (p.func_241776_b_() == field_241770_g_) {
                    playSound(null);
                }
            }

            if (!triggers.get(0)) return;

            if (e.getPacket() instanceof CUseEntityPacket p && p.getAction() == ATTACK) {
                targets.put(p.getEntityFromWorld(mc.world), System.currentTimeMillis());
            }

            if (targets.isEmpty()) return;

            if (e.getPacket() instanceof SEntityStatusPacket p) {
                targets.forEach((entity, time) -> {
                    if (entity != null && entity.getEntityId() == p.entityId) {
                        if (time + 500 >= System.currentTimeMillis()) {
                            playSound(entity);
                        }
                    }
                });
            }
        }
    }

    public void playSound(Entity e) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(mc.getResourceManager().getResource(new ResourceLocation("expensive/sounds/" + sound.get() + ".wav")).getInputStream());
            if (audioInputStream == null) {
                System.out.println("Sound not found!");
                return;
            }
            clip.open(audioInputStream);
            clip.start();
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (e != null) {
                FloatControl balance = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
                Vector3d vec = e.getPositionVec().subtract(Minecraft.getInstance().player.getPositionVec());


                double yaw = wrapDegrees(toDegrees(atan2(vec.z, vec.x)) - 90);
                double delta = wrapDegrees(yaw - mc.player.rotationYaw);

                if (abs(delta) > 180) delta -= signum(delta) * 360;
                try {
                    balance.setValue((float) delta / 180);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            floatControl.setValue(-(mc.player.getDistance(e) * 5) - (volume.getMax() / volume.getValue().floatValue()));
        } catch (Exception exception) {
            //exception.printStackTrace();
        }
    }

    @Override
    protected void onEnable() {
        super.onEnable();

        targets.clear();
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        targets.clear();
    }
}
