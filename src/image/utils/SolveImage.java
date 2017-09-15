package image.utils;

import brick.Block;
import brick.Board;
import image.utils.worker.WindowForReader;
import image.utils.worker.WindowForSolver;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.Charset;

public class SolveImage {

    private JFrame frmExample;

    public static void main(String[] args) {
        EventQueue.invokeLater(
                new Runnable() {
                    public void run() {
                        try {
                            ReadImage windowR = new ReadImage();
                            SolveImage window = new SolveImage();
                            window.frmExample.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public SolveImage() {
        Board board = readBoard();
        System.out.println("Board read.");
        WindowForSolver.init(board);

        frmExample = new JFrame();
        frmExample.setTitle("Example");
        frmExample.setBounds(100, 100, 266, 240);
        frmExample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmExample.getContentPane().setLayout(null);

        JButton btnNewButton = new JButton("Next Step");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WindowForSolver.nextStep();
                WindowForSolver.redraw();
            }
        });
        btnNewButton.setBounds(63, 34, 138, 48);
        frmExample.getContentPane().add(btnNewButton);

        JButton btnProcess = new JButton("Process");
        btnProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WindowForSolver.processBoard();
            }
        });
        btnProcess.setBounds(63, 116, 138, 48);
        frmExample.getContentPane().add(btnProcess);
    }

    public Board readBoard() {
        Board board = new Board();
        String line;
        try (
                InputStream fis = new FileInputStream("files\\positions.txt");
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            Color whiteSpace = new Color(247,239,228);
            while ((line = br.readLine()) != null) {
                String[] lineValues = line.split(";");
                int i = Integer.valueOf(lineValues[0]);
                int j = Integer.valueOf(lineValues[1]);
                int r = Integer.valueOf(lineValues[2]);
                int g = Integer.valueOf(lineValues[3]);
                int b = Integer.valueOf(lineValues[4]);

                Color blockColor = new Color(r,g,b);

                if(ColorUtils.isSimilarColors(whiteSpace, blockColor)) {
                    continue;
                }

                board.addBlock(i, j, new Block(blockColor));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return board;
    }

}
