/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.client.Minecraft
 *  net.minecraft.event.ClickEvent
 *  net.minecraft.event.ClickEvent$Action
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemSkull
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemTool
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C01PacketChatMessage
 *  net.minecraft.network.play.client.C0EPacketClickWindow
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S2DPacketOpenWindow
 *  net.minecraft.network.play.server.S2FPacketSetSlot
 *  net.minecraft.util.ChatStyle
 *  net.minecraft.util.IChatComponent
 *  org.apache.commons.io.IOUtils
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoBypass", category=ModuleCategory.PLAYER, description="He died.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0010\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0010\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u0005H\u0002J\u0010\u0010\u001f\u001a\u00020\u00172\u0006\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020\u00172\u0006\u0010 \u001a\u00020!H\u0002J\b\u0010#\u001a\u00020\u0017H\u0016J\u0010\u0010$\u001a\u00020\u00172\u0006\u0010 \u001a\u00020!H\u0007J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010 \u001a\u00020&H\u0007R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006'"}, d2={"Lme/report/liquidware/modules/player/AutoBypass;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "brLangMap", "Ljava/util/HashMap;", "", "clickedSlot", "Ljava/util/ArrayList;", "", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "jsonParser", "Lcom/google/gson/JsonParser;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "packets", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "skull", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "type", "click", "", "windowId", "slot", "item", "Lnet/minecraft/item/ItemStack;", "getItemLocalName", "getSkinURL", "data", "handleRedeSky", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "handleRemiCraft", "onEnable", "onPacket", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoBypass
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Redesky", "RemiCraft"}, "Redesky");
    private final IntegerValue delayValue = new IntegerValue("Delay", 1500, 100, 5000);
    private String skull;
    private String type = "none";
    private final ArrayList<Packet<INetHandlerPlayServer>> packets = new ArrayList();
    private final ArrayList<Integer> clickedSlot = new ArrayList();
    private final MSTimer timer = new MSTimer();
    private final JsonParser jsonParser = new JsonParser();
    private final HashMap<String, String> brLangMap = new HashMap();

    @Override
    public void onEnable() {
        this.skull = null;
        this.type = "none";
        this.packets.clear();
        this.clickedSlot.clear();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Collection collection = this.packets;
        boolean bl = false;
        if (!collection.isEmpty() && this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            for (Packet<INetHandlerPlayServer> packet : this.packets) {
                Minecraft minecraft = AutoBypass.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a(packet);
            }
            this.packets.clear();
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "redesky": {
                this.handleRedeSky(event);
                break;
            }
            case "remicraft": {
                this.handleRemiCraft(event);
            }
        }
    }

    private final void handleRemiCraft(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String raw;
            IChatComponent component;
            IChatComponent iChatComponent = component = ((S02PacketChat)packet).func_148915_c();
            Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "component");
            String string = raw = iChatComponent.func_150260_c();
            Intrinsics.checkExpressionValueIsNotNull(string, "raw");
            if (StringsKt.contains$default((CharSequence)string, "\u6ce8\u518c\u524d\u60a8\u9700\u8981\u5148\u63d0\u4f9b\u9a8c\u8bc1\u7801\uff0c\u8bf7\u4f7f\u7528\u6307\u4ee4\uff1a/captcha", false, 2, null)) {
                String string2;
                this.timer.reset();
                String string3 = raw;
                int n = StringsKt.indexOf$default((CharSequence)raw, "/captcha", 0, false, 6, null);
                ArrayList<Packet<INetHandlerPlayServer>> arrayList = this.packets;
                boolean bl = false;
                String string4 = string3.substring(n);
                Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).substring(startIndex)");
                String string5 = string2 = string4;
                arrayList.add((Packet<INetHandlerPlayServer>)new C01PacketChatMessage(string5));
            } else {
                List list = component.func_150253_a();
                Intrinsics.checkExpressionValueIsNotNull(list, "component.siblings");
                Iterable $this$forEach$iv = list;
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    IChatComponent sib = (IChatComponent)element$iv;
                    boolean bl = false;
                    IChatComponent iChatComponent2 = sib;
                    Intrinsics.checkExpressionValueIsNotNull(iChatComponent2, "sib");
                    ChatStyle chatStyle = iChatComponent2.func_150256_b();
                    Intrinsics.checkExpressionValueIsNotNull(chatStyle, "sib.chatStyle");
                    ClickEvent clickEvent = chatStyle.func_150235_h();
                    if (clickEvent == null || clickEvent.func_150669_a() != ClickEvent.Action.RUN_COMMAND) continue;
                    String string6 = clickEvent.func_150668_b();
                    Intrinsics.checkExpressionValueIsNotNull(string6, "clickEvent.value");
                    if (!StringsKt.startsWith$default(string6, ".say", false, 2, null)) continue;
                    this.timer.reset();
                    this.packets.add((Packet<INetHandlerPlayServer>)new C01PacketChatMessage(clickEvent.func_150668_b()));
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private final void handleRedeSky(PacketEvent event) {
        int n;
        Packet<?> packet = event.getPacket();
        if (packet instanceof S2FPacketSetSlot) {
            int slot = ((S2FPacketSetSlot)packet).func_149173_d();
            int windowId = ((S2FPacketSetSlot)packet).func_149175_c();
            ItemStack item = ((S2FPacketSetSlot)packet).func_149174_e();
            if (windowId == 0) return;
            if (item == null) return;
            if (Intrinsics.areEqual(this.type, "none")) return;
            if (this.clickedSlot.contains(slot)) {
                return;
            }
            String itemName = item.func_77977_a();
            String string = this.type;
            n = 0;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "skull": {
                    String string4 = itemName;
                    Intrinsics.checkExpressionValueIsNotNull(string4, "itemName");
                    if (!StringsKt.contains((CharSequence)string4, "item.skull.char", true)) break;
                    NBTTagCompound nBTTagCompound = item.func_77978_p();
                    if (nBTTagCompound == null) return;
                    NBTTagCompound nbt = nBTTagCompound;
                    NBTTagCompound nBTTagCompound2 = nbt.func_74775_l("SkullOwner").func_74775_l("Properties");
                    Intrinsics.checkExpressionValueIsNotNull(NBTTagCompound.field_82578_b, "NBTTagCompound.NBT_TYPES");
                    String string5 = nBTTagCompound2.func_150295_c("textures", ArraysKt.indexOf(NBTTagCompound.field_82578_b, "COMPOUND")).func_150305_b(0).func_74779_i("Value");
                    Intrinsics.checkExpressionValueIsNotNull(string5, "nbt.getCompoundTag(\"Skul\u2026gAt(0).getString(\"Value\")");
                    String data = this.getSkinURL(string5);
                    if (this.skull == null) {
                        this.skull = data;
                        break;
                    }
                    if (!(Intrinsics.areEqual(this.skull, data) ^ true)) break;
                    this.skull = null;
                    this.click(windowId, slot, item);
                    break;
                }
                case "enchada": {
                    this.click(windowId, slot, item);
                    break;
                }
                case "cabe\u00e7a": {
                    if (!(item.func_77973_b() instanceof ItemSkull)) break;
                    this.click(windowId, slot, item);
                    break;
                }
                case "ferramenta": {
                    if (!(item.func_77973_b() instanceof ItemTool)) break;
                    this.click(windowId, slot, item);
                    break;
                }
                case "comida": {
                    if (!(item.func_77973_b() instanceof ItemFood)) break;
                    this.click(windowId, slot, item);
                    break;
                }
                default: {
                    if (!StringsKt.contains$default((CharSequence)this.getItemLocalName(item), this.type, false, 2, null)) break;
                    this.click(windowId, slot, item);
                }
            }
        }
        if (!(packet instanceof S2DPacketOpenWindow)) return;
        IChatComponent iChatComponent = ((S2DPacketOpenWindow)packet).func_179840_c();
        Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "packet.windowTitle");
        String windowName = iChatComponent.func_150260_c();
        if (((S2DPacketOpenWindow)packet).func_148898_f() == 27) {
            String string = ((S2DPacketOpenWindow)packet).func_148902_e();
            Intrinsics.checkExpressionValueIsNotNull(string, "packet.guiId");
            if (StringsKt.contains((CharSequence)string, "container", true)) {
                String string6 = windowName;
                Intrinsics.checkExpressionValueIsNotNull(string6, "windowName");
                if (StringsKt.startsWith(string6, "Clique", true)) {
                    String string7;
                    AutoBypass autoBypass = this;
                    if (StringsKt.contains((CharSequence)windowName, "bloco", true)) {
                        string7 = "skull";
                    } else {
                        List splited = StringsKt.split$default((CharSequence)windowName, new String[]{" "}, false, 0, 6, null);
                        String string8 = StringsKt.replace$default((String)splited.get(splited.size() - 1), ".", "", false, 4, null);
                        AutoBypass autoBypass2 = autoBypass;
                        int n2 = 0;
                        String string9 = string8;
                        if (string9 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string10 = string9.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(string10, "(this as java.lang.String).toLowerCase()");
                        String string11 = string10;
                        autoBypass = autoBypass2;
                        String str = string11;
                        if (StringsKt.endsWith$default(str, "s", false, 2, null)) {
                            string8 = str;
                            n2 = 0;
                            n = str.length() - 1;
                            autoBypass2 = autoBypass;
                            boolean bl = false;
                            String string12 = string8;
                            if (string12 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            String string13 = string12.substring(n2, n);
                            Intrinsics.checkExpressionValueIsNotNull(string13, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                            string11 = string13;
                            autoBypass = autoBypass2;
                            str = string11;
                        }
                        string7 = str;
                    }
                    autoBypass.type = string7;
                    this.packets.clear();
                    this.clickedSlot.clear();
                    event.cancelEvent();
                    this.timer.reset();
                    return;
                }
            }
        }
        this.type = "none";
    }

    private final void click(int windowId, int slot, ItemStack item) {
        this.clickedSlot.add(slot);
        this.packets.add((Packet<INetHandlerPlayServer>)new C0EPacketClickWindow(windowId, slot, 0, 0, item, (short)RandomUtils.nextInt(114, 514)));
    }

    private final String getItemLocalName(ItemStack item) {
        String string = this.brLangMap.get(item.func_77977_a());
        if (string == null) {
            string = "null";
        }
        return string;
    }

    private final String getSkinURL(String data) {
        byte[] byArray = Base64.getDecoder().decode(data);
        Intrinsics.checkExpressionValueIsNotNull(byArray, "Base64.getDecoder().decode(data)");
        byte[] byArray2 = byArray;
        JsonParser jsonParser = this.jsonParser;
        boolean bl = false;
        String string = new String(byArray2, Charsets.UTF_8);
        JsonElement jsonElement = jsonParser.parse(string);
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "jsonParser.parse(String(\u2026tDecoder().decode(data)))");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url");
        Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "jsonObject\n            .\u2026)\n            .get(\"url\")");
        String string2 = jsonElement2.getAsString();
        Intrinsics.checkExpressionValueIsNotNull(string2, "jsonObject\n            .\u2026     .get(\"url\").asString");
        return string2;
    }

    /*
     * WARNING - void declaration
     */
    public AutoBypass() {
        JsonElement jsonElement = new JsonParser().parse(IOUtils.toString((InputStream)AutoBypass.class.getClassLoader().getResourceAsStream("assets/minecraft/fdpclient/misc/item_names_in_pt_BR.json"), (Charset)StandardCharsets.UTF_8));
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "JsonParser().parse(IOUti\u2026 StandardCharsets.UTF_8))");
        JsonObject localeJson = jsonElement.getAsJsonObject();
        this.brLangMap.clear();
        for (Map.Entry entry : localeJson.entrySet()) {
            String string;
            void key;
            Object object = entry;
            boolean bl = false;
            String string2 = (String)object.getKey();
            object = entry;
            bl = false;
            JsonElement element = (JsonElement)object.getValue();
            Map map = this.brLangMap;
            String string3 = "item." + (String)key;
            JsonElement jsonElement2 = element;
            Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "element");
            String string4 = jsonElement2.getAsString();
            Intrinsics.checkExpressionValueIsNotNull(string4, "element.asString");
            object = string4;
            String string5 = string3;
            Map map2 = map;
            bl = false;
            Object object2 = object;
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            Intrinsics.checkExpressionValueIsNotNull(((String)object2).toLowerCase(), "(this as java.lang.String).toLowerCase()");
            map2.put(string5, string);
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

