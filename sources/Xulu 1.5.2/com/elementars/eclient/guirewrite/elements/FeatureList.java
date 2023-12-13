// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import net.minecraft.client.gui.FontRenderer;
import java.util.Iterator;
import com.elementars.eclient.font.CFontManager;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.client.gui.Gui;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.xulu.newgui.util.ColorUtil;
import java.awt.Color;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.Xulu;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import com.google.common.collect.Maps;
import java.util.List;
import com.elementars.eclient.util.Pair;
import com.elementars.eclient.util.Triplet;
import com.elementars.eclient.module.Module;
import java.util.Map;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class FeatureList extends Element
{
    private Value<Boolean> corner;
    public static Value<Boolean> animation;
    public static Value<String> type;
    private Value<Boolean> alphab;
    private Value<Boolean> box;
    private Value<String> boxMode;
    private Value<String> mode;
    private Value<String> order;
    private Value<String> rlist;
    private Value<Integer> rainbowspeed;
    private Value<Integer> rspeed;
    private Value<Integer> rsaturation;
    private Value<Integer> rlightness;
    private Value<String> prefix;
    private Value<String> suffix;
    private Value<String> categoryProfile;
    private Rainbow rainbow;
    String comp;
    private Map<Module, Triplet<Double, Double, Pair<Double, Integer>>> animationMap;
    private List<Module> removal;
    
    public FeatureList() {
        super("FeatureList");
        this.rainbow = new Rainbow();
        this.animationMap = (Map<Module, Triplet<Double, Double, Pair<Double, Integer>>>)Maps.newHashMap();
        this.removal = new ArrayList<Module>();
        this.corner = this.register(new Value<Boolean>("List In Corner", this, false));
        FeatureList.animation = this.register(new Value<Boolean>("Animation", this, false));
        FeatureList.type = this.register(new Value<String>("Type", this, "Both", new String[] { "Both", "Enable", "Disable" }));
        this.alphab = this.register(new Value<Boolean>("Alphabetical", this, false));
        this.box = this.register(new Value<Boolean>("Boxes", this, false));
        this.boxMode = this.register(new Value<String>("Box Mode", this, "Tag", new String[] { "Black", "Tag", "Outline" }));
        this.prefix = this.register(new Value<String>("Prefix", this, "None", new String[] { "None", ">", ")", "]", "}", ">(space)", "->", "-", "=", "<", "(", "[", "{" }));
        this.suffix = this.register(new Value<String>("Suffix", this, "None", new String[] { "None", ">", ")", "]", "}", "(space)<", "<-", "-", "=", "<", "(", "[", "{" }));
        this.mode = this.register(new Value<String>("Aligned", this, "Left", new ArrayList<String>(Arrays.asList("Left", "Right"))));
        this.order = this.register(new Value<String>("Ordering", this, "Up", new ArrayList<String>(Arrays.asList("Up", "Down"))));
        this.rlist = this.register(new Value<String>("Color Mode", this, "ClickGui", new String[] { "ClickGui", "Rainbow", "Category" }));
        this.categoryProfile = this.register(new Value<String>("Category Mode", this, "Xulu", new String[] { "Xulu", "Impact", "DotGod" }));
        this.rainbowspeed = this.register(new Value<Integer>("Rainbow Speed", this, 5, 1, 100));
        this.rspeed = this.register(new Value<Integer>("Rainbow Size", this, 2, 0, 20));
        this.rsaturation = this.register(new Value<Integer>("Rainbow Sat.", this, 255, 0, 255));
        this.rlightness = this.register(new Value<Integer>("Rainbow Light.", this, 255, 0, 255));
    }
    
    @Override
    public void onEnable() {
        this.width = 80.0;
        this.height = 80.0;
    }
    
    private int betterCompare(final Module mod, final String stringIn) {
        int returnOut = 0;
        returnOut = mod.getName().compareTo(stringIn);
        this.comp = stringIn;
        return returnOut;
    }
    
    @Override
    public void onRender() {
        final ScaledResolution s = new ScaledResolution(FeatureList.mc);
        double yCount = 3.0;
        double right = s.getScaledWidth() - 3 - this.getFrame().width;
        if (!this.corner.getValue()) {
            yCount = this.y + 1.0;
            right = this.x + 1.0;
        }
        this.rainbow.updateRainbow();
        if (Xulu.CustomFont) {
            final CFontManager cFontRenderer;
            String string;
            final StringBuilder sb;
            final List<Module> mods = Xulu.MODULE_MANAGER.getModules().stream().filter(Module::isToggledAnim).filter(Module::isDrawn).sorted(Comparator.comparing(module -> {
                cFontRenderer = Xulu.cFontRenderer;
                new StringBuilder().append(module.getName());
                if (module.getHudInfo() == null) {
                    string = "";
                }
                else {
                    string = Command.SECTIONSIGN() + "7 [" + module.getHudInfo() + "]";
                }
                return Integer.valueOf(cFontRenderer.getStringWidth(sb.append(string).toString()) * -1);
            })).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
            if (this.alphab.getValue()) {
                final String[] names = mods.stream().map((Function<? super Object, ?>)Module::getName).toArray(String[]::new);
                for (int count = mods.size(), i = 0; i < count; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        if (names[i].compareTo(names[j]) > 0) {
                            final String temp = names[i];
                            names[i] = names[j];
                            names[j] = temp;
                        }
                    }
                }
                mods.clear();
                for (final String modname : names) {
                    try {
                        mods.add(Xulu.MODULE_MANAGER.getModuleByName(modname));
                    }
                    catch (Exception ex) {}
                }
            }
            boolean start = true;
            if (this.order.getValue().equalsIgnoreCase("Down")) {
                yCount += 69.0;
            }
            float hue = this.rainbow.hue;
            String pre = null;
            String suf = null;
            for (final Module module2 : mods) {
                int rgb2 = Color.HSBtoRGB(hue, this.rsaturation.getValue() / 255.0f, this.rlightness.getValue() / 255.0f);
                final String s2 = this.rlist.getValue();
                switch (s2) {
                    case "ClickGui": {
                        rgb2 = ColorUtil.getClickGUIColor().getRGB();
                        break;
                    }
                    case "Category": {
                        rgb2 = this.getCategoryColor(module2);
                        break;
                    }
                }
                final String s3 = this.prefix.getValue();
                switch (s3) {
                    case "None": {
                        pre = "";
                        break;
                    }
                    case ">(space)": {
                        pre = "> ";
                        break;
                    }
                    default: {
                        pre = this.prefix.getValue();
                        break;
                    }
                }
                final String s4 = this.suffix.getValue();
                switch (s4) {
                    case "None": {
                        suf = "";
                        break;
                    }
                    case "(space)<": {
                        suf = " <";
                        break;
                    }
                    default: {
                        suf = this.suffix.getValue();
                        break;
                    }
                }
                final String display = pre + module2.getName() + ((module2.getHudInfo() == null) ? "" : (Command.SECTIONSIGN() + "7 [" + Command.SECTIONSIGN() + "f" + module2.getHudInfo() + Command.SECTIONSIGN() + "7]" + ChatFormatting.RESET)) + suf;
                final double right2 = this.mode.getValue().equalsIgnoreCase("Right") ? (right - Xulu.cFontRenderer.getStringWidth(display) + this.getFrame().width - 3.0) : right;
                final double width = Xulu.cFontRenderer.getStringWidth(display);
                if (this.box.getValue()) {
                    final String s5 = this.boxMode.getValue();
                    switch (s5) {
                        case "Black": {
                            Gui.drawRect((int)right2 - 1, (int)yCount - 1, (int)right2 + (int)width + 3, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), 1427181841);
                            break;
                        }
                        case "Tag": {
                            Gui.drawRect((int)right2 - 1, (int)yCount - 1, (int)right2 + (int)width + 3, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), 1427181841);
                            Gui.drawRect((int)right2 - 1, (int)yCount - 1, (int)right2 + 1, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), rgb2);
                            break;
                        }
                        case "Outline": {
                            Gui.drawRect((int)right2 - 1, (int)yCount - 1, (int)right2 + (int)width + 3, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), 1427181841);
                            XuluTessellator.drawRectOutline((int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width + 4, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), (int)right2 - 1, (int)yCount - 1, (int)right2 + (int)width + 3, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), rgb2);
                            if (mods.indexOf(module2) == 0) {
                                XuluTessellator.drawRectOutline((int)right2 - 2, (int)yCount - 2, (int)right2 + (int)width + 4, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), (int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width + 4, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), rgb2);
                                if (mods.indexOf(module2) + 1 < mods.size()) {
                                    final Module mod = mods.get(mods.indexOf(module2) + 1);
                                    final String next = pre + mod.getName() + ((mod.getHudInfo() == null) ? "" : (Command.SECTIONSIGN() + "7 [" + Command.SECTIONSIGN() + "f" + mod.getHudInfo() + Command.SECTIONSIGN() + "7]" + ChatFormatting.RESET)) + suf;
                                    final double nextWidth = Xulu.cFontRenderer.getStringWidth(next);
                                    XuluTessellator.drawRectOutline((int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width - nextWidth - 1.0, (int)yCount + (int)Xulu.cFontRenderer.getHeight() + 1, (int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width - nextWidth - 1.0, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), rgb2);
                                    break;
                                }
                                break;
                            }
                            else {
                                if (mods.indexOf(module2) + 1 == mods.size()) {
                                    XuluTessellator.drawRectOutline((int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width + 4, (int)yCount + (int)Xulu.cFontRenderer.getHeight() + 1, (int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width + 4, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), rgb2);
                                    break;
                                }
                                final Module mod = mods.get(mods.indexOf(module2) + 1);
                                final String next = pre + mod.getName() + ((mod.getHudInfo() == null) ? "" : (Command.SECTIONSIGN() + "7 [" + Command.SECTIONSIGN() + "f" + mod.getHudInfo() + Command.SECTIONSIGN() + "7]" + ChatFormatting.RESET)) + suf;
                                final double nextWidth = Xulu.cFontRenderer.getStringWidth(next);
                                XuluTessellator.drawRectOutline((int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width - nextWidth - 1.0, (int)yCount + (int)Xulu.cFontRenderer.getHeight() + 1, (int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width - nextWidth - 1.0, (int)yCount + (int)Xulu.cFontRenderer.getHeight(), rgb2);
                                break;
                            }
                            break;
                        }
                    }
                    start = false;
                }
                Xulu.cFontRenderer.drawStringWithShadow((module2.inAnimation.getValue() != Animation.NONE) ? "" : display, right2 + 1.0, yCount, ColorUtils.changeAlpha(rgb2, Global.hudAlpha.getValue()), true);
                if (!this.animationMap.containsKey(module2)) {
                    if (module2.inAnimation.getValue() != Animation.NONE) {
                        if (this.mode.getValue().equalsIgnoreCase("Right")) {
                            if (module2.inAnimation.getValue() == Animation.ENABLE) {
                                this.animationMap.put(module2, new Triplet<Double, Double, Pair<Double, Integer>>(right2 + width, yCount, new Pair<Double, Integer>(right2, rgb2)));
                            }
                            else {
                                this.animationMap.put(module2, new Triplet<Double, Double, Pair<Double, Integer>>(right2, yCount, new Pair<Double, Integer>(right2 + width, rgb2)));
                            }
                        }
                        else if (this.mode.getValue().equalsIgnoreCase("Left")) {
                            if (module2.inAnimation.getValue() == Animation.ENABLE) {
                                this.animationMap.put(module2, new Triplet<Double, Double, Pair<Double, Integer>>(right2 - width, yCount, new Pair<Double, Integer>(right2, rgb2)));
                            }
                            else {
                                this.animationMap.put(module2, new Triplet<Double, Double, Pair<Double, Integer>>(right2, yCount, new Pair<Double, Integer>(right2 - width, rgb2)));
                            }
                        }
                    }
                }
                else {
                    this.animationMap.get(module2).getThird().setValue(rgb2);
                }
                yCount += (this.order.getValue().equalsIgnoreCase("Up") ? 10 : -10);
                final double speed = this.rspeed.getValue();
                hue += (float)(speed / 100.0);
            }
            for (final Module m : this.animationMap.keySet()) {
                final Triplet<Double, Double, Pair<Double, Integer>> triplet = this.animationMap.get(m);
                final String s6 = this.prefix.getValue();
                switch (s6) {
                    case "None": {
                        pre = "";
                        break;
                    }
                    case ">(space)": {
                        pre = "> ";
                        break;
                    }
                    default: {
                        pre = this.prefix.getValue();
                        break;
                    }
                }
                final String s7 = this.suffix.getValue();
                switch (s7) {
                    case "None": {
                        suf = "";
                        break;
                    }
                    case "(space)<": {
                        suf = " <";
                        break;
                    }
                    default: {
                        suf = this.suffix.getValue();
                        break;
                    }
                }
                final String display = pre + m.getName() + ((m.getHudInfo() == null) ? "" : (Command.SECTIONSIGN() + "7 [" + Command.SECTIONSIGN() + "f" + m.getHudInfo() + Command.SECTIONSIGN() + "7]" + ChatFormatting.RESET)) + suf;
                Xulu.cFontRenderer.drawStringWithShadow(display, triplet.getFirst(), triplet.getSecond(), ColorUtils.changeAlpha(triplet.getThird().getValue(), Global.hudAlpha.getValue()), true);
                if (!triplet.getFirst().equals(triplet.getThird().getKey())) {
                    if (triplet.getFirst() > triplet.getThird().getKey()) {
                        if (this.mode.getValue().equalsIgnoreCase("Left")) {
                            triplet.setFirst(triplet.getThird().getKey());
                        }
                        triplet.setFirst(triplet.getFirst() - 1.0);
                    }
                    if (triplet.getFirst() >= triplet.getThird().getKey()) {
                        continue;
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Right")) {
                        triplet.setFirst(triplet.getThird().getKey());
                    }
                    triplet.setFirst(triplet.getFirst() + 1.0);
                }
                else {
                    m.inAnimation.setEnumValue("NONE");
                    this.removal.add(m);
                }
            }
            this.removal.forEach(module -> {
                if (module.inAnimation.getValue() == Animation.NONE) {
                    this.animationMap.remove(module);
                }
                return;
            });
            this.removal.clear();
        }
        else {
            final FontRenderer fontRenderer;
            String string2;
            final StringBuilder sb2;
            final List<Module> mods = Xulu.MODULE_MANAGER.getModules().stream().filter(Module::isToggledAnim).filter(Module::isDrawn).sorted(Comparator.comparing(module -> {
                fontRenderer = Wrapper.getMinecraft().fontRenderer;
                new StringBuilder().append(module.getName());
                if (module.getHudInfo() == null) {
                    string2 = "";
                }
                else {
                    string2 = Command.SECTIONSIGN() + "7 [" + module.getHudInfo() + "]";
                }
                return Integer.valueOf(fontRenderer.getStringWidth(sb2.append(string2).toString()) * -1);
            })).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
            if (this.alphab.getValue()) {
                final String[] names = mods.stream().map((Function<? super Object, ?>)Module::getName).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).toArray(new String[0]);
                for (int count = mods.size(), i = 0; i < count; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        if (names[i].compareTo(names[j]) > 0) {
                            final String temp = names[i];
                            names[i] = names[j];
                            names[j] = temp;
                        }
                    }
                }
                mods.clear();
                for (final String modname : names) {
                    try {
                        mods.add(Xulu.MODULE_MANAGER.getModuleByName(modname));
                    }
                    catch (Exception ex2) {}
                }
            }
            float hue2 = this.rainbow.hue;
            if (this.order.getValue().equalsIgnoreCase("Down")) {
                yCount += 69.0;
            }
            String pre2 = null;
            String suf2 = null;
            for (final Module module3 : mods) {
                int rgb3 = Color.HSBtoRGB(hue2, this.rsaturation.getValue() / 255.0f, this.rlightness.getValue() / 255.0f);
                final String s8 = this.rlist.getValue();
                switch (s8) {
                    case "ClickGui": {
                        rgb3 = ColorUtil.getClickGUIColor().getRGB();
                        break;
                    }
                    case "Category": {
                        rgb3 = this.getCategoryColor(module3);
                        break;
                    }
                }
                final String s9 = this.prefix.getValue();
                switch (s9) {
                    case "None": {
                        pre2 = "";
                        break;
                    }
                    case ">(space)": {
                        pre2 = "> ";
                        break;
                    }
                    default: {
                        pre2 = this.prefix.getValue();
                        break;
                    }
                }
                final String s10 = this.suffix.getValue();
                switch (s10) {
                    case "None": {
                        suf2 = "";
                        break;
                    }
                    case "(space)<": {
                        suf2 = " <";
                        break;
                    }
                    default: {
                        suf2 = this.suffix.getValue();
                        break;
                    }
                }
                boolean start2 = true;
                final String display = pre2 + module3.getName() + ((module3.getHudInfo() == null) ? "" : (Command.SECTIONSIGN() + "7 [" + Command.SECTIONSIGN() + "f" + module3.getHudInfo() + Command.SECTIONSIGN() + "7]" + ChatFormatting.RESET)) + suf2;
                final double right2 = (int)(this.mode.getValue().equalsIgnoreCase("Right") ? (right - Wrapper.getMinecraft().fontRenderer.getStringWidth(display) + this.getFrame().width - 3.0) : right);
                final double width = Wrapper.getMinecraft().fontRenderer.getStringWidth(display);
                if (this.box.getValue()) {
                    final String s11 = this.boxMode.getValue();
                    switch (s11) {
                        case "Black": {
                            Gui.drawRect((int)right2 + 2, (int)yCount - 1, (int)right2 + (int)width + 3, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, 1427181841);
                            break;
                        }
                        case "Tag": {
                            Gui.drawRect((int)right2 - 2, (int)yCount - 1, (int)right2 + (int)width + 3, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, 1427181841);
                            Gui.drawRect((int)right2 - 2, (int)yCount - 1, (int)right2 + 1, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, rgb3);
                            break;
                        }
                        case "Outline": {
                            Gui.drawRect((int)right2 + 2, (int)yCount - 1, (int)right2 + (int)width + 3, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, 1427181841);
                            XuluTessellator.drawRectOutline((int)right2 + 1, (int)yCount - 1, (int)right2 + (int)width + 4, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, (int)right2 + 2, (int)yCount - 1, (int)right2 + (int)width + 3, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, rgb3);
                            if (mods.indexOf(module3) == 0) {
                                XuluTessellator.drawRectOutline((int)right2 + 1, (int)yCount - 2, (int)right2 + (int)width + 4, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, (int)right2 + 1, (int)yCount - 1, (int)right2 + (int)width + 4, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, rgb3);
                                if (mods.indexOf(module3) + 1 < mods.size()) {
                                    final Module mod = mods.get(mods.indexOf(module3) + 1);
                                    final String next = pre2 + mod.getName() + ((mod.getHudInfo() == null) ? "" : (Command.SECTIONSIGN() + "7 [" + Command.SECTIONSIGN() + "f" + mod.getHudInfo() + Command.SECTIONSIGN() + "7]" + ChatFormatting.RESET)) + suf2;
                                    final double nextWidth = Wrapper.getMinecraft().fontRenderer.getStringWidth(next);
                                    XuluTessellator.drawRectOutline((int)right2 + 1, (int)yCount - 1, (int)right2 + (int)width - nextWidth + 2.0, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT + 1, (int)right2 + 1, (int)yCount - 1, (int)right2 + (int)width - nextWidth + 2.0, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, rgb3);
                                    break;
                                }
                                break;
                            }
                            else {
                                if (mods.indexOf(module3) + 1 == mods.size()) {
                                    XuluTessellator.drawRectOutline((int)right2 + 1, (int)yCount - 1, (int)right2 + (int)width + 4, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT + 1, (int)right2 + 1, (int)yCount - 1, (int)right2 + (int)width + 4, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, rgb3);
                                    break;
                                }
                                final Module mod = mods.get(mods.indexOf(module3) + 1);
                                final String next = pre2 + mod.getName() + ((mod.getHudInfo() == null) ? "" : (Command.SECTIONSIGN() + "7 [" + Command.SECTIONSIGN() + "f" + mod.getHudInfo() + Command.SECTIONSIGN() + "7]" + ChatFormatting.RESET)) + suf2;
                                final double nextWidth = Wrapper.getMinecraft().fontRenderer.getStringWidth(next);
                                XuluTessellator.drawRectOutline((int)right2 + 1, (int)yCount - 1, (int)right2 + (int)width - nextWidth + 2.0, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT + 1, (int)right2 + 1, (int)yCount - 1, (int)right2 + (int)width - nextWidth + 2.0, (int)yCount + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT, rgb3);
                                break;
                            }
                            break;
                        }
                    }
                    start2 = false;
                }
                Wrapper.getMinecraft().fontRenderer.drawStringWithShadow((module3.inAnimation.getValue() != Animation.NONE) ? "" : display, (float)(int)(this.mode.getValue().equalsIgnoreCase("Right") ? (right - Wrapper.getMinecraft().fontRenderer.getStringWidth(display) + this.getFrame().width) : right), (float)(int)yCount, ColorUtils.changeAlpha(rgb3, Global.hudAlpha.getValue()));
                if (!this.animationMap.containsKey(module3)) {
                    if (module3.inAnimation.getValue() != Animation.NONE) {
                        if (this.mode.getValue().equalsIgnoreCase("Right")) {
                            if (module3.inAnimation.getValue() == Animation.ENABLE) {
                                this.animationMap.put(module3, new Triplet<Double, Double, Pair<Double, Integer>>(right2 + width, yCount, new Pair<Double, Integer>(right2, rgb3)));
                            }
                            else {
                                this.animationMap.put(module3, new Triplet<Double, Double, Pair<Double, Integer>>(right2, yCount, new Pair<Double, Integer>(right2 + width, rgb3)));
                            }
                        }
                        else if (this.mode.getValue().equalsIgnoreCase("Left")) {
                            if (module3.inAnimation.getValue() == Animation.ENABLE) {
                                this.animationMap.put(module3, new Triplet<Double, Double, Pair<Double, Integer>>(right2 - width, yCount, new Pair<Double, Integer>(right2, rgb3)));
                            }
                            else {
                                this.animationMap.put(module3, new Triplet<Double, Double, Pair<Double, Integer>>(right2, yCount, new Pair<Double, Integer>(right2 - width, rgb3)));
                            }
                        }
                    }
                }
                else {
                    this.animationMap.get(module3).getThird().setValue(rgb3);
                }
                yCount += (this.order.getValue().equalsIgnoreCase("Up") ? 10 : -10);
                final double speed = this.rspeed.getValue();
                hue2 += (float)(speed / 100.0);
            }
            for (final Module k : this.animationMap.keySet()) {
                final Triplet<Double, Double, Pair<Double, Integer>> triplet2 = this.animationMap.get(k);
                final String s12 = this.prefix.getValue();
                switch (s12) {
                    case "None": {
                        pre2 = "";
                        break;
                    }
                    case ">(space)": {
                        pre2 = "> ";
                        break;
                    }
                    default: {
                        pre2 = this.prefix.getValue();
                        break;
                    }
                }
                final String s13 = this.suffix.getValue();
                switch (s13) {
                    case "None": {
                        suf2 = "";
                        break;
                    }
                    case "(space)<": {
                        suf2 = " <";
                        break;
                    }
                    default: {
                        suf2 = this.suffix.getValue();
                        break;
                    }
                }
                final String display2 = pre2 + k.getName() + ((k.getHudInfo() == null) ? "" : (Command.SECTIONSIGN() + "7 [" + Command.SECTIONSIGN() + "f" + k.getHudInfo() + Command.SECTIONSIGN() + "7]" + ChatFormatting.RESET)) + suf2;
                Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(display2, triplet2.getFirst().floatValue(), triplet2.getSecond().floatValue(), ColorUtils.changeAlpha(triplet2.getThird().getValue(), Global.hudAlpha.getValue()));
                if (!triplet2.getFirst().equals(triplet2.getThird().getKey())) {
                    if (triplet2.getFirst() > triplet2.getThird().getKey()) {
                        if (this.mode.getValue().equalsIgnoreCase("Left")) {
                            triplet2.setFirst(triplet2.getThird().getKey());
                        }
                        triplet2.setFirst(triplet2.getFirst() - 1.0);
                    }
                    if (triplet2.getFirst() >= triplet2.getThird().getKey()) {
                        continue;
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Right")) {
                        triplet2.setFirst(triplet2.getThird().getKey());
                    }
                    triplet2.setFirst(triplet2.getFirst() + 1.0);
                }
                else {
                    k.inAnimation.setEnumValue("NONE");
                    this.removal.add(k);
                }
            }
            this.removal.forEach(module -> {
                if (module.inAnimation.getValue() == Animation.NONE) {
                    this.animationMap.remove(module);
                }
                return;
            });
            this.removal.clear();
        }
    }
    
    public String getTitle(String in) {
        in = Character.toUpperCase(in.toLowerCase().charAt(0)) + in.toLowerCase().substring(1);
        return in;
    }
    
    private int getCategoryColor(final Module m) {
        final String s = this.categoryProfile.getValue();
        int n = -1;
        switch (s.hashCode()) {
            case 2737510: {
                if (s.equals("Xulu")) {
                    n = 0;
                    break;
                }
                break;
            }
            case -2100942490: {
                if (s.equals("Impact")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 2052820627: {
                if (s.equals("DotGod")) {
                    n = 2;
                    break;
                }
                break;
            }
        }
        Label_0539: {
            switch (n) {
                case 0: {
                    switch (m.getCategory()) {
                        case CORE: {
                            return new Color(0, 218, 242).getRGB();
                        }
                        case COMBAT: {
                            return new Color(222, 57, 11).getRGB();
                        }
                        case MOVEMENT: {
                            return new Color(189, 28, 173).getRGB();
                        }
                        case PLAYER: {
                            return new Color(83, 219, 41).getRGB();
                        }
                        case RENDER: {
                            return new Color(255, 242, 62).getRGB();
                        }
                        case MISC: {
                            return new Color(255, 143, 15).getRGB();
                        }
                        case DUMMY: {
                            return new Color(222, 57, 209).getRGB();
                        }
                        case HUD: {
                            return new Color(255, 0, 123).getRGB();
                        }
                        case HIDDEN: {
                            return -1;
                        }
                        default: {
                            break Label_0539;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (m.getCategory()) {
                        case CORE: {
                            return new Color(0, 218, 242).getRGB();
                        }
                        case COMBAT: {
                            return new Color(229, 30, 16).getRGB();
                        }
                        case MOVEMENT: {
                            return new Color(8, 116, 227).getRGB();
                        }
                        case PLAYER: {
                            return new Color(43, 203, 55).getRGB();
                        }
                        case RENDER: {
                            return new Color(227, 162, 50).getRGB();
                        }
                        case MISC: {
                            return new Color(97, 30, 212).getRGB();
                        }
                        case DUMMY: {
                            return new Color(241, 243, 90).getRGB();
                        }
                        case HUD: {
                            return new Color(255, 0, 123).getRGB();
                        }
                        case HIDDEN: {
                            return -1;
                        }
                        default: {
                            break Label_0539;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (m.getCategory()) {
                        case CORE: {
                            return new Color(0, 218, 242).getRGB();
                        }
                        case COMBAT: {
                            return new Color(39, 181, 171).getRGB();
                        }
                        case MOVEMENT: {
                            return new Color(26, 84, 219).getRGB();
                        }
                        case PLAYER: {
                            return new Color(219, 184, 190).getRGB();
                        }
                        case RENDER: {
                            return new Color(169, 204, 83).getRGB();
                        }
                        case MISC: {
                            return new Color(215, 214, 216).getRGB();
                        }
                        case DUMMY: {
                            return new Color(222, 57, 209).getRGB();
                        }
                        case HUD: {
                            return new Color(255, 0, 123).getRGB();
                        }
                        case HIDDEN: {
                            return -1;
                        }
                        default: {
                            break Label_0539;
                        }
                    }
                    break;
                }
            }
        }
        return -1;
    }
    
    public class Rainbow
    {
        public int rgb;
        public int a;
        public int r;
        public int g;
        public int b;
        float hue;
        
        public Rainbow() {
            this.hue = 0.01f;
        }
        
        public void updateRainbow() {
            this.rgb = Color.HSBtoRGB(this.hue, Xulu.MODULE_MANAGER.getModuleT(FeatureList.class).rsaturation.getValue() / 255.0f, Xulu.MODULE_MANAGER.getModuleT(FeatureList.class).rlightness.getValue() / 255.0f);
            this.a = (this.rgb >>> 24 & 0xFF);
            this.r = (this.rgb >>> 16 & 0xFF);
            this.g = (this.rgb >>> 8 & 0xFF);
            this.b = (this.rgb & 0xFF);
            this.hue += Xulu.MODULE_MANAGER.getModuleT(FeatureList.class).rainbowspeed.getValue() / 10000.0f;
            if (this.hue > 1.0f) {
                --this.hue;
            }
        }
    }
}
