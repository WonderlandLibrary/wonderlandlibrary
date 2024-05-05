package artemis;

public enum Category {
  PLAYER, RENDER, COMBAT, MOVEMENT, OTHER, EXPLOIT, GUI;
	
	public static int size(Category cat) {
		
		int i = 0;
		
		for(module m : Artemis.getModules()) {
			if(m.getCategory().equals(cat)) {
				i++;
			}
		}
		return i;
	}
	
	public static int placeInListRender(module m) {
		
		int i = 0;
		
		for(module mod : Artemis.getModules()) {
			if(mod.getCategory().equals(RENDER) && !mod.equals(m)) {
				i++;
				continue;
			}
			if(mod.getCategory().equals(RENDER) && mod.equals(m)) {
				return i;
			}
		}
		return 0;
	}
	
public static int placeInListCombat(module m) {
		
		int i = 1;
		
		for(module mod : Artemis.getModules()) {
			if(mod.getCategory().equals(COMBAT) && !mod.equals(m)) {
				i++;
				continue;
			}
			if(mod.getCategory().equals(COMBAT) && mod.equals(m)) {
				return i;
			}
		}
		return 0;
	}
public static int placeInListMovement(module m) {
	
	int i = 1;
	
	for(module mod : Artemis.getModules()) {
		if(mod.getCategory().equals(MOVEMENT) && !mod.equals(m)) {
			i++;
			continue;
		}
		if(mod.getCategory().equals(MOVEMENT) && mod.equals(m)) {
			return i;
		}
	}
	return 0;
}

public static int placeInListOther(module m) {
	
	int i = 1;
	
	for(module mod : Artemis.getModules()) {
		if(mod.getCategory().equals(OTHER) && !mod.equals(m)) {
			i++;
			continue;
		}
		if(mod.getCategory().equals(OTHER) && mod.equals(m)) {
			return i;
		}
	}
	return 0;
}
	
}

