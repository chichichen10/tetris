package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tetris extends JFrame {
    private TetrisPanel a;
    private JMenuItem j1, j2, j3;


    public Tetris() {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu menuGame = new JMenu("GAME");
        menubar.add(menuGame);

        j1 = new JMenuItem("NEW GAME");
        j1.setActionCommand("newGame");
        menuGame.add(j1);
        j2 = new JMenuItem("PAUSE");
        j2.setActionCommand("pause");
        menuGame.add(j2);
        j3 = new JMenuItem("CONTINUE");
        j3.setActionCommand("resume");
        menuGame.add(j3);

        MenuListener ml = new MenuListener();
        j1.addActionListener(ml);
        j2.addActionListener(ml);
        j3.addActionListener(ml);

        menuGame.add("OTHER").addActionListener(ml);


        a = new TetrisPanel();
        this.addKeyListener(a.listener);
        add(a);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 550);
        setLocation(400, 100);
        setTitle("Tetris");
        setResizable(false);
    }

    public static void main(String[] args) {
        Tetris te = new Tetris();
        te.setVisible(true);
    }

    public String[] shapeOfBrick = {"I", "S", "Z", "J", "O", "L", "T", ""};

    class TetrisPanel extends JPanel {
        int[][] map = new int[14][23];// 為解決越界,在邊上預留了1列
 // [brickType][rotate][]
        int shapes[][][] = new int[][][]{

                {{0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0}},
// S
                {{0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}},
// Z
                {{1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}},
// J
                {{1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}},
// O
                {{1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}},
// L
                {{1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0}},
// T
                {{1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}}};
        private int blockType; // 0-6 3
        private int nextOne;
        private int nextTwo;
        private int turnState; // 0-3 2
        private int x;
        private int y;
        private int score;
        private int delay;
        private TimerListener listener = new TimerListener();
        private Timer timer;
        public int holded;

        public TetrisPanel() {
            newGame();
        }

        private int[] blocks = {0, 0, 0, 0, 0, 0, 0};
        private int numOfBlocks;

        public void iniBlock() {
            int temp;
            temp = (int) (Math.random() * 1000) % 4;
            while (blocks[temp] != 0) {
                temp += 1;
                temp = temp % 7;
            }
            blocks[temp] = 1;
            numOfBlocks++;
            blockType = temp;
            temp = (int) (Math.random() * 1000) % 4;
            while (blocks[temp] != 0) {
                temp += 1;
                temp = temp % 7;
            }
            blocks[temp] = 1;
            numOfBlocks++;
            nextOne = temp;
            while (blocks[temp] != 0) {
                temp += 1;
                temp = temp % 7;
            }
            blocks[temp] = 1;
            numOfBlocks++;
            nextTwo = temp;
        }


        public void nextBlock() {
            int temp;
            temp = (int) (Math.random() * 1000) % 4;
            while (blocks[temp] != 0) {
                temp += 1;
                temp = temp % 7;
            }
            blockType = nextOne;
            nextOne = nextTwo;
            nextTwo = temp;
            blocks[temp] = 1;
            System.out.println(temp + " " + numOfBlocks);
            numOfBlocks += 1;
            if (numOfBlocks == 7) {
                numOfBlocks = 0;
                for (int i = 0; i < 7; i++) {
                    blocks[i] = 0;
                }
            }
            turnState = (int) (Math.random() * 1000) % 4;
            x = 4;
            y = 0;
            if (crash(x, y, blockType, turnState) == 0) {
                timer.stop();
                int option = JOptionPane.showConfirmDialog(this,
                        "--Game Over--\nPlay Again?");
                if (option == JOptionPane.OK_OPTION) {
                    newGame();
                } else if (option == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        }

        public void nextHoldedBlock() {

            blockType = holded;
            turnState = (int) (Math.random() * 1000) % 4;
            x = 4;
            y = 0;
            if (crash(x, y, blockType, turnState) == 0) {
                timer.stop();
                int option = JOptionPane.showConfirmDialog(this,
                        "--Game Over--\nPlay Again?");
                if (option == JOptionPane.OK_OPTION) {
                    newGame();
                } else if (option == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        }

        public void newGame() {
            for (int i = 1; i < 13; i++) {// 5
                for (int j = 0; j < 22; j++) {
                    map[i][j] = 0;
                    map[12][j] = map[1][j] = 3;
                }
                map[i][21] = 3;
            }
// timer = new Timer(300, listener);
            delay = 750;
            timer = new Timer(delay, listener);
            timer.start();
            score = 0;// 分數初始化為0
            numOfBlocks = 0;
            for (int i = 0; i < 7; i++) {
                blocks[i] = 0;
            }
            holded = 7;

            j3.setEnabled(false);//把“新遊戲”選單滅掉
            repaint();

            iniBlock();
        }
        private void pause() {
            timer.stop();
        }
        private void resume() {
            timer.restart();
        }
        private void down() {
            if (crash(x, y + 1, blockType, turnState) == 0) {
                add(x, y, blockType, turnState);
                nextBlock();
            }
            y++;
            repaint();
        }
        private void downPress() {
            if (crash(x, y + 1, blockType, turnState) == 0) {
                return;
            }
            y++;
            repaint();
        }
        private void toEnd() {
            while (crash(x, y + 1, blockType, turnState) != 0) {
                y++;
            }
            add(x, y, blockType, turnState);
            nextBlock();
            y++;
            repaint();
        }
        private void hold() {
            if (holded == 7) {
                holded = blockType;
                nextBlock();
            } else {
                int temp = blockType;
                nextHoldedBlock();
                holded = temp;

            }
        }
        private int crash(int x, int y, int blockType, int turnState) {
            for (int a = 0; a < 4; a++) {
                for (int b = 0; b < 4; b++) {
                    if ((shapes[blockType][turnState][a * 4 + b] & map[x + b
                            + 2][y + a]) == 1) {
                        return 0;
                    }
                }
            }
            return 1;
        }

        private void add(int x, int y, int blockType, int turnState) {
            for (int a = 0; a < 4; a++) {
                for (int b = 0; b < 4; b++) {
                    if (shapes[blockType][turnState][a * 4 + b] == 1)
                        map[x + b + 2][y + a] = shapes[blockType][turnState][a
                                * 4 + b];
                }
            }
            tryDeline();
        }

        private void turn() {
            turnState = (turnState + crash(x, y, blockType, (turnState + 1) % 4)) % 4;
            repaint();
        }

        private void turn2() {
            if (crash(x, y, blockType, (turnState + 3) % 4) != 0) {
                turnState = (turnState + 3) % 4;
            }
            repaint();
        }

        private void left() {
            if (x >= -1) {
                x -= crash(x - 1, y, blockType, turnState);
                repaint();
            }
        }

        private void right() {
            if (x < 8) {
                x += crash(x + 1, y, blockType, turnState);
                repaint();
            }
        }

        public void tryDeline() {
            for (int b = 0; b < 21; b++) {// b是行號
                int c = 1;
                for (int a = 1; a < 13; a++) {// 10列是真正的方塊區,外面有兩列是邊界
                    c &= map[a][b];
                } // 實心塊是1,邊界是3。這兩者的最後一位都是1,和1&;運算的結果是1
                if (c == 1) {// 整行的格子要麼是實心塊,要麼是邊界。因為只要有一個0,&;運算的結果就是0
// 消行
// 從上往下,逐行掃描,把下一行的格子依次往上移
                    for (int d = b; d > 0; d--) {
                        for (int e = 2; e < 12; e++) {
                            map[e][d] = map[e][d - 1];
                        }
                    }
// 遊戲加分
                    score += 1;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);// 清除殘影
// now and shadow
            g.setColor(Color.gray);
            int k = y;
            while (crash(x, k + 1, blockType, turnState) != 0) {
                k++;
            }
            for (int j = 0; j < 16; j++) {
                if (shapes[blockType][turnState][j] == 1) {
                    g.fillRect((j % 4 + x + 2) * 20, (j / 4 + k) * 20, 20, 20);
                }
            }
//I S Z J O L T
            if (blockType == 0) g.setColor(Color.decode("#00BFFF"));
            else if (blockType == 1) g.setColor(Color.green);
            else if (blockType == 2) g.setColor(Color.red);
            else if (blockType == 3) g.setColor(Color.blue);
            else if (blockType == 4) g.setColor(Color.decode("#FFD700"));
            else if (blockType == 5) g.setColor(Color.decode("#FF8800"));
            else if (blockType == 6) g.setColor(Color.decode("#CC00FF"));

            for (int j = 0; j < 16; j++) {
                if (shapes[blockType][turnState][j] == 1) {
                    g.fillRect((j % 4 + x + 2) * 20, (j / 4 + y) * 20, 20, 20);
                }
            }


            g.setColor(Color.black);
// 畫地圖(已經固定的方塊)
            for (int j = 0; j < 22; j++) {
                for (int i = 1; i < 13; i++) {
                    if (map[i][j] == 1) {
                        g.fillRect(i * 20, j * 20, 20, 20);
// 讓堆積塊有分界線
                        g.setColor(Color.gray);
                        g.drawRect(i * 20, j * 20, 20, 20);
                        g.setColor(Color.black);
                    } else if (map[i][j] == 3) {
                        g.drawRect(i * 20, j * 20, 20, 20);
                    }
                }
            }
// 畫方塊區右側部分
            g.setColor(Color.blue);
            g.setFont(new Font("aa", Font.BOLD, 18));
            g.drawString("hold: " + shapeOfBrick[holded], 268, 20);
            for (int i = 0; i < 16; i++) {
                if (holded < 7) {
                    if (holded == 0) g.setColor(Color.decode("#00BFFF"));
                    else if (holded == 1) g.setColor(Color.green);
                    else if (holded == 2) g.setColor(Color.red);
                    else if (holded == 3) g.setColor(Color.blue);
                    else if (holded == 4) g.setColor(Color.decode("#FFD700"));
                    else if (holded == 5) g.setColor(Color.decode("#FF8800"));
                    else if (holded == 6) g.setColor(Color.decode("#CC00FF"));
                    if (shapes[holded][0][i] == 1) {
                        g.fillRect(285 + (i % 4) * 12, 32 + i / 4 * 14, 14, 14);
                    }

                }
            }
            g.setColor(Color.blue);
            g.drawString("score=" + score, 268, 260);
            g.setColor(Color.gray);
            g.drawString("next: ", 268, 92);
            for (int i = 0; i < 16; i++) {
                if (nextOne < 7) {
                    if (nextOne == 0) g.setColor(Color.decode("#00BFFF"));
                    else if (nextOne == 1) g.setColor(Color.green);
                    else if (nextOne == 2) g.setColor(Color.red);
                    else if (nextOne == 3) g.setColor(Color.blue);
                    else if (nextOne == 4) g.setColor(Color.decode("#FFD700"));
                    else if (nextOne == 5) g.setColor(Color.decode("#FF8800"));
                    else if (nextOne == 6) g.setColor(Color.decode("#CC00FF"));
                    if (shapes[nextOne][0][i] == 1) {
                        g.fillRect(285 + (i % 4) * 12, 108 + i / 4 * 14, 14, 14);
                    }

                }
            }
            g.setColor(Color.gray);
            for (int i = 0; i < 16; i++) {
                if (nextTwo < 7) {
                    if (nextTwo == 0) g.setColor(Color.decode("#00BFFF"));
                    else if (nextTwo == 1) g.setColor(Color.green);
                    else if (nextTwo == 2) g.setColor(Color.red);
                    else if (nextTwo == 3) g.setColor(Color.blue);
                    else if (nextTwo == 4) g.setColor(Color.decode("#FFD700"));
                    else if (nextTwo == 5) g.setColor(Color.decode("#FF8800"));
                    else if (nextTwo == 6) g.setColor(Color.decode("#CC00FF"));
                    if (shapes[nextTwo][0][i] == 1) {
                        g.fillRect(285 + (i % 4) * 12, 160 + i / 4 * 14, 14, 14);
                    }

                }
            }
            g.setFont(new Font("aa", Font.PLAIN, 13));
        }

        class TimerListener extends KeyAdapter implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                down();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_DOWN:
                        downPress();
                        break;
                    case KeyEvent.VK_UP:
                        turn();
                        break;
                    case KeyEvent.VK_LEFT:
                        left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        right();
                        break;
                    case KeyEvent.VK_Z:
                        turn2();
                        break;
                    case KeyEvent.VK_SPACE:
                        toEnd();
                        break;
                    case KeyEvent.VK_SHIFT:
                        hold();
                }
            }
        }
    }

    class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("newGame")) {
                a.newGame();
            } else if (e.getActionCommand().equals("pause")) {
                a.pause();
                j2.setEnabled(false);
                j3.setEnabled(true);

            } else if (e.getActionCommand().equals("resume")) {
                a.resume();
                j3.setEnabled(false);
                j2.setEnabled(true);
            }
        }
    }
}
