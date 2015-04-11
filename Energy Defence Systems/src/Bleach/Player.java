package Bleach;

public class Player extends EntityLiving {

	public Player(Sprite sprite, double x, double y) {
		super(sprite, x, y, 8, // radius
		80, // health
		1, // attack power
		120 // speed
		);
	}

	@Override
	void AI(LevelInteractable activeLevel) {
		// this could be used for some debuffs like 'confusion', 'fear' etc.
	}

	@Override
	double dealDamage() {
		return attackPower;
	}

	@Override
	double takeDamage(double amount) {
		health = Math.max(0, health - amount);
		return health;
	}
}
