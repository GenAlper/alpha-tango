package Bleach;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TerrainBlock extends Entity implements EntityTranslatable {

	double gridWidth, gridHeight;
	double absPosX, absPosY;
	int gridX, gridY;

	public TerrainBlock(Sprite sprite, int gridPositionX, int gridPositionY) {
		this(sprite, gridPositionX, gridPositionY, 32.0, 32.0, 0.0, 0.0);
	}

	public TerrainBlock(Sprite sprite, int gridPositionX, int gridPositionY, double cellWidth, double cellHeight) {
		this(sprite, gridPositionX, gridPositionY, cellWidth, cellHeight, 0.0, 0.0);
	}

	public TerrainBlock(Sprite sprite, int gridPositionX, int gridPositionY, double cellWidth, double cellHeight, double absPosX, double absPosY) {
		super(sprite, 0, 0, 0);
		this.absPosX = absPosX;
		this.absPosY = absPosY;
		gridX = gridPositionX;
		gridY = gridPositionY;
		gridWidth = cellWidth;
		gridHeight = cellHeight;
		mass = 0;
		hasRectangularCollisionModel = true;
	}

	public Point2D.Double getAbsolutePosition() {
		return new Point2D.Double(absPosX, absPosY);
	}

	@Override
	public Rectangle2D.Double getBoundary() {
		return new Rectangle2D.Double(absPosX + gridX * gridWidth, absPosY + gridY * gridHeight, gridWidth, gridHeight);
	}

	public double getGridHeight() {
		return gridHeight;
	}

	public Point2D.Double getGridPosition() {
		return new Point2D.Double(gridX, gridY);
	}

	public double getGridWidth() {
		return gridWidth;
	}

	@Override
	public Point2D.Double getPosition() {
		return new Point2D.Double(absPosX + gridX * gridWidth, absPosY + gridY * gridHeight);
	}

	public void setAbsolutePosition(Point2D.Double position) {
		/**
		 * Sets the absolute position in pixel where the grid starts on the
		 * level.
		 * */

		absPosX = position.x;
		absPosY = position.y;
	}

	public void setGridDimension(double width, double height) {
		/**
		 * Sets the size of a cell in the grid.
		 * */

		gridWidth = width;
		gridHeight = height;
	}

	public void setGridPosition(int x, int y) {
		/**
		 * Set the position of this TerrainBlock in the grid.
		 * */

		gridX = x;
		gridY = y;
	}

	@Override
	public void setPosition(Point2D.Double position) {
		x = position.x - gridX * gridWidth;
		y = position.y - gridY * gridHeight;
		absPosX = x;
		absPosY = y;
	}
}