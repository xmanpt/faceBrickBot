package image.utils.worker;

import brick.Board;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

class SolutionFromImage extends JPanel {

	public static final int xi = 20;
	public static final int yi = 20;
	public static final int dist = 32;
	public static final int rad = 16;

	public static Integer hoverI;
	public static Integer hoverJ;

	public static Integer mouseX;
	public static Integer mouseY;

	private Board board;


	public SolutionFromImage(Board board) {
		this.board = board;
	}

	public SolutionFromImage() {
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(240, 240, 240));
		g.fillRect(0, 0, WindowForSolver.width, WindowForSolver.height);
		try {
			printBrickBoard(g);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}

		if (getBoard().getBestSolutionScore() != null) {
			drawBoardNextClick(g);
		}


		g.drawString("X: " + SolutionFromImage.mouseX, 300, 50);
		g.drawString("Y: " + SolutionFromImage.mouseY, 300, 70);
		checkHover(g);
	}

	private void checkHover(Graphics g) {
		if (hoverI != null && hoverJ != null) {
			if (getBoard().getValue()[hoverI] == null || getBoard().getValue()[hoverI][hoverJ] == null)
				return;

			int x0 = xi + hoverI * dist;
			int y0 = yi + hoverJ * dist;
			g.setColor(Color.BLACK);
			g.drawRect(x0 - 2, y0 - 2, rad + 4, rad + 4);

		}

	}

	private void drawBoardNextClick(Graphics g) {
		for (String nID : getBoard().getBestSolutionKey()) {
			String coords[] = nID.split(";");
			int i = Integer.valueOf(coords[0]);
			int j = Integer.valueOf(coords[1]);
			printBlock(i, j, g, true);
		}
	}


	public Board getBoard() {
		return board;
	}

	public void printBlock(int i, int j, Graphics g, boolean mark) {
		if (getBoard().getValue()[i] == null || getBoard().getValue()[i][j] == null)
			return;

		int x0 = xi + i * dist;
		int y0 = yi + j * dist;
		g.setColor(getBoard().getValue()[i][j].getColor());
		g.fillOval(x0, y0, rad, rad);

		g.setColor(Color.black);
		g.drawOval(x0, y0, rad, rad);

		if (mark) {
			g.setColor(Color.black);
			g.drawLine(x0, y0, x0 + rad, y0 + rad);
			g.drawLine(x0, y0 + rad, x0 + rad, y0);
		}
	}

	public void printBlock(int i, int j, Graphics g) {
		printBlock(i, j, g, false);
	}

	private void printBrickBoard(Graphics g) throws FileNotFoundException, UnsupportedEncodingException {

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				printBlock(i, j, g);
			}
		}


	}

	public void setBoard(Board board) {
		this.board = board;
	}
}
