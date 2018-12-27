package TetrisGame;

import java.util.Random;

public class Shape {
	
	protected enum shapes {noShape, Z, S, Line, T, Square, L};
	private int[][] coords;
	private int[][][] allCoords;
	private shapes PieceShape;
	
	public Shape() {
		coords = new int[4][2];
		setShape(shapes.noShape);
	}
	
	public shapes getShape() {
		return PieceShape;
	}
	
	public int x(int index) {
		return coords[index][0];
	}
	
	public int y(int index) {
		return coords[index][1];
	}
	
	public int minX() {
		int min = coords[0][0];
		for(int i=0; i<4; i++) {
			min = Math.min(min,  coords[i][0]);
		}
		return min;
	}
	
	private void setX(int index, int x) {
		coords[index][0] = x;
	}
	
	private void setY(int index, int y) {
		coords[index][1] = 1;
	}
	
	public int minY() {
		int min = coords[0][1];
		for(int i=0; i<4; i++) {
			min = Math.min(min,  coords[i][1]);
		}
		return min;
	}
	
	public Shape rotateLeft() {
		if(PieceShape == shapes.noShape) {
			return this;
		}
		Shape toReturn = new Shape();
		toReturn.PieceShape = PieceShape;
		for(int i=0; i<4; i++) {
			toReturn.setX(i, y(i));
			toReturn.setY(i, -x(i));
		}
		return toReturn;
	}
	
	public Shape rotateRight() {
		if(PieceShape == shapes.noShape) {
			return this;
		}
		Shape toReturn = new Shape();
		toReturn.PieceShape = PieceShape;
		for(int i=0; i<4; i++) {
			toReturn.setX(i, -y(i));
			toReturn.setY(i, x(i));
		}
		return toReturn;
	}
	
	public void setShape(shapes currShape) {
		allCoords = new int[][][] {
			{ { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
	        { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
	        { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
	        { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
	        { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
	        { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
	        { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
	        { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
		};
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				coords[i][j] = allCoords[currShape.ordinal()][i][j];
			}
		}
		PieceShape = currShape;
	}
	
	public void RandomShape() {
		Random r = new Random();
		int x = Math.abs(r.nextInt())%7+1;
		shapes[] values = shapes.values();
		setShape(values[x]);
	}
}