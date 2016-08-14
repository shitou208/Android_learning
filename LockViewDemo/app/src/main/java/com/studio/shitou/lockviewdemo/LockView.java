package com.studio.shitou.lockviewdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * TODO: document your custom view class.
 */
public class LockView extends View {
    private TextPaint mTextPaint;
    private Paint mPaint;
    private float radius=0;
    private int BackgroundColor=0xffffffff;
    private boolean isDraw=false;//判断是否是人为触摸进行的绘制
    private float[] startPosition=new float[2];//保存手开始触摸的坐标
    private float[] endPosition=new float[2];//保存结束时的坐标
    private float[][] centerPoint=new float[9][2];//保存9个圆的圆心位置
    private boolean[] drawSubCircle=new boolean[9];//是否绘制子圆的标志
    private ArrayList<Integer> savedPoints=new ArrayList<Integer>();//保存已经选中的点，数字从上到下，从左到右依次为1-9
    private OnDrawFinishedListener listener;
    public LockView(Context context) {
        super(context);
        init(null, 0);
    }

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        //在xml中指定的属性值统一在此处获取（颜色、长宽等）
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.LockView, defStyle, 0);
        BackgroundColor=a.getColor(R.styleable.LockView_bgColor,0xffffffff);
        a.recycle();

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //通过设置宽、高为相同值，可以绘制正方形
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        canvas.drawColor(BackgroundColor);
        mPaint=new Paint();
        mPaint.setColor(0xff000000);
        //抗锯齿，画图质量提高
        mPaint.setAntiAlias(true);
        //设置画笔样式，空心
        mPaint.setStyle(Paint.Style.STROKE);
        //设置半径
        radius=contentWidth/10;
        mPaint.setStrokeWidth((float) 8.0);              //设置线宽
        //按行顺序绘制九个圆
        for(int i=1;i<=3;i++){//i表示行
            for(int j=1;j<=3;j++){//j表示列
                canvas.drawCircle(contentWidth*(2*j-1)/6,contentWidth*(2*i-1)/6,radius,mPaint);
                centerPoint[(i-1)*3+j-1][0]=contentWidth*(2*j-1)/6;
                centerPoint[(i-1)*3+j-1][1]=contentWidth*(2*i-1)/6;
            }
        }
        //处理直线的绘制
        if(isDraw){
            mPaint.setStrokeWidth((float) 4.0);              //设置线宽
            //先将已经保存的点之间用直线连接
            int size=savedPoints.size();
            for(int i=1;i<size;i++){
                canvas.drawLine(centerPoint[savedPoints.get(i-1)-1][0],centerPoint[savedPoints.get(i-1)-1][1],centerPoint[savedPoints.get(i)-1][0],centerPoint[savedPoints.get(i)-1][1],mPaint);
            }
            canvas.drawLine(startPosition[0],startPosition[1],endPosition[0],endPosition[1],mPaint);
        }
        //处理子圆的绘制
        mPaint.setColor(0xFFFF3F3F);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for(int i:savedPoints){
                canvas.drawCircle(centerPoint[i-1][0],centerPoint[i-1][1],radius/4,mPaint);
        }
    }
    //手指触摸屏幕
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取手指的位置
        float mouseX = event.getX();
        float mouseY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指按下时，先判断是否按中了某个大圆
                for(int i=0;i<9;i++){
                    if(isInCircle(centerPoint[i][0],centerPoint[i][1],mouseX,mouseY)){
                        //如果按中了则将该点保存，并将该点设为绘制直线的起点
                        //drawSubCircle[i]=true;
                        savedPoints.add(i+1);
                        startPosition[0]=centerPoint[i][0];
                        startPosition[1]=centerPoint[i][1];
                        isDraw=true;
                        break;
                    }
                }
                //startPosition[0]=mouseX;
                //startPosition[1]=mouseY;
                break;
            case MotionEvent.ACTION_MOVE:
                endPosition[0]=mouseX;
                endPosition[1]=mouseY;
                for(int i=0;i<9;i++){
                    if(isInCircle(centerPoint[i][0],centerPoint[i][1],mouseX,mouseY)){
                        if(!savedPoints.contains(i+1)){
                            isDraw=true;
                            savedPoints.add(i+1);
                            startPosition[0]=centerPoint[i][0];
                            startPosition[1]=centerPoint[i][1];
                        }
                        break;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDraw=false;
                //System.out.println("保存的密码序列为："+savedPoints);
                if(!savedPoints.isEmpty()){
                   listener.OnDrawFinished(getPassword());//此步很重要，什么时候调用，什么时候向主Activity反馈
                }
                savedPoints.clear();
                invalidate();
                break;
            default:
                break;
        }
        //invalidate();
        //this.postInvalidate();
        return true;
    }
    //判断手指是否碰到了圆
    public boolean isInCircle(float centerX,float centerY,float touchX,float touchY){
        float distance=(float) Math.sqrt(Math.pow(centerX-touchX,2)+Math.pow(centerY-touchY,2));
        return distance<=radius;
    }
    public String getPassword(){
        String password="";
        for(int i:savedPoints){
            password+=i;
        }
        return password;
    }
    public interface OnDrawFinishedListener{
        boolean OnDrawFinished(String password);
    }

    public void setOnDrawFinishedListener(OnDrawFinishedListener listener){
        this.listener = listener;
    }

}
