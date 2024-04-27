package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.ui.client.hud.designer.GuiHudDesigner;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Print;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.FadeState;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Type;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.aquavit.liquidsense.utils.module.Particles.roundToPlace;

@ElementInfo(name = "Print")
public class Prints extends Element {

    private final Print exampleNotification = new Print("Example Print", 0.0F, Type.info);

    public Prints() {
        super(0, 30, 1f, new Side(Side.Horizontal.LEFT, Side.Vertical.UP));
    }

    public Stream<Print> print;
    private HashMap<EntityLivingBase, Float> healthMap = new HashMap<EntityLivingBase, Float>();

    @Override
    public Border drawElement() {
        print = LiquidSense.hud.getPrints().stream();
        int index = 0;
        for(Print print : print.collect(Collectors.toList())) {
            if (!print.removeing) {
                print.index = index;
                print.translate.translate(0f, (LiquidSense.hud.getPrints().size() * 11) - (index * 11), 1.5);
            }
            print.y = print.translate.getY();
            print.drawPrint();
            if(print.fadeState == FadeState.END) {
                LiquidSense.hud.removePrint(print);
                index--;
            }
            index++;
        }
        if (mc.currentScreen instanceof GuiHudDesigner) {
            if (!LiquidSense.getHud().getPrints().contains(exampleNotification)) {
                LiquidSense.hud.addPrint(exampleNotification);
            }
            exampleNotification.fadeState = FadeState.STAY;
            exampleNotification.x = ((float) this.exampleNotification.textLength + 8.0F);
            return new Border(-exampleNotification.x + 12 + exampleNotification.textLength, 0, -exampleNotification.x - 35, 20 + 11F * LiquidSense.getHud().getNotifications().size());
        }
        return new Border(0f , 0f , 0f , 0f);
    }

    @Override
    public void livingupdateElement(){
        Aura a = (Aura) LiquidSense.moduleManager.getModule(Aura.class);
        EntityLivingBase entity = a.getTarget();
        if (entity != null && entity != mc.thePlayer) {
            if (!this.healthMap.containsKey(entity)) {
                this.healthMap.put(entity, entity.getHealth());
            }
            float floatValue = this.healthMap.get(entity);
            float health = entity.getHealth();
            String name = entity.getName();
            if (floatValue != health) {
                int remaining = floatValue - (floatValue - health) < 0 ? 0 : (int) Math.floor(floatValue - (floatValue - health));
                String text = "Hurt " + name + " for " + roundToPlace(floatValue - health, 1) + " hp " + "(" + remaining + " remaining).";
                LiquidSense.hud.addPrint(new Print(text,3000, Type.success));
                this.healthMap.remove(entity);
                this.healthMap.put(entity, entity.getHealth());
            }
        }
    }
}

