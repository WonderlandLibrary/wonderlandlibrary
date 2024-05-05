package me.Womas.Light.Module;

import me.Womas.Light.Interfaces.IBindable;
import me.Womas.Light.Interfaces.IToggleable;

public class Template implements IToggleable,IBindable {

    private String name;
    private int bind;
    private Category category;
    private boolean isToggled;


    public Template(String name, int bind, Category category) {
        this.name = name;
        this.bind = bind;
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isToggled() { return isToggled; }

    @Override
    public void onToggle(){}

    @Override
    public void onEnable(){}

    @Override
    public void onDisable(){}


    @Override
    public void setToggled(boolean toggle) {
        onToggle();
        this.isToggled = toggle;

        if(toggle){
            onEnable();
        }else{
            onDisable();
        }

    }

    @Override
    public void toggle() {this.setToggled(!isToggled);}


    public void onUpdate(){}
    public void onRender(){}

    @Override
    public void onKeyDown(int keycode) {
        if(this.bind == keycode)
            this.toggle();
    }
}

