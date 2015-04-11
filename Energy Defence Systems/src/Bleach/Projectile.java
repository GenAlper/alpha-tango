package Bleach;

public abstract class Projectile extends Entity {

	protected EntityLiving owner = null; // Used for checking if the projectile
											// came from the player or enemies.
											// Makes it so that enemies don't
											// shoot each other and enables
											// "friendly fire" options for
											// players.

	protected Projectile(Sprite sprite, double x, double y, double r, double angle, EntityLiving owner) {
		super(sprite, x, y, r);
		this.owner = owner;
		this.getForce().setVectorAngle(angle);
		bMoving = true;
	}

	abstract double dealDamage();

	protected EntityLiving getOwner() {
		return owner;
	}
}
