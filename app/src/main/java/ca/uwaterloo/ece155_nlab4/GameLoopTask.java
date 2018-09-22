package ca.uwaterloo.ece155_nlab4;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.TimerTask;

/**
 * Created by ChaosK9 on 2017-06-27.
 */

public class GameLoopTask extends TimerTask{
    private Activity myActivity;
    private Context myContext;
    private RelativeLayout myRL;
    private boolean mergeCheck = false;
    private int time = 0;
    private static int topb = -44;
    private static int leftb = -44;
    private static int interval = 250;
    private boolean createBlock = false;
    private boolean moveCheck = false;
    public boolean mergeA = false;
    public boolean moving = true;
    //private GameBlock newBlock;
    LinkedList<GameBlock> myBlocks = new LinkedList<>();
    LinkedList<GameBlock> xBlocks1 = new LinkedList<>();
    LinkedList<GameBlock> xBlocks2 = new LinkedList<>();
    LinkedList<GameBlock> xBlocks3 = new LinkedList<>();
    LinkedList<GameBlock> xBlocks4 = new LinkedList<>();
    LinkedList<GameBlock> yBlocks1 = new LinkedList<>();
    LinkedList<GameBlock> yBlocks2 = new LinkedList<>();
    LinkedList<GameBlock> yBlocks3 = new LinkedList<>();
    LinkedList<GameBlock> yBlocks4 = new LinkedList<>();
    LinkedList<GameBlock> blockDelete = new LinkedList<>();
    public enum Directions {UP,DOWN,LEFT,RIGHT,NO_MOVEMENT};
    Directions direction = Directions.NO_MOVEMENT;
    Directions direction2 = Directions.NO_MOVEMENT;
    public GameLoopTask(Activity _myActivity, Context _myContext, RelativeLayout _myRL){
        myActivity = _myActivity;
        myContext = _myContext;
        myRL = _myRL;
        createBlock();
    }

    public void run(){
        myActivity.runOnUiThread(new Runnable(){
            public void run(){//Insert your Periodic Tasks Here!
                time += 20;
                /*if(time%1000 == 0){
                    Log.d("Time: ", ""+time);
                }*/
                moving = true;
                xBlocks1.clear();
                xBlocks2.clear();
                xBlocks3.clear();
                xBlocks4.clear();
                yBlocks1.clear();
                yBlocks2.clear();
                yBlocks3.clear();
                yBlocks4.clear();
                for(GameBlock gb : myBlocks){
                    if(!gb.remove && direction != Directions.NO_MOVEMENT){
                        if(gb.cX == -44){
                            xBlocks1.add(gb);
                        }
                        if(gb.cX == -44+250){
                            xBlocks2.add(gb);
                        }
                        if(gb.cX == -44+250*2){
                            xBlocks3.add(gb);
                        }
                        if(gb.cX == -44+250*3){
                            xBlocks4.add(gb);
                        }
                        if(gb.cY == -44){
                            yBlocks1.add(gb);
                        }
                        if(gb.cY == -44+250){
                            yBlocks2.add(gb);
                        }
                        if(gb.cY == -44+250*2){
                            yBlocks3.add(gb);
                        }
                        if(gb.cY == -44+250*3){
                            yBlocks4.add(gb);
                        }
                    }
                }
                for(GameBlock gb : myBlocks) {
                    if(mergeCheck == false) {
                        if (!gb.remove) {
                            if (direction == Directions.UP) {
                                //gb.mergeUp(myBlocks);
                                upMerge(xBlocks1);
                                upMerge(xBlocks2);
                                upMerge(xBlocks3);
                                upMerge(xBlocks4);
                            } else if (direction == Directions.DOWN) {
                                downMerge(xBlocks1);
                                downMerge(xBlocks2);
                                downMerge(xBlocks3);
                                downMerge(xBlocks4);
                                //gb.mergeDown(myBlocks);
                            }else if (direction == Directions.LEFT) {
                                //gb.mergeLeft(myBlocks);
                                leftMerge(yBlocks1);
                                leftMerge(yBlocks2);
                                leftMerge(yBlocks3);
                                leftMerge(yBlocks4);
                            } else if (direction == Directions.RIGHT) {
                                rightMerge(yBlocks1);
                                rightMerge(yBlocks2);
                                rightMerge(yBlocks3);
                                rightMerge(yBlocks4);
                                //gb.mergeRight(myBlocks);
                            }
                        }
                        mergeCheck = true;
                    }
                }
                moveCheck = false;
                for(GameBlock gb : myBlocks){
                    gb.move();
                    if((gb.velX != 0 || gb.velY != 0) && !gb.remove || (gb.cY != gb.y1 && gb.cY != gb.y2 && (direction == Directions.UP || direction == Directions.DOWN) || gb.cX != gb.x1 && gb.cX != gb.x2 && (direction == Directions.LEFT || direction == Directions.RIGHT))){ //If one block moves or isnt at final destination then check move is true
                        moveCheck = true;
                    }
                }
                if(myBlocks.size() < 16 && createBlock && !moveCheck){
                    for(GameBlock gb : myBlocks){
                        if(gb.remove){
                            blockDelete.add(gb);
                        }
                    }
                    for(GameBlock gb : blockDelete){
                        myRL.removeView(gb.mytv);
                        myRL.removeView(gb);
                        myBlocks.remove(gb);

                    }
                    createBlock();
                    for(GameBlock gb : myBlocks){
                        if(!gb.remove) {
                            gb.numberUp(myBlocks);
                            gb.numberDown(myBlocks);
                            gb.numberLeft(myBlocks);
                            gb.numberRight(myBlocks);
                        }
                    }
                    setDirection(Directions.NO_MOVEMENT);
                    for(GameBlock gb : myBlocks){
                        gb.mytv.setText(" " + gb.value);
                        gb.mytv.bringToFront();
                    }
                    //Log.d("moveCheck", " " + moveCheck);
                    createBlock = false;
                    Log.d("Number of Blocks", " " + myBlocks.size());
                }

            }
        });
    }

    private void createBlock(){
        int x = ((int)(Math.random()*4))*250 - 44;
        int y = ((int)(Math.random()*4))*250 - 44;
        boolean make = true;
        while(make) {
            x = ((int)(Math.random()*4))*250 - 44;
            y = ((int)(Math.random()*4))*250 - 44;
            make = false;
            for (GameBlock gb : myBlocks) {
                if (x == gb.cX && y == gb.cY) {
                    make = true;
                }
            }
            if(myBlocks.size() == 0){
                break;
            }
        }
        GameBlock newBlock = new GameBlock(myContext, x, y, myRL);
        myBlocks.add(newBlock);
    }

    public void setDirection(Directions _direction){
        mergeCheck = false;
        this.direction = _direction;
        //Log.d("Direction: ", "CHANGED");
        for (int i = 0; i < myBlocks.size(); i++) {
            myBlocks.get(i).setBlockDirection(this.direction);
        }
        createBlock = true;
    }

    public void upMerge(LinkedList<GameBlock> blocks){
        //Log.d("@@@@@@@@@@@@@@@@@@@@", "TOTAL" + blocks.size());
        LinkedList<GameBlock> sortBlocks = new LinkedList<>();
        for(int i=0; i<blocks.size(); i++){
            if(-44 == blocks.get(i).cY){
                sortBlocks.add(blocks.get(i));
                ////Log.d("HIT", "upMerge: Start");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250 == blocks.get(i).cY) {
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End1");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250*2 == blocks.get(i).cY){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End2");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250*3 == blocks.get(i).cY){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End3");
            }
        }
        if(sortBlocks.size() == 1){
            sortBlocks.get(0).y1 = -44;
        }else if(sortBlocks.size() == 2 && sortBlocks.get(0).value == sortBlocks.get(1).value){
            sortBlocks.get(0).y1 = -44;
            sortBlocks.get(1).y1 = -44;
            sortBlocks.get(1).remove = true;
            sortBlocks.get(0).value *= 2;
            //Log.d("Delete", "2 Blocks");
        }else if(sortBlocks.size() == 3){
            if(sortBlocks.get(0).value == sortBlocks.get(1).value){
                sortBlocks.get(0).y1 = -44;
                sortBlocks.get(1).y1 = -44;
                sortBlocks.get(1).remove = true;
                sortBlocks.get(2).y1 = -44+250;
                sortBlocks.get(0).value *= 2;
            }else if(sortBlocks.get(1).value == sortBlocks.get(2).value){
                sortBlocks.get(0).y1 = -44;
                sortBlocks.get(1).y1 = -44+250;
                sortBlocks.get(2).y1 = -44+250;
                sortBlocks.get(2).remove = true;
                sortBlocks.get(1).value *= 2;
            }
            //Log.d("Delete", "3 Blocks");
        }else if(sortBlocks.size() == 4){
            if(sortBlocks.get(0).value == sortBlocks.get(1).value) {
                sortBlocks.get(0).y1 = -44;
                sortBlocks.get(1).y1 = -44;
                sortBlocks.get(1).remove = true;
                sortBlocks.get(0).value *= 2;
                if(sortBlocks.get(2).value == sortBlocks.get(3).value){
                    sortBlocks.get(2).y1 = -44 + 250;
                    sortBlocks.get(3).y1 = -44 + 250;
                    sortBlocks.get(3).remove = true;
                    sortBlocks.get(2).value *= 2;
                }else {
                    sortBlocks.get(2).y1 = -44 + 250;
                    sortBlocks.get(3).y1 = -44 + 250*2;
                }
            }else if(sortBlocks.get(1).value == sortBlocks.get(2).value){
                sortBlocks.get(0).y1 = -44;
                sortBlocks.get(1).y1 = -44+250;
                sortBlocks.get(2).y1 = -44+250;
                sortBlocks.get(2).remove = true;
                sortBlocks.get(1).value *= 2;
                sortBlocks.get(3).y1 = -44+250*2;
            }else if(sortBlocks.get(2).value == sortBlocks.get(3).value){
                sortBlocks.get(0).y1 = -44;
                sortBlocks.get(1).y1 = -44+250;
                sortBlocks.get(2).y1 = -44+250*2;
                sortBlocks.get(3).y1 = -44+250*2;
                sortBlocks.get(3).remove = true;
                sortBlocks.get(2).value *= 2;
            }
            //Log.d("Delete", "4 Blocks");
        }
        sortBlocks.clear();
        blocks.clear();
    }

    public void downMerge(LinkedList<GameBlock> blocks){
        //Log.d("@@@@@@@@@@@@@@@@@@@@", "TOTAL" + blocks.size());
        LinkedList<GameBlock> sortBlocks = new LinkedList<>();
        for(int i=0; i<blocks.size(); i++){
            if(-44 == blocks.get(i).cY){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: Start");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250 == blocks.get(i).cY) {
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End1");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250*2 == blocks.get(i).cY){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End2");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250*3 == blocks.get(i).cY){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End3");
            }
        }
        if(sortBlocks.size() == 1){
            sortBlocks.get(0).y2 = -44+250*3;
        }else if(sortBlocks.size() == 2 && sortBlocks.get(0).value == sortBlocks.get(1).value){
            sortBlocks.get(0).y2 = -44+250*3;
            sortBlocks.get(1).y2 = -44+250*3;
            sortBlocks.get(1).remove = true;
            sortBlocks.get(0).value *= 2;
            //Log.d("Delete", "2 Blocks");
        }else if(sortBlocks.size() == 3){
            if(sortBlocks.get(1).value == sortBlocks.get(2).value){
                sortBlocks.get(2).y2 = -44+250*3;
                sortBlocks.get(1).y2 = -44+250*3;
                sortBlocks.get(1).remove = true;
                sortBlocks.get(0).y2 = -44+250*2;
                sortBlocks.get(2).value *= 2;
            }else if(sortBlocks.get(0).value == sortBlocks.get(1).value){
                sortBlocks.get(2).y2 = -44+250*3;
                sortBlocks.get(1).y2 = -44+250*2;
                sortBlocks.get(0).y2 = -44+250*2;
                sortBlocks.get(0).remove = true;
                sortBlocks.get(1).value *= 2;
            }
            //Log.d("Delete", "3 Blocks");
        }else if(sortBlocks.size() == 4){
            if(sortBlocks.get(3).value == sortBlocks.get(2).value) {
                sortBlocks.get(3).y2 = -44+250*3;
                sortBlocks.get(2).y2 = -44+250*3;
                sortBlocks.get(2).remove = true;
                sortBlocks.get(3).value *= 2;
                if(sortBlocks.get(0).value == sortBlocks.get(1).value){
                    sortBlocks.get(1).y2 = -44+250*2;
                    sortBlocks.get(0).y2 = -44+250*2;
                    sortBlocks.get(0).remove = true;
                    sortBlocks.get(1).value *= 2;
                }else {
                    sortBlocks.get(1).y2 = -44 + 250*2;
                    sortBlocks.get(0).y2 = -44 + 250;
                }
            }else if(sortBlocks.get(2).value == sortBlocks.get(1).value){
                sortBlocks.get(3).y2 = -44+250*3;
                sortBlocks.get(2).y2 = -44+250*2;
                sortBlocks.get(1).y2 = -44+250*2;
                sortBlocks.get(1).remove = true;
                sortBlocks.get(2).value *= 2;
                sortBlocks.get(0).y2 = -44+250;
            }else if(sortBlocks.get(1).value == sortBlocks.get(0).value){
                sortBlocks.get(3).y2 = -44+250*3;
                sortBlocks.get(2).y2 = -44+250*2;
                sortBlocks.get(1).y2 = -44+250;
                sortBlocks.get(0).y2 = -44+250;
                sortBlocks.get(0).remove = true;
                sortBlocks.get(1).value *= 2;
            }
            //Log.d("Delete", "4 Blocks");
        }
        sortBlocks.clear();
        blocks.clear();
    }

    public void leftMerge(LinkedList<GameBlock> blocks){
        //Log.d("@@@@@@@@@@@@@@@@@@@@", "TOTAL" + blocks.size());
        LinkedList<GameBlock> sortBlocks = new LinkedList<>();
        for(int i=0; i<blocks.size(); i++){
            if(-44 == blocks.get(i).cX){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: Start");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250 == blocks.get(i).cX) {
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End1");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250*2 == blocks.get(i).cX){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End2");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250*3 == blocks.get(i).cX){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End3");
            }
        }
        if(sortBlocks.size() == 1){
            sortBlocks.get(0).x1 = -44;
        }else if(sortBlocks.size() == 2 && sortBlocks.get(0).value == sortBlocks.get(1).value){
            sortBlocks.get(0).x1 = -44;
            sortBlocks.get(1).x1 = -44;
            sortBlocks.get(1).remove = true;
            sortBlocks.get(0).value *= 2;
            //Log.d("Delete", "2 Blocks");
        }else if(sortBlocks.size() == 3){
            if(sortBlocks.get(0).value == sortBlocks.get(1).value){
                sortBlocks.get(0).x1 = -44;
                sortBlocks.get(1).x1 = -44;
                sortBlocks.get(1).remove = true;
                sortBlocks.get(2).x1 = -44+250;
                sortBlocks.get(0).value *= 2;
            }else if(sortBlocks.get(1).value == sortBlocks.get(2).value){
                sortBlocks.get(0).x1 = -44;
                sortBlocks.get(1).x1 = -44+250;
                sortBlocks.get(2).x1 = -44+250;
                sortBlocks.get(2).remove = true;
                sortBlocks.get(1).value *= 2;
            }
            //Log.d("Delete", "3 Blocks");
        }else if(sortBlocks.size() == 4){
            if(sortBlocks.get(0).value == sortBlocks.get(1).value) {
                sortBlocks.get(0).x1 = -44;
                sortBlocks.get(1).x1 = -44;
                sortBlocks.get(1).remove = true;
                sortBlocks.get(0).value *= 2;
                if(sortBlocks.get(2).value == sortBlocks.get(3).value){
                    sortBlocks.get(2).x1 = -44 + 250;
                    sortBlocks.get(3).x1 = -44 + 250;
                    sortBlocks.get(3).remove = true;
                    sortBlocks.get(2).value *= 2;
                }else {
                    sortBlocks.get(2).x1 = -44 + 250;
                    sortBlocks.get(3).x1 = -44 + 250*2;
                }
            }else if(sortBlocks.get(1).value == sortBlocks.get(2).value){
                sortBlocks.get(0).x1 = -44;
                sortBlocks.get(1).x1 = -44+250;
                sortBlocks.get(2).x1 = -44+250;
                sortBlocks.get(2).remove = true;
                sortBlocks.get(1).value *= 2;
                sortBlocks.get(3).x1 = -44+250*2;
            }else if(sortBlocks.get(2).value == sortBlocks.get(3).value){
                sortBlocks.get(0).x1 = -44;
                sortBlocks.get(1).x1 = -44+250;
                sortBlocks.get(2).x1 = -44+250*2;
                sortBlocks.get(3).x1 = -44+250*2;
                sortBlocks.get(3).remove = true;
                sortBlocks.get(2).value *= 2;
            }
            //Log.d("Delete", "4 Blocks");
        }
        sortBlocks.clear();
        blocks.clear();
    }

    public void rightMerge(LinkedList<GameBlock> blocks){
        //Log.d("@@@@@@@@@@@@@@@@@@@@", "TOTAL" + blocks.size());
        LinkedList<GameBlock> sortBlocks = new LinkedList<>();
        for(int i=0; i<blocks.size(); i++){
            if(-44 == blocks.get(i).cX){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: Start");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250 == blocks.get(i).cX) {
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End1");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250*2 == blocks.get(i).cX){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End2");
            }
        }
        for(int i=0; i<blocks.size(); i++){
            if(-44+250*3 == blocks.get(i).cX){
                sortBlocks.add(blocks.get(i));
                //Log.d("HIT", "upMerge: End3");
            }
        }
        Log.d("AAAAA", "rightMerge: " + sortBlocks.size());
        if(sortBlocks.size() == 1){
            sortBlocks.get(0).x2 = -44+250*3;
        }else if(sortBlocks.size() == 2 && sortBlocks.get(0).value == sortBlocks.get(1).value){
            sortBlocks.get(0).x2 = -44+250*3;
            sortBlocks.get(1).x2 = -44+250*3;
            sortBlocks.get(1).remove = true;
            sortBlocks.get(0).value *= 2;
            //Log.d("Delete", "2 Blocks");
        }else if(sortBlocks.size() == 3){
            if(sortBlocks.get(1).value == sortBlocks.get(2).value){
                sortBlocks.get(2).x2 = -44+250*3;
                sortBlocks.get(1).x2 = -44+250*3;
                sortBlocks.get(1).remove = true;
                sortBlocks.get(0).x2 = -44+250*2;
                sortBlocks.get(2).value *= 2;
            }else if(sortBlocks.get(0).value == sortBlocks.get(1).value){
                sortBlocks.get(2).x2 = -44+250*3;
                sortBlocks.get(1).x2 = -44+250*2;
                sortBlocks.get(0).x2 = -44+250*2;
                sortBlocks.get(0).remove = true;
                sortBlocks.get(1).value *= 2;
            }
            //Log.d("Delete", "3 Blocks");
        }else if(sortBlocks.size() == 4){
            if(sortBlocks.get(3).value == sortBlocks.get(2).value) {
                sortBlocks.get(3).x2 = -44+250*3;
                sortBlocks.get(2).x2 = -44+250*3;
                sortBlocks.get(2).remove = true;
                sortBlocks.get(3).value *= 2;
                if(sortBlocks.get(0).value == sortBlocks.get(1).value){
                    sortBlocks.get(1).x2 = -44+250*2;
                    sortBlocks.get(0).x2 = -44+250*2;
                    sortBlocks.get(0).remove = true;
                    sortBlocks.get(1).value *= 2;
                }else {
                    sortBlocks.get(1).x2 = -44 + 250*2;
                    sortBlocks.get(0).x2 = -44 + 250;
                }
            }else if(sortBlocks.get(2).value == sortBlocks.get(1).value){
                sortBlocks.get(3).x2 = -44+250*3;
                sortBlocks.get(2).x2 = -44+250*2;
                sortBlocks.get(1).x2 = -44+250*2;
                sortBlocks.get(1).remove = true;
                sortBlocks.get(2).value *= 2;
                sortBlocks.get(0).x2 = -44+250;
            }else if(sortBlocks.get(1).value == sortBlocks.get(0).value){
                sortBlocks.get(3).x2 = -44+250*3;
                sortBlocks.get(2).x2 = -44+250*2;
                sortBlocks.get(1).x2 = -44+250;
                sortBlocks.get(0).x2 = -44+250;
                sortBlocks.get(0).remove = true;
                sortBlocks.get(1).value *= 2;
            }
            //Log.d("Delete", "4 Blocks");
        }
        sortBlocks.clear();
        blocks.clear();
    }
}