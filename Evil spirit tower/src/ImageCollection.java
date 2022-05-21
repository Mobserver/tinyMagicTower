import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;


public class ImageCollection{

    private final Image hero = ImageIO.read(new FileInputStream("src\\resources\\hero.png"));
    private final Image wall = ImageIO.read(new FileInputStream("src\\resources\\wall.png"));
    private final Image uiWall = ImageIO.read(new FileInputStream("src\\resources\\UIwall.png"));
    private final Image road = ImageIO.read(new FileInputStream("src\\resources\\road.jpg"));
    public ImageCollection() throws IOException {
    }

    public Image getHeroImage(){ return hero; }
    public Image getWallImage(){ return wall; }
    public Image getUiWallImage(){ return uiWall; }
    public Image getRoadImage(){ return road; }


    //added
    private final Image S1Background1 = ImageIO.read(new FileInputStream("src\\resources\\state1\\layer1.png"));
    private final Image S1Background2 = ImageIO.read(new FileInputStream("src\\resources\\state1\\layer2.png"));
    private final Image S1Background3 = ImageIO.read(new FileInputStream("src\\resources\\state1\\layer3.png"));
    private final Image S1Background4 = ImageIO.read(new FileInputStream("src\\resources\\state1\\layer4.png"));
    private final Image S1Background5 = ImageIO.read(new FileInputStream("src\\resources\\state1\\layer5.png"));
    public Image getState1Background1(){return S1Background1;}
    public Image getState1Background2(){return S1Background2;}
    public Image getState1Background3(){return S1Background3;}
    public Image getState1Background4(){return S1Background4;}
    public Image getState1Background5(){return S1Background5;}
}
