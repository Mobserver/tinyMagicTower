package resources;

import java.awt.*;

public class Segment {
    private int x;
    private int y;
    private Image image;
    public Segment(int a, int b, Image i){
        x = a;
        y = b;
        image = i;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public Image getImage(){
        return image;
    }


    public void setX(int num){
        x = num;
    }

    public void setY(int num){
        y = num;
    }


}
