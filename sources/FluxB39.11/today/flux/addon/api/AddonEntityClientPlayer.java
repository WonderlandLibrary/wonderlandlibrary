package today.flux.addon.api;

import com.soterdev.SoterObfuscator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import today.flux.addon.api.entities.AddonEntityPlayer;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.utility.MoveUtils;

/**
 * 玩家信息获取
 */
public class AddonEntityClientPlayer extends AddonEntityPlayer {
    /**
     * 获取玩家实体
     *
     * @return 玩家实体对象
     */
    public static AddonEntityClientPlayer getClientPlayer() {
        return new AddonEntityClientPlayer();
    }

    public AddonEntityClientPlayer() {
        super(mc.thePlayer);
    }


    public EntityPlayerSP getEntity() {
        return ((EntityPlayerSP) entity);
    }

    /**
     * 发送聊天信息
     *
     * @param message 聊天信息字符串
     */

    public void sendChatMessage(String message) {
        getEntity().sendChatMessage(message);
    }

    /**
     * 玩家跳跃
     */

    public void jump() {
        getEntity().jump();
    }

    /**
     * 发送数据包
     *
     * @param packet AddonPacket数据包对象
     */

    public void sendPacket(AddonPacket packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet.nativePacket);
    }

    /**
     * 发送无事件包
     *
     * @param packet AddonPacket数据包对象
     */

    public void sendPacketNoEvent(AddonPacket packet) {
        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet.nativePacket);
    }

    /**
     * 设置当前玩家视角速度
     *
     * @param speed 需要设置的速度
     */

    public void setSpeed(double speed) {
        MoveUtils.setMotion(speed);
    }

    /**
     * 设置当前玩家视角速度和角度
     *
     * @param speed 需要设置的速度
     * @param yaw   需要设置的角度
     */

    public void setSpeed(double speed, float yaw) {
        MoveUtils.setMotion(speed, yaw);
    }
}
