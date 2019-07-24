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
    private JButton b;


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

        b = new JButton();
        b.setLocation(120, 100);
        b.setSize(200, 100);
        b.setText("Start Game");
        b.setActionCommand("newGame");
        add(b);

        MenuListener ml = new MenuListener();
        j1.addActionListener(ml);
        j2.addActionListener(ml);
        j3.addActionListener(ml);
        b.addActionListener(ml);


//        Cat cat = new Cat();
//        b.addActionListener(cat);


        a = new TetrisPanel();

        this.addKeyListener(a.listener);
        add(a);
//        a.add(b);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 550);
        setLocation(400, 100);
        setTitle("Tetris");
        setResizable(false);
        requestFocus();
    }

    public static void main(String[] args) {
        Tetris te = new Tetris();
        te.setVisible(true);
        te.enableInputMethods(false);
    }

    public String[] shapeOfBrick = {"I", "S", "Z", "J", "O", "L", "T", ""};

    class TetrisPanel extends JPanel {
        int[][] map = new int[16][26];// 為解決越界,在邊上預留了1列
        // [brickType][rotate][]
        int shapes[][][] = new int[][][]{
///I
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
//                {{1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                        {0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0},
//                        {0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
//                        {1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}},
//                SRS
                {{1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0}},
// O
                {{1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}},
// L
//                {{1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                        {0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
//                        {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0},
//                        {1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0}},
                //SRS
                {{0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}},
// T
//                {{1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                        {0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
//                        {0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
//                        {1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}}};
                //SRS
                {{0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}}};
        private int colored[] = {5, 7, 9, 11, 13, 15, 17};//101,111,1001,1011,1101....
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
        private int combo;
        boolean running;
        private int lineSent;
        private int lines;
        private int remainBlocks;
        private boolean perfect;
        private boolean tSpin;
        private boolean lock;
        private int spin;
        private boolean tCheck;
        private int tCount;
        private boolean turn;
        public boolean tetris;
        public boolean b2b;
        public boolean showB2B;

        public TetrisPanel() {
            preGame();
            //newGame();
        }

        private int[] blocks = {0, 0, 0, 0, 0, 0, 0};
        private int numOfBlocks;

        public void iniBlock() {
            int temp;
            temp = (int) (Math.random() * 1000) % 7;
            while (blocks[temp] != 0) {
                temp += 1;
                temp = temp % 7;
            }
            blocks[temp] = 1;
            numOfBlocks++;
            blockType = temp;
            if (blockType != 4)
                x = 3;
            else x = 4;
            if (blockType != 0)
                y = 0;
            else y = 1;
            temp = (int) (Math.random() * 1000) % 7;
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
            down();
        }


        public void nextBlock() {
            int temp;
            temp = (int) (Math.random() * 1000) % 7;
            while (blocks[temp] != 0) {
                temp += 1;
                temp = temp % 7;
            }
            tCheck = false;
            blockType = nextOne;
            nextOne = nextTwo;
            lock = false;
            spin = 0;
            nextTwo = temp;
            blocks[temp] = 1;
            turn = false;
            numOfBlocks += 1;
            if (numOfBlocks == 7) {
                numOfBlocks = 0;
                for (int i = 0; i < 7; i++) {
                    blocks[i] = 0;
                }
            }
            turnState = 0;
            if (blockType != 4)
                x = 3;
            else x = 4;
            if (blockType != 0)
                y = 0;
            else y = 1;
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
            down();
        }

        public void nextHoldedBlock() {
            spin = 0;
            lock = false;
            tCheck = false;
            blockType = holded;
            turnState = 0;
            turn = false;
            if (blockType != 4)
                x = 3;
            else x = 4;
            if (blockType != 0)
                y = 0;
            else y = 1;
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
            down();
        }

        private boolean game;

        public void stop() {
            game = false;
            JLabel label = new JLabel("<html>Welcome to Tetris<br/>&lt HOW TO PLAY &gt<br/>right,left: move<br/>down: soft drop<br/>space: hard drop<br/>up:rotate clockwise<br/>z: rotate counter-clockwise<br/>shift: hold</html>");
            label.setFont(new Font("Arial", Font.BOLD, 18));
            JOptionPane option = new JOptionPane(label);
            final JDialog d = option.createDialog((JFrame) null, "Welcome");
            d.setLocation(440, 350);
            d.setVisible(true);
            preGame();
//            newGame();
        }

        public void preGame() {
            game = false;
            b.setVisible(true);
            repaint();

//            newGame();

        }

        public void newGame() {
            b.setVisible(false);
            game = true;
            for (int i = 2; i < 14; i++) {// 5
                for (int j = 0; j < 24; j++) {
                    map[i][j] = 0;
                    map[13][j] = map[2][j] = 3;
                }
                map[i][23] = 3;
            }
            // timer = new Timer(300, listener);
            delay = 1000;
            timer = new Timer(delay, listener);
            timer.start();
            score = 0;
            combo = -1;
            numOfBlocks = 0;
            for (int i = 0; i < 7; i++) {
                blocks[i] = 0;
            }
            holded = 7;
            running = true;
            lineSent = 0;
            remainBlocks = 0;
            spin = 0;
            tSpin = false;
            lock = false;
            perfect = false;
            turn = false;
            b2b = false;
            j3.setEnabled(false);
            iniBlock();
            repaint();


        }

        private void pause() {
            timer.stop();
        }

        private void resume() {
            timer.restart();
        }

        private void down() {
            turn = false;
            if (blockType == 6)
                if (crash(x, y + 2, blockType, turnState) == 0)
                    lock = true;
            if (crash(x, y + 1, blockType, turnState) == 0) {
                if (blockType == 6)
                    lock = true;
                add(x, y, blockType, turnState);
                nextBlock();
            }
            y++;
            repaint();
        }

        private void downPress() {
            try {
                if (blockType == 6)
                    if (crash(x, y + 2, blockType, turnState) == 0)
                        lock = true;
                turn = false;
                if (crash(x, y + 1, blockType, turnState) == 0) {
                    if (blockType == 6)
                        lock = true;
                    timer.restart();
                    timer.wait(500);
                    add(x, y, blockType, turnState);
                    nextBlock();
                }
                y++;
                repaint();
            } catch (Exception e) {
            }
        }


        private void toEnd() {
            turn = false;
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
                            + 3][y + a]) == 1) {
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
                        map[x + b + 3][y + a] = colored[blockType];
                }
            }
            if (blockType == 6)
                tCheck = tSpinCheck();
            System.out.println(tCheck);
            remainBlocks += 4;
            tryDeline();
        }

        private void turn() {
            turn = true;
            int temp = turnState;
            if (crash(x, y, blockType, (turnState + 1) % 4) == 0) {
                if (blockType != 0 && blockType != 4)
                    wallKick(turnState, (turnState + 1) % 4);
                else if (blockType == 0)
                    wallKickForI(turnState, (turnState + 1) % 4);
            } else {
                turnState = (turnState + 1) % 4;
            }
            if (lock && (turnState != temp)) {
                spin++;
            }
            repaint();
        }

        private void turn2() {
            turn = true;
            int temp = turnState;
            if (crash(x, y, blockType, (turnState + 3) % 4) != 0) {
                turnState = (turnState + 3) % 4;
            } else {
                if (blockType != 0 && blockType != 4)
                    wallKick(turnState, (turnState + 3) % 4);
                else if (blockType == 0)
                    wallKickForI(turnState, (turnState + 3) % 4);
            }
            if (lock && (turnState != temp)) {
                spin++;
            }
            repaint();
        }

        private void wallKick(int from, int to) {
            if (from == 0) {
                if (to == 1) {
                    int[][] data1 = {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data1[i][0], data1[i][1], to)) {
                            x += data1[i][0];
                            y += data1[i][1];
                            turnState = to;
                            break;
                        }
                    }
                } else if (to == 3) {
                    int[][] data2 = {{1, 0}, {1, -1}, {0, 2}, {1, 2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data2[i][0], data2[i][1], to)) {
                            x += data2[i][0];
                            y += data2[i][1];
                            turnState = to;
                            break;
                        }
                    }
                }
            } else if (from == 1) {
                if (to == 2) {
                    int[][] data3 = {{1, 0}, {1, 1}, {0, -2}, {1, -2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data3[i][0], data3[i][1], to)) {
                            x += data3[i][0];
                            y += data3[i][1];
                            turnState = to;
                            break;
                        }
                    }
                } else if (to == 0) {
                    int[][] data4 = {{1, 0}, {1, 1}, {0, -2}, {1, -2}};
                    System.out.println(blockType + " " + turnState);
                    for (int i = 0; i < 4; i++) {
                        System.out.println(data4[i][0] + "," + data4[i][1]);
                        System.out.println(wallTest(data4[i][0], data4[i][1], to));
                        System.out.println(crash(x + 1, y - 1, blockType, (turnState + 3) % 4));
                        if (wallTest(data4[i][0], data4[i][1], to)) {
                            x += data4[i][0];
                            y += data4[i][1];
                            turnState = to;
                            break;
                        }
                    }
                }
            } else if (from == 2) {
                if (to == 3) {
                    int[][] data5 = {{1, 0}, {1, -1}, {0, 2}, {1, 2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data5[i][0], data5[i][1], to)) {
                            x += data5[i][0];
                            y += data5[i][1];
                            turnState = to;
                            break;
                        }
                    }
                } else if (to == 1) {
                    int[][] data6 = {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data6[i][0], data6[i][1], to)) {
                            x += data6[i][0];
                            y += data6[i][1];
                            turnState = to;
                            break;
                        }
                    }
                }
            } else if (from == 3) {
                if (to == 0) {
                    int[][] data7 = {{-1, 0}, {-1, 1}, {0, -2}, {-1, -2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data7[i][0], data7[i][1], to)) {
                            x += data7[i][0];
                            y += data7[i][1];
                            turnState = to;
                            break;
                        }
                    }
                } else if (to == 2) {
                    int[][] data8 = {{-1, 0}, {-1, 1}, {0, -2}, {-1, -
                            2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data8[i][0], data8[i][1], to)) {
                            x += data8[i][0];
                            y += data8[i][1];
                            turnState = to;
                            break;
                        }
                    }
                }
            }
        }

        private void wallKickForI(int from, int to) {
            if (from == 0) {
                if (to == 1) {
                    int[][] data1 = {{-2, 0}, {1, 0}, {-2, 1}, {1, -2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data1[i][0], data1[i][1], to)) {
                            x += data1[i][0];
                            y += data1[i][1];
                            turnState = to;
                            break;
                        }
                    }
                } else if (to == 3) {
                    int[][] data2 = {{-1, 0}, {2, 0}, {-1, -2}, {2, 1}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data2[i][0], data2[i][1], to)) {
                            x += data2[i][0];
                            y += data2[i][1];
                            turnState = to;
                            break;
                        }
                    }
                }
            } else if (from == 1) {
                if (to == 2) {
                    int[][] data3 = {{-1, 0}, {2, 0}, {-1, -2}, {2, 1}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data3[i][0], data3[i][1], to)) {
                            x += data3[i][0];
                            y += data3[i][1];
                            turnState = to;
                            break;
                        }
                    }
                } else if (to == 0) {
                    int[][] data4 = {{2, 0}, {-1, 0}, {2, -1}, {-1, 2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data4[i][0], data4[i][1], to)) {
                            x += data4[i][0];
                            y += data4[i][1];
                            turnState = to;
                            break;
                        }
                    }
                }
            } else if (from == 2) {
                if (to == 3) {
                    int[][] data5 = {{2, 0}, {-1, 0}, {2, -1}, {-1, 2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data5[i][0], data5[i][1], to)) {
                            x += data5[i][0];
                            y += data5[i][1];
                            turnState = to;
                            break;
                        }
                    }
                } else if (to == 1) {
                    int[][] data6 = {{1, 0}, {-2, 0}, {1, 2}, {-2, -1}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data6[i][0], data6[i][1], to)) {
                            x += data6[i][0];
                            y += data6[i][1];
                            turnState = to;
                            break;
                        }
                    }
                }
            } else if (from == 3) {
                if (to == 0) {
                    int[][] data7 = {{1, 0}, {-2, 0}, {1, 2}, {-2, -1}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data7[i][0], data7[i][1], to)) {
                            x += data7[i][0];
                            y += data7[i][1];
                            turnState = to;
                            break;
                        }
                    }
                } else if (to == 2) {
                    int[][] data8 = {{-2, 0}, {1, 0}, {-2, 1}, {1, -2}};
                    for (int i = 0; i < 4; i++) {
                        if (wallTest(data8[i][0], data8[i][1], to)) {
                            x += data8[i][0];
                            y += data8[i][1];
                            turnState = to;
                            break;
                        }
                    }
                }
            }
        }

        private boolean wallTest(int i, int j, int turn) {
            if (crash(x + i, y + j, blockType, turn) != 0)
                return true;
            else return false;
        }

        private void left() {
            turn = false;
            if (x >= -1) {
                x -= crash(x - 1, y, blockType, turnState);
                repaint();
            }
        }

        private void right() {
            turn = false;
            if (x < 8) {
                x += crash(x + 1, y, blockType, turnState);
                repaint();
            }
        }

        public void tryDeline() {
            tCount = 0;
            lines = 0;
            showB2B = b2b;
            for (int b = 2; b < 23; b++) {
                int c = 1;
                for (int a = 2; a < 14; a++) {// 10
                    c &= map[a][b];
                } // 1: 0001 3 :0011  1&1=1
                if (c == 1) {
                    for (int d = b; d > 0; d--) {
                        for (int e = 3; e < 13; e++) {
                            map[e][d] = map[e][d - 1];
                        }
                    }
                    lines++;
                    remainBlocks -= 10;
                }
            }
            boolean gameover = false;
            for(int a = 3;a<13;a++){
                if(map[a][1]!=0){
                    gameover = true;
                    break;
                }
            }
            if(gameover){
                timer.stop();
                int option = JOptionPane.showConfirmDialog(this,
                        "--Game Over--\nPlay Again?");
                if (option == JOptionPane.OK_OPTION) {
                    newGame();
                } else if (option == JOptionPane.NO_OPTION) {
                    System.exit(0);

                }
            }
            if (lines > 0) {
                combo++;
                score += combo * 50;
            } else combo = -1;
            if (lines == 1)
                score += 100;
            if (lines >= 2 && lines < 4) {
                lineSent += lines - 1;
                score += (200 * lines - 100);
                tetris = false;
            } else if (lines == 4) {
                lineSent += 4;
                score += 800;
                if (b2b) {
                    lineSent += 2;
                    score += 400;
                }
                tetris = true;
            } else tetris = false;

            if (combo >= 1 && combo < 3) {
                lineSent += 1;
            } else if (combo >= 3 && combo < 5) {
                lineSent += 2;
            } else if (combo >= 5 && combo < 7) {
                lineSent += 3;
            } else if (combo >= 7) {
                lineSent += 4;
            }
            if (remainBlocks == 0) {
                perfect = true;
                lineSent += 10;
            } else perfect = false;
            //t-spin
            System.out.println("spin: " + spin + tCheck);
            if (spin > 0 && tCheck) {
                tSpin = true;
                if (lines == 1) {
                    if (turnState == 2) {//T-Spin Single
                        lineSent += 2;
                        tCount = 2;
                        score += b2b ? 650 : 400;
                    } else { //T-Spin Mini
                        lineSent++;
                        tCount = 1;
                        score += b2b ? 350 : 200;
                    }
                    if (b2b)
                        lineSent += 1;

                } else if (lines == 2) {
                    lineSent += b2b ? 5 : 3;
                    score += b2b ? 900 : 500;
                    tCount = 3;
                } else if (lines == 3) {
                    lineSent += b2b ? 7 : 4;
                    score += b2b ? 1300 : 700;
                    tCount = 4;
                } else if (lines == 0) {
                    tCount = 0;
                    score += 100;
                }
            } else tSpin = false;

            if (lines > 0) {
                b2b = (tetris || tSpin);
            }

        }

        public boolean tSpinCheck() {
            int count = 0;
            if ((map[x + 3][y] & 1) == 1)
                count++;
            if ((map[x + 2 + 3][y] & 1) == 1)
                count++;
            if ((map[x + 3][y + 2] & 1) == 1)
                count++;
            if ((map[x + 2 + 3][y + 2] & 1) == 1)
                count++;
            if (count >= 3)
                return true;
            else return false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (game) {
                super.paintComponent(g);// 清除殘影
                for (int i = 3; i < 13; i++) {
                    for (int j = 3; j < 23; j++) {
                        if ((i + j) % 2 == 0)
                            g.setColor(Color.decode("#272727"));
                        else g.setColor(Color.decode("#000000"));
                        g.fillRect(i * 20, j * 20, 20, 20);
                    }
                }
// now and shadow
                g.setColor(Color.gray);
                int k = y;
                while (crash(x, k + 1, blockType, turnState) != 0) {
                    k++;
                }
                for (int j = 0; j < 16; j++) {
                    if (shapes[blockType][turnState][j] == 1) {
                        g.fillRect((j % 4 + x + 3) * 20, (j / 4 + k) * 20, 20, 20);
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
                    if (shapes[blockType][turnState][j] == 1 && j / 4 + y > 2) {
                        g.fillRect((j % 4 + x + 3) * 20, (j / 4 + y) * 20, 20, 20);
                    }
                }
                g.setColor(Color.black);
                for (int j = 0; j < 16; j++) {
                    if (shapes[blockType][turnState][j] == 1 && j / 4 + y > 2) {
                        g.drawRect((j % 4 + x + 3) * 20, (j / 4 + y) * 20, 20, 20);
                    }
                }
// fixed blocks
                for (int j = 3; j < 24; j++) {
                    for (int i = 2; i < 14; i++) {
                        if (map[i][j] == 1) {//no use
                            g.fillRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.gray);
                            g.drawRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.black);
                        } else if (map[i][j] == 3) {
                            g.drawRect(i * 20, j * 20, 20, 20);
                        } else if (map[i][j] == 5) {
                            g.setColor(Color.decode("#00BFFF"));
                            g.fillRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.black);
                            g.drawRect(i * 20, j * 20, 20, 20);
                        } else if (map[i][j] == 7) {
                            g.setColor(Color.green);
                            g.fillRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.black);
                            g.drawRect(i * 20, j * 20, 20, 20);
                        } else if (map[i][j] == 9) {
                            g.setColor(Color.red);
                            g.fillRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.black);
                            g.drawRect(i * 20, j * 20, 20, 20);
                        } else if (map[i][j] == 11) {
                            g.setColor(Color.blue);
                            g.fillRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.black);
                            g.drawRect(i * 20, j * 20, 20, 20);
                        } else if (map[i][j] == 13) {
                            g.setColor(Color.decode("#FFD700"));
                            g.fillRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.black);
                            g.drawRect(i * 20, j * 20, 20, 20);
                        } else if (map[i][j] == 15) {
                            g.setColor(Color.decode("#FF8800"));
                            g.fillRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.black);
                            g.drawRect(i * 20, j * 20, 20, 20);
                        } else if (map[i][j] == 17) {
                            g.setColor(Color.decode("#CC00FF"));
                            g.fillRect(i * 20, j * 20, 20, 20);
                            g.setColor(Color.black);
                            g.drawRect(i * 20, j * 20, 20, 20);
                        }
                    }
                }
// right side
                g.setColor(Color.blue);
                g.setFont(new Font("aa", Font.BOLD, 18));
                g.drawString("hold: " + shapeOfBrick[holded], 288, 20);
                //g.drawString("x: " + x + " y: " + y, 288, 380);
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
                            g.fillRect(288 + (i % 4) * 12, 32 + i / 4 * 14, 14, 14);
                        }

                    }
                }
                g.setColor(Color.blue);
                g.drawString("score: " + score, 288, 260);
                g.drawString("LS: " + lineSent, 288, 290);
                if (combo >= 1) {
                    g.setColor(Color.red);
                    g.drawString(combo + " COMBO!", 288, 320);
                }
                if (perfect) {
                    g.setColor(Color.green);
                    g.drawString("PERFECT CLEAR", 288, 360);
                }
                if (tetris) {
                    g.setColor(Color.decode("#00BFFF"));
                    g.drawString("TETRIS", 288, 370);
                    if (showB2B)
                        g.drawString("B2B", 288, 352);
                }
                if (tSpin) {
                    g.setColor(Color.decode("#CC00FF"));
                    if (showB2B)
                        g.drawString("B2B", 288, 340);
                    g.drawString("T-SPIN", 288, 356);
                    if (tCount == 1)
                        g.drawString("MINI", 288, 372);
                    else if (tCount == 2) {
                        g.drawString("SINGLE", 288, 372);
                    } else if (tCount == 3) {
                        g.drawString("DOUBLE", 288, 372);
                    } else if (tCount == 4) {
                        g.drawString("TRIPLE", 288, 372);
                    }
                }

                g.setColor(Color.gray);
                g.drawString("next: ", 288, 92);
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
                            g.fillRect(288 + (i % 4) * 12, 108 + i / 4 * 14, 14, 14);
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
                            g.fillRect(288 + (i % 4) * 12, 160 + i / 4 * 14, 14, 14);
                        }

                    }
                }
                g.setFont(new Font("aa", Font.PLAIN, 13));
            }
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
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (running) {
                            pause();
                            running = false;
                        } else resume();
                }
            }
        }
    }
//    class Cat implements ActionListener{
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            a.newGame();
//        }
//    }


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
            Tetris.this.requestFocus();
        }
    }
}
