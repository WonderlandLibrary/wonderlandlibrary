package com.google.gson;

import java.lang.reflect.Type;

public interface JsonSerializer {
   JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3);
}
