package Bleach;

public abstract class EntityLiving extends Entity {

	protected double health; // Current HP
	protected double healthMax; // Maximum HP
	protected double attackPower; // How much damage the entity deals when it
									// attacks.
	protected Inventory inventory;

	protected EntityLiving(Sprite sprite, double x, double y, double r, double health, double attackPower, double velocity) {
		super(sprite, x, y, r);
		this.health = this.healthMax = health;
		this.attackPower = attackPower;
		this.getForce().setVelocity(velocity);
		inventory = new Inventory();
		mass = 5;
	}

	public double getDamage() {
		return attackPower;
	}

	public double getHealth() {
		return health;
	}

	public double getHealthMax() {
		return healthMax;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void tick(LevelInteractable activeLevel) {
		super.tick(activeLevel);

		AI(activeLevel);
		timePreviousTick = System.currentTimeMillis();
	}

	abstract void AI(LevelInteractable activeLevel);

	abstract double dealDamage();;

	abstract double takeDamage(double amount); // Returns health after damage.
}
