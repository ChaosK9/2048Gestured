package ca.uwaterloo.ece155_nlab4;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

import lab2_204_01.uwaterloo.ca.lab4_204_01.R;

/**
 * Created by ChaosK9 on 2017-06-28.
 */

public class GameBlock extends GameBlockTemplate{
    private final float IMAGE_SCALE = 0.6f;
    private int myCoordX;
    private int myCoordY;
    public int x1 = -44;
    public int x2 = 706;
    public int y1 = -44;
    public int y2 = 706;
    public int xRank=-1;
    public int yRank=-1;
    public int cX;
    public int cY;
    public int tX;
    public int tY;
    public int velX;
    public int velY;
    public int value;
    public int xdelete=-1;
    public int ydelete=-1;
    public boolean remove = false;
    private RelativeLayout myRL;
    public TextView mytv;
    private GameLoopTask.Directions direction = GameLoopTask.Directions.NO_MOVEMENT;
    public GameBlock(Context _myContext, int coordX, int coordY, RelativeLayout _myRL){
        super(_myContext);
        myCoordX = coordX;
        myCoordY = coordY;
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.setX(myCoordX);
        this.setY(myCoordY);
        cX = myCoordX;
        cY = myCoordY;
        tX = -44;
        tY = -44;
        velX = 0;
        velY = 0;
        myRL = _myRL;
        myRL.addView(this);
        value = ((int)(Math.random()*2+1))*2;
        mytv = new TextView(_myContext);
        mytv.setText(" " + value);
        mytv.setTextColor(Color.BLACK);
        mytv.setX(myCoordX+100);
        mytv.setY(myCoordY+100);
        myRL.addView(mytv);
        mytv.setTextSize(40);
        mytv.bringToFront();
    }
    public void setBlockDirection(GameLoopTask.Directions _direction){
        direction = _direction;
    }

    public void move(){
        switch(direction){
            case UP:
                tY = y1;
                velY=velY+3;
                if(cY-velY>tY){
                    cY -= velY;
                }else{
                    velY = 0;
                    cY=tY;
                }
                this.setY(cY);
                mytv.setY(cY+100);
                break;
            case DOWN:
                tY = y2;
                velY=velY+3;
                if(cY+velY<tY){
                    cY += velY;
                }else{
                    velY = 0;
                    cY=tY;
                }
                this.setY(cY);
                mytv.setY(cY+100);
                break;
            case LEFT:
                tX = x1;
                velX=velX+3;;
                if(cX-velX>tX){
                    cX -= velX;
                }else{
                    velX = 0;
                    cX=tX;
                }
                this.setX(cX);
                mytv.setX(cX+100);
                break;
            case RIGHT:
                tX = x2;
                velX=velX+3;
                if(cX+velX<tX){
                    cX += velX;
                }else{
                    velX = 0;
                    cX=tX;
                }
                this.setX(cX);
                mytv.setX(cX+100);
                break;
            default:
                break;
        }
    }

    @Override
    public void setDestination() {

    }

    public int getcX(){
        return cX;
    }
    public int getcY(){
        return cY;
    }
    public int gettX(){
        return tX;
    }
    public int gettY(){
        return tY;
    }

    public GameLoopTask.Directions getDirection(){
        return direction;
    }

    public void changetx(int a){
        this.tX = a;
    }
    public void changety(int a){
        this.tY = a;
    }
    public void changecx(int a){
        this.cX = a;
    }
    public void changecy(int a){
        this.cY = a;
    }

    public void numberUp(LinkedList<GameBlock> blocks){
        int n = 0;
        for(GameBlock gb : blocks){
            if(gb.cX == this.cX && !this.remove){
                if(this.cY>gb.cY){
                    n++;
                }
            }
        }
        this.y1=n*250-44;
    }
    public void numberDown(LinkedList<GameBlock> blocks){
        int n = 3;
        for(GameBlock gb : blocks){
            if(gb.cX == this.cX && !this.remove){
                if(this.cY<gb.cY){
                    n--;
                }
            }
        }
        this.y2=n*250-44;
    }
    public void numberLeft(LinkedList<GameBlock> blocks){
        int n = 0;
        for(GameBlock gb : blocks){
            if(gb.cY == this.cY && !this.remove){
                if(this.cX>gb.cX){
                    n++;
                }
            }
        }
        this.x1=n*250-44;
    }
    public void numberRight(LinkedList<GameBlock> blocks){
        int n = 3;
        for(GameBlock gb : blocks){
            if(gb.cY == this.cY && !this.remove){
                if(this.cX<gb.cX){
                    n--;
                }
            }
        }
        this.x2=n*250-44;
    }
}
