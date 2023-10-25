package com.github.creeper123123321.viafabric.platform;

import java.lang.reflect.Method;
import java.util.Arrays;
import us.myles.ViaVersion.api.platform.ViaInjector;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonObject;

public class VRInjector implements ViaInjector {
  public void inject() {}
  
  public void uninject() {}
  
  public int getServerProtocolVersion() throws NoSuchFieldException, IllegalAccessException {
    return getClientProtocol();
  }
  
  private int getClientProtocol() throws NoSuchFieldException, IllegalAccessException {
    return 47;
  }
  
  public String getEncoderName() {
    return "via-encoder";
  }
  
  public String getDecoderName() {
    return "via-decoder";
  }
  
  public JsonObject getDump() {
    JsonObject obj = new JsonObject();
    try {
      obj.add("serverNetworkIOChInit", GsonUtil.getGson().toJsonTree(
            Arrays.<Method>stream(Class.forName("net.minecraft.class_3242$1").getDeclaredMethods())
            .map(Method::toString)
            .toArray(x$0 -> new String[x$0])));
    } catch (ClassNotFoundException classNotFoundException) {}
    try {
      obj.add("clientConnectionChInit", GsonUtil.getGson().toJsonTree(
            Arrays.<Method>stream(Class.forName("net.minecraft.class_2535$1").getDeclaredMethods())
            .map(Method::toString)
            .toArray(x$0 -> new String[x$0])));
    } catch (ClassNotFoundException classNotFoundException) {}
    return obj;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\platform\VRInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */