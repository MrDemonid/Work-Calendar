package mr.demonid.workcalendar.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Превьюшка цвета заголовка календаря.
 */
public class TitlePreviewCell extends View {

    private int color;

    private Paint bkgrPaint;
    private Paint borderPaint;


    public TitlePreviewCell(Context context) {
        super(context);
        init();
    }

    public TitlePreviewCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bkgrPaint = new Paint();
        bkgrPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bkgrPaint.setAntiAlias(true);
        borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        color = Color.LTGRAY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        bkgrPaint.setColor(color);
        canvas.drawRect(0, 0, getWidth(), getHeight(), bkgrPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), borderPaint);
    }

}
