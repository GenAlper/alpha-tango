package Bleach.PhysicsEngine.TranslationEngine;

import java.awt.geom.Point2D;
import java.util.Iterator;

import Bleach.Entity;
import Bleach.PhysicsEngine.Force.ExternalForce;

public class Translatraton {

	private static double gravity = 1.0;

	public static Point2D.Double translate(Entity entity, double deltaTime) {
		// Gets current values
		double vectorAngle = entity.getForce().getVectorAngle();
		double deltaVelocity = entity.getForce().getMagnitude(deltaTime);

		// Calculates the next position based on velocity
		Point2D.Double nextPosition = entity.getPosition();
		if (entity.isMoving()) {
			nextPosition.x += Math.cos(vectorAngle) * deltaVelocity;
			nextPosition.y += Math.sin(vectorAngle) * deltaVelocity;
		}

		// Re-calculates the next Y-position based on velocity + gravity
		if (entity.isLanded() == false && entity.getMass() > 0.0) {
			double magicGravityModifier = 12.3;
			double gravitionalAcceleration = gravity * entity.getMass();
			entity.setWeight(entity.getWeight() + gravitionalAcceleration);
			nextPosition.y += (gravity * magicGravityModifier) * Math.pow(entity.getFallingTime(), 2);
		}

		Iterator<ExternalForce> externalForceIt = entity.getExternalForces().values().iterator();
		ExternalForce externalForce;
		while (externalForceIt.hasNext()) {
			externalForce = externalForceIt.next();
			double magnitude = externalForce.getMagnitude(deltaTime);
			nextPosition.x += Math.cos(externalForce.getVectorAngle()) * magnitude;
			nextPosition.y += Math.sin(externalForce.getVectorAngle()) * magnitude;

			if (externalForce.isExhaused())
				externalForceIt.remove();
		}

		// Sets the position to the newly calculated one
		entity.setPosition(nextPosition);

		return nextPosition;
	}

}
