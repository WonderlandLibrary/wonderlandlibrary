package best.azura.client.impl.module.impl.combat.copsncrims;

public enum CopsNCrimsWeapons {
	USP("USP", 1),
	HK45("HK45", 1),
	MP5("MP5", 3),
	M4("M4", 2),
	P90("P90", 3),
	PUMP_ACTION("Pump Action", 2),
	SPAS("SPAS-12", 2),
	DEAGLE("Desert Eagle", 3),
	AUG("Steyr AUG", 4),
	AWP("50 Cal", 1),
	AK_47("AK-47", 4);
	private final String weapon;
	private final int recoil;

	CopsNCrimsWeapons(String weapon, int recoil) {
		this.weapon = weapon;
		this.recoil = recoil;
	}

	public int getRecoil() {
		return recoil;
	}

	public String getWeapon() {
		return weapon;
	}
}