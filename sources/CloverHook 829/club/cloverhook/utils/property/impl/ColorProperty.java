package club.cloverhook.utils.property.impl;

import java.awt.*;
import java.util.Map;

import club.cloverhook.Cloverhook;
import club.cloverhook.event.cloverhook.UpdateValueEvent;
import club.cloverhook.utils.property.abs.Property;

/**
 * @author antja03
 */
public class ColorProperty extends Property<Color> {

    private float hue;
    private float saturation;
    private float brightness;
    private int alpha;

    public ColorProperty(String id, String description, club.cloverhook.utils.Dependency dependency, float hue, float saturation, float brightness, int alpha) {
        super(id, description, dependency);
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.alpha = alpha;
        Color hsb = Color.getHSBColor(hue, saturation, brightness);
        this.defaultValue = new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
        this.value = defaultValue;
    }

    @Override
    public void setValue(String input) {
        String hString = input.split(":")[0];
        String sString = input.split(":")[1];
        String bString = input.split(":")[2];
        String aString = input.split(":")[3];

        try {
            hue = Float.parseFloat(hString);
            saturation = Float.parseFloat(sString);
            brightness = Float.parseFloat(bString);
            alpha = Integer.parseInt(aString);

            if (hue >= 0.0f && hue <= 1.0f && saturation >= 0.0f && saturation <= 1.0f && brightness >= 0.0f && brightness <= 1.0f && alpha >= 0 && alpha <= 255) {
                Color hsb = Color.getHSBColor(hue, saturation, brightness);
                Color converted = new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
                
                Cloverhook.eventBus.publish(new UpdateValueEvent(this, getValue(), converted));
                
                value = converted;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getValueAsString() {
        return hue + ":" + saturation + ":" + brightness + ":" + alpha;
    }


    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
    	if (hue < 0.0f || hue > 1.0f)
    		return;
    	
        this.hue = hue;

        Color hsb = Color.getHSBColor(hue, saturation, brightness);
        Color converted = new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
      
        Cloverhook.eventBus.publish(new UpdateValueEvent(this, getValue(), converted));
        
        value = converted;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
    	if (saturation < 0.0f || saturation > 1.0f)
    		return;
    	
        this.saturation = saturation;

        Color hsb = Color.getHSBColor(hue, saturation, brightness);
        Color converted = new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
        
        Cloverhook.eventBus.publish(new UpdateValueEvent(this, getValue(), converted));
        
        value = converted;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
    	if (brightness < 0.0f || brightness > 1.0f)
    		return;
    	
        this.brightness = brightness;

        Color hsb = Color.getHSBColor(hue, saturation, brightness);
        Color converted = new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
      
        Cloverhook.eventBus.publish(new UpdateValueEvent(this, getValue(), converted));
        
        value = converted;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
    	if (alpha < 0 || alpha > 255)
    		return;
    	
        this.alpha = alpha;
        
        Color hsb = Color.getHSBColor(hue, saturation, brightness);
        Color converted = new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
      
        Cloverhook.eventBus.publish(new UpdateValueEvent(this, getValue(), converted));
        
        value = converted;
    }
}
