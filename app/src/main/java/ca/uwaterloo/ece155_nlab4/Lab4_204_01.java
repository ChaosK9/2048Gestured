package ca.uwaterloo.ece155_nlab4;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.PrintWriter;
import java.util.Timer;

import lab2_204_01.uwaterloo.ca.lab4_204_01.R;

public class Lab4_204_01 extends AppCompatActivity {
    // THRESHOLD Constants stored in arrays
    //Index[0]=THRES_A1
    //Index[1]=THRES_A2
    //Index[2]=THRES_A3
    //Index[3]=THRES_B1
    //Index[4]=THRES_B2
    //Index[5]=THRES_B3

    final float[] X_THRES = {(float)0.7,(float)3,(float)-0.3,(float)-0.7,(float)-3,(float)0.3};
    final float[] Y_THRES = {(float)1,(float)2,(float)-0.2,(float)-1,(float)-2,(float)0.2};

    finiteStateMachine fsmX;
    finiteStateMachine fsmY;

    File file = null;
    PrintWriter prt = null;

    public Timer gameTimer = new Timer();
    public GameLoopTask gameTask;

    final int GAMEBOARD_DIMENSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_204_01);

        //Creating Light sensor reading display object
        TextView lightSensorTV = (TextView) findViewById(R.id.label1);
        lightSensorTV.setText("");

        //Get the layout object
        RelativeLayout r1 = (RelativeLayout) findViewById(R.id.label2);

        r1.getLayoutParams().width = GAMEBOARD_DIMENSION;
        r1.getLayoutParams().height = GAMEBOARD_DIMENSION;

        r1.setBackgroundResource(R.drawable.gameboard);

        gameTask = new GameLoopTask(this, getApplicationContext(), r1);
        gameTimer.schedule(gameTask, 17, 17);


        TextView latestGestureTV = new TextView(getApplicationContext());
        latestGestureTV.setText("LATEST GESTURE");
        latestGestureTV.setTextSize(50);
        r1.addView(latestGestureTV);
        latestGestureTV.setTextColor(Color.CYAN);

        //Create instance of the Finite State Machine
        fsmX = new finiteStateMachine(X_THRES);
        fsmY = new finiteStateMachine(Y_THRES);


        //Initializing Sensors and registering them as listeners
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final AccelerometerSensorEventListener sel_accelerometer = new AccelerometerSensorEventListener(fsmX, fsmY, latestGestureTV, gameTask);
        sensorManager.registerListener(sel_accelerometer,accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        /*ImageView myPicture = new ImageView(getApplicationContext());
        myPicture.setImageResource(R.drawable.gameblock);
        myPicture.setScaleX(1.0f);
        myPicture.setScaleY(1.0f);
        myPicture.setX(10);
        myPicture.setY(10);
        r1.addView(myPicture);*/

        Button button1 = new Button(getApplicationContext());
        button1.setText("up");
        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sel_accelerometer.setup();
            }
        });
        Button button2 = new Button(getApplicationContext());
        button2.setText("down");
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sel_accelerometer.setdown();
            }
        });
        Button button3 = new Button(getApplicationContext());
        button3.setText("left");
        button3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sel_accelerometer.setleft();
            }
        });
        Button button4 = new Button(getApplicationContext());
        button4.setText("right");
        button4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sel_accelerometer.setright();
            }
        });
        button1.setY(0);
        button2.setY(250);
        button3.setY(500);
        button4.setY(750);
        r1.addView(button1);
        r1.addView(button2);
        r1.addView(button3);
        r1.addView(button4);

    }
    //Write the past 100 accelerometer readigs to  Output.txt once button is pressed
    /*private void writeToFile(float[][] savedValues, int startIndex){

        try{
            file = new File(getExternalFilesDir("AccelerometerReadings"), "Output.csv");
            prt = new PrintWriter(file);

            for (int i=0; i<100; i++) {
                prt.println(String.format("%f,%f,%f", savedValues[(startIndex + i) % 100][0], savedValues[(startIndex + i) % 100][1], savedValues[(startIndex + i) % 100][2]));
            }
        }catch(IOException e){
            //Log.e("Failed");
        }finally{
            if(prt != null){
                prt.close();
            }
            //Log.d("Ended");

        }
    }*/
}
