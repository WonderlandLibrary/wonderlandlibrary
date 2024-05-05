package rosegoldclient.features;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import rosegoldclient.Main;
import rosegoldclient.events.TickEndEvent;

import java.util.HashSet;

public class Phase {

    public static HashSet<BlockPos> phaseable = new HashSet<>();
    private static BlockPos lastCheckedPosition = null;
    private static final KeyBinding sneak = Main.mc.gameSettings.keyBindSneak;
    private static int sneakClicks = 0;
    private static int elseClicks = 0;

    private static final int range = 3;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!isEnabled()) {
            phaseable.clear();
            return;
        }
        Vec3d playerVec = Main.mc.player.getPositionVector();
        BlockPos playerPosition = new BlockPos(playerVec.x, playerVec.y, playerVec.z);
        if ((lastCheckedPosition == null || !lastCheckedPosition.equals(playerPosition))) {
            phaseable.clear();
            lastCheckedPosition = playerPosition;
            for (int x = playerPosition.getX() - range; x < playerPosition.getX() + range + 1; x++) {
                for (int y = playerPosition.getY(); y < playerPosition.getY() + range; y++) {
                    for (int z = playerPosition.getZ() - range; z < playerPosition.getZ() + range + 1; z++) {
                        phaseable.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void descentControl(TickEndEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!isEnabled()) return;
        Vec3d playerVec = Main.mc.player.getPositionVector();
        BlockPos playerPosition = new BlockPos(playerVec.x, playerVec.y, playerVec.z);
        if(Main.configFile.sneakHeldDescent) {
            if(sneak.isKeyDown()) {
                for (int x = playerPosition.getX() - range; x < playerPosition.getX() + range + 1; x++) {
                    for (int y = playerPosition.getY() - 1; y < playerPosition.getY(); y++) {
                        for (int z = playerPosition.getZ() - range; z < playerPosition.getZ() + range + 1; z++) {
                            phaseable.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void keyPress(InputEvent.KeyInputEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!isEnabled()) return;
        if(Main.configFile.sneakHeldDescent) return;
        Vec3d playerVec = Main.mc.player.getPositionVector();
        BlockPos playerPosition = new BlockPos(playerVec.x, playerVec.y, playerVec.z);
        if (sneak.isPressed()) {
            sneakClicks++;
        } else {
            if(sneakClicks == 0) {
                elseClicks = 0;
            }
            if(elseClicks > 1) {
                sneakClicks = 0;
                elseClicks = 0;
            }
            elseClicks++;
        }
        if (sneakClicks > 1) {
            for (int x = playerPosition.getX() - range; x < playerPosition.getX() + range + 1; x++) {
                for (int y = playerPosition.getY() - 1; y < playerPosition.getY(); y++) {
                    for (int z = playerPosition.getZ() - range; z < playerPosition.getZ() + range + 1; z++) {
                        phaseable.add(new BlockPos(x, y, z));
                    }
                }
            }
            sneakClicks = 0;
        }
    }

    private boolean isEnabled() {
        if(Main.configFile.phaseWithKeybind) {
            return Main.doPhase;
        } else {
            return Main.configFile.phase;
        }
    }
}
