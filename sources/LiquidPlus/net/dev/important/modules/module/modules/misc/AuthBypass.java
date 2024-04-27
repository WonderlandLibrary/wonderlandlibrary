/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemSkull
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemTool
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C0EPacketClickWindow
 *  net.minecraft.network.play.server.S2DPacketOpenWindow
 *  net.minecraft.network.play.server.S2FPacketSetSlot
 *  org.apache.commons.io.IOUtils
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.misc.RandomUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.IntegerValue;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Info(name="AuthBypass", spacedName="Auth Bypass", description="Bypass auth when join server.", category=Category.MISC, cnName="\u7ed5\u8fc7\u673a\u5668\u4eba\u68c0\u6d4b")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0010\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001b\u001a\u00020\u0015H\u0016J\u0010\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001eH\u0007J\u0010\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020 H\u0007J\u0010\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u0005H\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2={"Lnet/dev/important/modules/module/modules/misc/AuthBypass;", "Lnet/dev/important/modules/module/Module;", "()V", "brLangMap", "Ljava/util/HashMap;", "", "clickedSlot", "Ljava/util/ArrayList;", "", "delayValue", "Lnet/dev/important/value/IntegerValue;", "jsonParser", "Lcom/google/gson/JsonParser;", "packets", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "skull", "timer", "Lnet/dev/important/utils/timer/MSTimer;", "type", "click", "", "windowId", "slot", "item", "Lnet/minecraft/item/ItemStack;", "getItemLocalName", "onEnable", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "process", "data", "LiquidBounce"})
public final class AuthBypass
extends Module {
    @NotNull
    private final IntegerValue delayValue = new IntegerValue("Delay", 1500, 100, 5000, "ms");
    @Nullable
    private String skull;
    @NotNull
    private String type = "none";
    @NotNull
    private final ArrayList<Packet<INetHandlerPlayServer>> packets = new ArrayList();
    @NotNull
    private final ArrayList<Integer> clickedSlot = new ArrayList();
    @NotNull
    private final MSTimer timer = new MSTimer();
    @NotNull
    private final JsonParser jsonParser = new JsonParser();
    @NotNull
    private final HashMap<String, String> brLangMap = new HashMap();

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (!((Collection)this.packets).isEmpty() && this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            for (Packet<INetHandlerPlayServer> packet : this.packets) {
                MinecraftInstance.mc.func_147114_u().func_147297_a(packet);
            }
            this.packets.clear();
            Client.INSTANCE.getHud().addNotification(new Notification("Authentication bypassed.", Notification.Type.INFO));
        }
    }

    @Override
    public void onEnable() {
        this.skull = null;
        this.type = "none";
        this.packets.clear();
        this.clickedSlot.clear();
        new Thread(() -> AuthBypass.onEnable$lambda-0(this)).start();
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        String string;
        String[] item;
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof S2FPacketSetSlot) {
            int slot = ((S2FPacketSetSlot)packet).func_149173_d();
            int windowId = ((S2FPacketSetSlot)packet).func_149175_c();
            item = ((S2FPacketSetSlot)packet).func_149174_e();
            if (windowId == 0) return;
            if (item == null) return;
            if (Intrinsics.areEqual(this.type, "none")) return;
            if (this.clickedSlot.contains(slot)) {
                return;
            }
            String itemName = item.func_77977_a();
            string = this.type.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            switch (string) {
                case "skull": {
                    Intrinsics.checkNotNullExpressionValue(itemName, "itemName");
                    if (!StringsKt.contains((CharSequence)itemName, "item.skull.char", true)) break;
                    NBTTagCompound nBTTagCompound = item.func_77978_p();
                    if (nBTTagCompound == null) {
                        return;
                    }
                    NBTTagCompound nbt = nBTTagCompound;
                    NBTTagCompound nBTTagCompound2 = nbt.func_74775_l("SkullOwner").func_74775_l("Properties");
                    String[] stringArray = NBTTagCompound.field_82578_b;
                    Intrinsics.checkNotNullExpressionValue(stringArray, "NBT_TYPES");
                    String string2 = nBTTagCompound2.func_150295_c("textures", ArraysKt.indexOf((Object[])stringArray, "COMPOUND")).func_150305_b(0).func_74779_i("Value");
                    Intrinsics.checkNotNullExpressionValue(string2, "nbt.getCompoundTag(\"Skul\u2026gAt(0).getString(\"Value\")");
                    String data = this.process(string2);
                    if (this.skull == null) {
                        this.skull = data;
                        break;
                    }
                    if (Intrinsics.areEqual(this.skull, data)) break;
                    this.skull = null;
                    this.timer.reset();
                    this.click(windowId, slot, (ItemStack)item);
                    break;
                }
                case "enchada": {
                    this.click(windowId, slot, (ItemStack)item);
                    break;
                }
                case "cabe\u00e7a": {
                    if (!(item.func_77973_b() instanceof ItemSkull)) break;
                    this.click(windowId, slot, (ItemStack)item);
                    break;
                }
                case "ferramenta": {
                    if (!(item.func_77973_b() instanceof ItemTool)) break;
                    this.click(windowId, slot, (ItemStack)item);
                    break;
                }
                case "comida": {
                    if (!(item.func_77973_b() instanceof ItemFood)) break;
                    this.click(windowId, slot, (ItemStack)item);
                    break;
                }
                default: {
                    if (!StringsKt.contains$default((CharSequence)this.getItemLocalName((ItemStack)item), this.type, false, 2, null)) break;
                    this.click(windowId, slot, (ItemStack)item);
                }
            }
        }
        if (!(packet instanceof S2DPacketOpenWindow)) return;
        String windowName = ((S2DPacketOpenWindow)packet).func_179840_c().func_150260_c();
        if (((S2DPacketOpenWindow)packet).func_148898_f() == 27) {
            String windowId = ((S2DPacketOpenWindow)packet).func_148902_e();
            Intrinsics.checkNotNullExpressionValue(windowId, "packet.guiId");
            if (StringsKt.contains((CharSequence)windowId, "container", true)) {
                Intrinsics.checkNotNullExpressionValue(windowName, "windowName");
                if (StringsKt.startsWith(windowName, "Clique", true)) {
                    String string3;
                    if (StringsKt.contains((CharSequence)windowName, "bloco", true)) {
                        string3 = "skull";
                    } else {
                        item = new String[]{" "};
                        List splited = StringsKt.split$default((CharSequence)windowName, item, false, 0, 6, null);
                        String string4 = StringsKt.replace$default((String)splited.get(splited.size() - 1), ".", "", false, 4, null).toLowerCase();
                        Intrinsics.checkNotNullExpressionValue(string4, "this as java.lang.String).toLowerCase()");
                        String str = string4;
                        if (StringsKt.endsWith$default(str, "s", false, 2, null)) {
                            string = str.substring(0, str.length() - 1);
                            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                            str = string;
                        }
                        string3 = str;
                    }
                    this.type = string3;
                    this.packets.clear();
                    this.clickedSlot.clear();
                    event.cancelEvent();
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

    private final String process(String data) {
        Object object = Base64.getDecoder().decode(data);
        Intrinsics.checkNotNullExpressionValue(object, "getDecoder().decode(data)");
        JsonObject jsonObject = this.jsonParser.parse(new String((byte[])object, Charsets.UTF_8)).getAsJsonObject();
        object = jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
        Intrinsics.checkNotNullExpressionValue(object, "jsonObject\n            .\u2026     .get(\"url\").asString");
        return object;
    }

    private static final void onEnable$lambda-0(AuthBypass this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        JsonObject localeJson = new JsonParser().parse(IOUtils.toString((InputStream)AuthBypass.class.getClassLoader().getResourceAsStream("br_items.json"), (String)"utf-8")).getAsJsonObject();
        this$0.brLangMap.clear();
        for (Map.Entry entry : localeJson.entrySet()) {
            Intrinsics.checkNotNullExpressionValue(entry, "localeJson.entrySet()");
            String key = (String)entry.getKey();
            JsonElement element = (JsonElement)entry.getValue();
            Map map = this$0.brLangMap;
            String string = Intrinsics.stringPlus("item.", key);
            String string2 = element.getAsString();
            Intrinsics.checkNotNullExpressionValue(string2, "element.asString");
            String string3 = string2.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).toLowerCase()");
            map.put(string, string3);
        }
    }
}

