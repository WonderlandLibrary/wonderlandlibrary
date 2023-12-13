package team.massacre.api.manager;

import com.google.gson.JsonObject;

public interface Serializable {
   JsonObject save();

   void load(JsonObject var1);
}
