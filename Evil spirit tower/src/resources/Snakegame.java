package resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import static java.lang.Thread.sleep;
import java.awt.Graphics;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;




public class Snakegame extends JPanel implements KeyListener {

    Image apple = ImageIO.read(new FileInputStream("src\\resources\\apple.png"));
    Image dot = ImageIO.read(new FileInputStream("src\\resources\\dot.png"));
    Image head = ImageIO.read(new FileInputStream("src\\resources\\head.png"));
    Image live = ImageIO.read(new FileInputStream("src\\resources\\live.png"));
    Image badapple = ImageIO.read(new FileInputStream("src\\resources\\badapple.png"));
    int gameState = 0; //0:playing 1:game over 2:check help 3:pause
    ArrayList<Segment> snakebody = new ArrayList<>();
    double dt = 1.0/60;
    int applex = -10, appley = -10;
    int badapplex = -10, badappley = -10;
    Random r = new Random(1);
    String direction = "right";
    int score = 0;
    JMenu Play;
    JMenu Help;
    JMenu Quit;
    JMenuItem NewGame;
    int lives = 4;
    int counttime = 0;
    int GameTime = 0;

    public static void main(String[] args) throws InterruptedException, IOException {
        Snakegame snakegame = new Snakegame();
    }

    public void setNewGame(){
        direction = "right";
        snakebody.clear();
        Segment segment1 = new Segment(120,100,head);
        Segment segment2 = new Segment(110,100,dot);
        Segment segment3 = new Segment(100,100,dot);
        snakebody.add(segment1);
        snakebody.add(segment2);
        snakebody.add(segment3);
        score = 0;
        lives = 4;
        GameTime = 0;
        gameState = 0; //playing
    }

    public Snakegame() throws IOException, InterruptedException {
        setupWindow(500,520);
        Segment segment1 = new Segment(120,100,head);
        Segment segment2 = new Segment(110,100,dot);
        Segment segment3 = new Segment(100,100,dot);
        snakebody.add(segment1);
        snakebody.add(segment2);
        snakebody.add(segment3);
        generateapple();
        while (true){
            dt = getFramerate(7);
            update();
            repaint();
        }
    }

    public void setupWindow(int width, int height){
        JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);

        JMenuBar bar = new JMenuBar();
        Play = new JMenu("Play");
        NewGame = new JMenuItem("NewGame");
        Play.add(NewGame);
        Help = new JMenu("Help");
        Quit = new JMenu("Quit");
        bar.add(Play);
        bar.add(Help);
        bar.add(Quit);
        generatemenu();
        frame.setJMenuBar(bar);

        frame.setVisible(true);
        setDoubleBuffered(true);

        Insets insets = frame.getInsets();
        frame.setSize(width + insets.left + insets.right,
                height + insets.top + insets.bottom);

    }

    public void generatemenu(){
        MouseListener qml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int modifiers = e.getModifiers();
                if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                    System.exit(0);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                Quit.setSelected(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Quit.setSelected(false);
            }
        };
        Quit.addMouseListener(qml);

        MouseListener ngml = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                NewGame.setSelected(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                NewGame.setSelected(false);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                int modifiers = e.getModifiers();
                if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                    setNewGame();
                }
            }
        };
        NewGame.addMouseListener(ngml);

        MouseListener pml = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Play.setSelected(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Play.setSelected(false);
            }
        };
        Play.addMouseListener(pml);

        MouseListener hml = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Help.setSelected(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Help.setSelected(false);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                int modifiers = e.getModifiers();
                if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                    gameState = 2;
                }
            }
        };
        Help.addMouseListener(hml);
    }

    public void update() {
        //If the game is playing
        if(gameState == 0) {
            checkAppleLocation();
            checkcollision();
            move();
            counttime = counttime + 1;
            if (counttime != 0 && counttime % 7 == 0){
                GameTime = GameTime + 1;
            }
            if (counttime == 70){
                generatebadapple();
                counttime = 0;
            }
            checkBadAppleLocation();
        }
    }

    public double getFramerate(double framerate) throws InterruptedException {
        double interval = 1000.0 / framerate;
        sleep((long) interval);
        return interval / 1000.0;
    }
    /*base*/

    public void generateapple(){
        int applex1 = r.nextInt(48);
        while(applex1 <= 2){
            applex1 = r.nextInt(48);
        }
        int appley1 = r.nextInt(48);
        while(appley1 <= 2){
            appley1 = r.nextInt(48);
        }
        for (Segment s : snakebody){
            while (s.getX()/10 == applex1 && s.getY()/10 == appley1){
                applex1 = r.nextInt(48);
                while(applex1 <= 2){
                    applex1 = r.nextInt(48);
                }
                appley1 = r.nextInt(48);
                while(appley1 <= 2){
                    appley1 = r.nextInt(48);
                }
            }
        }
        applex = applex1 * 10;
        appley = appley1 * 10;

    }

    public void generatebadapple(){
        int applex1 = r.nextInt(48);
        while(applex1 <= 2){
            applex1 = r.nextInt(48);
        }
        int appley1 = r.nextInt(48);
        while(appley1 <= 2){
            appley1 = r.nextInt(48);
        }
        for (Segment s : snakebody){
            while ((s.getX()/10 == applex1 && s.getY()/10 == appley1) || (applex1*10 == applex && appley1*10 == appley)){
                applex1 = r.nextInt(48);
                while(applex1 <= 2){
                    applex1 = r.nextInt(48);
                }
                appley1 = r.nextInt(48);
                while(appley1 <= 2){
                    appley1 = r.nextInt(48);
                }
            }
        }
        badapplex = applex1 * 10;
        badappley = appley1 * 10;

    }

    public void checkAppleLocation(){
        if (snakebody.get(0).getX() == applex && snakebody.get(0).getY() == appley){
            generateapple();
            score = score + 1;
            if (snakebody.size() < 20){
                snakebody.add(1,new Segment(snakebody.get(0).getX(),snakebody.get(0).getY(),dot));
            }
        }
    }
    public void checkBadAppleLocation(){
        if (snakebody.get(0).getX() == badapplex && snakebody.get(0).getY() == badappley){
            lives = lives - 1;
            if (lives == 0){
                gameState = 1;
            }
            badapplex = -10;
            badappley = -10;
        }
    }

    public void checkcollision(){
        int headx = snakebody.get(0).getX();
        int heady = snakebody.get(0).getY();
        if (heady < 10){
            gameState = 1;
        }
        if (heady >= 490){
            gameState = 1;
        }
        if (headx < 10){
            gameState = 1;
        }
        if (headx >= 490){
            gameState = 1;
        }
        for (Segment s : snakebody){
            if (s != snakebody.get(0) && s != snakebody.get(1)){
                if (s.getX() == snakebody.get(0).getX() && s.getY() == snakebody.get(0).getY()){
                    gameState = 1;
                }
            }
        }
    }

    public void move(){
        if (direction.equals("right")){
            rightDirection(snakebody.get(0));
        }
        if (direction.equals("left")){
            leftDirection(snakebody.get(0));
        }
        if (direction.equals("up")){
            upDirection(snakebody.get(0));
        }
        if (direction.equals("down")){
            downDirection(snakebody.get(0));
        }
    }

    public void leftDirection(Segment s){
        int oldx = s.getX();
        s.setX(oldx - 10);
        snakebody.remove(snakebody.size()-1);
        snakebody.add(1,new Segment(oldx,s.getY(),dot));
    }

    public void rightDirection(Segment s){
        int oldx = s.getX();
        s.setX(oldx + 10);
        snakebody.remove(snakebody.size()-1);
        snakebody.add(1,new Segment(oldx,s.getY(),dot));
    }

    public void upDirection(Segment s){
        int oldy = s.getY();
        s.setY(oldy - 10);
        snakebody.remove(snakebody.size()-1);
        snakebody.add(1,new Segment(s.getX(),oldy,dot));
    }

    public void downDirection(Segment s){
        int oldy = s.getY();
        s.setY(oldy + 10);
        snakebody.remove(snakebody.size()-1);
        snakebody.add(1,new Segment(s.getX(),oldy,dot));
    }
    /*event*/


    public void changeBackgroundColor(Graphics g, int red, int green, int blue) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setBackground(new Color(red,green,blue));
    }
    public void clearBackground(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.clearRect(0, 0, width, height);
    }
    public void changeColor(Graphics g, Color c) {
        g.setColor(c);
    }
    public void paintComponent(Graphics g){
        changeBackgroundColor(g, 0,0,0);
        clearBackground(g, 500, 500);
        changeColor(g, Color.blue);
        g.fillRect(0,0,500,10);
        g.fillRect(0,0,10,500);
        g.fillRect(490,0,10,500);
        g.fillRect(0,490,500,10);
        changeColor(g, Color.red);
        g.drawString("Score: " + String.valueOf(score),15,25);
        g.drawString("GameTime: " + String.valueOf(GameTime) + "s",350,25);
        for (Segment s : snakebody){
            g.drawImage(s.getImage(),s.getX(),s.getY(),null);
        }
        g.drawImage(apple,applex,appley,null);
        for (int i=0;i<lives;i++){
            g.drawImage(live,75 + i * 12,17,null);
        }
        g.drawImage(badapple,badapplex,badappley,null);
        if (gameState == 1){
            changeBackgroundColor(g, 255,255,255);
            clearBackground(g, 500, 500);
            changeColor(g, Color.black);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g.drawString("Game Over",120,200);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("Press any key to start a new game",95,300);
        }
        if (gameState == 2){
            changeBackgroundColor(g, 255,255,255);
            clearBackground(g, 500, 500);
            changeColor(g, Color.black);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            g.drawString("Rules of the game:",20,50);
            g.drawString("      Control the snake to eat apples. The more apples the snake",50,70);
            g.drawString("eats, the longer the body will be. When the snake collides the",50,90);
            g.drawString("boundary of the map or collides itself, the game is over.",50,110);
            g.drawString("Please notice that, poisonous apples will generate periodically",50,130);
            g.drawString("on the field, and the normal apple will refresh only after you eat it.",50,150);
            g.drawString("The poisonous apples are blue and normal apples are green.",50,170);
            g.drawString("Every time you eat a poisoned apple, you will reduce your",50,190);
            g.drawString("HP, if your HP is 0, the game is over.",50,210);
            g.drawString("You are welcome to challenge yourself to get higher scores.",50,230);

            g.drawString("P: pause.",50,270);
            g.drawString("[WASD] OR [↑↓←→]: control the snake.",50,290);
            g.drawString("Press any key to continue",50,330);
        }
        if (gameState == 3){
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("Press Space to continue",150,200);
        }
    }
    /*draw*/



    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            if (direction != "right"){
                direction = "left";
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            if (direction != "left"){
                direction = "right";
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            if (direction != "down"){
                direction = "up";
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            if (direction != "up"){
                direction = "down";
            }
        }
        if (gameState == 1 ){
            setNewGame();
        }
        if (gameState == 2 ){
            gameState = 0;
        }
        if(e.getKeyCode() == KeyEvent.VK_P && gameState == 0){
            gameState = 3;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE && gameState == 3){
            gameState = 0;
        }

    }

    public void keyReleased(KeyEvent e) {

    }
    /*input*/

}

