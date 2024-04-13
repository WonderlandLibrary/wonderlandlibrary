//package dev.tenacity.module.impl.funny;
//
//import dev.tenacity.event.IEventListener;
//import dev.tenacity.event.impl.packet.PacketSendEvent;
//import dev.tenacity.event.impl.player.BlockPlaceableEvent;
//import dev.tenacity.event.impl.player.MotionEvent;
//import dev.tenacity.module.Module;
//import dev.tenacity.module.ModuleCategory;
//import dev.tenacity.setting.impl.BooleanSetting;
//import dev.tenacity.setting.impl.ModeSetting;
//import dev.tenacity.setting.impl.NumberSetting;
//import dev.tenacity.util.misc.ChatUtil;
//import dev.tenacity.util.misc.RaycastUtil;
//import dev.tenacity.util.misc.TimerUtil;
//import dev.tenacity.util.player.InventoryUtil;
//import dev.tenacity.util.player.MovementUtil;
//import dev.tenacity.util.player.RotationUtil;
//import net.minecraft.block.BlockAir;
//import net.minecraft.block.material.Material;
//import net.minecraft.network.play.client.C09PacketHeldItemChange;
//import net.minecraft.network.play.client.C0APacketAnimation;
//import net.minecraft.util.BlockPos;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.MovingObjectPosition;
//import net.minecraft.util.Vec3;
//import org.apache.velocity.runtime.log.NullLogChute;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.util.vector.Vector2f;
//
//public final class realwatchdogfullstrafeandautoblockand8bpsscaffoldModule extends Module {
//
//    private final BooleanSetting sprint = new BooleanSetting("Sprint", true),
//            swing = new BooleanSetting("Swing", true),
//            rotate = new BooleanSetting("Rotate", true),
//            tower = new BooleanSetting("Tower", true);
//
//    private final NumberSetting searchRange = new NumberSetting("Sell range", 3, 1, 6, 1),
//            bruteForceRayCastIntensity = new NumberSetting("aggressive humping intensity", 5, 1, 10, 1);
//
//    private final ModeSetting switchItemMode = new ModeSetting("Switch Item Mode", "Dog pee", "Dog Piss Sever"),
//            swingMode = new ModeSetting("Swing Mode", "Dog pee", "Dog piss server"),
//            rotationMode = new ModeSetting("Rotation Mode", "Watchdog", "WatchCat"),
//            towerMode = new ModeSetting("Tower Mode", "Watchdog", "realwatchdogfullstrafeandautoblockand8bpsscaffoldModule");
//
//    private BlockCache blockCache, lastBlockCache;
//    private int startSlot, slot, lastSlot;
//    private final TimerUtil timerUtil = new TimerUtil();
//
//    private float yaw, pitch;
//
//    public realwatchdogfullstrafeandautoblockand8bpsscaffoldModule() {
//        super("realwatchdogfullstrafeandautoblockand8bpsscaffoldModule", "realwatchdogfullstrafeandautoblockand8bpsscaffoldModule", ModuleCategory.FUNNY);
//        swingMode.addParent(swing, BooleanSetting::isEnabled);
//        rotationMode.addParent(rotate, BooleanSetting::isEnabled);
//        towerMode.addParent(tower, BooleanSetting::isEnabled);
//        bruteForceRayCastIntensity.addParent(rotationMode, rotation -> rotationMode.isMode("aggressive humping intensity"));
//        initializeSettings(sprint, searchRange, rotate, rotationMode, bruteForceRayCastIntensity, switchItemMode, swing, swingMode, tower, towerMode);
//    }
//
//    private final IEventListener<BlockPlaceableEvent> onBlockPlaceableEvent = event -> {
//        placeBlock();
//    };
//
//    private final IEventListener<MotionEvent> onMotionEvent = event -> {
//        slot = InventoryUtil.getBlockSlot();
//
//        if(sprint.isEnabled())
//            mc.gameSettings.keyBindSprint.pressed = true;
//        else {
//            mc.gameSettings.keyBindSprint.pressed = false;
//            mc.thePlayer.setSprinting(false);
//        }
//
//        if(slot != -1) {
//            if(switchItemMode.isMode("Client") && mc.thePlayer.inventory.currentItem != slot)
//                mc.thePlayer.inventory.currentItem = slot;
//            if(switchItemMode.isMode("Server") && lastSlot != slot) {
//                mc.getNetHandler().addToSendQueueNoEvent(new C09PacketHeldItemChange(slot));
//                lastSlot = slot;
//            }
//        }
//
//        if(event.isPre()) {
//            blockCache = getBlockCache();
//            if(blockCache != null)
//                lastBlockCache = blockCache;
//
//            final float sensitivityMultiplier = RotationUtil.getSensitivityMultiplier();
//            final float fixedYaw = yaw - yaw % sensitivityMultiplier;
//            final float fixedPitch = pitch - pitch % sensitivityMultiplier;
//
//            event.setYaw(fixedYaw);
//            event.setPitch(fixedPitch);
//
//            // Have to do this or else watchdog will delete your blocks
//            if(mc.thePlayer.onGround) {
//                mc.thePlayer.motionX *= 0.91f;
//                mc.thePlayer.motionZ *= 0.91f;
//            }
//
//            if(tower.isEnabled() && mc.gameSettings.keyBindJump.isKeyDown() && blockCache != null) {
//                switch (towerMode.getCurrentMode()) {
//                    case "Watchdog":
//                        mc.thePlayer.motionY = 0.02f;
//                        break;
//                    case "realwatchdogfullstrafeandautoblockand8bpsscaffoldModule":
//                        mc.thePlayer.motionY = 1f;
//                        break;
//                }
//            }
//        }
//
//    };
//    @Override
//    public void onEnable() {
//        if (mc.thePlayer != null) {
//            startSlot = mc.thePlayer.inventory.currentItem;
//        } else {
//            return;
//        }
//
//        slot = lastSlot = -1;
//        blockCache = lastBlockCache = null;
//
//        yaw = MovementUtil.getMoveYaw() + 180;
//        pitch = 80;
//        super.onEnable();
//    }
//
//    @Override
//    public void onDisable() {
//        if(mc.thePlayer.inventory.currentItem != startSlot || lastSlot != startSlot) {
//            if(switchItemMode.isMode("Client"))
//                mc.thePlayer.inventory.currentItem = startSlot;
//            else
//                mc.getNetHandler().addToSendQueueNoEvent(new C09PacketHeldItemChange(startSlot));
//        }
//        super.onDisable();
//    }
//
//    private void placeBlock() {
//        if(blockCache == null || lastBlockCache == null || slot == -1) return;
//
//        if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(slot),
//                lastBlockCache.getBlockPos(), lastBlockCache.getEnumFacing(), getHitVec())) {
//
//            if(rotate.isEnabled())
//                updateRotations();
//
//            if(swing.isEnabled()) {
//                if(swingMode.isMode("Client"))
//                    mc.thePlayer.swingItem();
//                else
//                    mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
//            }
//
//        }
//        blockCache = null;
//    }
//
//    private void updateRotations() {
//        switch(rotationMode.getCurrentMode()) {
//            case "Enum":
//                this.yaw = (float) lastBlockCache.getEnumFacing().angleFromEnumFacing() - 180;
//                this.pitch = 77;
//                break;
//            case "Brute Force RayCast":
//                for (float yaw = MovementUtil.getMoveYaw() - 180; yaw < MovementUtil.getMoveYaw() + 180; yaw += bruteForceRayCastIntensity.getCurrentValue()) {
//                    for (float pitch = 90; pitch > -90; pitch -= bruteForceRayCastIntensity.getCurrentValue()) {
//                        final MovingObjectPosition cursor = RaycastUtil.getMouseOver(new Vector2f(yaw, pitch), searchRange.getCurrentValue());
//                        if (cursor != null && cursor.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
//                            BlockPos pos = cursor.getBlockPos();
//                            if (pos.getX() == lastBlockCache.getBlockPos().getX() && pos.getY() == lastBlockCache.getBlockPos().getY() && pos.getZ() == lastBlockCache.getBlockPos().getZ()) {
//                                this.yaw = yaw;
//                                this.pitch = pitch;
//                                break;
//                            }
//                        }
//                    }
//                }
//                break;
//        }
//    }
//
//    private Vec3 getHitVec() {
//        double x = lastBlockCache.getBlockPos().getX() + 0.5,
//                y = lastBlockCache.getBlockPos().getY() + 0.5,
//                z = lastBlockCache.getBlockPos().getZ() + 0.5;
//
//        if(lastBlockCache.getEnumFacing() != EnumFacing.UP && lastBlockCache.getEnumFacing() != EnumFacing.DOWN) {
//            y += 0.5;
//        } else {
//            x += 0.3;
//            z += 0.3;
//        }
//
//        if(lastBlockCache.getEnumFacing() == EnumFacing.SOUTH || lastBlockCache.getEnumFacing() == EnumFacing.NORTH)
//            x += 0.15;
//        if(lastBlockCache.getEnumFacing() == EnumFacing.EAST || lastBlockCache.getEnumFacing() == EnumFacing.WEST)
//            z += 0.15;
//
//        return new Vec3(x, y, z);
//    }
//
//    private BlockCache getBlockCache() {
//        final BlockPos belowBlockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
//        if (mc.theWorld.getBlockState(belowBlockPos).getBlock() instanceof BlockAir) {
//            for (int x = 0; x < searchRange.getCurrentValue(); x++) {
//                for (int z = 0; z < searchRange.getCurrentValue(); z++) {
//                    for (int i = 1; i > -3; i -= 2) {
//                        final BlockPos blockPos = belowBlockPos.add(x * i, 0, z * i);
//                        if (mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockAir) {
//                            for (final EnumFacing direction : EnumFacing.values()) {
//                                final BlockPos block = blockPos.offset(direction);
//                                final Material material = mc.theWorld.getBlockState(block).getBlock().getMaterial();
//                                if (material.isSolid() && !material.isLiquid())
//                                    return new BlockCache(block, direction.getOpposite());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    public static final class BlockCache {
//
//        private final BlockPos blockPos;
//        private final EnumFacing enumFacing;
//
//        public BlockCache(final BlockPos blockPos, final EnumFacing enumFacing) {
//            this.blockPos = blockPos;
//            this.enumFacing = enumFacing;
//        }
//
//        public BlockPos getBlockPos() {
//            return blockPos;
//        }
//
//        public EnumFacing getEnumFacing() {
//            return enumFacing;
//        }
//    }
//
//}
