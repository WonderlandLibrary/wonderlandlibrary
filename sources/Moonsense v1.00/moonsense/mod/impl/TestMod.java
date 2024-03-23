package moonsense.mod.impl;

import moonsense.mod.Category;
import moonsense.mod.Mod;

public class TestMod extends Mod {

	public TestMod() {
		super("Toggle Sprint", "TestMod", Category.MISC);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		System.out.println("[Developer Log] TestMod Turned On");

	}

}
