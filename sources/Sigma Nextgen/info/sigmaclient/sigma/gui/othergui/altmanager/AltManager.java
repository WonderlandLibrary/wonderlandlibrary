package info.sigmaclient.sigma.gui.othergui.altmanager;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.JelloTextField;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.alts.Alt;
import info.sigmaclient.sigma.config.alts.AltConfig;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.alt.HCMLJsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.UUID;

import static info.sigmaclient.sigma.gui.mainmenu.SigmaGuiMainMenu.animatedMouseX;
import static info.sigmaclient.sigma.gui.mainmenu.SigmaGuiMainMenu.animatedMouseY;
import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static info.sigmaclient.sigma.utils.render.RenderUtils.drawRect;
import static info.sigmaclient.sigma.utils.render.RenderUtils.drawTexture;
public class AltManager extends Screen {
    JelloTextField nameInput, password;
    Screen parent;
    HCMLJsonParser parser = new HCMLJsonParser();
    int showTime = 0, scroll = 0;
    String showString = "";
    public AltManager(Screen parent){
        super(new StringTextComponent("AltManager"));
        this.parent = parent;
    }
    @Override
    public void initGui() {
        nameInput = new JelloTextField(0, mc.fontRenderer, 5, 130, 180, 17, "Username...");
        password = new JelloTextField(0, mc.fontRenderer, 5, 155, 180, 17, "Password...");
        addListener(nameInput);
        addListener(password);
        super.initGui();
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigmang/images/jelloblur.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float m1 = sr.getScaledWidth() / 960f * 3840;
        float h1 = sr.getScaledHeight() / 501f * 1080;
        float m = 1.0f;
        float addM = m1 / 2f - sr.getScaledWidth() / 2F;
        float addH = h1 / 2f - sr.getScaledHeight() / 2F;
        drawTexture(
                -addM + -(animatedMouseX / sr.getScaledWidth() - 0.5f) * 2 * addM,
                -addH + -(animatedMouseY / sr.getScaledHeight() - 0.5f) * 2 * addH,
                0, 0,
                m1 * m,
                h1 * m,
                m1 * m,
                h1 * m
        );
        drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0xFCF2F1F2, true).getRGB());



        drawRect(0, 0, 200, sr.getScaledHeight(), new Color(100, 100, 100, 50).getRGB());
        drawRect(0, 0, 201, sr.getScaledHeight(), new Color(100, 100, 100, 40).getRGB());
        drawRect(0, 0, 204, sr.getScaledHeight(), new Color(60, 60, 60, 40).getRGB());
        FontUtil.sfuiFont18.drawString("Back", 7, 8, -1);
//        RenderUtils.drawRoundedRect(5, 30, 120, 45, 3, new Color(0, 0, 0, 100).getRGB());
        FontUtil.sfuiFont18.drawString("§6Random Alt", 7, 33, -1);
        FontUtil.sfuiFont18.drawString("§aCurrent account:", 7, 53, -1);
        FontUtil.sfuiFont18.drawString("Name: §b" + mc.session.getUsername(), 7, 67, -1);

//        drawRect(5, 90, 150, 110, new Color(0, 0, 0, 100).getRGB());

        drawRect(6, 92, 14, 99, new Color(255, 0, 0, 255).getRGB());
        drawRect(6, 100, 14, 108, new Color(71, 180, 30, 255).getRGB());
        drawRect(15, 92, 22, 99, new Color(60, 115, 255, 255).getRGB());
        drawRect(15, 100, 22, 108, new Color(255, 202, 36, 255).getRGB());
        FontUtil.sfuiFont18.drawString("Login with §cMicrosoft", 25, 97, -1);
        FontUtil.sfuiFont18.drawString("Copy from HCML to login.", 5, 115, -1);



        FontUtil.sfuiFont18.drawString("User Info:", 210, 5, -1);
        FontUtil.sfuiFont18.drawString("Token: " + (mc.session.getToken().length() > 30 ? ((mc.session.getToken().substring(0, 30) + "...")) : "offline"), 210, 15, -1);
        FontUtil.sfuiFont18.drawString("UUID " + mc.session.getPlayerID(), 210, 25, -1);
        FontUtil.sfuiFont18.drawString("Usename: " + mc.session.getUsername(), 210, 35, -1);
        int o = 10 + scroll;
        for(Alt alt : AltConfig.Instance.alts){
//            RenderUtils.drawRoundedRectWithGlow(215, 40 + o, width - 10, 40 + o + 30, 2,7, new Color(0, 0, 0, 0).getRGB());
            RenderUtils.drawRoundedRect(215, 40 + o, width - 10, 50 + o + 30, 2, new Color(200, 200, 200, 230).getRGB());

            FontUtil.sfuiFont18.drawString(alt.name,215 + 3, 40 + o + 3, new Color(0x000000));
            FontUtil.sfuiFont18.drawString(alt.offline ? "\u00a7coffline" : "\u00a7aonline",215 + 3, 40 + o + 3 + 8 + 2, new Color(0x000000));
            FontUtil.sfuiFont18.drawString("\u00a7cDelete",width - 40, 40 + o + 3 + 8 + 2 - 1, new Color(0x000000));
            FontUtil.sfuiFont18.drawString((System.currentTimeMillis() - alt.time) / (1000 * 60 * 60) + " hours ago",215 + 3, 40 + o + 3 + 8 + 2 + 8, new Color(0x000000));
            o += 50;
        }
        drawRect(5, 180, 60, 180 + 15, new Color(52, 176, 206, 255).getRGB());
        FontUtil.sfuiFont18.drawString("Login", 15, 184, -1);
        if(showTime > 0){
            float mwidth = 130;
            float mheight = 30;
            float perc = (showTime > 20 ? (showTime > 40 ? (60 - showTime) / 20f : 1f) : (showTime / 20f));
            showTime -= 1;
            float perc2 = (showTime > 20 ? (showTime > 40 ? (60 - showTime) / 20f : 1f) : (showTime / 20f));
            showTime += 1;
            float smooth = perc2 + (perc - perc2) * mc.timer.renderPartialTicks;
            drawRect(width / 2f - mwidth / 2, (height / 2f - mheight / 2) * smooth, width / 2f + mwidth / 2f, (height / 2f + mheight / 2f) * smooth, new Color(0, 0, 0, 123).getRGB());
            FontUtil.sfuiFont18.drawString(showString, (int) (width / 2f - FontUtil.sfuiFont18.getStringWidth(showString) / 2), (int) (height / 2f) * smooth, -1);
        }
        animatedMouseX = mouseX;
        animatedMouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if(showTime > 0) return false;
        return super.charTyped(codePoint, modifiers);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void tick()
    {
        if(showTime > 0){
            showTime --;
        }
    }
    public void clear(){
        password.setText("");
        nameInput.setText("");
    }
    public void alert(String str){
        showTime = 20;
        showString = str;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(delta != 0){
            scroll += delta > 0 ? 60 : -60;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(showTime > 0) return false;
        int o = 10 + scroll;
        for(Alt alt : AltConfig.Instance.alts){
            if(ClickUtils.isClickable(215, 40 + o, width - 10, 50 + o + 30, mouseX, mouseY)){
                if(ClickUtils.isClickable(width - 50, 40 + o + 3 + 8 + 2, width - 5, 40 + o + 3 + 8 + 2 + 8, mouseX, mouseY)){
                    AltConfig.Instance.alts.remove(alt);
                    SigmaNG.getSigmaNG().configManager.saveDefaultConfig();
                    return false;
                }
                alert("Login " + alt.name);
                if(alt.offline){
                    mc.session = new Session(alt.name, alt.uuid, "0", "Legacy");
                }else{
                    mc.session = new Session(alt.name, alt.uuid, alt.token, "Legacy");
                }
                return false;
            }
            o += 50;
        }
        if(ClickUtils.isClickable(10, 180, 22 + 100, 180 + 15, mouseX, mouseY)){
            if(!nameInput.getText().isEmpty()){
                if(password.getText().isEmpty()){
                    mc.session = new Session(nameInput.getText(), UUID.randomUUID().toString(), "0", "Legacy");
                    AltConfig.Instance.alts.add(new Alt(mc.session.getUsername(), mc.session.getPlayerID()));
                    SigmaNG.getSigmaNG().configManager.saveDefaultConfig();
                    return false;
                }else{
                    alert("we unsupported this...");
                    return false;
                }
            }
            return false;
        }
        if(ClickUtils.isClickable(5, 5, 80, 20, mouseX, mouseY)){
            mc.displayGuiScreen(parent);
        }
        if(ClickUtils.isClickable(5, 30, 120, 45, mouseX, mouseY)){
            mc.session = new Session(Dictionary.autoGet(), UUID.randomUUID().toString(), "0", "Legacy");
            AltConfig.Instance.alts.add(new Alt(mc.session.getUsername(), mc.session.getPlayerID()));
            SigmaNG.getSigmaNG().configManager.saveDefaultConfig();
        }
        if(ClickUtils.isClickable(5, 90, 150, 110, mouseX,mouseY)){
            //https://pan.nyaproxy.xyz/9X4VtHo6yN/AmpleStraw64396.txt
            parser.parserCheck();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
