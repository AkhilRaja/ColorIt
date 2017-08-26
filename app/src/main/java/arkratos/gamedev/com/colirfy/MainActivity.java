package arkratos.gamedev.com.colirfy;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;


public class MainActivity extends AppCompatActivity {

    private int image_res;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ConstraintLayout drawingLayout;
    private MyView myView;
    Paint paint;
    Palette palette;
    public static int palette_selected;

    static List<Palette> paletteList;
    static BoomMenuButton boomMenuButton;

    Matrix drawMatrix = new Matrix();

    String colorset[][] ={
            {"#3FB8AF","#7FC7AF","#DAD8A7","#FF9E9D","#FF3D7F"},
            {"#BBBB88","#CCC68D","#EEDD99","#EEC290","#EEAA88"},
            {"#C75233","#C78933","#D6CEAA","#79B5AC","#5E2F46"},
            {"#805841","#DCF7F3","#FFFCDD","#FFD8D8","#F5A2A2"},
            {"#3C515D","#3D6868","#40957F","#A7C686","#FCEE8C"},
            {"#8D1042","#A25D47","#A08447","#97AA66","#B8B884"},
            {"#A7321C","#FFDC68","#CC982A","#928941","#352504"},
            {"#6E9167","#FFDD8C","#FF8030","#CC4E00","#700808"},
            {"#5E9FA3","#DCD1B4","#FAB87F","#F87E7B","#B05574"},
            {"#F6D76B","#FF9036","#D6254D","#FF5475","#FDEBA9"},
            {"#5E412F","#FCEBB6","#78C0A8","#F07818","#F0A830"},
            {"#A70267","#F10C49","#FB6B41","#F6D86B","#339194"},
            {"#D1F2A5","#EFFAB4","#FFC48C","#FF9F80","#F56991"},
            {"#F9AAAA","#A0EAC6","#F6F692","#FFF9C0","#FFFFFF"}

    };

    public static void changeBoom(){
        for (int i = 0; i < boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
            boomMenuButton.getBuilder(i).normalColor(paletteList.get(palette_selected).get_color(i));
            boomMenuButton.getBuilder(i).pieceColor(paletteList.get(palette_selected).get_color(i));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_res = getIntent().getIntExtra("Image",R.drawable.cartoon);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_palette);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomAdapter(colorset);
        mRecyclerView.setAdapter(mAdapter);


//        menuOptions.add("Hello");
  //      menuOptions.add("World");
    //    menuOptions.add("Akhil");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DuoDrawerLayout drawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
        DuoDrawerToggle drawerToggle = new DuoDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        paletteList =  new ArrayList<>();
        for (String[] i : colorset) {
            palette = new Palette(i);
            paletteList.add(palette);
        }



        myView = new MyView(this);
        drawingLayout = (ConstraintLayout) findViewById(R.id.constraint);
        drawingLayout.removeViewAt(0);
        drawingLayout.addView(myView);
        drawingLayout.setBackgroundColor(getResources().getColor(R.color.White));

        boomMenuButton = (BoomMenuButton) findViewById(R.id.bmb);
        boomMenuButton.setButtonEnum(ButtonEnum.SimpleCircle);

        boomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.DOT_5_4);
        boomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.SC_5_4);

        for (int i = 0; i < boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
            SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                           // Toast.makeText(MainActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                            paint.setColor(paletteList.get(palette_selected).get_color(index));
                        }
                    }).normalColor(paletteList.get(palette_selected).get_color(i));
                    boomMenuButton.addBuilder(builder);
        }

        boomMenuButton.bringToFront();
        boomMenuButton.setNormalColor(R.color.colorPrimaryDark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    public class MyView extends View {

        private Path path;
        Bitmap mBitmap;
       // ProgressDialog pd;
        final Point p1 = new Point();
       // Canvas canvas;
        int width;
        int height;
        int reduceX;
        int reduceY;

        GestureDetector gestures;
        ScaleGestureDetector scaleGesture;

        Rect rect;
        // Bitmap mutableBitmap ;
        public MyView(Context context) {
            super(context);

            paint = new Paint();
            paint.setAntiAlias(true);
           // pd = new ProgressDialog(context);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(5f);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    image_res,options).copy(Bitmap.Config.ARGB_8888, true);

            this.path = new Path();

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();

            reduceX = 0;//(metrics.widthPixels/9);
            reduceY = (metrics.heightPixels/13);
            width = metrics.widthPixels - reduceX;
            height = metrics.heightPixels - reduceY;

            Log.d("Width and Height" , ""+reduceX+"  "+reduceY);


          //  rect = new Rect(0,0,width,height);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
            drawMatrix.postTranslate(0,reduceY/1.5f);
            scaleGesture = new ScaleGestureDetector(getContext(),
                    new ScaleListener());
            gestures = new GestureDetector(getContext(), new GestureListener());
        }

        public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            private float lastFocusX;
            private float lastFocusY;


            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Matrix transformationMatrix = new Matrix();
                float focusX = detector.getFocusX();
                float focusY = detector.getFocusY();

                //Zoom focus is where the fingers are centered,
                transformationMatrix.postTranslate(-focusX, -focusY);
                transformationMatrix.postScale(detector.getScaleFactor(), detector.getScaleFactor());

                /* Adding focus shift to allow for scrolling with two pointers down.
                 Remove it to skip this functionality.
                 This could be done in fewer lines, but for clarity I do it this way here */
                //Edited after comment by chochim

                float focusShiftX = focusX - lastFocusX;
                float focusShiftY = focusY - lastFocusY;

                transformationMatrix.postTranslate(focusX + focusShiftX, focusY + focusShiftY );
                drawMatrix.postConcat(transformationMatrix);


                lastFocusX = focusX;
                lastFocusY = focusY;

                invalidate();
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                lastFocusX = detector.getFocusX();
                lastFocusY = detector.getFocusY();
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

            }
        }

        public class GestureListener extends GestureDetector.SimpleOnGestureListener{

            float[] points = new float[2];

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                drawMatrix.reset();
                drawMatrix.postTranslate(0,reduceY/1.5f);
                invalidate();
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        points[0] = x;
                        points[1] = y;

                        Matrix inverse = new Matrix();
                        drawMatrix.invert(inverse);
                        inverse.mapPoints(points);

                        p1.x = (int)points[0];
                        p1.y = (int)points[1];

                        try {
                            final int sourceColor = mBitmap.getPixel(p1.x, p1.y);
                            final int targetColor = paint.getColor();

                            new TheTask(mBitmap, p1, sourceColor, targetColor).execute();
                            invalidate();
                        }
                        catch(Exception exc)
                        {
                            Log.d("Exception","Stack" + e);
                        }
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                if(e2.getPointerCount() == 2){
                    drawMatrix.postTranslate(-distanceX, -distanceY);
                    invalidate();
                    return true;
                }

                return false;
            }

        }


            @Override
        protected void onDraw(Canvas canvas) {
            //this.canvas = canvas;
           // paint.setColor(Color.GREEN);
            //canvas.drawBitmap(mBitmap,null,rect,paint);
//            canvas.drawBitmap(mBitmap,reduceX,reduceY,paint);
                canvas.drawBitmap(mBitmap,drawMatrix,paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
/*
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    p1.x = (int) x - (reduceX);
                    p1.y = (int) y - (reduceY);
                    final int sourceColor = mBitmap.getPixel(p1.x,p1.y);
                    final int targetColor = paint.getColor();
                    new TheTask(mBitmap, p1, sourceColor, targetColor).execute();
                    invalidate();
            }*/
            gestures.onTouchEvent(event);
            scaleGesture.onTouchEvent(event);

            return true;

        }

        public void clear() {
            path.reset();
            invalidate();
        }

        public int getCurrentPaintColor() {
            return paint.getColor();
        }

        class TheTask extends AsyncTask<Void, Integer, Void> {

            Bitmap bmp;
            Point pt;
            int replacementColor, targetColor;

            public TheTask(Bitmap bm, Point p, int sc, int tc) {
                this.bmp = bm;
                this.pt = p;
                this.replacementColor = tc;
                this.targetColor = sc;
               // pd.setMessage("Filling....");
              //  pd.show();
            }

            @Override
            protected void onPreExecute() {
               // pd.show();

            }

            @Override
            protected void onProgressUpdate(Integer... values) {

            }

            @Override
            protected Void doInBackground(Void... params) {
                //FloodFill f = new FloodFill();
                //f.floodFill(bmp, pt, targetColor, replacementColor);
                QueueLinearFloodFiller filler = new QueueLinearFloodFiller(mBitmap, targetColor, replacementColor);
                filler.setTolerance(30);
                filler.floodFill(pt.x, pt.y);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
              //  pd.dismiss();
                invalidate();
            }
        }
    }

    //http://stackoverflow.com/questions/8070401/android-flood-fill-algorithm/17426163#17426163
   // http://stackoverflow.com/questions/6371999/android-fill-image-with-colors
    //IMPORTANT
   // http://stackoverflow.com/questions/16968412/how-to-use-flood-fill-algorithm-in-android
    //Mayor Class Optimize

    public class QueueLinearFloodFiller {

        protected Bitmap image = null;
        protected int[] tolerance = new int[] { 0, 0, 0 };
        protected int width = 0;
        protected int height = 0;
        protected int[] pixels = null;
        protected int fillColor = 0;
        protected int[] startColor = new int[] { 0, 0, 0 };
        protected boolean[] pixelsChecked;
        protected Queue<FloodFillRange> ranges;

        // Construct using an image and a copy will be made to fill into,
        // Construct with BufferedImage and flood fill will write directly to
        // provided BufferedImage
        public QueueLinearFloodFiller(Bitmap img) {
            copyImage(img);
        }

        public QueueLinearFloodFiller(Bitmap img, int targetColor, int newColor) {
            useImage(img);
            setFillColor(newColor);
            setTargetColor(targetColor);
        }

        public void setTargetColor(int targetColor) {
            startColor[0] = Color.red(targetColor);
            startColor[1] = Color.green(targetColor);
            startColor[2] = Color.blue(targetColor);
        }

        public int getFillColor() {
            return fillColor;
        }

        public void setFillColor(int value) {
            fillColor = value;
        }

        public int[] getTolerance() {
            return tolerance;
        }

        public void setTolerance(int[] value) {
            tolerance = value;
        }

        public void setTolerance(int value) {
            tolerance = new int[] { value, value, value };
        }

        public Bitmap getImage() {
            return image;
        }

        public void copyImage(Bitmap img) {
            // Copy data from provided Image to a BufferedImage to write flood fill
            // to, use getImage to retrieve
            // cache data in member variables to decrease overhead of property calls
            width = img.getWidth();
            height = img.getHeight();

            image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(image);
            canvas.drawBitmap(img, 0, 0, null);

            pixels = new int[width * height];

            image.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
        }

        public void useImage(Bitmap img) {
            // Use a pre-existing provided BufferedImage and write directly to it
            // cache data in member variables to decrease overhead of property calls
            width = img.getWidth();
            height = img.getHeight();
            image = img;

            pixels = new int[width * height];

            image.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
        }

        protected void prepare() {
            // Called before starting flood-fill
            pixelsChecked = new boolean[pixels.length];
            ranges = new LinkedList<>();
        }

        // Fills the specified point on the bitmap with the currently selected fill
        // color.
        // int x, int y: The starting coords for the fill
        public void floodFill(int x, int y) {
            // Setup
            prepare();

            if (startColor[0] == 0) {
                // ***Get starting color.
                int startPixel = pixels[(width * y) + x];
                startColor[0] = (startPixel >> 16) & 0xff;
                startColor[1] = (startPixel >> 8) & 0xff;
                startColor[2] = startPixel & 0xff;
            }

            // ***Do first call to floodfill.
            LinearFill(x, y);

            // ***Call floodfill routine while floodfill ranges still exist on the
            // queue
            FloodFillRange range;

            while (ranges.size() > 0) {
                // **Get Next Range Off the Queue
                range = ranges.remove();

                // **Check Above and Below Each Pixel in the Floodfill Range
                int downPxIdx = (width * (range.Y + 1)) + range.startX;
                int upPxIdx = (width * (range.Y - 1)) + range.startX;
                int upY = range.Y - 1;// so we can pass the y coord by ref
                int downY = range.Y + 1;

                for (int i = range.startX; i <= range.endX; i++) {
                    // *Start Fill Upwards
                    // if we're not above the top of the bitmap and the pixel above
                    // this one is within the color tolerance
                    if (range.Y > 0 && (!pixelsChecked[upPxIdx])
                            && CheckPixel(upPxIdx))
                        LinearFill(i, upY);

                    // *Start Fill Downwards
                    // if we're not below the bottom of the bitmap and the pixel
                    // below this one is within the color tolerance
                    if (range.Y < (height - 1) && (!pixelsChecked[downPxIdx])
                            && CheckPixel(downPxIdx))
                        LinearFill(i, downY);

                    downPxIdx++;
                    upPxIdx++;
                }
            }

            image.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
        }

        // Finds the furthermost left and right boundaries of the fill area
        // on a given y coordinate, starting from a given x coordinate, filling as
        // it goes.
        // Adds the resulting horizontal range to the queue of floodfill ranges,
        // to be processed in the main loop.

        // int x, int y: The starting coords
        protected void LinearFill(int x, int y) {
            // ***Find Left Edge of Color Area
            int lFillLoc = x; // the location to check/fill on the left
            int pxIdx = (width * y) + x;

            while (true) {
                // **fill with the color
                pixels[pxIdx] = fillColor;

                // **indicate that this pixel has already been checked and filled
                pixelsChecked[pxIdx] = true;

                // **de-increment
                lFillLoc--; // de-increment counter
                pxIdx--; // de-increment pixel index

                // **exit loop if we're at edge of bitmap or color area
                if (lFillLoc < 0 || (pixelsChecked[pxIdx]) || !CheckPixel(pxIdx)) {
                    break;
                }
            }

            lFillLoc++;

            // ***Find Right Edge of Color Area
            int rFillLoc = x; // the location to check/fill on the left

            pxIdx = (width * y) + x;

            while (true) {
                // **fill with the color
                pixels[pxIdx] = fillColor;

                // **indicate that this pixel has already been checked and filled
                pixelsChecked[pxIdx] = true;

                // **increment
                rFillLoc++; // increment counter
                pxIdx++; // increment pixel index

                // **exit loop if we're at edge of bitmap or color area
                if (rFillLoc >= width || pixelsChecked[pxIdx] || !CheckPixel(pxIdx)) {
                    break;
                }
            }

            rFillLoc--;

            // add range to queue
            FloodFillRange r = new FloodFillRange(lFillLoc, rFillLoc, y);

            ranges.offer(r);
        }

        // Sees if a pixel is within the color tolerance range.
        protected boolean CheckPixel(int px) {
            int red = (pixels[px] >>> 16) & 0xff;
            int green = (pixels[px] >>> 8) & 0xff;
            int blue = pixels[px] & 0xff;

            return (red >= (startColor[0] - tolerance[0])
                    && red <= (startColor[0] + tolerance[0])
                    && green >= (startColor[1] - tolerance[1])
                    && green <= (startColor[1] + tolerance[1])
                    && blue >= (startColor[2] - tolerance[2]) && blue <= (startColor[2] + tolerance[2]));
        }

        // Represents a linear range to be filled and branched from.
        protected class FloodFillRange {
            private int startX;
            private int endX;
            private int Y;

            public FloodFillRange(int startX, int endX, int y) {
                this.startX = startX;
                this.endX = endX;
                this.Y = y;
            }
        }
    }
}
