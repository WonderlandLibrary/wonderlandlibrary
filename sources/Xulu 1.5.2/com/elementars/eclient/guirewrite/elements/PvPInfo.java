// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.ListHelper;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import dev.xulu.newgui.util.ColorUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.module.Module;
import net.minecraft.util.math.Vec3d;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class PvPInfo extends Element
{
    private final Value<Boolean> rainbow;
    public static boolean attack;
    public static boolean place;
    public static boolean surround;
    private Vec3d[] offset;
    
    public PvPInfo() {
        super("PvPInfo");
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.offset = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, -1.0, 0.0) };
    }
    
    public void checkSurround() {
        if (PvPInfo.mc.player == null || PvPInfo.mc.world == null) {
            return;
        }
        boolean isSafe = true;
        for (final Vec3d pos : this.offset) {
            if (PvPInfo.mc.world.getBlockState(new BlockPos(PvPInfo.mc.player.getPositionVector()).add(pos.x, pos.y, pos.z)).getBlock() != Blocks.OBSIDIAN && PvPInfo.mc.world.getBlockState(new BlockPos(PvPInfo.mc.player.getPositionVector()).add(pos.x, pos.y, pos.z)).getBlock() != Blocks.BEDROCK) {
                isSafe = false;
            }
        }
        PvPInfo.surround = isSafe;
    }
    
    @Override
    public void onRender() {
        this.checkSurround();
        float yCount = (float)this.y;
        int color = ColorUtil.getClickGUIColor().getRGB();
        int totems = PvPInfo.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (PvPInfo.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++totems;
        }
        final String[] pvpinfo = { "ATT: " + this.getFromBoolean(PvPInfo.attack), "PLC: " + this.getFromBoolean(PvPInfo.place), "FOB: " + this.getFromBoolean(PvPInfo.surround), "PING: " + this.getPing((PvPInfo.mc.getConnection() != null && PvPInfo.mc.player != null && PvPInfo.mc.getConnection().getPlayerInfo(PvPInfo.mc.player.entityUniqueID) != null) ? PvPInfo.mc.getConnection().getPlayerInfo(PvPInfo.mc.player.entityUniqueID).getResponseTime() : -1), "TOTEMS: " + this.getTotems(totems), "AT: " + this.getAutoTrap(), "SU: " + this.getSurround(), "CA: " + this.getCaura() };
        this.width = PvPInfo.fontRenderer.getStringWidth(ListHelper.longest(pvpinfo)) + 2;
        this.height = (PvPInfo.fontRenderer.FONT_HEIGHT + 1) * pvpinfo.length + 1;
        if (this.rainbow.getValue()) {
            color = Xulu.rgb;
        }
        if (Xulu.CustomFont) {
            for (final String s : pvpinfo) {
                Xulu.cFontRenderer.drawStringWithShadow(s, this.x + 1.0, yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
                yCount += 10.0f;
            }
        }
        else {
            for (final String s : pvpinfo) {
                PvPInfo.fontRenderer.drawStringWithShadow(s, (float)this.x + 1.0f, yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
                yCount += 10.0f;
            }
        }
    }
    
    private String getTotems(final int totems) {
        if (totems > 0) {
            return "" + ChatFormatting.GREEN + totems;
        }
        return "" + ChatFormatting.RED + totems;
    }
    
    private String getPing(final long ping) {
        if (ping > 100L) {
            return "" + ChatFormatting.RED + ping;
        }
        return "" + ChatFormatting.GREEN + ping;
    }
    
    private String getAutoTrap() {
        if (Xulu.MODULE_MANAGER.getModuleByName("AutoTrap") == null) {
            return "NULL";
        }
        if (Xulu.MODULE_MANAGER.getModuleByName("AutoTrap").isToggled()) {
            return ChatFormatting.GREEN + "ON";
        }
        return ChatFormatting.RED + "OFF";
    }
    
    private String getSurround() {
        if (Xulu.MODULE_MANAGER.getModuleByName("Surround") == null) {
            return "NULL";
        }
        if (Xulu.MODULE_MANAGER.getModuleByName("Surround").isToggled()) {
            return ChatFormatting.GREEN + "ON";
        }
        return ChatFormatting.RED + "OFF";
    }
    
    private String getCaura() {
        boolean caura = false;
        if (Xulu.MODULE_MANAGER.getModuleByName("AutoCrystal") != null) {
            caura = Xulu.MODULE_MANAGER.getModuleByName("AutoCrystal").isToggled();
            if (caura) {
                return ChatFormatting.GREEN + "ON";
            }
        }
        if (Xulu.MODULE_MANAGER.getModuleByName("AutoCrystalO") != null) {
            caura = Xulu.MODULE_MANAGER.getModuleByName("AutoCrystalO").isToggled();
            if (caura) {
                return ChatFormatting.GREEN + "ON";
            }
        }
        if (Xulu.MODULE_MANAGER.getModuleByName("AutoCrystalX") != null) {
            caura = Xulu.MODULE_MANAGER.getModuleByName("AutoCrystalX").isToggled();
            if (caura) {
                return ChatFormatting.GREEN + "ON";
            }
        }
        return ChatFormatting.RED + "OFF";
    }
    
    public String getFromBoolean(final boolean b) {
        if (b) {
            return ChatFormatting.GREEN + "TRUE";
        }
        return ChatFormatting.RED + "FALSE";
    }
    
    static {
        PvPInfo.attack = false;
        PvPInfo.place = false;
        PvPInfo.surround = false;
    }
}
