/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.IImageBuffer
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.cape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.cape.CapeInfo;
import net.ccbluex.liquidbounce.cape.CapeService;
import net.ccbluex.liquidbounce.cape.ServiceAPI;
import net.ccbluex.liquidbounce.cape.ServiceList;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\fR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/cape/CapeAPI;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "capeService", "Lnet/ccbluex/liquidbounce/cape/CapeService;", "hasCapeService", "", "loadCape", "Lnet/ccbluex/liquidbounce/cape/CapeInfo;", "uuid", "Ljava/util/UUID;", "registerCapeService", "", "KyinoClient"})
public final class CapeAPI
extends MinecraftInstance {
    private static CapeService capeService;
    public static final CapeAPI INSTANCE;

    /*
     * WARNING - void declaration
     */
    public final void registerCapeService() {
        String serviceType;
        JsonElement jsonElement = new JsonParser().parse(HttpUtils.get("https://cloud.liquidbounce.net/LiquidBounce/capes.json"));
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "JsonParser()\n           \u2026IENT_CLOUD}/capes.json\"))");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = jsonObject.get("serviceType");
        Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "jsonObject.get(\"serviceType\")");
        String string = serviceType = jsonElement2.getAsString();
        Intrinsics.checkExpressionValueIsNotNull(string, "serviceType");
        String string2 = string;
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
        switch (string4) {
            case "api": {
                String url;
                JsonElement jsonElement3 = jsonObject.get("api");
                Intrinsics.checkExpressionValueIsNotNull(jsonElement3, "jsonObject.get(\"api\")");
                JsonElement jsonElement4 = jsonElement3.getAsJsonObject().get("url");
                Intrinsics.checkExpressionValueIsNotNull(jsonElement4, "jsonObject.get(\"api\").asJsonObject.get(\"url\")");
                String string5 = url = jsonElement4.getAsString();
                Intrinsics.checkExpressionValueIsNotNull(string5, "url");
                capeService = new ServiceAPI(string5);
                ClientUtils.getLogger().info("Registered " + url + " as '" + serviceType + "' service type.");
                break;
            }
            case "list": {
                HashMap users = new HashMap();
                JsonElement jsonElement5 = jsonObject.get("users");
                Intrinsics.checkExpressionValueIsNotNull(jsonElement5, "jsonObject.get(\"users\")");
                Iterator iterator2 = jsonElement5.getAsJsonObject().entrySet().iterator();
                while (iterator2.hasNext()) {
                    void key;
                    Map.Entry entry;
                    Map.Entry entry2 = entry = (Map.Entry)iterator2.next();
                    boolean bl2 = false;
                    String string6 = (String)entry2.getKey();
                    entry2 = entry;
                    bl2 = false;
                    JsonElement value = (JsonElement)entry2.getValue();
                    Map map = users;
                    void v10 = key;
                    Intrinsics.checkExpressionValueIsNotNull(v10, "key");
                    JsonElement jsonElement6 = value;
                    Intrinsics.checkExpressionValueIsNotNull(jsonElement6, "value");
                    String string7 = jsonElement6.getAsString();
                    Intrinsics.checkExpressionValueIsNotNull(string7, "value.asString");
                    map.put(v10, string7);
                    ClientUtils.getLogger().info("Loaded user cape for '" + (String)key + "'.");
                }
                capeService = new ServiceList(users);
                ClientUtils.getLogger().info("Registered '" + serviceType + "' service type.");
            }
        }
        ClientUtils.getLogger().info("Loaded.");
    }

    @Nullable
    public final CapeInfo loadCape(@NotNull UUID uuid) {
        String string;
        Intrinsics.checkParameterIsNotNull(uuid, "uuid");
        CapeService capeService = CapeAPI.capeService;
        if (capeService == null) {
            return null;
        }
        String string2 = capeService.getCape(uuid);
        if (string2 == null) {
            return null;
        }
        String url = string2;
        String string3 = "capes/%s.png";
        Object[] objectArray = new Object[]{uuid.toString()};
        boolean bl = false;
        String string4 = String.format(string3, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkExpressionValueIsNotNull(string4, "java.lang.String.format(this, *args)");
        String string5 = string = string4;
        ResourceLocation resourceLocation = new ResourceLocation(string5);
        CapeInfo capeInfo = new CapeInfo(resourceLocation, false, 2, null);
        ThreadDownloadImageData threadDownloadImageData2 = new ThreadDownloadImageData(null, url, null, new IImageBuffer(capeInfo){
            final /* synthetic */ CapeInfo $capeInfo;

            @NotNull
            public BufferedImage func_78432_a(@NotNull BufferedImage image2) {
                Intrinsics.checkParameterIsNotNull(image2, "image");
                return image2;
            }

            public void func_152634_a() {
                this.$capeInfo.setCapeAvailable(true);
            }
            {
                this.$capeInfo = $captured_local_variable$0;
            }
        });
        Minecraft minecraft = CapeAPI.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_110434_K().func_110579_a(resourceLocation, (ITextureObject)threadDownloadImageData2);
        return capeInfo;
    }

    public final boolean hasCapeService() {
        return capeService != null;
    }

    private CapeAPI() {
    }

    static {
        CapeAPI capeAPI;
        INSTANCE = capeAPI = new CapeAPI();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

