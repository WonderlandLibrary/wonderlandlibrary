package me.earth.phobos.features.modules.player;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockTweaks
        extends Module {
    private static BlockTweaks INSTANCE = new BlockTweaks();
    public Setting<Boolean> autoTool = this.register(new Setting<Boolean>("AutoTool", false));
    public Setting<Boolean> autoWeapon = this.register(new Setting<Boolean>("AutoWeapon", false));
    public Setting<Boolean> noFriendAttack = this.register(new Setting<Boolean>("NoFriendAttack", false));
    public Setting<Boolean> noBlock = this.register(new Setting<Boolean>("NoHitboxBlock", true));
    public Setting<Boolean> noGhost = this.register(new Setting<Boolean>("NoGlitchBlocks", false));
    public Setting<Boolean> destroy = this.register(new Setting<Object>("Destroy", Boolean.valueOf(false), v -> this.noGhost.getValue()));
    private int lastHotbarSlot = -1;
    private int currentTargetSlot = -1;
    private boolean switched = false;

    public BlockTweaks() {
        super("BlockTweaks", "Some tweaks for blocks.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static BlockTweaks getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new BlockTweaks();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        if (this.switched) {
            this.equip(this.lastHotbarSlot, false);
        }
        this.lastHotbarSlot = -1;
        this.currentTargetSlot = -1;
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if (BlockTweaks.fullNullCheck() || !this.noGhost.getValue().booleanValue() || !this.destroy.getValue().booleanValue()) {
            return;
        }
        if (!(BlockTweaks.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            BlockPos pos = BlockTweaks.mc.player.getPosition();
            this.removeGlitchBlocks(pos);
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (this.autoTool.getValue().booleanValue() && (Speedmine.getInstance().mode.getValue() != Speedmine.Mode.PACKET || Speedmine.getInstance().isOff() || !Speedmine.getInstance().tweaks.getValue().booleanValue()) && !BlockTweaks.fullNullCheck() && event.getPos() != null) {
            this.equipBestTool(BlockTweaks.mc.world.getBlockState(event.getPos()));
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (this.autoWeapon.getValue().booleanValue() && !BlockTweaks.fullNullCheck() && event.getTarget() != null) {
            this.equipBestWeapon(event.getTarget());
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketUseEntity packet;
        Entity entity;
        if (BlockTweaks.fullNullCheck()) {
            return;
        }
        if (this.noFriendAttack.getValue().booleanValue() && event.getPacket() instanceof CPacketUseEntity && (entity = (packet = event.getPacket()).getEntityFromWorld(BlockTweaks.mc.world)) != null && Phobos.friendManager.isFriend(entity.getName())) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        if (!BlockTweaks.fullNullCheck()) {
            if (BlockTweaks.mc.player.inventory.currentItem != this.lastHotbarSlot && BlockTweaks.mc.player.inventory.currentItem != this.currentTargetSlot) {
                this.lastHotbarSlot = BlockTweaks.mc.player.inventory.currentItem;
            }
            if (!BlockTweaks.mc.gameSettings.keyBindAttack.isKeyDown() && this.switched) {
                this.equip(this.lastHotbarSlot, false);
            }
        }
    }

    private void removeGlitchBlocks(BlockPos pos) {
        for (int dx = -4; dx <= 4; ++dx) {
            for (int dy = -4; dy <= 4; ++dy) {
                for (int dz = -4; dz <= 4; ++dz) {
                    BlockPos blockPos = new BlockPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    if (!BlockTweaks.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) continue;
                    BlockTweaks.mc.playerController.processRightClickBlock(BlockTweaks.mc.player, BlockTweaks.mc.world, blockPos, EnumFacing.DOWN, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
                }
            }
        }
    }

    private void equipBestTool(IBlockState blockState) {
        int bestSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            int eff;
            float speed;
            ItemStack stack = BlockTweaks.mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty || !((speed = stack.getDestroySpeed(blockState)) > 1.0f) || !((double) (speed = (float) ((double) speed + ((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0 ? Math.pow(eff, 2.0) + 1.0 : 0.0))) > max))
                continue;
            max = speed;
            bestSlot = i;
        }
        this.equip(bestSlot, true);
    }

    public void equipBestWeapon(Entity entity) {
        int bestSlot = -1;
        double maxDamage = 0.0;
        EnumCreatureAttribute creatureAttribute = EnumCreatureAttribute.UNDEFINED;
        if (EntityUtil.isLiving(entity)) {
            EntityLivingBase base = (EntityLivingBase) entity;
            creatureAttribute = base.getCreatureAttribute();
        }
        for (int i = 0; i < 9; ++i) {
            double damage;
            ItemStack stack = BlockTweaks.mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty) continue;
            if (stack.getItem() instanceof ItemTool) {
                damage = (double) ((ItemTool) stack.getItem()).attackDamage + (double) EnchantmentHelper.getModifierForCreature(stack, creatureAttribute);
                if (!(damage > maxDamage)) continue;
                maxDamage = damage;
                bestSlot = i;
                continue;
            }
            if (!(stack.getItem() instanceof ItemSword) || !((damage = (double) ((ItemSword) stack.getItem()).getAttackDamage() + (double) EnchantmentHelper.getModifierForCreature(stack, creatureAttribute)) > maxDamage))
                continue;
            maxDamage = damage;
            bestSlot = i;
        }
        this.equip(bestSlot, true);
    }

    private void equip(int slot, boolean equipTool) {
        if (slot != -1) {
            if (slot != BlockTweaks.mc.player.inventory.currentItem) {
                this.lastHotbarSlot = BlockTweaks.mc.player.inventory.currentItem;
            }
            this.currentTargetSlot = slot;
            BlockTweaks.mc.player.inventory.currentItem = slot;
            BlockTweaks.mc.playerController.syncCurrentPlayItem();
            this.switched = equipTool;
        }
    }
}

