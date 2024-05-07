package com.masterof13fps.manager.clickguimanager.listeners;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.manager.clickguimanager.components.*;
import com.masterof13fps.manager.settingsmanager.Setting;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author sendQueue <Vinii>
 * <p>
 * Further info at Vinii.de or github@vinii.de, file created at
 * 11.11.2020. Use is only authorized if given credit!
 */
public class ComponentsListener extends ComponentListener {
    private GuiButton button;

    public ComponentsListener(GuiButton button) {
        this.button = button;
    }

    @Override
    public void addComponents() {
        add(new GuiLabel("Settings >"));
        final Module m = Client.main().modMgr().getByName(button.getText());

        for (Setting set : Client.main().setMgr().getSettings()) {
            if (set.getParentMod() == m) {
                switch (set.getSettingType()) {
                    case BOOLEAN:
                        GuiToggleButton toggleButton = new GuiToggleButton(set.getName());
                        toggleButton.setToggled(set.isToggled());
                        toggleButton.addClickListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                set.setBool(toggleButton.isToggled());
                            }
                        });
                        add(toggleButton);
                        break;
                    case VALUE:
                        GuiSlider slider = new GuiSlider(set.getName(), (float) set.min(), (float) set.max(), (float) set.getCurrentValue(),
                                set.onlyInt() ? 0 : 2);
                        slider.addValueListener(new ValueListener() {

                            @Override
                            public void valueUpdated(float value) {
                                set.setNum(value);
                            }

                            @Override
                            public void valueChanged(float value) {
                                set.setNum(value);
                            }
                        });
                        add(slider);
                        break;
                    case COMBO:
                        GuiComboBox comboBox = new GuiComboBox(set);
                        comboBox.addComboListener(new ComboListener() {

                            @Override
                            public void comboChanged(String combo) {
                            }
                        });
                        add(comboBox);
                        break;
                    default:
                        break;
                }

            }
        }
        GuiGetKey ggk = new GuiGetKey("KeyBind", m.bind());
        ggk.addKeyListener(new KeyListener() {

            @Override
            public void keyChanged(int key) {
                if (!(key == 27) && !(key == 14)) {
                    m.setBind(0);
                } else {
                    m.setBind(key);
                }
            }
        });
        add(ggk);
    }

}
