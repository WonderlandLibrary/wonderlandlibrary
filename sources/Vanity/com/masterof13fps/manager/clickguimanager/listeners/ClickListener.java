package com.masterof13fps.manager.clickguimanager.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.manager.clickguimanager.components.GuiButton;


/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at 11.11.2020. 
 *         Use is only authorized if given credit!
 * 
 */
public class ClickListener implements ActionListener {

	private GuiButton button;

	public ClickListener(GuiButton button) {
		this.button = button;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Module m = Client.main().modMgr().getByName(button.getText());
		m.toggle();
	}
}
