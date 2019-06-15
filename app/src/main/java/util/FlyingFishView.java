package util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.logicaltech.gamerecharge.Battle_ResultActivity;
import com.logicaltech.gamerecharge.GameOverActivity;
import com.logicaltech.gamerecharge.HighScoreActivity;
import com.logicaltech.gamerecharge.JumpFishActivity;
import com.logicaltech.gamerecharge.R;

public class FlyingFishView extends View
{
    private Bitmap fish[] = new Bitmap[2];
    private Bitmap backgroundImage;
    private Paint scorePain = new Paint();
    private Bitmap life[] = new Bitmap[2];
    private int fishx = 10;
    private int fishy ;
    private int fishspeed;
    private int canvasWidth,canvasHeight;
    private int yellowx,yellowy,yellowSpeed = 16;
    private Paint yellowPaint = new Paint();
    private int greenx,greeny,greenSpeed=20;
    private Paint greenPaint = new Paint();
    private int redx,redy,redSpeed=25;
    private Paint redPaint = new Paint();
    private int score,lifeCounterOfFish;
    private boolean touch=false;

    public FlyingFishView(Context context)
    {
        super(context);
        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(),R.drawable.fish2);
        backgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);
        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);
        scorePain.setColor(Color.WHITE);
        scorePain.setTextSize(70);
        scorePain.setAntiAlias(true);
        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);
        fishy = 550;
        score=0;
        lifeCounterOfFish=3;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        canvas.drawBitmap(backgroundImage,0,0,null);
        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight - fish[0].getHeight() * 3;
        fishy = fishy+fishspeed;
        if (fishy < minFishY)
        {
            fishy=minFishY;
        }
        if (fishy>maxFishY)
        {
            fishy = maxFishY;
        }
        fishspeed = fishspeed + 2;
        if (touch)
        {
            canvas.drawBitmap(fish[1],fishx,fishy,null);
            touch = false;
        }
        else
        {
            canvas.drawBitmap(fish[0],fishx,fishy,null);
        }
        yellowx = yellowx -yellowSpeed;
        if (hitBallChecker(yellowx,yellowy))
        {
            score = score + 10;
            yellowx = -100;
        }
        if (yellowx<0)
        {
            yellowx = canvasWidth+21;
            yellowy = (int) Math.floor(Math.random()* (maxFishY- minFishY))+ minFishY;
        }
        canvas.drawCircle(yellowx,yellowy,25,yellowPaint);
        greenx = greenx - greenSpeed;
        if (hitBallChecker(greenx,greeny))
        {
            score = score + 20;
            greenx = -100;
        }
        if (greenx<0)
        {
            greenx = canvasWidth+21;
            greeny = (int) Math.floor(Math.random()* (maxFishY- minFishY))+ minFishY;
        }
        canvas.drawCircle(greenx,greeny,30,greenPaint);
        redx = redx - redSpeed;
        if (hitBallChecker(redx,redy))
        {
            redx = -100;
            lifeCounterOfFish--;
            if (lifeCounterOfFish==0)
            {
                //Toast.makeText(getContext(),"Game Over:",Toast.LENGTH_SHORT).show();

                if (JumpFishActivity.COB.equals("1"))
                {
                    Intent intent = new Intent(getContext(), Battle_ResultActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("score",score);
                    intent.putExtra("srno", JumpFishActivity.SRNO);
                    intent.putExtra("gtype","1");
                    getContext().startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getContext(), HighScoreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("score",score);
                    intent.putExtra("srno", JumpFishActivity.SRNO);
                    intent.putExtra("gtype","1");
                    getContext().startActivity(intent);
                }
            }
        }
        if (redx<0)
        {
            redx = canvasWidth+21;
            redy = (int) Math.floor(Math.random()* (maxFishY- minFishY))+ minFishY;
        }
        canvas.drawCircle(redx,redy,35,redPaint);
        canvas.drawText("Score: "+score,20,60,scorePain);

        for (int i=0;i<3;i++)
        {
            int x = (int) (int) (580 + life[0].getWidth() * 1.5*i);
            int y=30;

            if (i<lifeCounterOfFish)
            {
                canvas.drawBitmap(life[0],x,y,null);
            }
            else
            {
                canvas.drawBitmap(life[1],x,y,null);
            }
        }

//        canvas.drawBitmap(fish,0,0,null);
        /*canvas.drawBitmap(life[0],580,10,null);
        canvas.drawBitmap(life[0],680,10,null);
        canvas.drawBitmap(life[0],780,10,null);*/
    }

    public boolean hitBallChecker(int x,int y) {
        if (fishx<x && x<(fishx + fish[0].getWidth()) && fishy < y && y<(fishy+fish[0].getHeight()))
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN)
        {
            touch = true;
            fishspeed = -22;
        }
        return super.onTouchEvent(event);
    }
}
