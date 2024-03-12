// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import java.awt.Color;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockOre;
import net.minecraft.world.IBlockAccess;
import dev.tenacity.utils.misc.Multithreading;
import java.util.concurrent.TimeUnit;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import dev.tenacity.event.impl.player.PlayerMoveUpdateEvent;
import dev.tenacity.module.settings.Setting;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import dev.tenacity.module.Category;
import net.minecraft.world.World;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.block.Block;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import net.minecraft.util.BlockPos;
import java.util.HashSet;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class XRay extends Module
{
    public static final NumberSetting opacity;
    public static final BooleanSetting bypass;
    public static final HashSet<BlockPos> checkedOres;
    public static final HashSet<BlockPos> queuedOres;
    public static final HashSet<BlockPos> oresToRender;
    public static boolean enabled;
    private static BooleanSetting redstone;
    private static BooleanSetting diamond;
    private static BooleanSetting emerald;
    private static BooleanSetting lapis;
    private static BooleanSetting iron;
    private static BooleanSetting coal;
    private static BooleanSetting gold;
    private final MultipleBoolSetting ores;
    private final HashSet<Block> blocks;
    private final TimerUtil updateTimer;
    private final TimerUtil searchTimer;
    private World lastWorld;
    
    public XRay() {
        super("XRay", "X Ray", Category.RENDER, "Shows blocks through the world");
        this.ores = new MultipleBoolSetting("Ores", new BooleanSetting[] { XRay.redstone = new BooleanSetting("Redstone", true), XRay.diamond = new BooleanSetting("Diamond", true), XRay.emerald = new BooleanSetting("Emerald", true), XRay.lapis = new BooleanSetting("Lapis", true), XRay.iron = new BooleanSetting("Iron", true), XRay.coal = new BooleanSetting("Coal", true), XRay.gold = new BooleanSetting("Gold", true) });
        this.blocks = new HashSet<Block>(Arrays.asList(Blocks.obsidian, Blocks.clay, Blocks.mossy_cobblestone, Blocks.diamond_ore, Blocks.redstone_ore, Blocks.iron_ore, Blocks.coal_ore, Blocks.gold_ore, Blocks.emerald_ore, Blocks.lapis_ore));
        this.updateTimer = new TimerUtil();
        this.searchTimer = new TimerUtil();
        this.addSettings(this.ores, XRay.opacity, XRay.bypass);
    }
    
    @Override
    public void onPlayerMoveUpdateEvent(final PlayerMoveUpdateEvent e) {
        if (XRay.bypass.isEnabled() && !XRay.mc.isSingleplayer()) {
            if (this.updateTimer.hasTimeElapsed(2000L)) {
                this.search();
                this.updateTimer.reset();
            }
            if (!XRay.queuedOres.isEmpty()) {
                int i;
                BlockPos pos;
                Multithreading.runAsync(() -> {
                    for (i = 0; i < 18; ++i) {
                        if (XRay.queuedOres.size() >= 1 && XRay.queuedOres.size() >= i + 1) {
                            pos = XRay.queuedOres.iterator().next();
                            XRay.queuedOres.remove(pos);
                            PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                            XRay.checkedOres.add(pos);
                            Multithreading.schedule(() -> XRay.checkedOres.remove(pos), 30L, TimeUnit.SECONDS);
                        }
                    }
                });
            }
        }
    }
    
    public static boolean isWhitelisted(final Block block) {
        for (final Ore o : Ore.values()) {
            if (o.ore == block) {
                return o.setting.isEnabled();
            }
        }
        return false;
    }
    
    public static boolean isExposed(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side, final double minY, final double maxY, final double minZ, final double maxZ, final double minX, final double maxX) {
        return (side == EnumFacing.DOWN && minY > 0.0) || (side == EnumFacing.UP && maxY < 1.0) || (side == EnumFacing.NORTH && minZ > 0.0) || (side == EnumFacing.SOUTH && maxZ < 1.0) || (side == EnumFacing.WEST && minX > 0.0) || (side == EnumFacing.EAST && maxX < 1.0) || !worldIn.getBlockState(pos).getBlock().isOpaqueCube();
    }
    
    @Override
    public void onEnable() {
        XRay.enabled = true;
        XRay.mc.renderGlobal.loadRenderers();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        XRay.enabled = false;
        super.onDisable();
        XRay.mc.renderGlobal.loadRenderers();
    }
    
    public void search() {
        if (XRay.mc.theWorld != null && this.lastWorld != XRay.mc.theWorld) {
            this.lastWorld = XRay.mc.theWorld;
            XRay.oresToRender.clear();
            XRay.checkedOres.clear();
            XRay.queuedOres.clear();
        }
        final int radius = 3;
        final int n;
        int x;
        int y;
        int z;
        BlockPos pos;
        Block block;
        Multithreading.runAsync(() -> {
            for (x = -n; x <= n; ++x) {
                for (y = -n; y <= n; ++y) {
                    for (z = -n; z <= n; ++z) {
                        pos = new BlockPos(XRay.mc.thePlayer.posX + x, XRay.mc.thePlayer.posY + y, XRay.mc.thePlayer.posZ + z);
                        block = XRay.mc.theWorld.getBlockState(pos).getBlock();
                        if ((block instanceof BlockOre || block instanceof BlockRedstoneOre) && !XRay.checkedOres.contains(pos) && !XRay.queuedOres.contains(pos) && this.blocks.contains(block) && this.shouldAdd(block, pos)) {
                            XRay.queuedOres.add(pos);
                        }
                    }
                }
            }
        });
    }
    
    public boolean shouldAdd(final Block block, final BlockPos pos) {
        for (final EnumFacing si : EnumFacing.VALUES) {
            if (isExposed(XRay.mc.theWorld, pos.offset(si), si, block.getBlockBoundsMinY(), block.getBlockBoundsMaxY(), block.getBlockBoundsMinZ(), block.getBlockBoundsMaxZ(), block.getBlockBoundsMinX(), block.getBlockBoundsMaxX())) {
                return false;
            }
        }
        return true;
    }
    
    static {
        opacity = new NumberSetting("Opacity", 60.0, 100.0, 0.0, 1.0);
        bypass = new BooleanSetting("Anti-xray bypass", false);
        checkedOres = new HashSet<BlockPos>();
        queuedOres = new HashSet<BlockPos>();
        oresToRender = new HashSet<BlockPos>();
    }
    
    private enum Ore
    {
        DIAMOND(Blocks.diamond_ore, new Color(93, 235, 245), XRay.diamond), 
        IRON(Blocks.iron_ore, Color.WHITE, XRay.iron), 
        GOLD(Blocks.gold_ore, new Color(239, 236, 79), XRay.gold), 
        REDSTONE(Blocks.redstone_ore, Color.RED, XRay.redstone), 
        REDSTONE_LIT(Blocks.lit_redstone_ore, Color.RED, XRay.redstone), 
        COAL(Blocks.coal_ore, Color.BLACK, XRay.coal), 
        LAPIS(Blocks.lapis_ore, new Color(21, 69, 185), XRay.lapis), 
        EMERALD(Blocks.emerald_ore, new Color(23, 221, 98), XRay.emerald);
        
        private final Block ore;
        private final Color color;
        private final BooleanSetting setting;
        
        private Ore(final Block ore, final Color color, final BooleanSetting setting) {
            this.ore = ore;
            this.color = color;
            this.setting = setting;
        }
    }
}
