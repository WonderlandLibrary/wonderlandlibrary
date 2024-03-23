package xyz.northclient.features;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import xyz.northclient.InstanceAccess;
import xyz.northclient.NorthSingleton;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.Mode;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.util.animations.Animation;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DropdownClickGUI extends GuiScreen implements InstanceAccess {
    @Override
    public void initGui() {
        super.initGui();
    }

    public static HashMap<Category,Boolean> expanded = new HashMap<Category, Boolean>();
    public static HashMap<AbstractModule,Boolean> AbstractModuleExpanded = new HashMap<AbstractModule, Boolean>();

    public static Animation animation;

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        animation  = null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if(animation == null){
            animation = new Animation();
        }


        float begin = new ScaledResolution(mc).getScaledWidth()/2.0f;
        for(Category c : Category.values()) {
            begin-=125.0f/2.0f;
        }

        animation.update();
        animation.setFromValue(-50);
        animation.animate(50,0.2);

        GlStateManager.translate(0,(-50)+animation.getValue(),0);

        AbstractModule selected = null;

        int categoryOffset = (int) begin;
        for(Category c : Category.values()) {
            if(!expanded.containsKey(c)) { expanded.put(c,false); }

            int cHeight = 20;
            for(AbstractModule m : north.getModules().ByCategory(c)) {
                if(expanded.get(c)) {
                    int b = 0;
                    if(AbstractModuleExpanded.get(m)) {
                        for(Value s : m.getValues()) {
                            if(s.isVisible().get()) {
                                b+=16.5f;
                            }
                        }
                    }
                    cHeight+=23.5f+b;
                }
            }
            

            RectUtil.drawBloom(categoryOffset,30,120,cHeight,16,new Color(27,21,20));
            RectUtil.drawRoundedRect(categoryOffset,30,120,cHeight,cHeight == 20 ? 5 : 7 ,new Color(27,21,20));
            Gui.drawRect(0,0,0,0,0);

            FontUtil.DefaultSmall.drawStringWithShadow(c.name().toLowerCase().substring(0,1).toUpperCase() + c.name().toLowerCase().substring(1),categoryOffset+5,35.5f+1,-1);

            if(expanded.get(c)) {
                int AbstractModuleOffset = 50;
                for(AbstractModule m : north.getModules().ByCategory(c)) {
                    if(!AbstractModuleExpanded.containsKey(m)) { AbstractModuleExpanded.put(m,false); }

                    int mHeight = 20;
                    for(Value s : m.getValues()) { if(AbstractModuleExpanded.get(m)) { if(s.isVisible().get()) {mHeight+=16.5f;} } }

                    RectUtil.drawRoundedRect(categoryOffset+2,AbstractModuleOffset,120-4,mHeight,5,new Color(32,32,32));

                    RectUtil.drawBloom(categoryOffset+2,AbstractModuleOffset,120-4,20,16,!m.isEnabled() ? new Color(52,48,45) : NorthSingleton.INSTANCE.getUiHook().getTheme().getClickguiEnabledColor());

                    RectUtil.drawRoundedRect(categoryOffset+2,AbstractModuleOffset,120-4,20,5,!m.isEnabled() ? new Color(52,48,45) : NorthSingleton.INSTANCE.getUiHook().getTheme().getClickguiEnabledColor());
                    FontUtil.DefaultSmall.drawString(m.getName(),categoryOffset+2 + (116/2) - (FontUtil.DefaultSmall.getWidth(m.getName())/2),AbstractModuleOffset+5.5f+1,!m.isEnabled() ? -1 : NorthSingleton.INSTANCE.getUiHook().getTheme().getClickguiEnabledTextColor().getRGB());

                    if(mouseX > categoryOffset && mouseY > AbstractModuleOffset && mouseX < categoryOffset+120 && mouseY < AbstractModuleOffset+25) {
                        selected = m;
                    }

                    if(AbstractModuleExpanded.get(m)) {
                        int ValueOffset = AbstractModuleOffset+25;
                        for(Value s : m.getValues()) {
                            if(s.isVisible().get()) {
                                if(s instanceof BoolValue) {
                                    FontUtil.DefaultSmall.drawStringWithShadow(s.getName(),categoryOffset+5,ValueOffset+2,-1);

                                    RectUtil.drawRoundedRect(categoryOffset+85,ValueOffset-2,30,15,7,((BoolValue) s).get() ? NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiTheme() : new Color(48,48,48));
                                    if(((BoolValue) s).get()) {
                                        RectUtil.drawRoundedRect(categoryOffset+102,ValueOffset,11,11,5, NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiCircle());
                                    } else {
                                        RectUtil.drawRoundedRect(categoryOffset+87,ValueOffset,11,11,5,NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiCircle());
                                    }
                                }

                                if(s instanceof ModeValue) {
                                    RectUtil.drawRoundedRect(categoryOffset+1.5f,ValueOffset-2,115,15,5,new Color(48,48,48));
                                    FontUtil.DefaultSmall.drawStringWithShadow(s.getName(),categoryOffset+5,ValueOffset+2,-1);
                                    FontUtil.DefaultSmall.drawStringWithShadow(((ModeValue) s).getOptions().get(((ModeValue) s).getSelected()).getName(),categoryOffset+115 - FontUtil.DefaultSmall.getWidth(((ModeValue) s).getOptions().get(((ModeValue) s).getSelected()).getName()) -2,ValueOffset+2,-1);
                                }
                                if(s instanceof DoubleValue) {
                                    ((DoubleValue) s).slider.render(categoryOffset,ValueOffset);
                                    FontUtil.DefaultSmall.drawStringWithShadow(s.getName(),categoryOffset+5,ValueOffset+2,-1);
                                }
                                ValueOffset+=16.5f;
                            }
                        }
                    }

                    AbstractModuleOffset+=mHeight + 3.5f;
                }
            }

            categoryOffset+=121;
        }

        if(selected != null) {
            RectUtil.drawBloom(mouseX-5,mouseY, (int) (FontUtil.DefaultSmall.getWidth(selected.getDescription())+5),10,16,new Color(0,0,0,153));
            FontUtil.DefaultSmall.drawString(selected.getDescription(),mouseX,mouseY,-1);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);


        float begin = new ScaledResolution(mc).getScaledWidth()/2.0f;
        for(Category c : Category.values()) {
            begin-=125.0f/2.0f;
        }


        int categoryOffset = (int) begin;
        for(Category c : Category.values()) {
            if(!expanded.containsKey(c)) { expanded.put(c,false); }

            int cHeight = 20;
            for(AbstractModule m : north.getModules().ByCategory(c)) {
                if(expanded.get(c) && AbstractModuleExpanded.get(m)) {
                    int b = 0;
                    for(Value s : m.getValues()) {
                        b+=16.5f;
                    }
                    cHeight+=23.5f+b;
                }
            }


            if(mouseX > categoryOffset && mouseY > 30 && mouseX < categoryOffset+120 && mouseY < 50 && mouseButton == 1) {
                boolean var = expanded.get(c);
                expanded.remove(c);
                expanded.put(c,!var);
            }

            if(expanded.get(c)) {
                int AbstractModuleOffset = 50;
                for(AbstractModule m : north.getModules().ByCategory(c)) {
                    if(!AbstractModuleExpanded.containsKey(m)) { AbstractModuleExpanded.put(m,false); }
                    int mHeight = 20;
                    for(Value s : m.getValues()) { if(AbstractModuleExpanded.get(m)) { mHeight+=16.5f; } }


                    if(mouseX > categoryOffset+2 && mouseY > AbstractModuleOffset && mouseX < categoryOffset+2+120-4 && mouseY < AbstractModuleOffset+20 && mouseButton == 0) {
                        m.toggle();
                    }

                    if(mouseX > categoryOffset+2 && mouseY > AbstractModuleOffset && mouseX < categoryOffset+2+120-4 && mouseY < AbstractModuleOffset+20 && mouseButton == 1) {
                        boolean var = AbstractModuleExpanded.get(m);
                        AbstractModuleExpanded.remove(m);
                        AbstractModuleExpanded.put(m,!var);
                    }

                    if(AbstractModuleExpanded.get(m)) {
                        int ValueOffset = AbstractModuleOffset+25;
                        for(Value s : m.getValues()) {
                            if(s.isVisible().get()) {
                                if(s instanceof BoolValue) {
                                    if(mouseX > categoryOffset+83 && mouseY > ValueOffset-2 && mouseX < categoryOffset+83+30 && mouseY < ValueOffset-2+15 && mouseButton == 0) {
                                        ((BoolValue) s).set(!((BoolValue) s).get());
                                    }
                                }

                                if(s instanceof DoubleValue) {
                                    ((DoubleValue)s).slider.click(mouseX,mouseY);
                                }

                                if(s instanceof ModeValue) {
                                    RectUtil.drawRoundedRect(categoryOffset+1.5f,ValueOffset-2,115,15,5,new Color(48,48,48));
                                    FontUtil.DefaultSmall.drawStringWithShadow(s.getName(),categoryOffset+5,ValueOffset,-1);
                                    FontUtil.DefaultSmall.drawStringWithShadow(((ModeValue) s).getOptions().get(((ModeValue) s).getSelected()).getName(),categoryOffset+115 - FontUtil.DefaultSmall.getWidth(((ModeValue) s).getOptions().get(((ModeValue) s).getSelected()).getName()) -2,ValueOffset,-1);

                                    if(mouseX > categoryOffset && mouseY > ValueOffset && mouseX < categoryOffset+120 && mouseY < ValueOffset+15) {
                                        try {
                                            if(mouseButton == 0) {
                                                List<Mode> modes = ((ModeValue)s).getOptions();
                                                int current = modes.indexOf(((ModeValue) s).getOptions().get(((ModeValue) s).getSelected()));
                                                if(current == modes.size()-1) {
                                                    current = -1;
                                                }
                                                current++;
                                                ((ModeValue) s).set(modes.get(current));
                                            } else if(mouseButton == 1) {
                                                List<Mode> modes = ((ModeValue)s).getOptions();
                                                int current = modes.indexOf(((ModeValue) s).getOptions().get(((ModeValue) s).getSelected()));
                                                if(current == modes.size()-1) {
                                                    current = -1;
                                                }
                                                current--;
                                                ((ModeValue) s).set(modes.get(current));
                                            }
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                ValueOffset+=16.5f;
                            }
                        }
                    }

                    AbstractModuleOffset+=mHeight + 3.5f;
                }
            }

            categoryOffset+=121;
        }
























    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
