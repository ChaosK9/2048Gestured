package ca.uwaterloo.ece155_nlab4;

import android.support.v7.widget.AppCompatImageView;
import android.content.Context;

/**
 * Created by ChaosK9 on 2017-07-11.
 */

public abstract class GameBlockTemplate extends AppCompatImageView {
    public abstract void setDestination();
    public abstract void move();

    public GameBlockTemplate(Context _myContext){
        super(_myContext);
    }
}
