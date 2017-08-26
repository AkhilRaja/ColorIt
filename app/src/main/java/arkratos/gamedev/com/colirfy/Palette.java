package arkratos.gamedev.com.colirfy;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkhilRaja on 20/08/17.
 */

public class Palette {

    public List<Integer> color2;

    public Palette(String passedcolor[])
    {

        color2 = new ArrayList<>();
        for (String i : passedcolor ) {
            //Log.d("Color", Color.parseColor(i) + "") ;
            color2.add(Color.parseColor(i));
        }

    }
    public int get_color(int i)
    {
        return color2.get(i);
    }
}

