package wtf.evolution.module.impl.Movement;

import net.minecraft.network.play.client.CPacketPlayer;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

import java.util.concurrent.TimeUnit;

@ModuleInfo(name = "HighJump", type = Category.Movement)
public class HighJump extends Module {

    public SliderSetting value = new SliderSetting("Height", 5, 1, 10, 0.1f).call(this);

    @Override
    public void onEnable() {
        super.onEnable();
        MovementUtil.setSpeed(0);
        mc.player.connection.sendPacket(new CPacketPlayer(true));
        if (mc.player.onGround)
            mc.player.jump();
        new Thread(() -> {
            double jump = value.get();
            mc.player.motionY = jump;try {
                TimeUnit.MILLISECONDS.sleep(MathHelper.getRandomNumberBetween(200, 250));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mc.player.motionY = jump - (0.098 + (0.01 * (jump * 2 - 2)));

            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            toggle();
        }).start();
    }
}
