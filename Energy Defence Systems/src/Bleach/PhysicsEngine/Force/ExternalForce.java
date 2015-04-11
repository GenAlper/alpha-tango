package Bleach.PhysicsEngine.Force;

import Bleach.PhysicsEngine.CollisionEngine.CollisionListener;

public class ExternalForce {

	public static enum ForceIdentifier {
		GRAVITY, WIND, JUMP;
	}

	private Force force;
	private CollisionListener collisionListener = null;

	private boolean isExhausted = false;

	public ExternalForce(double vectorAngle, double deltaVelocity) {
		this.force = new Force(vectorAngle, deltaVelocity);
	}

	public CollisionListener getCollisionListener() {
		return collisionListener;
	}

	public double getMagnitude(double deltaTime) {
		if (isExhausted)
			return Double.MIN_NORMAL;

		double magnitude = force.getMagnitude(deltaTime);

		double newVelocity = force.getVelocity() - magnitude;

		if (newVelocity <= Double.MIN_NORMAL) {
			magnitude = magnitude - newVelocity;
			isExhausted = true;
			force.setVelocity(Double.MIN_NORMAL);
		}

		force.setVelocity(newVelocity);

		return magnitude;
	}

	public double getVectorAngle() {
		return force.getVectorAngle();
	}

	public boolean hasCollisionListener() {
		return this.collisionListener != null ? true : false;
	}

	public boolean isExhaused() {
		return isExhausted;
	}

	public void kill() {
		this.isExhausted = true;
	}

	public void setOnCollision(CollisionListener onCollision) {
		this.collisionListener = onCollision;
	}
}