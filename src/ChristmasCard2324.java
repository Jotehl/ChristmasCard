import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ChristmasCard2324 extends JPanel implements ActionListener, KeyListener {
    private JFrame window;
    public static final int WIDTH = 1000, HEIGHT = (int) (WIDTH / 1.75);

    Timer tm = new Timer(40, this);
//    double ticks = 0;

    //    Snow
    int numberOfBalls = 500;
    double[] ballXPos = new double[numberOfBalls];
    double[] ballYPos = new double[numberOfBalls];
    double[] ballVelX = new double[numberOfBalls];
    double[] ballVelY = new double[numberOfBalls];
    int[] snowSize = new int[numberOfBalls];

    int santaPosX = 200, santaPosY = 150, santaVelX, santaVelY = 0;

    Image backgroundImage, cabin, santaLeft, santaRight, santaDirection, tree, tree1, tree2 = null;
    BufferedImage bufferTree = null;
    File musicFile;
    Clip playMusic;
    int rotate, santaFlipValue;
    boolean drawImage, santaDirectionLeft = true;
    boolean run, rotation, music = false;


    public static void main(String[] args) {
        JFrame window = new JFrame("God Jul osv.");
        ChristmasCard2324 content = new ChristmasCard2324(window);
        window.setContentPane(content);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        content.setUp();
    }

    // Konstruktor?
    public ChristmasCard2324(JFrame window) {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        this.window = window;
        window.setIconImage(new ImageIcon("resources/images/favicon.png").getImage());
    }

    public void setUp() {
        try {
            backgroundImage = ImageIO.read(new File("resources/images/background.png")).getScaledInstance(WIDTH, -1, 0);
            cabin = ImageIO.read(new File("resources/images/cabin.png")).getScaledInstance(300, -1, 0);
            santaLeft = ImageIO.read(new File("resources/images/santaClause.png"));
            santaRight = ImageIO.read(new File("resources/images/santaClauseFlipped.png"));
            bufferTree = ImageIO.read(new File("resources/images/tree.png"));
            tree = bufferTree.getScaledInstance(150, -1, 0);
            tree1 = bufferTree.getScaledInstance(130, -1, 0);
            tree2 = bufferTree.getScaledInstance(140, -1, 0);
            musicFile = new File("resources/sound/christmasMusic.wav");
        } catch (IOException e) {
            System.out.println("ERROR: Couldn't read image " + e);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Failed to read image " + e);
        }


        for (int i = 0; i < numberOfBalls; i++) {
            ballXPos[i] = randomizerDouble(WIDTH - 10);
            ballYPos[i] = randomizerDouble(HEIGHT - 10);
            ballVelX[i] = randomizerDouble(1) + 0.25;
            ballVelY[i] = randomizerDouble(1) + 1;
            snowSize[i] = (int) (randomizerDouble(5) + 8);

            if (ballVelY[i] < 0) {
                ballVelY[i] *= -1;
            }
            if (ballVelY[i] < 1 && ballVelY[i] >= 0) {
                ballVelY[i] += 0.5;
            }
        }

        santaSetup();


        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        tm.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);

//      Drawing santa on the screen
        if (drawImage && santaDirectionLeft) {
            g.drawImage(santaLeft, santaPosX, santaPosY, this);
        }
        if (drawImage && !santaDirectionLeft) {
            g.drawImage(santaRight, santaPosX, santaPosY, this);
        }

//      Draw cabin
        g.drawImage(cabin, WIDTH / 2 + 50, HEIGHT - 300, this);

//      Draw trees
        g.drawImage(tree, 50, HEIGHT - 250, this);
        g.drawImage(tree1, 350, HEIGHT - 200, this);
        g.drawImage(tree2, 175, HEIGHT - 190, this);

//       Create snow. Color: HEX: fffafa
        g.setColor(new Color(255, 250, 255));
        for (int i = 0; i < numberOfBalls; i++) {
            g.fillOval((int) (ballXPos[i]), (int) (ballYPos[i]), snowSize[i], snowSize[i]);
        }

//      Santa flipping
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(rotate), santaPosX + santaFlipValue, santaPosY - 50);
        g2d.drawImage(santaDirection, santaPosX, santaPosY, this);


    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        for (int i = 0; i < numberOfBalls; i++) {
            ballXPos[i] += ballVelX[i];
            ballYPos[i] += ballVelY[i];

            if (ballYPos[i] + 10 > HEIGHT || ballYPos[i] <= 0) {
                ballYPos[i] = 0;
            }
            if (ballXPos[i] >= WIDTH) {
                ballXPos[i] = 0;
            }
        }


        if (rotation) {
            drawImage = false;
            if (santaDirectionLeft) {
                rotate += 8;
            } else {
                rotate -= 8;
            }
            if (rotate >= 358 || rotate <= -358) {
                rotation = false;
                rotate = 0;
                drawImage = true;
            }
        }

        if (santaPosX <= 0 || santaPosX >= WIDTH - 250) {
            santaVelX *= -1;
        }

        if (santaPosY > (int) (HEIGHT * 0.63) || santaPosY < -10 || santaPosY + 86 > HEIGHT * 0.75) {
            santaVelY *= -1;
        }


        if (santaVelX < 0) {
            santaDirectionLeft = true;
            santaDirection = santaLeft;
            santaFlipValue = 160;
        }
        if (santaVelX > 0) {
            santaDirectionLeft = false;
            santaDirection = santaRight;
            santaFlipValue = 90;
        }

        if (run) {
            santaPosX += santaVelX;
            santaPosY += santaVelY;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_A:
//              Start
                run = true;
                break;

            case KeyEvent.VK_S:
//              Stop
                run = false;
                if (music) {
                    playMusic.stop();
                    music = false;
                }
                break;

            case KeyEvent.VK_F:
//              Rotate
                rotation = true;
                break;

            case KeyEvent.VK_M:
//              Play music
                if (!music) {
                    try {
                        music = true;
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
                        playMusic = AudioSystem.getClip();
                        playMusic.open(audioInputStream);
                        playMusic.start();

                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        System.out.println("Error: " + e);
                    }
                } else {
                    music = false;
                    playMusic.stop();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public double randomizerDouble(int x) {
        return (Math.random() * x);
    }

    public void santaSetup() {
        santaVelX = (int) (Math.random() * 2);
        santaVelY = (int) (Math.random() * 2);

        if (santaVelX == 0) {
            santaVelX = -3;
        } else {
            santaVelX = 3;
        }

        if (santaVelY == 0) {
            santaVelY = -1;
        } else {
            santaVelY = 1;
        }
    }
}