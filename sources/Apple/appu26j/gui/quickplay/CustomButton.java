package appu26j.gui.quickplay;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import appu26j.Apple;
import appu26j.Cache;
import appu26j.fontrenderer.SizedFontRenderer;
import appu26j.interfaces.MinecraftInterface;
import appu26j.utils.SoundUtil;
import net.minecraft.client.gui.ScaledResolution;
import quexii.RoundedUtil;

public class CustomButton implements MinecraftInterface
{
    private final String text;
    private final float x, y;
    
    public CustomButton(String text, float x, float y)
    {
        this.text = text;
        this.x = x;
        this.y = y;
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        RoundedUtil.drawRoundedRect(this.x, this.y, this.x + 80, this.y + 20, 2, (this.isInsideBox(mouseX, mouseY, this.x, this.y, this.x + 80, this.y + 20) ? new Color(25, 25, 40, 200) : new Color(25, 25, 40, 175)).getRGB());
        float temp = 96 / (this.text.length() == 0 ? 1 : this.text.length());
        temp = temp > 8 ? 8 : temp;
        SizedFontRenderer.drawStringWithShadow(this.text, (this.x + 40) - (SizedFontRenderer.getStringWidth(this.text, temp) / 2), this.y + temp / (temp / 8) - (temp / 3.5F) + 1, temp, -1);
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (this.isInsideBox(mouseX, mouseY, this.x, this.y, this.x + 80, this.y + 20) && mouseButton == 0)
        {
            SoundUtil.playClickSound();
            
            switch (this.text)
            {
                case "BedWars":
                {
                    ArrayList<CustomButton> customButtons = new ArrayList<>();
                    ScaledResolution scaledResolution = Cache.getSR();
                    float f = scaledResolution.getScaledWidth() / 2;
                    float g = scaledResolution.getScaledHeight() / 2;
                    float temp = 4 * 41.75F;
                    f -= temp;
                    customButtons.add(new CustomButton("BedWars Trios", f + 170, g - 10));
                    customButtons.add(new CustomButton("BedWars Solo", f, g - 10));
                    customButtons.add(new CustomButton("BedWars Duos", f + 85, g - 10));
                    customButtons.add(new CustomButton("BedWars Quads", f + 255, g - 10));
                    Apple.CLIENT.getQuickPlayGUI().customButtons.clear();
                    Apple.CLIENT.getQuickPlayGUI().customButtons.addAll(customButtons);
                    break;
                }
                
                case "SkyWars":
                {
                    ArrayList<CustomButton> customButtons = new ArrayList<>();
                    ScaledResolution scaledResolution = Cache.getSR();
                    float f = scaledResolution.getScaledWidth() / 2;
                    float g = scaledResolution.getScaledHeight() / 2;
                    float temp = 4 * 41.75F;
                    f -= temp;
                    customButtons.add(new CustomButton("SkyWars Duos Normal", f + 170, g - 10));
                    customButtons.add(new CustomButton("SkyWars Solo Normal", f, g - 10));
                    customButtons.add(new CustomButton("SkyWars Solo Insane", f + 85, g - 10));
                    customButtons.add(new CustomButton("SkyWars Duos Insane", f + 255, g - 10));
                    Apple.CLIENT.getQuickPlayGUI().customButtons.clear();
                    Apple.CLIENT.getQuickPlayGUI().customButtons.addAll(customButtons);
                    break;
                }
                
                case "TNT Games":
                {
                    ArrayList<CustomButton> customButtons = new ArrayList<>();
                    ScaledResolution scaledResolution = Cache.getSR();
                    float f = scaledResolution.getScaledWidth() / 2;
                    float g = scaledResolution.getScaledHeight() / 2;
                    float temp = 4 * 41.75F;
                    f -= temp;
                    customButtons.add(new CustomButton("TNT Run", f + 170, g - 10));
                    customButtons.add(new CustomButton("PvP Run", f, g - 10));
                    customButtons.add(new CustomButton("Bow Spleef", f + 85, g - 10));
                    customButtons.add(new CustomButton("TNT Tag", f + 255, g - 10));
                    Apple.CLIENT.getQuickPlayGUI().customButtons.clear();
                    Apple.CLIENT.getQuickPlayGUI().customButtons.addAll(customButtons);
                    break;
                }

                case "Murder Mystery":
                {
                    ArrayList<CustomButton> customButtons = new ArrayList<>();
                    ScaledResolution scaledResolution = Cache.getSR();
                    float f = scaledResolution.getScaledWidth() / 2;
                    float g = scaledResolution.getScaledHeight() / 2;
                    float temp = 4 * 41.75F;
                    f -= temp;
                    customButtons.add(new CustomButton("Classic", f + 170, g - 10));
                    customButtons.add(new CustomButton("Double Up", f, g - 10));
                    customButtons.add(new CustomButton("Assassins", f + 85, g - 10));
                    customButtons.add(new CustomButton("Infection", f + 255, g - 10));
                    Apple.CLIENT.getQuickPlayGUI().customButtons.clear();
                    Apple.CLIENT.getQuickPlayGUI().customButtons.addAll(customButtons);
                    break;
                }
                
                case "Duels":
                {
                    ArrayList<CustomButton> customButtons = new ArrayList<>();
                    ScaledResolution scaledResolution = Cache.getSR();
                    float f = scaledResolution.getScaledWidth() / 2;
                    float g = scaledResolution.getScaledHeight() / 2;
                    float temp = 5 * 41.75F;
                    f -= temp;
                    customButtons.add(new CustomButton("Duels Classic", f + 170, g - 10));
                    customButtons.add(new CustomButton("No Debuff", f, g - 10));
                    customButtons.add(new CustomButton("Combo", f + 85, g - 10));
                    customButtons.add(new CustomButton("UHC Deathmatch", f + 255, g - 10));
                    customButtons.add(new CustomButton("Sumo", f + 340, g - 10));
                    Apple.CLIENT.getQuickPlayGUI().customButtons.clear();
                    Apple.CLIENT.getQuickPlayGUI().customButtons.addAll(customButtons);
                    break;
                }
                
                case "UHC":
                {
                    ArrayList<CustomButton> customButtons = new ArrayList<>();
                    ScaledResolution scaledResolution = Cache.getSR();
                    float f = scaledResolution.getScaledWidth() / 2;
                    float g = scaledResolution.getScaledHeight() / 2;
                    float temp = 2 * 41.75F;
                    f -= temp;
                    customButtons.add(new CustomButton("UHC Solo", f, g - 10));
                    customButtons.add(new CustomButton("UHC Teams", f + 85, g - 10));
                    Apple.CLIENT.getQuickPlayGUI().customButtons.clear();
                    Apple.CLIENT.getQuickPlayGUI().customButtons.addAll(customButtons);
                    break;
                }
                
                case "The Bridge":
                {
                    ArrayList<CustomButton> customButtons = new ArrayList<>();
                    ScaledResolution scaledResolution = Cache.getSR();
                    float f = scaledResolution.getScaledWidth() / 2;
                    float g = scaledResolution.getScaledHeight() / 2;
                    float temp = 4 * 41.75F;
                    f -= temp;
                    customButtons.add(new CustomButton("Bridge Solo", f, g - 10));
                    customButtons.add(new CustomButton("Bridge Duos", f + 85, g - 10));
                    customButtons.add(new CustomButton("Bridge Trios", f + 170, g - 10));
                    customButtons.add(new CustomButton("Bridge Quads", f + 255, g - 10));
                    Apple.CLIENT.getQuickPlayGUI().customButtons.clear();
                    Apple.CLIENT.getQuickPlayGUI().customButtons.addAll(customButtons);
                    break;
                }
                
                case "BedWars Solo":
                {
                    this.cmd("bedwars_eight_one");
                    break;
                }
                
                case "BedWars Duos":
                {
                    this.cmd("bedwars_eight_two");
                    break;
                }

                case "BedWars Trios":
                {
                    this.cmd("bedwars_four_three");
                    break;
                }

                case "BedWars Quads":
                {
                    this.cmd("bedwars_four_four");
                    break;
                }
                
                case "SkyWars Solo Normal":
                {
                    this.cmd("solo_normal");
                    break;
                }

                case "SkyWars Solo Insane":
                {
                    this.cmd("solo_insane");
                    break;
                }

                case "SkyWars Duos Normal":
                {
                    this.cmd("teams_normal");
                    break;
                }

                case "SkyWars Duos Insane":
                {
                    this.cmd("teams_insane");
                    break;
                }
                
                case "PvP Run":
                {
                    this.cmd("tnt_pvprun");
                    break;
                }
                
                case "Bow Spleef":
                {
                    this.cmd("tnt_bowspleef");
                    break;
                }
                
                case "TNT Run":
                {
                    this.cmd("tnt_tntrun");
                    break;
                }

                case "TNT Tag":
                {
                    this.cmd("tnt_tntag");
                    break;
                }

                case "Classic":
                {
                    this.cmd("murder_classic");
                    break;
                }
                
                case "Double Up":
                {
                    this.cmd("murder_double_up");
                    break;
                }
                
                case "Assassins":
                {
                    this.cmd("murder_assassins");
                    break;
                }
                
                case "Infection":
                {
                    this.cmd("murder_infection");
                    break;
                }
                
                case "Duels Classic":
                {
                    this.cmd("duels_classic_duel");
                    break;
                }
                
                case "No Debuff":
                {
                    this.cmd("duels_potion_duel");
                    break;
                }

                case "Combo":
                {
                    this.cmd("duels_combo_duel");
                    break;
                }
                
                case "UHC Deathmatch":
                {
                    this.cmd("duels_uhc_meetup");
                    break;
                }
                
                case "Sumo":
                {
                    this.cmd("duels_sumo_duel");
                    break;
                }
                
                case "The Pit":
                {
                    this.cmd("pit");
                    break;
                }
                
                case "Dropper":
                {
                    this.cmd("prototype_dropper");
                    break;
                }
                
                case "WoolWars":
                {
                    this.cmd("wool_wool_wars_two_four");
                    break;
                }
                
                case "UHC Solo":
                {
                    this.cmd("uhc_solo");
                    break;
                }
                
                case "UHC Teams":
                {
                    this.cmd("uhc_teams");
                    break;
                }
                
                case "Bridge Solo":
                {
                    this.cmd("duels_bridge_duel");
                    break;
                }
                
                case "Bridge Duos":
                {
                    this.cmd("duels_bridge_doubles");
                    break;
                }
                
                case "Bridge Trios":
                {
                    this.cmd("duels_bridge_threes");
                    break;
                }
                
                case "Bridge Quads":
                {
                    this.cmd("duels_bridge_four");
                    break;
                }
            }
        }
    }
    
    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected boolean keyTyped(char typedChar, int keyCode) throws IOException
    {
        switch (this.text)
        {
            case "BedWars":
            {
                return true;
            }
            
            case "SkyWars":
            {
                return true;
            }

            case "TNT Games":
            {
                return true;
            }

            case "Murder Mystery":
            {
                return true;
            }
            
            case "Duels":
            {
                return true;
            }
            
            case "The Pit":
            {
                return true;
            }
            
            case "Dropper":
            {
                return true;
            }
            
            case "WoolWars":
            {
                return true;
            }
            
            case "UHC":
            {
                return true;
            }
            
            case "The Bridge":
            {
                return true;
            }
            
            default:
            {
                Apple.CLIENT.getQuickPlayGUI().customButtons.clear();
                Apple.CLIENT.getQuickPlayGUI().customButtons.addAll(Apple.CLIENT.getQuickPlayGUI().originalButtons);
                return false;
            }
        }
    }
    
    public void cmd(String command)
    {
        this.mc.thePlayer.sendChatMessage("/play " + command);
        this.mc.displayGuiScreen(null);
    }
    
    public boolean isInsideBox(int mouseX, int mouseY, float x, float y, float width, float height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
}
