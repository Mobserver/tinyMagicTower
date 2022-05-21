import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import static java.lang.Thread.sleep;
import java.awt.Graphics;
import java.util.Collections;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EvilspiritTower extends JPanel implements KeyListener{

    double dt;   //游戏帧数

    JMenu Play;
    JMenu Help;
    JMenu Quit;
    JMenuItem NewGame;    //上方菜单栏属性

    int gameState = 0;                              /* 游戏状态   0: playing  1: check help（点击上方菜单help）*/
    int countFrame = 0;                             //用来计算勇者的行走动画和怪的待机动画

    ImageCollection IC = new ImageCollection();     //用来存储图片的类，提前实例化
    Hero hero = new Hero();                         //实例化一个勇者


    public static void main(String[] args) throws InterruptedException, IOException {
        EvilspiritTower EST = new EvilspiritTower();
    } /*main函数，实例化魔塔游戏*/

    public EvilspiritTower() throws IOException, InterruptedException {
        setupWindow(960,640);
        while (true){
            dt = getFramerate(120);
            repaint();
        }
    }     /*类同名函数实例化时自动调用*/

    public double getFramerate(double framerate) throws InterruptedException {
        double interval = 1000.0 / framerate;
        sleep((long) interval);
        return interval / 1000.0;
    } /*设置游戏帧数*/

    public void setupWindow(int width, int height){
        JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("Evil spirit Tower");
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
        generateMenu();
        frame.setJMenuBar(bar);

        frame.setVisible(true);
        setDoubleBuffered(true);

        Insets insets = frame.getInsets();
        frame.setSize(width + insets.left + insets.right,
                height + insets.top + insets.bottom + 20);

    }                         /*建立游戏窗口*/

    public void generateMenu(){
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
                    gameState = 1;
                }
            }
        };
        Help.addMouseListener(hml);
    }                                            //建立上方菜单栏

    public void setNewGame(){

    }                                                 //从菜单栏new game开启一把新的游戏（未实现）

    public void paintComponent(Graphics g){
        changeBackgroundColor(g, 0,0,0);
        clearBackground(g, 960, 640);
        drawUI(g);
        drawBasemap(g);
        hero.heroWalk(g,countFrame);
        if (gameState == 1){
            changeBackgroundColor(g, 255,255,255);
            clearBackground(g, 960, 640);
            changeColor(g, Color.black);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            g.drawString("Rules of the game:",20,50);
            g.drawImage(IC.getState1Background1(),0,0,);
        }
    }                               //每帧都刷新的方法，用来刷新屏幕
    public void drawBasemap(Graphics g){
        for(int wallup = 256;wallup < 960; wallup = wallup +32){
            g.drawImage(IC.getWallImage(), wallup,0,wallup+32,32,0,0,32,32,null);
        }
        for(int walldown = 256;walldown < 960; walldown = walldown +32){
            g.drawImage(IC.getWallImage(),walldown,608,walldown+32,640,0,0,32,32,null);
        }
        for(int wallleft = 0;wallleft < 608; wallleft = wallleft +32){
            g.drawImage(IC.getWallImage(),256,wallleft,288,wallleft+32,0,0,32,32,null);
        }
        for(int wallright = 0;wallright < 608; wallright = wallright +32){
            g.drawImage(IC.getWallImage(),928,wallright,960,wallright+32,0,0,32,32,null);
        }
        for(int wallx = 288;wallx < 928; wallx = wallx +32){
            for(int wally = 32;wally < 608; wally = wally +32){
                g.drawImage(IC.getRoadImage(),wallx,wally,wallx+32,wally+32,96,0,128,32,null);
            }
        }
    }                                  //画基本的地图，四周墙和路
    public void drawUI(Graphics g){
        for(int wallx = 0;wallx < 256; wallx = wallx +32){
            for(int wally = 0;wally < 640; wally = wally +32){
                g.drawImage(IC.getUiWallImage(),wallx,wally,wallx+32,wally+32,0,0,32,32,null);
            }
        }
        g.drawImage(IC.getHeroImage(),60,75,110,125,0,0,32,32,null);
        changeColor(g, Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString("Level: " + String.valueOf(hero.getHeroLevel()),150,120);
        g.drawString("HP:",60,200);
        g.drawString(String.valueOf(hero.getHeroHP()),170,200);
        g.drawString("Attack:",60,250);
        g.drawString(String.valueOf(hero.getHeroAttack()),170,250);
        g.drawString("Defense:",60,300);
        g.drawString(String.valueOf(hero.getHeroDefense()),170,300);

    }                                       //画左侧UI

    public void changeBackgroundColor(Graphics g, int red, int green, int blue) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setBackground(new Color(red,green,blue));
    }   //这三方法是设置背景颜色、改变字体颜色
    public void clearBackground(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.clearRect(0, 0, width, height);
    }
    public void changeColor(Graphics g, Color c) {
        g.setColor(c);
    }

    public void keyTyped(KeyEvent e) {

    }                                        //这仨方法检测用户从键盘上的输入
    public void keyPressed(KeyEvent e) {
        if (gameState == 1){
            gameState = 0;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            hero.setHeroDirection("left");
            if(countFrame < 3){
                countFrame = countFrame + 1;
            }else {
                countFrame = 0;
            }
            if(hero.getHeroX() - 32 > 256){
                hero.setHeroX(hero.getHeroX() - 32);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            hero.setHeroDirection("right");
            if(countFrame < 3){
                countFrame = countFrame + 1;
            }else {
                countFrame = 0;
            }
            if(hero.getHeroX() + 32 < 928){
                hero.setHeroX(hero.getHeroX() + 32);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            hero.setHeroDirection("back");
            if(countFrame < 3){
                countFrame = countFrame + 1;
            }else {
                countFrame = 0;
            }
            if(hero.getHeroY() - 32 > 0){
                hero.setHeroY(hero.getHeroY() - 32);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            hero.setHeroDirection("front");
            if(countFrame < 3){
                countFrame = countFrame + 1;
            }else {
                countFrame = 0;
            }
            if(hero.getHeroY() + 32 < 608){
                hero.setHeroY(hero.getHeroY() + 32);
            }
        }
    }                                   //这个方法检测用户按下键盘某个键，在这里实现了勇者行走、四周墙壁的碰撞检测、在help界面按下任意键返回游戏
    public void keyReleased(KeyEvent e) {

    }


}
