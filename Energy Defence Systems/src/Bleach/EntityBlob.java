package Bleach;

public class EntityBlob extends EntityLiving {

	public EntityBlob(Sprite sprite, double x, double y) {
		super(sprite, x, y, 8, 5, 2, 50); // radius: 11, hp: 5, attackdamage:
											// 2, speed: 50
	}

	@Override
	void AI(LevelInteractable activeLevel) {

		// BS AI
		if (System.currentTimeMillis() % 1000 == 0) {
			bMoving = !bMoving;
			if (bMoving)
				getForce().setVectorAngle((Math.random()) * (2 * Math.PI));
		}
		if (System.currentTimeMillis() % 100 == 0) {
			((Level) activeLevel).addProjectile(new ProjectileBullet(x, y, getForce().getVectorAngle(), this));
		}
		// end BS AI
	}

	@Override
	double dealDamage() {
		// modifiers?
		return attackPower;
	}

	@Override
	double takeDamage(double amount) {
		health = Math.max(0, health - amount);
		// animation? sound?
		return health;
	}
}
