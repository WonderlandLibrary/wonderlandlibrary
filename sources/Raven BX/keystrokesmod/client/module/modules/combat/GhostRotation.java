package keystrokesmod.client.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class GhostRotation {
    private final Minecraft mc = Minecraft.getMinecraft();
    private int hitCount = 0;

    public void performComboMove(EntityLivingBase enemy) {
        Vec3 playerPos = mc.thePlayer.getPositionVector();
        Vec3 playerLook = mc.thePlayer.getLookVec();
        double scale = 3.0;

        Vec3 rightOffset = new Vec3(-playerLook.zCoord * scale, playerLook.yCoord * scale, playerLook.xCoord * scale);
        Vec3 leftOffset = new Vec3(playerLook.zCoord * scale, playerLook.yCoord * scale, -playerLook.xCoord * scale);

        new Thread(() -> {
            int ticks = 0;
            while (ticks < 20) {
                Vec3 newPosition;
                if (ticks < 10) {
                    newPosition = playerPos.add(rightOffset);
                } else {
                    newPosition = playerPos.add(leftOffset);
                }
                mc.thePlayer.setPositionAndUpdate(newPosition.xCoord, newPosition.yCoord, newPosition.zCoord);
                try {
                    Thread.sleep(50); // Adjust this delay based on desired duration
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ticks++;
            }
            hitCount = 0; // Reset hit count after combo
        }).start();
    }

    public void onPlayerAttack(EntityLivingBase enemy) {
        double distance = mc.thePlayer.getDistanceToEntity(enemy);

        if (distance >= 1.0 && distance <= 2.7) {
            hitCount++;

            if (hitCount > 3) {
                performComboMove(enemy);
            }
        }
    }
}
