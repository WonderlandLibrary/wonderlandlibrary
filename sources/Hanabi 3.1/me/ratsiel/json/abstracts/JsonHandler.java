package me.ratsiel.json.abstracts;

public abstract class JsonHandler {
   public abstract Object serialize(JsonValue var1);

   public abstract JsonValue deserialize(Object var1);
}
