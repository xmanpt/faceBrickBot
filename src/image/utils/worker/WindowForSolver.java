package image.utils.worker;

import brick.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WindowForSolver extends JFrame implements MouseMotionListener, MouseListener {

	public static String image = "a.png";
	public static BufferedImage image1 = readImage("files//" + image);
	public static SolutionFromImage solution;

	public static int width = 360;
	public static int height = 390;

	private boolean startHover;


	public static BufferedImage readImage(String fileLocation) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public static void Window(Board board) {

		solution = new SolutionFromImage(board);

		JFrame frame = new JFrame("ReDraw");
		frame.getContentPane().add(solution, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setSize(width, height);

		WindowForSolver windowForSolver = new WindowForSolver();
		frame.addMouseMotionListener(windowForSolver);
		frame.addMouseListener(windowForSolver);

		board.gatherNeighbours();
		//board.process();
		board.checkSolution();
	}


	public static void init(Board board) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Window(board);
			}
		});
	}


	public static void nextStep() {
		Board nextBoard = solution.getBoard().getBestSolution();
		solution.setBoard(nextBoard);
	}

	public static void redraw() {
		solution.revalidate();
		solution.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}


	@Override
	public void mouseMoved(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		if (mx > 0 && mx < WindowForSolver.width && my > 0 && my < WindowForSolver.height) {
			startHover = true;
			checkHover(mx, my);

			SolutionFromImage.mouseX = mx;
			SolutionFromImage.mouseY = my;
		} else {
			startHover = false;
			SolutionFromImage.hoverI = null;
			SolutionFromImage.hoverJ = null;

			SolutionFromImage.mouseX = null;
			SolutionFromImage.mouseY = null;
		}

		WindowForSolver.redraw();
	}

	private void checkHover(int mx, int my) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				int x0 = SolutionFromImage.xi + i * SolutionFromImage.dist+8;
				int y0 = SolutionFromImage.yi + j * SolutionFromImage.dist+30;
				int x1 = x0 + SolutionFromImage.rad;
				int y1 = y0 + SolutionFromImage.rad;
				if (mx > x0 && mx < x1 && my > y0 && my < y1) {
					if (solution.getBoard().getValue()[i] == null || solution.getBoard().getValue()[i][j] == null) {
						return;
					}
					SolutionFromImage.hoverI = i;
					SolutionFromImage.hoverJ = j;
					return;

				}
			}
		}


	}

	@Override
	public void mouseClicked(MouseEvent e) {
		solution.getBoard().removeGroupByBlock(SolutionFromImage.hoverI, SolutionFromImage.hoverJ);
		Board clone = solution.getBoard().clone();
		solution.setBoard(clone);
		WindowForSolver.redraw();

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public static void processBoard() {
		solution.getBoard().process();
		solution.getBoard().checkSolution();
	}
}

