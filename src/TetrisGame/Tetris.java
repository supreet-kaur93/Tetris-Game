package TetrisGame;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tetris extends JFrame{
	
	//static int BOX_HEIGHT = 400;
	//static int BOX_WIDTH = 400;
	public JLabel statusBar;
	
	Tetris() {
		CreateFrame();
	}
	
	private void CreateFrame() {
		statusBar = new JLabel("0");
		add(statusBar, BorderLayout.SOUTH);		

		PlayGame board = new PlayGame(this);
		add(board);
		board.start();

		setSize(200, 400);
		//setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	public JLabel getStatusBar() {
		return statusBar;
	}
	
	public static void main(String[] args) {
		//EventQueue.invokeLater({

            Tetris game = new Tetris();
            game.setVisible(true);
        //});
	}
}
