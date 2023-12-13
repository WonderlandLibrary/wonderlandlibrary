/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.wintware.client.module.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.event.impl.EventReceivePacket;
import me.wintware.client.event.impl.EventRender2D;
import me.wintware.client.event.impl.EventRender3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.font.FontRenderer;
import me.wintware.client.utils.interact.BlockUtil;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class XrayBypass
extends Module {
    ArrayList<BlockPos> ores = new ArrayList();
    ArrayList<BlockPos> toCheck = new ArrayList();
    public static int done;
    public static int all;
    private final Setting diamond;
    private final Setting gold;
    private final Setting iron;
    private final Setting emerald;
    private final Setting redstone;
    private final Setting lapis;
    private final Setting coal;
    private final Setting checkSpeed;

    public XrayBypass() {
        super("XrayBypass", Category.World);
        ArrayList<String> options = new ArrayList<String>();
        options.add("FullBox");
        options.add("Frame");
        Main.instance.setmgr.rSetting(new Setting("BlockOutline Mode", this, "FullBox", options));
        this.diamond = new Setting("Diamond", this, true);
        Main.instance.setmgr.rSetting(this.diamond);
        this.gold = new Setting("Gold", this, false);
        Main.instance.setmgr.rSetting(this.gold);
        this.iron = new Setting("Iron", this, false);
        Main.instance.setmgr.rSetting(this.iron);
        this.emerald = new Setting("Emerald", this, false);
        Main.instance.setmgr.rSetting(this.emerald);
        this.redstone = new Setting("Redstone", this, false);
        Main.instance.setmgr.rSetting(this.redstone);
        this.lapis = new Setting("Lapis", this, false);
        Main.instance.setmgr.rSetting(this.lapis);
        this.coal = new Setting("Coal", this, false);
        Main.instance.setmgr.rSetting(this.coal);
        this.checkSpeed = new Setting("CheckSpeed", this, 4.0, 1.0, 5.0, true);
        Main.instance.setmgr.rSetting(this.checkSpeed);
        Main.instance.setmgr.rSetting(new Setting("Radius XZ", this, 20.0, 5.0, 200.0, true));
        Main.instance.setmgr.rSetting(new Setting("Radius Y", this, 6.0, 2.0, 50.0, true));
    }

    @Override
    public void onEnable() {
        this.ores.clear();
        this.toCheck.clear();
        int radXZ = Main.instance.setmgr.getSettingByName("Radius XZ").getValInt();
        int radY = Main.instance.setmgr.getSettingByName("Radius Y").getValInt();
        ArrayList<BlockPos> blockPositions = this.getBlocks(radXZ, radY, radXZ);
        for (BlockPos pos : blockPositions) {
            IBlockState state = BlockUtil.getState(pos);
            if (!this.isCheckableOre(Block.getIdFromBlock(state.getBlock()))) continue;
            this.toCheck.add(pos);
        }
        all = this.toCheck.size();
        done = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        XrayBypass.mc.renderGlobal.loadRenderers();
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate e) {
        String mode = Main.instance.setmgr.getSettingByName("BlockOutline Mode").getValString();
        this.setSuffix(mode);
        for (int i = 0; i < this.checkSpeed.getValInt(); ++i) {
            if (this.toCheck.size() < 1) {
                return;
            }
            BlockPos pos = this.toCheck.remove(0);
            ++done;
            mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (e.getPacket() instanceof SPacketBlockChange) {
            SPacketBlockChange p = (SPacketBlockChange)e.getPacket();
            if (this.isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                this.ores.add(p.getBlockPosition());
            }
        } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
            SPacketMultiBlockChange p = (SPacketMultiBlockChange)e.getPacket();
            for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                if (!this.isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock()))) continue;
                this.ores.add(dat.getPos());
            }
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D e) {
        for (BlockPos pos : this.ores) {
            IBlockState state = BlockUtil.getState(pos);
            Block mat = state.getBlock();
            String mode = Main.instance.setmgr.getSettingByName("BlockOutline Mode").getValString();
            if (mode.equalsIgnoreCase("FullBox")) {
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 56 && this.diamond.getValue() && Block.getIdFromBlock(mat) == 56) {
                    RenderUtil.blockEsp(pos, new Color(0, 255, 255, 50), 1.0, 1.0);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 14 && this.gold.getValue() && Block.getIdFromBlock(mat) == 14) {
                    RenderUtil.blockEsp(pos, new Color(255, 215, 0, 100), 1.0, 1.0);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 15 && this.iron.getValue() && Block.getIdFromBlock(mat) == 15) {
                    RenderUtil.blockEsp(pos, new Color(213, 213, 213, 100), 1.0, 1.0);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 129 && this.emerald.getValue() && Block.getIdFromBlock(mat) == 129) {
                    RenderUtil.blockEsp(pos, new Color(0, 255, 77, 100), 1.0, 1.0);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 73 && this.redstone.getValue() && Block.getIdFromBlock(mat) == 73) {
                    RenderUtil.blockEsp(pos, new Color(255, 0, 0, 100), 1.0, 1.0);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 16 && this.coal.getValue() && Block.getIdFromBlock(mat) == 16) {
                    RenderUtil.blockEsp(pos, new Color(0, 0, 0, 100), 1.0, 1.0);
                }
                if (Block.getIdFromBlock(mat) == 0 || Block.getIdFromBlock(mat) != 21 || !this.lapis.getValue() || Block.getIdFromBlock(mat) != 21) continue;
                RenderUtil.blockEsp(pos, new Color(38, 97, 156, 100), 1.0, 1.0);
                continue;
            }
            if (!mode.equalsIgnoreCase("Frame")) continue;
            if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 56 && this.diamond.getValue() && Block.getIdFromBlock(mat) == 56) {
                RenderUtil.blockEspFrame(pos, 0.0, 255.0, 255.0);
            }
            if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 14 && this.gold.getValue() && Block.getIdFromBlock(mat) == 14) {
                RenderUtil.blockEspFrame(pos, 255.0, 215.0, 0.0);
            }
            if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 15 && this.iron.getValue() && Block.getIdFromBlock(mat) == 15) {
                RenderUtil.blockEspFrame(pos, 213.0, 213.0, 213.0);
            }
            if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 129 && this.emerald.getValue() && Block.getIdFromBlock(mat) == 129) {
                RenderUtil.blockEspFrame(pos, 0.0, 255.0, 77.0);
            }
            if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 73 && this.redstone.getValue() && Block.getIdFromBlock(mat) == 73) {
                RenderUtil.blockEspFrame(pos, 255.0, 0.0, 0.0);
            }
            if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 16 && this.coal.getValue() && Block.getIdFromBlock(mat) == 16) {
                RenderUtil.blockEspFrame(pos, 0.0, 0.0, 0.0);
            }
            if (Block.getIdFromBlock(mat) == 0 || Block.getIdFromBlock(mat) != 21 || !this.lapis.getValue() || Block.getIdFromBlock(mat) != 21) continue;
            RenderUtil.blockEspFrame(pos, 38.0, 97.0, 156.0);
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        String f = "" + all;
        String g = "" + done;
        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer font = XrayBypass.mc.clickguimedium;
        int size = 125;
        float xOffset = (float)sr.getScaledWidth() / 2.0f - (float)size / 2.0f;
        float yOffset = 5.0f;
        float Y = 0.0f;
        RenderUtil.rectangleBordered(xOffset + 2.0f, yOffset + 1.0f, xOffset + 10.0f + (float)size + (float)font.getStringWidth(g) + 1.0f, yOffset + (float)size / 6.0f + 3.0f + ((float)font.getHeight() + 2.2f), 0.5, ColorUtils.getColor(90), ColorUtils.getColor(0));
        RenderUtil.rectangleBordered(xOffset + 3.0f, yOffset + 2.0f, xOffset + 10.0f + (float)size + (float)font.getStringWidth(g), yOffset + (float)size / 6.0f + 2.0f + ((float)font.getHeight() + 2.2f), 0.5, ColorUtils.getColor(27), ColorUtils.getColor(61));
        font.drawStringWithShadow("" + ChatFormatting.GREEN + "Done: " + ChatFormatting.WHITE + done + " / " + ChatFormatting.RED + "All: " + ChatFormatting.WHITE + all, xOffset + 25.0f, yOffset + (float)font.getHeight() + 4.0f, -1);
        GlStateManager.disableBlend();
    }

    private boolean isCheckableOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        if (this.diamond.getValue() && id != 0) {
            check = 56;
        }
        if (this.gold.getValue() && id != 0) {
            check1 = 14;
        }
        if (this.iron.getValue() && id != 0) {
            check2 = 15;
        }
        if (this.emerald.getValue() && id != 0) {
            check3 = 129;
        }
        if (this.redstone.getValue() && id != 0) {
            check4 = 73;
        }
        if (this.coal.getValue() && id != 0) {
            check5 = 16;
        }
        if (this.lapis.getValue() && id != 0) {
            check6 = 21;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }

    private boolean isEnabledOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        if (this.diamond.getValue() && id != 0) {
            check = 56;
        }
        if (this.gold.getValue() && id != 0) {
            check1 = 14;
        }
        if (this.iron.getValue() && id != 0) {
            check2 = 15;
        }
        if (this.emerald.getValue() && id != 0) {
            check3 = 129;
        }
        if (this.redstone.getValue() && id != 0) {
            check4 = 73;
        }
        if (this.coal.getValue() && id != 0) {
            check5 = 16;
        }
        if (this.lapis.getValue() && id != 0) {
            check6 = 21;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }

    private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(Minecraft.player.posX - (double)x, Minecraft.player.posY - (double)y, Minecraft.player.posZ - (double)z);
        BlockPos max = new BlockPos(Minecraft.player.posX + (double)x, Minecraft.player.posY + (double)y, Minecraft.player.posZ + (double)z);
        return BlockUtil.getAllInBox(min, max);
    }
}

