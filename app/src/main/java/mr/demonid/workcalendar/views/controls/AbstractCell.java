package mr.demonid.workcalendar.views.controls;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import mr.demonid.workcalendar.views.PaintStore;
import mr.demonid.workcalendar.types.DayType;

public abstract class AbstractCell {

    protected int id;

    protected float left;
    protected float top;
    protected float right;
    protected float bottom;

    protected DayType type;
    protected String text;
    protected float textSize;       // коэффициент размера текста к размерам ячейки
    protected boolean isBold;

    protected CellRenderer renderer;


    //region Конструкторы

    public AbstractCell(float left, float top, float right, float bottom, DayType type, String text, float textSize, boolean isBold) {
        this.id = 0;
        this.type = type;
        this.text = text;
        this.renderer = null;
        this.textSize = textSize;
        this.isBold = isBold;
        setRect(left, top, right, bottom);
    }
    //endregion

    /**
     * Метод рисования контрола.
     */
    public void onDraw(Canvas canvas) {
        if (renderer != null) {
            renderer.render(this, canvas);
        }
    }

    /**
     * Установка рендера прорисовки ячейки.
     */
    public void setRenderer(CellRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Метод для вывода текста в центре контрола.
     */
    public void drawText(DayType type, Canvas canvas, String txt) {
        Paint textPaint = PaintStore.getTextPaint(type);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(Math.max(Math.min(right - left, bottom - top) / textSize, 6f));
        textPaint.setFakeBoldText(isBold);

        float centerX = (left + right) / 2; // Центр по X
        float centerY = (top + bottom) / 2; // Центр по Y

        // Корректируем Y-координату для центрирования текста по вертикали
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeightOffset = (fontMetrics.ascent + fontMetrics.descent) / 2;

        canvas.drawText(txt, centerX, centerY - textHeightOffset, textPaint);
    }

    //region Геттеры и Сеттеры


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(DayType type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRect(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public DayType getType() {
        return type;
    }

    public String getText() {
        return text;
    }
    //endregion

}

