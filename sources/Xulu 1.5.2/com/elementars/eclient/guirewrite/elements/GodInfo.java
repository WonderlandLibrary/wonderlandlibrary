// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.awt.Color;
import com.elementars.eclient.module.combat.AutoCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import java.util.List;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.ListHelper;
import java.util.Comparator;
import com.elementars.eclient.friend.Friends;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import com.elementars.eclient.module.Module;
import net.minecraft.util.math.Vec3d;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class GodInfo extends Element
{
    private final Value<Boolean> cf;
    private Vec3d[] offset;
    
    public GodInfo() {
        super("GodInfo");
        this.cf = this.register(new Value<Boolean>("Custom Font", this, false));
        this.offset = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, -1.0, 0.0) };
    }
    
    @Override
    public void onRender() {
        int totems = GodInfo.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (GodInfo.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++totems;
        }
        final List<String> info = new ArrayList<String>(Arrays.asList("HTR", "PLR", String.valueOf(totems), "PING " + ((GodInfo.mc.getConnection() != null && GodInfo.mc.player != null && GodInfo.mc.getConnection().getPlayerInfo(GodInfo.mc.player.entityUniqueID) != null) ? Integer.valueOf(GodInfo.mc.getConnection().getPlayerInfo(GodInfo.mc.player.entityUniqueID).getResponseTime()) : "-1")));
        final EntityPlayer enemy = (EntityPlayer)GodInfo.mc.world.playerEntities.stream().filter(player -> !player.getName().equals(GodInfo.mc.player.getName())).filter(player -> !Friends.isFriend(player.getName())).min(Comparator.comparing(player -> GodInfo.mc.player.getDistance(player))).orElse(null);
        if (enemy != null) {
            info.add("LBY");
        }
        this.width = GodInfo.fontRenderer.getStringWidth(ListHelper.longest(info)) + 2;
        this.height = (GodInfo.fontRenderer.FONT_HEIGHT + 1) * info.size() + 1;
        float yCount = (float)this.y;
        for (final String s : info) {
            if (this.cf.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(s, (float)this.x + 1.0f, yCount + 1.0f, ColorUtils.changeAlpha(this.getColor(s, enemy), Global.hudAlpha.getValue()));
            }
            else {
                Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(s, (float)this.x + 1.0f, yCount + 1.0f, ColorUtils.changeAlpha(this.getColor(s, enemy), Global.hudAlpha.getValue()));
            }
            yCount += 10.0f;
        }
    }
    
    public boolean checkSurround(final EntityPlayer player) {
        if (GodInfo.mc.player == null || GodInfo.mc.world == null) {
            return false;
        }
        boolean isSafe = true;
        for (final Vec3d pos : this.offset) {
            if (GodInfo.mc.world.getBlockState(new BlockPos(player.getPositionVector()).add(pos.x, pos.y, pos.z)).getBlock() != Blocks.OBSIDIAN && GodInfo.mc.world.getBlockState(new BlockPos(player.getPositionVector()).add(pos.x, pos.y, pos.z)).getBlock() != Blocks.BEDROCK) {
                isSafe = false;
            }
        }
        return isSafe;
    }
    
    private int getColor(final String s, final EntityPlayer e) {
        switch (s) {
            case "HTR": {
                if (e != null && GodInfo.mc.player.getDistance((Entity)e) <= Xulu.VALUE_MANAGER.getValueT("Hit Range", AutoCrystal.class).getValue()) {
                    return new Color(0, 255, 0).getRGB();
                }
                return new Color(255, 0, 0).getRGB();
            }
            case "PLR": {
                if (e != null && GodInfo.mc.player.getDistance((Entity)e) <= Xulu.VALUE_MANAGER.getValueT("Hit Range", AutoCrystal.class).getValue()) {
                    return new Color(0, 255, 0).getRGB();
                }
                return new Color(255, 0, 0).getRGB();
            }
            case "LBY": {
                if (e != null && this.checkSurround(e)) {
                    return new Color(0, 255, 0).getRGB();
                }
                return new Color(255, 0, 0).getRGB();
            }
            default: {
                if (s.startsWith("PING")) {
                    final int num = Integer.valueOf(s.substring(5));
                    if (num > 100) {
                        return new Color(255, 0, 0).getRGB();
                    }
                    return new Color(0, 255, 0).getRGB();
                }
                else {
                    try {
                        final int num = Integer.valueOf(s);
                        if (num > 0) {
                            return new Color(0, 255, 0).getRGB();
                        }
                        return new Color(255, 0, 0).getRGB();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        return -1;
                    }
                }
                break;
            }
        }
    }
}
