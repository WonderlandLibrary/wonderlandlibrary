package space.clowdy.utils;

import java.awt.Color;

public class ColorUtils {
     public static int useless = -1;
     public static Color clientColor = new Color(-16711721, true);

     public static Color rainbow(int cycle) {
          double double2 = Math.ceil((double)(System.currentTimeMillis() + (long)cycle) / 20.0D);
          double2 %= 360.0D;
          return Color.getHSBColor((float)(double2 / 360.0D), 0.5F, 1.0F);
     }

     public static int _dying(int integer1, int integer2, float float3) {
          float float4 = 1.0F - float3;
          int integer5 = (int)((float)(integer1 >> 16 & 255) * float4 + (float)(integer2 >> 16 & 255) * float3);
          int integer6 = (int)((float)(integer1 >> 8 & 255) * float4 + (float)(integer2 >> 8 & 255) * float3);
          int integer7 = (int)((float)(integer1 & 255) * float4 + (float)(integer2 & 255) * float3);
          int integer8 = (int)((float)(integer1 >> 24 & 255) * float4 + (float)(integer2 >> 24 & 255) * float3);
          return (integer8 & 255) << 24 | (integer5 & 255) << 16 | (integer6 & 255) << 8 | integer7 & 255;
     }
}
