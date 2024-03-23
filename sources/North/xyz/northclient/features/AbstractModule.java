package xyz.northclient.features;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import xyz.northclient.InstanceAccess;
import xyz.northclient.NorthSingleton;
import xyz.northclient.draggable.AbstractDraggable;
import xyz.northclient.draggable.impl.Arraylist;
import xyz.northclient.features.modules.Animations;
import xyz.northclient.features.values.Mode;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.util.animations.Animation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractModule implements InstanceAccess {
    @Getter @Setter
    private String name = "", displayName = "", description = "";
    @Getter @Setter
    private int keyCode;
    @Getter @Setter
    private boolean enabled;
    @Getter @Setter
    private Category category;

    public static Minecraft mc = Minecraft.getMinecraft();

    @Getter @Setter
    private Animation x = new Animation(),y = new Animation();


    @Getter
    private List<Value> values = new ArrayList<>();

    public AbstractModule(boolean draggable) {

    }

    public AbstractModule() {
        if (this.getClass().isAnnotationPresent(ModuleInfo.class)) {
            ModuleInfo L = this.getClass().getAnnotation(ModuleInfo.class);

            this.name = L.name();
            this.description = L.description();
            this.category = L.category();
            this.displayName = L.name();
            this.keyCode = L.keyCode();
            if (L.enable()) {
                toggle();
            }
        } else {
            throw new RuntimeException("Error");
        }
    }

    public void setSuffix(String suffix) {
        this.displayName = this.name + EnumChatFormatting.GRAY + " " + suffix;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void toggle() {
       // if(Arraylist.mode != null) {
       //     if(Arraylist.mode.is("Astolfo") || Arraylist.mode.is("Astolfo Shadow")) {
       //         NorthSingleton.INSTANCE.getModules().toggle("Arraylist");
       //     }
      //  }
        this.setEnabled(!this.isEnabled());


        if (this.isEnabled()) {
            this.onEnable();
            north.getEventBus().register(this);
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("tile.piston.in"), 5.0F));
            for(Value val : values) {
                if(val instanceof ModeValue) {
                    Mode mode = ((ModeValue) val).getOptions().get(((ModeValue) val).getSelected());
                    north.getEventBus().register(mode);
                }
            }
        } else {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("tile.piston.out"), 5.0F));
            this.onDisable();
            north.getEventBus().unregister(this);
            for(Value val : values) {
                if(val instanceof ModeValue) {
                    Mode mode = ((ModeValue) val).getOptions().get(((ModeValue) val).getSelected());
                    north.getEventBus().unregister(mode);
                }
            }
        }
    }

    public void reflectValues() {
        for (Field field : getClass().getDeclaredFields()) {
            try {
                if (field.get(this) instanceof Value) {
                    if (!field.isAccessible())
                        field.setAccessible(true);

                    Value set = (Value) field.get(this);
                    values.add(set);
                }
            } catch (Exception _) {
            }
        }
    }

}
