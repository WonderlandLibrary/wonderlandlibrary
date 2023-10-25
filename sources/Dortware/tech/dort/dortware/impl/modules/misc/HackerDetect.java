package tech.dort.dortware.impl.modules.misc;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.utils.networking.ServerUtils;
import tech.dort.dortware.impl.utils.player.ChatUtil;
import tech.dort.dortware.impl.utils.time.Stopwatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HackerDetect extends Module {

    private final Stopwatch rotationsDelay = new Stopwatch();
    private final Stopwatch speedADelay = new Stopwatch();
    private final Stopwatch speedBDelay = new Stopwatch();
    private final Stopwatch speedCDelay = new Stopwatch();
    private final Stopwatch speedBDisabledDelay = new Stopwatch();

    private final Map<EntityPlayer, Map<String, Integer>> violations = new HashMap<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public HackerDetect(ModuleData moduleData) {
        super(moduleData);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        executor.execute(() -> {
            for (EntityPlayer player : mc.theWorld.playerEntities.stream().filter(player -> ServerUtils.onHypixel() ? player != mc.thePlayer && player.isValid() && !player.isInvisible() : player != mc.thePlayer).collect(Collectors.toList())) {

                Block blockBelow = mc.theWorld.getBlockState(new BlockPos(player).offsetDown()).getBlock();
                Block blockAbove = mc.theWorld.getBlockState(new BlockPos(player).offsetUp(2)).getBlock();

                // INVALID ROTATIONS
                if (Math.abs(player.rotationPitch) > 90 && rotationsDelay.timeElapsed(250L)) {
                    incrementVL(player, "Invalid Rotations");
                    ChatUtil.displayFlag(player.getName(), "Invalid Rotations", getViolations(player, "Invalid Rotations"));
                    rotationsDelay.resetTime();
                }

                if (!player.capabilities.isFlying) {
                    // SPEED A
                    double speedCap = 0.5F;

                    if (player.isPotionActive(Potion.moveSpeed)) {
                        int amp = player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
                        speedCap *= 1.2 * amp;
                    }

                    if (blockBelow instanceof BlockStairs || blockBelow instanceof BlockSlab || !(blockAbove instanceof BlockAir)) {
                        speedADelay.resetTime();
                    }

                    if (((Math.abs(player.lastTickPosX - player.posX) > speedCap || Math.abs(player.lastTickPosZ - player.posZ) > speedCap)) && !(Math.abs(player.lastTickPosX - player.posX) > speedCap * 10) && !(Math.abs(player.lastTickPosZ - player.posZ) > speedCap * 10) && player.onGround) {
                        incrementVL(player, "Speed A");
                        if (speedADelay.timeElapsed(50L)) {
                            ChatUtil.displayFlag(player.getName(), "Speed A", getViolations(player, "Speed A"));
                            speedADelay.resetTime();
                        }
                    }

                    // SPEED B
                    double ySpeedCap = 1F;

                    if (player.isPotionActive(Potion.jump)) {
                        int amplifier = player.getActivePotionEffect(Potion.jump).getAmplifier();
                        ySpeedCap += (amplifier + 1) * 0.1F;
                    }

                    if (blockBelow instanceof BlockSlime || blockBelow instanceof BlockStairs || blockBelow instanceof BlockSlab || player.isInWater() || player.isInLava()) {
                        speedBDisabledDelay.resetTime();
                    }

                    if (player.fallDistance < 1 && !player.onGround && speedBDisabledDelay.timeElapsed(2500L) && (Math.abs(player.lastTickPosY - player.posY) > ySpeedCap && !(Math.abs(player.lastTickPosY - player.posY) > ySpeedCap * 10))) {
                        incrementVL(player, "Speed B");
                        if (speedBDelay.timeElapsed(50L)) {
                            ChatUtil.displayFlag(player.getName(), "Speed B", getViolations(player, "Speed B"));
                            speedBDelay.resetTime();
                        }
                    }

                    // SPEED C
                    double speedCapOffGround = 0.7F;

                    if (player.isPotionActive(Potion.moveSpeed)) {
                        int amp = player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
                        speedCapOffGround *= 1.2 * amp;
                    }

                    if (((Math.abs(player.lastTickPosX - player.posX) > speedCapOffGround || Math.abs(player.lastTickPosZ - player.posZ) > speedCapOffGround)) && !(Math.abs(player.lastTickPosX - player.posX) > speedCapOffGround * 10) && !(Math.abs(player.lastTickPosZ - player.posZ) > speedCapOffGround * 10) && !player.onGround) {
                        incrementVL(player, "Speed C");
                        if (speedCDelay.timeElapsed(50L)) {
                            ChatUtil.displayFlag(player.getName(), "Speed C", getViolations(player, "Speed C"));
                            speedCDelay.resetTime();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onEnable() {
        super.onEnable();
        rotationsDelay.resetTime();
        speedADelay.resetTime();
        speedBDelay.resetTime();
        speedCDelay.resetTime();
        speedBDisabledDelay.resetTime();
        violations.clear();
    }

    public void incrementVL(EntityPlayer player, String checkName) {
        Map<String, Integer> map = violations.getOrDefault(player, new HashMap<>());
        map.put(checkName, map.getOrDefault(checkName, 0) + 1);
        violations.put(player, map);
    }

    public int getViolations(EntityPlayer player, String checkName) {
        return violations.getOrDefault(player, new HashMap<>()).getOrDefault(checkName, 0);
    }
}
