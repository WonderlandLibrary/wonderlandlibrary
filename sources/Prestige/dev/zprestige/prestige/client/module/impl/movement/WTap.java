package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.api.mixin.IPlayerInteractEntityC2SPacket;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.lwjgl.glfw.GLFW;

public class WTap extends Module {

    public FloatSetting chance;
    public DragSetting holdLenght;
    public DragSetting reaction;
    public TimerUtil timer;
    public TimerUtil timer2;
    public boolean tap;
    public boolean idk;

    public WTap() {
        super("W Tap", Category.Movement, "Releases W and holds it again to reset server-side sprinting for more knockback");
        chance = setting("Chance", 100f, 0f, 100f).description("Chance of W-tapping");
        holdLenght = setting("Hold Length", 100, 200, 0, 400).description("How long W should be released");
        reaction = setting("Reaction", 10, 30, 0, 50).description("Reaction time to W-tap after hitting an entity");
        timer = new TimerUtil();
        timer2 = new TimerUtil();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (GLFW.glfwGetKey(getMc().getWindow().getHandle(), 87) != 1) {
            idk = false;
            tap = false;
            return;
        }
        if (tap && timer.delay(reaction)) {
            getMc().options.forwardKey.setPressed(false);
            timer2.reset();
            holdLenght.setValue();
            idk = true;
            tap = false;
        }
        if (!idk || !timer2.delay(holdLenght)) {
            return;
        }
        getMc().options.forwardKey.setPressed(true);
        idk = false;
    }

    @EventListener
    public void event(PacketSendEvent event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet && ((IPlayerInteractEntityC2SPacket)packet).getType().getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK && getMc().options.forwardKey.isPressed()) {
            if (getMc().player.isSprinting() && RandomUtil.INSTANCE.getRandom().nextFloat(100.0f) <= chance.getObject()) {
                reaction.setValue();
                timer.reset();
                tap = true;
            }
        }
    }
}