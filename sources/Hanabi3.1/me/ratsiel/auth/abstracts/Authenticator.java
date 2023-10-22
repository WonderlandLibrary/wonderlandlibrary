package me.ratsiel.auth.abstracts;

import me.ratsiel.json.Json;

public abstract class Authenticator {
   protected final Json json = new Json();

   public abstract Object login(String var1, String var2);
}
