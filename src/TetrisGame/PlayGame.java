package TetrisGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PlayGame extends JPanel implements ActionListener {
	
	static int BOX_HEIGHT = 22;
	static int BOX_WIDTH = 10;
	static boolean isStarted = false;
	private Timer timer;
	private Shape.shapes[] board;
	private final int INITIAL_DELAY = 400;
	private Shape currPiece;
	boolean isPaused;
	boolean isFallingFinished;
	int numLinesRemoved = 0;
	int currX = 0;
	int currY = 0;
	private JLabel statusBar;
	
	public PlayGame(Tetris game) {
		initBoard(game);
	}
	
	void initBoard(Tetris game) {
		setFocusable(true);
		currPiece = new Shape();
		 timer = new Timer(INITIAL_DELAY, this);
	     timer.start();
	     statusBar = game.getStatusBar();
		 board = new Shape.shapes[BOX_HEIGHT*BOX_WIDTH];
		 addKeyListener(new TAdapter());
		 clearScreen();
	}
	
	void start() {
		
		if(isPaused) {
			return;
		}
		
		isStarted = true;
		isFallingFinished= false;
		numLinesRemoved = 0;
		clearScreen();

		newPiece();
		timer.start();
	}
	
	void clearScreen() {
		for(int i=0; i<BOX_WIDTH*BOX_HEIGHT; i++) {
			board[i] = Shape.shapes.noShape;
		}
	}
	
	@Override
    public void paintComponent(Graphics g) { 

        super.paintComponent(g);
        doDrawing(g);
    }
	
	void doDrawing(Graphics g) {
		Dimension size = getSize();
		int top = (int)size.getHeight() - BOX_HEIGHT*squareHeight();
		for(int i=0; i<BOX_HEIGHT; i++) {
			for(int j=0; j<BOX_WIDTH; j++) {
				Shape.shapes shape = shapeAt(j, BOX_HEIGHT-i-1);
				if(shape != Shape.shapes.noShape) {
					drawSquare(g, 0+j*squareWidth(), top+i*squareHeight(), shape);
				}
			}
		}
		if(currPiece.getShape() != Shape.shapes.noShape) {
			for(int i=0; i<4; i++) {
				int x = currX + currPiece.x(i);
				int y = currY - currPiece.y(i);
				drawSquare(g, 0+x*squareWidth(), top + (BOX_HEIGHT - y -1)*squareHeight(), currPiece.getShape());
			}
		}
	}
	
	void drawSquare(Graphics g, int x, int y, Shape.shapes shape) {
		Color colors[] = {
				new Color(0, 0, 0),
				new Color(204, 102, 102), 
	            new Color(102, 204, 102), new Color(102, 102, 204), 
	            new Color(204, 204, 102), new Color(204, 102, 204), 
	            new Color(102, 204, 204), new Color(218, 170, 0)
		};
		
		Color color = colors[shape.ordinal()];
		g.setColor(color);
		g.fillRect(x+1, y+1, squareWidth()-2, squareHeight()-2);
		g.setColor(color.brighter());
		g.drawLine(x, y+squareHeight()-1, x, y);
		g.drawLine(x, y, x+squareWidth()-1, y);
		g.setColor(color.darker());
		g.drawLine(x+1, y+squareHeight()-1, x+squareWidth()-1, y+squareHeight()-1);
		g.drawLine(x+squareWidth()-1, y+squareHeight()-1, x+squareWidth()-1, y+1);
	}
	
	private int squareWidth() {
		return (int)getSize().getWidth()/BOX_WIDTH;
	}
	
	private int squareHeight() {
		return (int)getSize().getHeight()/BOX_HEIGHT;
	}
	
	private void newPiece() {
		currPiece.RandomShape();
		currX = BOX_WIDTH/2+1;
		currY = BOX_HEIGHT-1 + currPiece.minY();
		if(!Moving(currPiece, currX, currY)) {
			currPiece.setShape(Shape.shapes.noShape);
			timer.stop();
			isStarted = false;
			statusBar.setText("Game Over");
		}
	}
	
	boolean Moving(Shape shape, int XFixed, int YFixed) {
		for(int i=0; i<4; i++) {
			int x = XFixed + shape.x(i);
			int y = YFixed - shape.y(i);
			if(x<0 || x>=BOX_WIDTH || y<0 || y>= BOX_HEIGHT) {
				return false;
			}
			if(shapeAt(x, y) != Shape.shapes.noShape) {
				return false;
			}
		}
		currPiece = shape;
		currX = XFixed;
		currY = YFixed;
		repaint();
		return true;
	}
	
	private Shape.shapes shapeAt(int x, int y) {
		return board[(y*BOX_WIDTH)+x];
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		}
		else {
			DropDownBySingle();
		}
	}
	
	void DropDownBySingle() {
		if(!Moving(currPiece, currX, currY-1)) {
			isDropped();
		}
	}
	
	void DropDown() {
		int newY = currY;
		while(newY > 0) {
			if(!Moving(currPiece, currX, newY-1))
				break;
			newY--;
		}
		isDropped();
	}
	
	private void isDropped() {
		for(int i=0; i<4; i++) {
			int x = currX + currPiece.x(i);
			int y = currY - currPiece.y(i);
			board[(y*BOX_WIDTH) + x] = currPiece.getShape();
		}
		
		isFullLine();
		
		if(!isFallingFinished) {
			newPiece();
		}
	}
	
	void isFullLine() {
		int noOfFull = 0;
		for(int i=BOX_HEIGHT-1; i>=0; i--) {
			boolean isFull = true;
			for(int j=0; j<BOX_WIDTH; j++) {
				if(shapeAt(j, i) == Shape.shapes.noShape) {
					isFull = false;
					break;
				}
			}
			if(isFull) {
				noOfFull++;
				for(int k=i; k<BOX_HEIGHT-1; k++) {
					for(int j=0; j<BOX_WIDTH; j++) {
						board[(k*BOX_WIDTH) + j] = shapeAt(j, k+1);
					}
				}
			}
		}
		if(noOfFull > 0) {
			numLinesRemoved = numLinesRemoved + noOfFull;
			statusBar.setText(String.valueOf(numLinesRemoved));
			isFallingFinished = false;
			currPiece.setShape(Shape.shapes.noShape);
			repaint();
		}
	}
	
	void pause() {
		if(!isStarted) {
			return;
		}
		isPaused = !isPaused;
		if(isPaused) {
			timer.stop();
			statusBar.setText("Paused");
		}
		else {
			timer.start();
			statusBar.setText(String.valueOf(numLinesRemoved));
		}
		repaint();
	}
	
	class TAdapter extends KeyAdapter{
        
        @Override
        public void keyPressed(KeyEvent e) {

            if (!isStarted || currPiece.getShape() == Shape.shapes.noShape) {  
                return;
            }

            int keycode = e.getKeyCode();

            if (keycode == 'P') {
                pause();
                return;
            }

            if (isPaused)
                return;

            switch (keycode) {
                
            case KeyEvent.VK_LEFT:
                Moving(currPiece, currX - 1, currY);
                break;
                
            case KeyEvent.VK_RIGHT:
                Moving(currPiece, currX + 1, currY);
                break;
                
            case KeyEvent.VK_DOWN:
                Moving(currPiece.rotateRight(), currX, currY);
                break;
                
            case KeyEvent.VK_UP:
                Moving(currPiece.rotateLeft(), currX, currY);
                break;
                
            case KeyEvent.VK_SPACE:
                DropDown();
                break;
                
            case 'D':
                DropDownBySingle();
                break;
            }
        }
    }
}