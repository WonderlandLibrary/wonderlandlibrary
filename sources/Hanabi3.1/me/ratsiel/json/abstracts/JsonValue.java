package me.ratsiel.json.abstracts;

public abstract class JsonValue {
   private int intend = 0;
   private String key;

   public JsonValue() {
   }

   public JsonValue(String key) {
      this.key = key;
   }

   protected String createSpace() {
      StringBuilder spaceBuilder = new StringBuilder();

      for(int i = 0; i < this.intend; ++i) {
         spaceBuilder.append(" ");
      }

      return spaceBuilder.toString();
   }

   public abstract String toString();

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public int getIntend() {
      return this.intend;
   }

   public void setIntend(int intend) {
      this.intend = intend;
   }
}
