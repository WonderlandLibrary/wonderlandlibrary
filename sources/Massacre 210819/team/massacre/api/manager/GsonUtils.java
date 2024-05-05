package team.massacre.api.manager;

import com.google.gson.Gson;
import java.io.File;
import org.apache.commons.io.FileUtils;

public final class GsonUtils {
   private static final Gson GSON = new Gson();

   private GsonUtils() {
   }

   public static boolean write(File file, Object src) {
      try {
         FileUtils.write(file, GSON.toJson(src));
         return true;
      } catch (Exception var3) {
         return false;
      }
   }
}
