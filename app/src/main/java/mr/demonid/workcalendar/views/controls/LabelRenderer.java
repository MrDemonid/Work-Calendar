package mr.demonid.workcalendar.views.controls;

import android.graphics.Canvas;
import android.graphics.Paint;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.views.PaintStore;

/**
 * Рендер для названий дней недели календаря.
 */
public class LabelRenderer implements CellRenderer {

    @Override
    public void render(AbstractCell cell, Canvas canvas) {
        canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBackgroundPaint(DayType.LABEL));
        drawText(cell, PaintStore.getTextPaint(DayType.LABEL), canvas, cell.text);
        canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBorderPaint(DayType.LABEL));
    }

    public void drawText(AbstractCell cell, Paint textPaint, Canvas canvas, String txt) {
        if (textPaint != null) {
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(Math.min(cell.right - cell.left, cell.bottom - cell.top) / 3f);
            textPaint.setFakeBoldText(true);
            float centerX = (cell.left + cell.right) / 2; // Центр по X
            float centerY = (cell.top + cell.bottom) / 2; // Центр по Y

            // Корректируем Y-координату для центрирования текста по вертикали
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float textHeightOffset = (fontMetrics.ascent + fontMetrics.descent) / 2;

            canvas.drawText(txt, centerX, centerY - textHeightOffset, textPaint);
        }
    }

}
