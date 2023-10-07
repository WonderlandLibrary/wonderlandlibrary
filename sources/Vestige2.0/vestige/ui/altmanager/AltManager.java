package vestige.ui.altmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import vestige.Vestige;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.ui.altmanager.components.AltButton;
import vestige.ui.menu.VestigeMainMenu;
import vestige.util.render.ColorUtil;
import vestige.util.render.DrawUtil;
import vestige.util.render.GLSLSandboxShader;
import vestige.util.sound.AudioUtil;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class AltManager extends GuiScreen {

    public ArrayList<Alt> alts = new ArrayList<>();
    private final ArrayList<AltButton> altButtons = new ArrayList<>();

    private Alt selectedAlt;

    private GLSLSandboxShader backgroundShader;

    private String status;

    private boolean firstInit = true;

    private double scrollY = 0;

    public AltManager() {
        try {
            File dir = new File(Minecraft.getMinecraft().mcDataDir, "Vestige 2.0");
            File configDir = new File(dir, "alts");

            if(!dir.exists()) {
                dir.mkdir();
            }

            if(!configDir.exists()) {
                configDir.mkdir();
            }


            File dataFile = new File(configDir, "alts.list");

            if(!dataFile.exists()) return;

            FileInputStream fileIn = new FileInputStream(dataFile);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();
            alts = (ArrayList<Alt>) obj;

            objectIn.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui() {
    	if(Vestige.getInstance().isShaderEnabled()) {
    		try {
                GlStateManager.disableCull();
                this.backgroundShader = new GLSLSandboxShader("/background.fsh");
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}

        addButtons();

        if(firstInit) {
            status = "Logged in : " + mc.session.getUsername();
            firstInit = false;
        }
    }

    private void addButtons() {
        altButtons.clear();

        altButtons.add(new AltButton("Alt Login") {
            @Override
            public void onClicked() {
                mc.displayGuiScreen(new AltLogin());
            }
        });

        altButtons.add(new AltButton("Back") {
            @Override
            public void onClicked() {
                mc.displayGuiScreen(new VestigeMainMenu());
            }
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        MinecraftFontRenderer fr = FontUtil.product_sans;
        ScaledResolution sr = new ScaledResolution(mc);

        if(Vestige.getInstance().isShaderEnabled()) {
			GlStateManager.enableAlpha();

			this.backgroundShader.useShader(this.width, this.height, mouseX, mouseY, Vestige.getInstance().getShaderTimeElapsed() / 1000F);

			GL11.glBegin(GL11.GL_QUADS);

			GL11.glVertex2f(-1F, -1F);
			GL11.glVertex2f(-1F, 1F);
			GL11.glVertex2f(1F, 1F);
			GL11.glVertex2f(1F, -1F);

			GL11.glEnd();

			GL20.glUseProgram(0);
		} else {
			mc.getTextureManager().bindTexture(new ResourceLocation("vestige/background.jpg"));
			this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
		}

        fr.drawStringWithShadow(status, sr.getScaledWidth() / 2 - fr.getStringWidth(status) / 2, 50, -1);

        float offsetX = sr.getScaledWidth() / 2 - 70;
        float startY = sr.getScaledHeight() - 50;
        float endY = sr.getScaledHeight() - 25;
        float textY = sr.getScaledHeight() - 42;

        for(AltButton button : altButtons) {
            String name = button.getName();
            double increment = fr.getStringWidth(name) + 20;

            Gui.drawRect(offsetX, startY, offsetX + increment, endY, 0x80000000);
            fr.drawStringWithShadow(name, offsetX + 10, textY, -1);

            offsetX += increment + 20;
        }

        renderAlts(fr, sr);
    }

    private void renderAlts(MinecraftFontRenderer fr, ScaledResolution sr) {
        float offsetX = 50;
        float offsetY = 130;

        int i = 0;

        GlStateManager.translate(0, scrollY, 0);

        for(Alt alt : alts) {
            double startX = offsetX - 20;
            double endX = startX + 200;
            double startY = offsetY - 35;
            double endY = startY + 61;

            //int color = ColorUtil.vestigeColors(3F, 50);
            int color = new Color(16, 36, 163).getRGB();
            //int color = new Color(205, 205, 205).getRGB();

            float bordersSize = 2F;
            float radius = 3F;

            Gui.drawRect(startX, startY, endX, endY, 0x90000000);

            DrawUtil.drawRoundedRect(startX, startY, endX, startY + bordersSize, radius, color);
            DrawUtil.drawRoundedRect(startX, endY - bordersSize, endX, endY, radius, color);

            DrawUtil.drawRoundedRect(startX, startY, startX + bordersSize, endY, radius, color);
            DrawUtil.drawRoundedRect(endX - bordersSize, startY, endX, endY, radius, color);

            if(alt.getSession() != null) {
                fr.drawStringWithShadow(alt.getUsername(), offsetX + 45, offsetY - 25, -1);
                fr.drawStringWithShadow(alt.getEmail(), offsetX + 45, offsetY - 14, -1);

                try {
                    String uuid = alt.getSession().getProfile().getId().toString();
                    ThreadDownloadImageData ab = AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(uuid), uuid);
                    ab.loadTexture(Minecraft.getMinecraft().getResourceManager());
                    mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(uuid));

                    Gui.drawScaledCustomSizeModalRect((int) startX + 6, (int) startY + 6, 8F, 8F, 8, 8, 49, 49, 64F, 64F);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                fr.drawStringWithShadow(alt.getEmail(), offsetX + 45, offsetY - 25, -1);
            }

            Gui.drawRect(endX - 100, endY - 25, endX - 60, endY - 4, 0x99051599);
            fr.drawStringWithShadow("Login", endX - 92, (float) (endY - 18), -1);

            Gui.drawRect(endX - 55, endY - 25, endX - 4, endY - 4, 0x99051599);
            fr.drawStringWithShadow("Delete", endX - 45, (float) (endY - 18), -1);

            //offsetX += 235 * sr.getScaledWidth() / (1920 / 2);
            offsetX += 210;

            if(sr.getScaledWidth() < 480) {
                offsetX = 50;
                offsetY += 75;
            } else if(sr.getScaledWidth() < 700) {
                if(i % 2 == 1) {
                    offsetX = 50;
                    offsetY += 75;
                }
            } else if(sr.getScaledWidth() < 900) {
                if(i % 3 == 2) {
                    offsetX = 50;
                    offsetY += 75;
                }
            } else {
                if(i % 4 == 3) {
                    offsetX = 50;
                    offsetY += 75;
                }
            }

            i++;
        }

        GlStateManager.translate(0, -scrollY, 0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        MinecraftFontRenderer fr = FontUtil.product_sans;
        ScaledResolution sr = new ScaledResolution(mc);

        float offsetX = sr.getScaledWidth() / 2 - 70;
        float startY = sr.getScaledHeight() - 50;
        float endY = sr.getScaledHeight() - 25;
        float textY = sr.getScaledHeight() - 42;

        for(AltButton altButton : altButtons) {
            String name = altButton.getName();
            double increment = fr.getStringWidth(name) + 20;

            if(mouseX > offsetX && mouseX < offsetX + increment && mouseY > startY && mouseY < endY && button == 0) {
                altButton.onClicked();
                AudioUtil.buttonClick();
            }

            offsetX += increment + 20;
        }

        altsClickHandle(mouseX, mouseY, button);
    }

    public void altsClickHandle(int mouseX, int mouseY, int button) {
        ScaledResolution sr = new ScaledResolution(mc);

        MinecraftFontRenderer fr = FontUtil.product_sans;

        float offsetX = 50;
        float offsetY = (float) (130 + scrollY);

        int i = 0;

        int toDelete = -1;

        for(Alt alt : alts) {
            String username = alt.getUsername() != null ? alt.getUsername() : alt.getEmail();

            double startX = offsetX - 20;
            double endX = startX + 200;
            double startY = offsetY - 35;
            double endY = startY + 61;

            if(mouseX > endX - 100 && mouseX < endX - 60 && mouseY > endY - 25 && mouseY < endY - 4) {
                alt.login();
            } else if(mouseX > endX - 55 && mouseX < endX - 4 && mouseY > endY - 25 && mouseY < endY - 4) {
                toDelete = i;
            }
            
            offsetX += 210;

            if(sr.getScaledWidth() < 480) {
                offsetX = 50;
                offsetY += 75;
            } else if(sr.getScaledWidth() < 700) {
                if(i % 2 == 1) {
                    offsetX = 50;
                    offsetY += 75;
                }
            } else if(sr.getScaledWidth() < 900) {
                if(i % 3 == 2) {
                    offsetX = 50;
                    offsetY += 75;
                }
            } else {
                if(i % 4 == 3) {
                    offsetX = 50;
                    offsetY += 75;
                }
            }

            i++;
        }

        if(toDelete != -1) {
            alts.remove(toDelete);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Integer.signum(Mouse.getEventDWheel());

        scrollY += (10 * i);
        scrollY = Math.max(scrollY, 0);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
