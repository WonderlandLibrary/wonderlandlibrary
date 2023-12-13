// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.item.ItemStack;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import com.elementars.eclient.util.PotionUtil;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.math.MathHelper;
import com.elementars.eclient.util.LagCompensator;
import net.minecraft.client.Minecraft;
import com.elementars.eclient.command.Command;
import net.minecraft.potion.PotionEffect;
import java.util.HashMap;
import java.util.ArrayList;
import com.elementars.eclient.Xulu;
import dev.xulu.newgui.util.ColorUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;
import com.elementars.eclient.util.ColorTextUtils;
import com.elementars.eclient.module.Module;
import java.text.DecimalFormat;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class Info extends Element
{
    private final Value<Boolean> rainbow;
    private final Value<Boolean> t24;
    private final Value<String> mode;
    private final Value<String> order;
    private final Value<String> color2;
    private final Value<Boolean> FPS;
    private final Value<Boolean> PING;
    private final Value<Boolean> TPS;
    private final Value<Boolean> SPEED;
    private final Value<Boolean> TIME;
    private final Value<Boolean> DURABILITY;
    private final Value<Boolean> SERVER_IP;
    private final Value<Boolean> POTIONS;
    private final Value<Boolean> ALPHABETICAL;
    DecimalFormat df;
    DecimalFormat df2;
    
    public Info() {
        super("Info");
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.t24 = this.register(new Value<Boolean>("24hr time", this, false));
        this.mode = this.register(new Value<String>("Aligned", this, "Right", new String[] { "Right", "Left" }));
        this.order = this.register(new Value<String>("Ordering", this, "Down", new String[] { "Up", "Down" }));
        this.color2 = this.register(new Value<String>("2nd Color", this, "LightGray", ColorTextUtils.colors));
        this.FPS = this.register(new Value<Boolean>("Fps", this, true));
        this.PING = this.register(new Value<Boolean>("Ping", this, true));
        this.TPS = this.register(new Value<Boolean>("Tps", this, true));
        this.SPEED = this.register(new Value<Boolean>("Speed", this, true));
        this.TIME = this.register(new Value<Boolean>("Time", this, true));
        this.DURABILITY = this.register(new Value<Boolean>("Durability", this, true));
        this.SERVER_IP = this.register(new Value<Boolean>("Server IP", this, true));
        this.POTIONS = this.register(new Value<Boolean>("Potions", this, true));
        this.ALPHABETICAL = this.register(new Value<Boolean>("ABC Potions", this, false));
        this.df = new DecimalFormat("#.#");
        this.df2 = new DecimalFormat("#.###");
    }
    
    public static double coordsDiff(final char s) {
        switch (s) {
            case 'x': {
                return Info.mc.player.posX - Info.mc.player.prevPosX;
            }
            case 'z': {
                return Info.mc.player.posZ - Info.mc.player.prevPosZ;
            }
            default: {
                return 0.0;
            }
        }
    }
    
    public boolean isToolArmor(final Item i) {
        return i instanceof ItemArmor || i == Items.DIAMOND_SWORD || i == Items.DIAMOND_PICKAXE || i == Items.DIAMOND_AXE || i == Items.DIAMOND_SHOVEL || i == Items.DIAMOND_HOE || i == Items.IRON_SWORD || i == Items.IRON_PICKAXE || i == Items.IRON_AXE || i == Items.IRON_SHOVEL || i == Items.IRON_HOE || i == Items.GOLDEN_SWORD || i == Items.GOLDEN_PICKAXE || i == Items.GOLDEN_AXE || i == Items.GOLDEN_SHOVEL || i == Items.GOLDEN_HOE || i == Items.STONE_SWORD || i == Items.STONE_PICKAXE || i == Items.STONE_AXE || i == Items.STONE_SHOVEL || i == Items.STONE_HOE || i == Items.WOODEN_SWORD || i == Items.WOODEN_PICKAXE || i == Items.WOODEN_AXE || i == Items.WOODEN_SHOVEL || i == Items.WOODEN_HOE;
    }
    
    @Override
    public void onRender() {
        float yCount = (float)this.y;
        int color = ColorUtil.getClickGUIColor().getRGB();
        if (this.rainbow.getValue()) {
            color = Xulu.rgb;
        }
        final float currentTps = Info.mc.timer.tickLength / 1000.0f;
        final ItemStack itemStack = Info.mc.player.getHeldItemMainhand();
        final List<String> infolist = new ArrayList<String>();
        final Map<String, PotionEffect> potionMap = new HashMap<String, PotionEffect>();
        List<String> potionList = new ArrayList<String>();
        if (this.FPS.getValue()) {
            infolist.add("FPS: " + Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color2.getValue()).substring(1) + Minecraft.getDebugFPS());
        }
        if (this.PING.getValue()) {
            infolist.add("Ping: " + Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color2.getValue()).substring(1) + ((Info.mc.getConnection() != null && Info.mc.player != null && Info.mc.getConnection().getPlayerInfo(Info.mc.player.entityUniqueID) != null) ? Integer.valueOf(Info.mc.getConnection().getPlayerInfo(Info.mc.player.entityUniqueID).getResponseTime()) : "-1") + "ms");
        }
        if (this.TPS.getValue()) {
            infolist.add("TPS: " + Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color2.getValue()).substring(1) + this.df2.format(LagCompensator.INSTANCE.getTickRate()));
        }
        if (this.SPEED.getValue()) {
            infolist.add("Speed: " + Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color2.getValue()).substring(1) + this.df.format(MathHelper.sqrt(Math.pow(coordsDiff('x'), 2.0) + Math.pow(coordsDiff('z'), 2.0)) / currentTps * 3.6) + " hm/h");
        }
        if (this.TIME.getValue()) {
            infolist.add("Time: " + Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color2.getValue()).substring(1) + (this.t24.getValue() ? new SimpleDateFormat("k:mm").format(new Date()) : new SimpleDateFormat("h:mm a").format(new Date())));
        }
        if (this.DURABILITY.getValue() && this.isToolArmor(itemStack.getItem())) {
            infolist.add("Durability: " + Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color2.getValue()).substring(1) + (itemStack.getMaxDamage() - itemStack.getItemDamage()));
        }
        if (this.SERVER_IP.getValue()) {
            infolist.add("Server IP: " + Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color2.getValue()).substring(1) + ((Info.mc.getCurrentServerData() == null) ? "None" : Info.mc.getCurrentServerData().serverIP));
        }
        if (this.POTIONS.getValue()) {
            for (final PotionEffect potionEffect : Info.mc.player.getActivePotionEffects()) {
                final String text = PotionUtil.getPotionName(potionEffect.getPotion()) + " " + ((potionEffect.getAmplifier() + 1 != 1) ? (potionEffect.getAmplifier() + 1 + " ") : "") + Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color2.getValue()).substring(1) + ((this.getTime(potionEffect.getDuration() / 20).length() == 1) ? "0" : "") + this.getTime(potionEffect.getDuration() / 20);
                potionMap.put(text, potionEffect);
            }
            if (Xulu.CustomFont) {
                potionList = potionMap.keySet().stream().sorted(Comparator.comparing(s -> Xulu.cFontRenderer.getStringWidth(s))).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
            }
            else {
                potionList = potionMap.keySet().stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)Info.fontRenderer::getStringWidth)).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
            }
            if (this.ALPHABETICAL.getValue()) {
                final String[] names = potionList.toArray(new String[0]);
                for (int count = potionList.size(), i = 0; i < count; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        if (names[i].compareTo(names[j]) > 0) {
                            final String temp = names[i];
                            names[i] = names[j];
                            names[j] = temp;
                        }
                    }
                }
                potionList.clear();
                for (final String modname : names) {
                    try {
                        potionList.add(modname);
                    }
                    catch (Exception ex) {}
                }
                if (this.order.getValue().equalsIgnoreCase("Down")) {
                    Collections.reverse(potionList);
                }
            }
        }
        this.width = 50.0;
        this.height = 50.0;
        if (Xulu.CustomFont) {
            infolist.sort(Comparator.comparing(s -> Xulu.cFontRenderer.getStringWidth(s)));
            Collections.reverse(infolist);
            if (this.order.getValue().equalsIgnoreCase("Down")) {
                yCount += 39.0f;
            }
            for (final String s2 : potionList) {
                Xulu.cFontRenderer.drawStringWithShadow(s2, this.x - (this.mode.getValue().equalsIgnoreCase("Right") ? 0 : Xulu.cFontRenderer.getStringWidth(s2)) + (this.mode.getValue().equalsIgnoreCase("Right") ? 1.0 : (this.getFrame().width - 2.0)), yCount + 1.0f, ColorUtils.changeAlpha(potionMap.get(s2).getPotion().getLiquidColor(), Global.hudAlpha.getValue()));
                yCount += (this.order.getValue().equalsIgnoreCase("Up") ? 10 : -10);
            }
            for (final String s2 : infolist) {
                Xulu.cFontRenderer.drawStringWithShadow(s2, this.x - (this.mode.getValue().equalsIgnoreCase("Right") ? 0 : Xulu.cFontRenderer.getStringWidth(s2)) + (this.mode.getValue().equalsIgnoreCase("Right") ? 1.0 : (this.getFrame().width - 2.0)), yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
                yCount += (this.order.getValue().equalsIgnoreCase("Up") ? 10 : -10);
            }
        }
        else {
            infolist.sort(Comparator.comparing((Function<? super String, ? extends Comparable>)Info.fontRenderer::getStringWidth));
            Collections.reverse(infolist);
            if (this.order.getValue().equalsIgnoreCase("Down")) {
                yCount += 39.0f;
            }
            for (final String s2 : potionList) {
                Info.fontRenderer.drawStringWithShadow(s2, (float)this.x - (this.mode.getValue().equalsIgnoreCase("Right") ? 0 : Info.fontRenderer.getStringWidth(s2)) + (float)(this.mode.getValue().equalsIgnoreCase("Right") ? 1.0 : (this.getFrame().width - 2.0)), yCount + 1.0f, ColorUtils.changeAlpha(potionMap.get(s2).getPotion().getLiquidColor(), Global.hudAlpha.getValue()));
                yCount += (this.order.getValue().equalsIgnoreCase("Up") ? 10 : -10);
            }
            for (final String s2 : infolist) {
                Info.fontRenderer.drawStringWithShadow(s2, (float)this.x - (this.mode.getValue().equalsIgnoreCase("Right") ? 0 : Info.fontRenderer.getStringWidth(s2)) + (float)(this.mode.getValue().equalsIgnoreCase("Right") ? 1.0 : (this.getFrame().width - 2.0)), yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
                yCount += (this.order.getValue().equalsIgnoreCase("Up") ? 10 : -10);
            }
        }
    }
    
    public String getTime(int duration) {
        int min;
        for (min = 0; duration > 59; duration -= 60, ++min) {}
        return min + ":" + ((this.df.format(duration).length() == 1) ? ("0" + this.df.format(duration)) : this.df.format(duration));
    }
}
