package cn.hanabi.utils.fileSystem;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.altmanager.GuiAltManager;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.FriendManager;
import cn.hanabi.utils.TargetManager;
import cn.hanabi.value.Value;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import me.yarukon.hud.window.HudWindow;
import me.yarukon.hud.window.HudWindowManager;
import me.yarukon.palette.ColorValue;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

@ObfuscationClass
public class FileManager {
   public final String fileDir;

   public FileManager() {
      Minecraft mc = Minecraft.getMinecraft();
      this.fileDir = mc.mcDataDir.getAbsolutePath() + "/" + "Hanabi";
      File fileFolder = new File(this.fileDir);
      if (!fileFolder.exists()) {
         fileFolder.mkdirs();
      }

      File configDir = new File(this.fileDir + "/Config");
      if (!configDir.exists()) {
         configDir.mkdirs();
      }

   }

   public void saveConfig(String configName) {
      if (configName.contains(".")) {
         ClientUtil.sendClientMessage("Config name shouldn't contain <.>.", Notification.Type.INFO);
      } else {
         File settingsFile = new File(this.fileDir + "/Config/" + configName + ".hanabi");

         try {
            if (!settingsFile.exists()) {
               settingsFile.createNewFile();
            }

            PrintWriter e = new PrintWriter(settingsFile);
            Iterator var4 = Value.list.iterator();

            String valueName;
            while(var4.hasNext()) {
               Value value = (Value)var4.next();
               valueName = "v:" + value.getValueName();
               if (value.isValueBoolean) {
                  e.println(valueName + ":b:" + value.getValueState());
               } else if (value.isValueDouble) {
                  e.println(valueName + ":d:" + value.getValueState());
               } else if (value.isValueMode) {
                  e.println(valueName + ":s:" + value.getModeTitle() + ":" + value.getCurrentMode());
               }
            }

            var4 = ModManager.getModList().iterator();

            Mod m;
            while(var4.hasNext()) {
               m = (Mod)var4.next();
               e.println("m:" + m.getName() + ":" + m.isEnabled());
            }

            var4 = ModManager.getModList().iterator();

            while(var4.hasNext()) {
               m = (Mod)var4.next();
               valueName = m.getKeybind() < 0 ? "None" : Keyboard.getKeyName(m.getKeybind());
               e.println("b:" + m.getName() + ":" + valueName);
            }

            e.close();
         } catch (Exception var7) {
            ClientUtil.sendClientMessage("Fail to save config <" + configName + ">.", Notification.Type.INFO);
            var7.printStackTrace();
         }

         ClientUtil.sendClientMessage("Successfully to save config <" + configName + ">.", Notification.Type.INFO);
      }
   }

   public void loadConfig(String configName) {
      if (configName.contains(".")) {
         ClientUtil.sendClientMessage("Config name shouldn't contain <.>.", Notification.Type.INFO);
      } else {
         File settingsFile = new File(this.fileDir + "/Config/" + configName + ".hanabi");

         try {
            BufferedReader br = new BufferedReader(new FileReader(settingsFile));

            label107:
            while(true) {
               String[] split;
               Mod m;
               do {
                  do {
                     while(true) {
                        String line;
                        do {
                           if ((line = br.readLine()) == null) {
                              break label107;
                           }
                        } while(!line.contains(":"));

                        split = line.split(":");
                        if (split[0].equalsIgnoreCase("v")) {
                           m = ModManager.getModule(split[1].split("_")[0]);
                           break;
                        }

                        if (split[0].equalsIgnoreCase("m")) {
                           m = ModManager.getModule(split[1]);
                           boolean state = Boolean.parseBoolean(split[2]);
                           if (m != null && m.getCategory() != Category.RENDER) {
                              try {
                                 if (m.isEnabled() != state) {
                                    m.set(state, false);
                                 }
                              } catch (Exception var10) {
                                 var10.printStackTrace();
                              }
                           }
                        } else if (split[0].equalsIgnoreCase("b")) {
                           m = ModManager.getModule(split[1]);
                           int key = Keyboard.getKeyIndex(split[2]);
                           if (m != null && key != -1 && m.getCategory() != Category.RENDER) {
                              m.setKeybind(key);
                           }
                        }
                     }
                  } while(m == null);
               } while(m.getCategory() == Category.RENDER);

               Iterator var13 = Value.list.iterator();

               while(true) {
                  while(true) {
                     Value value;
                     do {
                        if (!var13.hasNext()) {
                           continue label107;
                        }

                        value = (Value)var13.next();
                     } while(!value.getValueName().equalsIgnoreCase(split[1]));

                     if (value.isValueBoolean && split[2].equalsIgnoreCase("b")) {
                        value.setValueState(Boolean.parseBoolean(split[3]));
                     } else if (value.isValueDouble && split[2].equalsIgnoreCase("d")) {
                        value.setValueState(Double.parseDouble(split[3]));
                     } else if (value.isValueMode && split[2].equalsIgnoreCase("s") && split[3].equalsIgnoreCase(value.getModeTitle())) {
                        value.setCurrentMode(Integer.parseInt(split[4]));
                     }
                  }
               }
            }
         } catch (Exception var11) {
            ClientUtil.sendClientMessage("Fail to load config <" + configName + ">.", Notification.Type.INFO);
            var11.printStackTrace();
         }

         try {
            Hanabi.INSTANCE.fileManager.save();
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         ClientUtil.sendClientMessage("Successfully to load config <" + configName + ">.", Notification.Type.INFO);
      }
   }

   public void save() throws Exception {
      File targetFile = new File(this.fileDir + "/target.txt");
      File friendFile = new File(this.fileDir + "/friend.txt");
      File keyFile = new File(this.fileDir + "/keys.txt");
      File moduleFile = new File(this.fileDir + "/mods.txt");
      File valueFile = new File(this.fileDir + "/values.txt");
      File gen = new File(this.fileDir + "/gen.txt");

      try {
         if (!targetFile.exists()) {
            targetFile.createNewFile();
         }

         PrintWriter targetPM = new PrintWriter(targetFile);
         if (TargetManager.getTarget() != null && TargetManager.getTarget().size() > 0) {
            for(int i = 0; i < TargetManager.getTarget().size(); ++i) {
               String name = (String)TargetManager.getTarget().get(i);
               targetPM.write(name);
            }
         }

         targetPM.close();
         if (!friendFile.exists()) {
            friendFile.createNewFile();
         }

         PrintWriter friendPW = new PrintWriter(friendFile);
         if (FriendManager.getFriends() != null && FriendManager.getFriends().size() > 0) {
            for(int i = 0; i < FriendManager.getFriends().size(); ++i) {
               String name = (String)FriendManager.getFriends().get(i);
               friendPW.write(name);
            }
         }

         friendPW.close();
         if (!keyFile.exists()) {
            keyFile.createNewFile();
         }

         PrintWriter keyPw = new PrintWriter(keyFile);
         Iterator var20 = ModManager.getModules().iterator();

         while(var20.hasNext()) {
            Mod m = (Mod)var20.next();
            String keyName = m.getKeybind() < 0 ? "None" : Keyboard.getKeyName(m.getKeybind());
            keyPw.write(m.getName() + ":" + keyName + "\n");
         }

         keyPw.close();
         if (!gen.exists()) {
            gen.createNewFile();
         }

         PrintWriter genPW = new PrintWriter(gen);
         if (GuiAltManager.Api != null) {
            genPW.write(GuiAltManager.Api);
         }

         genPW.close();
         if (!moduleFile.exists()) {
            moduleFile.createNewFile();
         }

         PrintWriter modulePw = new PrintWriter(moduleFile);
         Iterator var23 = ModManager.getModules().iterator();

         while(var23.hasNext()) {
            Mod m = (Mod)var23.next();
            modulePw.print(m.getName() + ":" + m.isEnabled() + "\n");
         }

         modulePw.close();
         if (!valueFile.exists()) {
            valueFile.createNewFile();
         }

         PrintWriter valuePw = new PrintWriter(valueFile);
         Iterator var25 = Value.list.iterator();

         while(var25.hasNext()) {
            Value value = (Value)var25.next();
            String valueName = value.getValueName();
            if (value.isValueBoolean) {
               valuePw.print(valueName + ":b:" + value.getValueState() + "\n");
            } else if (value.isValueDouble) {
               valuePw.print(valueName + ":d:" + value.getValueState() + "\n");
            } else if (value.isValueMode) {
               valuePw.print(valueName + ":s:" + value.getModeTitle() + ":" + value.getCurrentMode() + "\n");
            }
         }

         valuePw.close();
      } catch (Exception var16) {
         var16.printStackTrace();
      }

   }

   public void load() {
      File targetFile = new File(this.fileDir + "/target.txt");
      File friendFile = new File(this.fileDir + "/friend.txt");
      File keyFile = new File(this.fileDir + "/keys.txt");
      File moduleFile = new File(this.fileDir + "/mods.txt");
      File valueFile = new File(this.fileDir + "/values.txt");
      File gen = new File(this.fileDir + "/gen.txt");
      File window = new File(this.fileDir + "/windows.txt");
      File colors = new File(this.fileDir + "/colors.txt");

      try {
         if (!window.exists()) {
            window.createNewFile();
         } else {
            this.loadWindows();
         }

         if (!colors.exists()) {
            colors.createNewFile();
         } else {
            this.loadColors();
         }

         BufferedReader br;
         if (!targetFile.exists()) {
            targetFile.createNewFile();
         } else {
            br = new BufferedReader(new FileReader(targetFile));
            if (br.readLine() != null) {
               TargetManager.getTarget().add(br.readLine());
            }
         }

         if (!friendFile.exists()) {
            friendFile.createNewFile();
         } else {
            br = new BufferedReader(new FileReader(friendFile));
            if (br.readLine() != null) {
               FriendManager.getFriends().add(br.readLine());
            }
         }

         BufferedReader br;
         String[] split;
         Mod m;
         String line;
         if (!keyFile.exists()) {
            keyFile.createNewFile();
         } else {
            br = new BufferedReader(new FileReader(keyFile));

            while((line = br.readLine()) != null) {
               if (line.contains(":")) {
                  split = line.split(":");
                  m = ModManager.getModule(split[0]);
                  int key = Keyboard.getKeyIndex(split[1]);
                  if (m != null && key != -1) {
                     m.setKeybind(key);
                  }
               }
            }
         }

         if (!gen.exists()) {
            gen.createNewFile();
         } else {
            br = new BufferedReader(new FileReader(gen));
            line = br.readLine();
            if (GuiAltManager.Api == null) {
               GuiAltManager.Api = line;
            }
         }

         if (!moduleFile.exists()) {
            moduleFile.createNewFile();
         } else {
            br = new BufferedReader(new FileReader(moduleFile));

            label141:
            while(true) {
               boolean state;
               do {
                  do {
                     if ((line = br.readLine()) == null) {
                        break label141;
                     }
                  } while(!line.contains(":"));

                  split = line.split(":");
                  m = ModManager.getModule(split[0]);
                  state = Boolean.parseBoolean(split[1]);
               } while(m == null);

               try {
                  if (!m.getName().equals("Fly") && !m.getName().equals("Blink") && !m.getName().equals("Scaffold")) {
                     m.setState(state, false);
                  } else {
                     m.setState(false, false);
                  }
               } catch (Exception var15) {
                  var15.printStackTrace();
               }
            }
         }

         if (!valueFile.exists()) {
            valueFile.createNewFile();
         } else {
            br = new BufferedReader(new FileReader(valueFile));

            label119:
            while(true) {
               do {
                  if ((line = br.readLine()) == null) {
                     return;
                  }
               } while(!line.contains(":"));

               split = line.split(":");
               Iterator var18 = Value.list.iterator();

               while(true) {
                  while(true) {
                     Value value;
                     do {
                        if (!var18.hasNext()) {
                           continue label119;
                        }

                        value = (Value)var18.next();
                     } while(!split[0].equalsIgnoreCase(value.getValueName()));

                     if (value.isValueBoolean && split[1].equalsIgnoreCase("b")) {
                        value.setValueState(Boolean.parseBoolean(split[2]));
                     } else if (value.isValueDouble && split[1].equalsIgnoreCase("d")) {
                        value.setValueState(Double.parseDouble(split[2]));
                     } else if (value.isValueMode && split[1].equalsIgnoreCase("s") && split[2].equalsIgnoreCase(value.getModeTitle())) {
                        value.setCurrentMode(Integer.parseInt(split[3]));
                     }
                  }
               }
            }
         }
      } catch (Exception var16) {
         var16.printStackTrace();
      }

   }

   public void loadWindows() {
      try {
         FileReader fileReader = new FileReader(this.fileDir + "/windows.txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         String line;
         while((line = bufferedReader.readLine()) != null) {
            String[] splited = line.split(":");
            if (splited.length >= 3) {
               HudWindow w = HudWindowManager.getWindowByID(splited[0]);
               if (w != null) {
                  w.x = Float.parseFloat(splited[1]);
                  w.y = Float.parseFloat(splited[2]);
                  if (w.resizeable && splited.length == 5) {
                     w.width = Float.parseFloat(splited[3]);
                     w.height = Float.parseFloat(splited[4]);
                  }
               }
            }
         }

         bufferedReader.close();
      } catch (Exception var6) {
      }

   }

   public void saveWindows() {
      try {
         FileWriter fileWriter = new FileWriter(this.fileDir + "/windows.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         Iterator var3 = HudWindowManager.windows.iterator();

         while(var3.hasNext()) {
            HudWindow w = (HudWindow)var3.next();
            bufferedWriter.write(w.windowID + ":" + Math.floor((double)w.x) + ":" + Math.floor((double)w.y) + (w.resizeable ? ":" + Math.floor((double)w.width) + ":" + Math.floor((double)w.height) : ""));
            bufferedWriter.newLine();
         }

         bufferedWriter.close();
      } catch (Exception var5) {
      }

   }

   public void loadColors() {
      try {
         FileReader fileReader = new FileReader(this.fileDir + "/colors.txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         String line;
         while((line = bufferedReader.readLine()) != null) {
            String[] splited = line.split(":");
            if (splited.length == 8) {
               ColorValue cv = ColorValue.getColorValueByName(splited[0]);
               if (cv != null) {
                  cv.hue = Float.parseFloat(splited[1]);
                  cv.saturation = Float.parseFloat(splited[2]);
                  cv.brightness = Float.parseFloat(splited[3]);
                  cv.alpha = Float.parseFloat(splited[4]);
                  cv.hasAlpha = Boolean.parseBoolean(splited[5]);
                  cv.rainbow = Boolean.parseBoolean(splited[6]);
                  cv.rainbowSpeed = Float.parseFloat(splited[7]);
               }
            }
         }

         bufferedReader.close();
      } catch (Exception var6) {
      }

   }

   public void saveColors() {
      try {
         FileWriter fileWriter = new FileWriter(this.fileDir + "/colors.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         Iterator var3 = ColorValue.colorValues.iterator();

         while(var3.hasNext()) {
            ColorValue v = (ColorValue)var3.next();
            bufferedWriter.write(v.name + ":" + v.hue + ":" + v.saturation + ":" + v.brightness + ":" + v.alpha + ":" + v.hasAlpha + ":" + v.rainbow + ":" + v.rainbowSpeed);
            bufferedWriter.newLine();
         }

         bufferedWriter.close();
      } catch (Exception var5) {
      }

   }
}
