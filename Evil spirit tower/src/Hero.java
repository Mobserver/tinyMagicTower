import java.awt.*;
import java.io.IOException;

public class Hero {
    private int Level = 1;
    private int HP = 1000;
    private int Attack = 10;
    private int Defense = 10;
    private int heroX = 576,heroY = 576;
    private String heroDirection = "front";   //勇者的属性、位置、朝向
    ImageCollection IC = new ImageCollection();  //用来存储图片的类，提前实例化

    public Hero() throws IOException {
    }

    public void heroWalk(Graphics g,int countFrame){
        if (heroDirection.equals("front")){
            g.drawImage(IC.getHeroImage(),heroX,heroY,heroX+32,heroY+32,countFrame*32,0,32 + countFrame*32,32,null);
        }
        if (heroDirection.equals("back")){
            g.drawImage(IC.getHeroImage(),heroX,heroY,heroX+32,heroY+32,countFrame*32,100,32 + countFrame*32,132,null);
        }
        if (heroDirection.equals("left")){
            g.drawImage(IC.getHeroImage(),heroX,heroY,heroX+32,heroY+32,countFrame*32,34,32 + countFrame*32,66,null);
        }
        if (heroDirection.equals("right")){
            g.drawImage(IC.getHeroImage(),heroX,heroY,heroX+32,heroY+32,countFrame*32,67,32 + countFrame*32,99,null);
        }
    }  //勇者行走实现

    public void setHeroLevel(int level){
        Level = level;
    }
    public void setHeroHP(int hp){
        HP = hp;
    }
    public void setHeroAttack(int attack){
        Attack = attack;
    }
    public void setHeroDefense(int defense){
        Defense = defense;
    }
    public void setHeroX(int x){
        heroX = x;
    }
    public void setHeroY(int y){
        heroY = y;
    }
    public void setHeroDirection(String s){
        heroDirection = s;
    }

    public String getHeroDirection(){
        return heroDirection;
    }
    public int getHeroLevel(){
        return Level;
    }
    public int getHeroHP(){
        return HP;
    }
    public int getHeroAttack(){
        return Attack;
    }
    public int getHeroDefense(){
        return Defense;
    }
    public int getHeroX(){
        return heroX;
    }
    public int getHeroY(){
        return heroY;
    }
}
