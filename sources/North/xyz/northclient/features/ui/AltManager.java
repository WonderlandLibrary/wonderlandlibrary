package xyz.northclient.features.ui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Session;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import xyz.northclient.UIHook;
import xyz.northclient.config.Config;
import xyz.northclient.util.MicroshitLogin;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.GLSL;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AltManager extends GuiScreen {




    @Override
    public void initGui() {
        super.initGui();

        MicrosoftLogin.getRefreshToken(refreshToken -> {
            MicrosoftLogin.LoginData loginData = loginWithRefreshToken(refreshToken);
            Alt alt = new Alt(AltType.MICROSOFT,loginData.username,loginData.uuid,loginData.mcToken,loginData.newRefreshToken);
            addAlt(alt);
            login(alt);
        });
    }

    private MicrosoftLogin.LoginData loginWithRefreshToken(String refreshToken) {
        final MicrosoftLogin.LoginData loginData = MicrosoftLogin.login(refreshToken);
        mc.session = new Session(loginData.username, loginData.uuid, loginData.mcToken, "microsoft");
        return loginData;
    }

    public enum AltType {
        CRACK, MICROSOFT, MICROSOFTUUID;
    }

    public static class Alt {
        public Alt(AltType type, String username, String uuid, String accessID, String refreshID) {
            this.type = type;
            this.username = username;
            this.uuid = uuid;
            this.accessID = accessID;
            this.refreshID = refreshID;
        }

        public Alt() {

        }

        public AltType type;
        public String username;
        public String uuid;
        public String accessID;
        public String refreshID;
        public boolean logged = false;
        public boolean lastLoginSuccess = true;
    }



    public static ConcurrentLinkedQueue<Alt> alts = new ConcurrentLinkedQueue<Alt>();


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GLSL.COLORS.useShader(mc.displayWidth,mc.displayHeight,mouseX,mouseY, (System.currentTimeMillis()- UIHook.SHADER_TIME)/1000f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0,0);
        GL11.glVertex2f(0,mc.displayHeight);
        GL11.glVertex2f(mc.displayWidth,mc.displayHeight);
        GL11.glVertex2f(mc.displayWidth,0);
        GL11.glEnd();
        GL20.glUseProgram(0);

        int yOffset = 50;
        int xOffset = 50;
        int howManyCanPutInOneLine = (new ScaledResolution(mc).getScaledWidth()+105) / 105;
        int counter = 0;
        for(Alt alt : alts) {
            Color c = new Color(255,255,255,50);
            if(alt.logged) {
                c = alt.lastLoginSuccess ? new Color(0,255,0,50) : new Color(255,0,0,50);
            }
            RectUtil.drawRoundedRect(xOffset,yOffset,100,40,6,c);
            FontUtil.Default.drawString(alt.username,xOffset+5,yOffset+5,new Color(255,255,255,120));
            FontUtil.DefaultSmall.drawString(alt.type.name().substring(0,1).toUpperCase() + alt.type.name().toLowerCase().substring(1),xOffset+5,yOffset+18,new Color(255,255,255,90));
            FontUtil.DefaultSmall.drawString("Delete",xOffset+5,yOffset+28,new Color(255,0,0,120));
            counter++;
            if(counter+2 < howManyCanPutInOneLine) {
                xOffset+=105;
            } else {
                xOffset = 50;
                yOffset+=50;
                counter = 0;
            }
        }


        RectUtil.drawRoundedRect(50,new ScaledResolution(mc).getScaledHeight()-50,70,20,5,new Color(255,255,255,50));
        FontUtil.DefaultSmall.drawString("Add Account",50 + (70/2) - (FontUtil.DefaultSmall.getStringWidth("Add Account")/2),new ScaledResolution(mc).getScaledHeight()-50+7,-1);
    }

    public static void load() {
        if(new File("./North/alts.json").exists()) {
            try {
                JsonObject obj = new Gson().fromJson(FileUtils.readFileToString(new File("./North/alts.json"),"UTF-8"), JsonObject.class);

                for(JsonElement elcamo : obj.getAsJsonArray("alts")) {
                    JsonObject obje = (JsonObject) elcamo;

                    Alt alt = new Alt();
                    alt.username = obje.get("username").getAsString();
                    alt.type = AltType.valueOf(obje.get("type").getAsString());

                    if(obje.get("type").getAsString().equalsIgnoreCase("MICROSOFTUUID")) {
                        alt.uuid = obje.get("uuid").getAsString();
                        alt.accessID = obje.get("access").getAsString();
                    }

                    if(obje.get("type").getAsString().equalsIgnoreCase("MICROSOFT")) {
                        alt.refreshID = obje.get("refresh").getAsString();
                    }
                    alts.add(alt);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        JsonObject obj = new JsonObject();

        JsonArray alts = new JsonArray();
        for(Alt alt : AltManager.alts) {
            JsonObject v = new JsonObject();
            v.addProperty("username",alt.username);
            v.addProperty("type", alt.type.name());
            if(alt.type == AltType.MICROSOFTUUID) {
                v.addProperty("uuid",alt.uuid);
                v.addProperty("access",alt.accessID);
            } else if(alt.type == AltType.MICROSOFT) {
                v.addProperty("refresh",alt.refreshID);
            }
            alts.add(v);
        }
        obj.add("alts",alts);

        String con = new Gson().toJson(obj);
        try {
            if(new File("./North/alts.json").exists()) {
                new File("./North/alts.json").delete();
            }
            new File("./North/alts.json").createNewFile();
            FileUtils.writeStringToFile(new File("./North/alts.json"),con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAlt(Alt alt) {
        alts.add(alt);
        save();
        System.out.println("saved");
    }

    public boolean login(Alt alt) {
        if(alt.type == AltType.CRACK) {
            this.mc.session = new Session(alt.username, "", "", "mojang");
            return true;
        }

        if(alt.type ==  AltType.MICROSOFTUUID) {
            //this.mc.session = new Session(alt.username,alt.uuid.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"),alt.accessID,"microsoft");
            try {
                System.out.println(alt.username);
                System.out.println(alt.uuid);
                System.out.println(alt.accessID);
                this.mc.getSessionService().joinServer(new GameProfile(UUID.fromString(alt.uuid), alt.username), alt.accessID, alt.uuid);
                this.mc.session = new Session(alt.username,alt.uuid,alt.accessID,"mojang");
            }catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if(alt.type == AltType.MICROSOFT) {
            loginWithRefreshToken(alt.refreshID);
            return true;
        }

        return false;
    }



    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int yOffset = 50;
        int xOffset = 50;
        int howManyCanPutInOneLine = (new ScaledResolution(mc).getScaledWidth()+105) / 105;
        int counter = 0;
        for(Alt alt : alts) {
            RectUtil.drawRoundedRect(xOffset,yOffset,100,40,6,new Color(255,255,255,50));

            if(mouseX > xOffset+5 && mouseY > yOffset+28 && mouseX < xOffset+5+FontUtil.DefaultSmall.getStringWidth("Delete") && mouseY < yOffset+28+FontUtil.DefaultSmall.getHeight()) {
                alts.remove(alt);
                save();
            } else if(mouseX > xOffset && mouseY > yOffset && mouseX < xOffset+100 && mouseY < yOffset+40) {
                for(Alt a : alts) {
                    a.logged = false;
                }
                alt.logged = true;
                alt.lastLoginSuccess = login(alt);
            }

            counter++;
            if(counter+2 < howManyCanPutInOneLine) {
                xOffset+=105;
            } else {
                xOffset = 50;
                yOffset+=50;
                counter = 0;
            }
        }

        if(mouseX >= 50 && mouseY >= new ScaledResolution(mc).getScaledHeight()-50 && mouseX <= 50+70 && mouseY <= new ScaledResolution(mc).getScaledHeight()-50+20) {
            mc.displayGuiScreen(new AltManagerLogin());
        }
    }
}
