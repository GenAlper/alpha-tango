package Bleach.PhysicsEngine.Force;

public class Force {
	private double vectorAngle;
	private double velocity;

	public Force(double vectorAngle, double velocity) {
		this.vectorAngle = vectorAngle;
		this.velocity = velocity;
	}

	public double getMagnitude(double deltaTime) {
		return velocity * deltaTime;
	}

	public double getVectorAngle() {
		return vectorAngle;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVectorAngle(double vectorAngle) {
		this.vectorAngle = vectorAngle;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
}