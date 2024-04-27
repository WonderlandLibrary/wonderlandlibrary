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
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IThreadDownloadImageData;
import net.ccbluex.liquidbounce.api.minecraft.client.render.WIImageBuffer;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.cape.CapeInfo;
import net.ccbluex.liquidbounce.cape.CapeService;
import net.ccbluex.liquidbounce.cape.ServiceAPI;
import net.ccbluex.liquidbounce.cape.ServiceList;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\bÆ\u000020B\b¢J0J0\b2\t0\nJ0\fR0X¢\n\u0000¨\r"}, d2={"Lnet/ccbluex/liquidbounce/cape/CapeAPI;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "capeService", "Lnet/ccbluex/liquidbounce/cape/CapeService;", "hasCapeService", "", "loadCape", "Lnet/ccbluex/liquidbounce/cape/CapeInfo;", "uuid", "Ljava/util/UUID;", "registerCapeService", "", "Pride"})
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
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "JsonParser()\n           …IENT_CLOUD}/capes.json\"))");
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
                Iterator iterator = jsonElement5.getAsJsonObject().entrySet().iterator();
                while (iterator.hasNext()) {
                    void key;
                    Map.Entry entry;
                    Map.Entry entry2 = entry = (Map.Entry)iterator.next();
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
                break;
            }
        }
        ClientUtils.getLogger().info("Loaded.");
    }

    @Nullable
    public final CapeInfo loadCape(@NotNull UUID uuid) {
        Intrinsics.checkParameterIsNotNull(uuid, "uuid");
        CapeService capeService = CapeAPI.capeService;
        if (capeService == null) {
            return null;
        }
        String string = capeService.getCape(uuid);
        if (string == null) {
            return null;
        }
        String url = string;
        String string2 = "capes/%s.png";
        Object[] objectArray = new Object[]{uuid.toString()};
        IClassProvider iClassProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
        boolean bl = false;
        String string3 = String.format(string2, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkExpressionValueIsNotNull(string3, "java.lang.String.format(this, *args)");
        String string4 = string3;
        IResourceLocation resourceLocation = iClassProvider.createResourceLocation(string4);
        CapeInfo capeInfo = new CapeInfo(resourceLocation, false, 2, null);
        IThreadDownloadImageData threadDownloadImageData2 = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createThreadDownloadImageData(null, url, null, new WIImageBuffer(capeInfo){
            final CapeInfo $capeInfo;

            @Nullable
            public BufferedImage parseUserSkin(@Nullable BufferedImage image) {
                return image;
            }

            public void skinAvailable() {
                this.$capeInfo.setCapeAvailable(true);
            }
            {
                this.$capeInfo = $captured_local_variable$0;
            }
        });
        MinecraftInstance.mc.getTextureManager().loadTexture(resourceLocation, threadDownloadImageData2);
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
}
