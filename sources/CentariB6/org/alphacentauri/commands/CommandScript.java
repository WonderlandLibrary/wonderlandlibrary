package org.alphacentauri.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.scripts.ScriptEngine;
import org.alphacentauri.scripts.ScriptInstance;

public class CommandScript implements ICommandHandler {
   public String getName() {
      return "Script";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         AC.addChat(this.getName(), "Usage: " + cmd.getCommand() + " <<absolute_path_to_a_alpha_script>/<instances>/<stop> <instance_id>>");
      } else if(args[0].equalsIgnoreCase("instances")) {
         AC.addChat(this.getName(), "Instances:");
         Set<Entry<Thread, ScriptInstance>> entries = ScriptEngine.instances.entrySet();
         int i = 0;

         for(Entry<Thread, ScriptInstance> entry : entries) {
            AC.addChat(this.getName(), "Instance " + i + ": " + ((ScriptInstance)entry.getValue()).path);
            ++i;
         }
      } else if(args[0].equalsIgnoreCase("stop")) {
         if(args.length != 2) {
            AC.addChat(this.getName(), "Usage: script stop <instance_id>");
            return true;
         }

         Set<Entry<Thread, ScriptInstance>> entries = ScriptEngine.instances.entrySet();
         int i = 0;

         try {
            int n = Integer.parseInt(args[1]);

            for(Entry<Thread, ScriptInstance> entry : entries) {
               if(i == n) {
                  AC.addChat(this.getName(), ((ScriptInstance)entry.getValue()).path + " has been stopped!");
                  ((ScriptInstance)entry.getValue()).abort();
                  ((Thread)entry.getKey()).interrupt();
                  ScriptEngine.instances.remove(entry.getKey());
                  break;
               }

               ++i;
            }
         } catch (NumberFormatException var23) {
            AC.addChat(this.getName(), "Instance ID must be an Integer!");
         }
      } else {
         StringBuilder pathBuilder = new StringBuilder();

         for(String arg : args) {
            pathBuilder.append(arg).append(' ');
         }

         pathBuilder.deleteCharAt(pathBuilder.length() - 1);
         File script = new File(pathBuilder.toString());
         if(!script.exists()) {
            (new Thread(() -> {
               AC.addChat(this.getName(), "File not found! Trying to download online script...");

               try {
                  URL url = new URL("https://alpha-centauri.tk/launcher/scripts/" + var28.getName());
                  HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
                  con.setDoOutput(false);
                  con.setDoInput(true);
                  con.setRequestProperty("User-Agent", "Alpha Centauri " + AC.getVersion());
                  con.setRequestMethod("GET");
                  InputStream in = con.getInputStream();
                  Throwable var6 = null;

                  try {
                     StringBuilder scriptBuilder = new StringBuilder();
                     byte[] buffer = new byte[1024];

                     for(int c = in.read(buffer); c > 0; c = in.read(buffer)) {
                        for(int i = 0; i < c; ++i) {
                           scriptBuilder.append((char)buffer[i]);
                        }
                     }

                     AC.addChat(this.getName(), "Online Script launched");
                     ScriptEngine.execute(scriptBuilder.toString(), pathBuilder.toString());
                  } catch (Throwable var19) {
                     var6 = var19;
                     throw var19;
                  } finally {
                     if(in != null) {
                        if(var6 != null) {
                           try {
                              in.close();
                           } catch (Throwable var18) {
                              var6.addSuppressed(var18);
                           }
                        } else {
                           in.close();
                        }
                     }

                  }
               } catch (Exception var21) {
                  AC.addChat(this.getName(), "File not found!");
               }

            })).start();
         } else {
            try {
               FileInputStream fin = new FileInputStream(script);
               Throwable var34 = null;

               try {
                  StringBuilder scriptBuilder = new StringBuilder();
                  byte[] buffer = new byte[1024];

                  for(int c = fin.read(buffer); c != -1; c = fin.read(buffer)) {
                     for(int i = 0; i < c; ++i) {
                        scriptBuilder.append((char)buffer[i]);
                     }
                  }

                  ScriptEngine.execute(scriptBuilder.toString(), pathBuilder.toString());
               } catch (Throwable var20) {
                  var34 = var20;
                  throw var20;
               } finally {
                  if(fin != null) {
                     if(var34 != null) {
                        try {
                           fin.close();
                        } catch (Throwable var19) {
                           var34.addSuppressed(var19);
                        }
                     } else {
                        fin.close();
                     }
                  }

               }
            } catch (Exception var22) {
               AC.addChat(this.getName(), "Error: " + var22.getMessage());
            }
         }
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"script"};
   }

   public String getDesc() {
      return "Make custom scripts";
   }

   public ArrayList autocomplete(Command cmd) {
      return null;
   }
}
