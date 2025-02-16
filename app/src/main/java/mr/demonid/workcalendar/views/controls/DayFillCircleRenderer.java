package mr.demonid.workcalendar.views.controls;

import android.graphics.Canvas;
import android.graphics.Paint;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.views.PaintStore;

public class DayFillCircleRenderer implements CellRenderer {

    @Override
    public void render(AbstractCell cell, Canvas canvas) {
        if (cell.type != DayType.EMPTY) {
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBackgroundPaint(DayType.FREE_DAY));
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBorderPaint(DayType.FREE_DAY));
            if (cell.type != DayType.FREE_DAY) {
                float width = cell.right - cell.left;
                float height = cell.bottom - cell.top;
                Paint p = PaintStore.getBorderPaint(cell.type);
                p.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cell.left + width / 2, cell.top + height / 2, Math.min(width, height) / 2.5f, p);
            }

            cell.drawText(DayType.FREE_DAY, canvas, cell.text);
        } else {
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBackgroundPaint(DayType.FREE_DAY));
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBorderPaint(DayType.FREE_DAY));
        }
    }
}
