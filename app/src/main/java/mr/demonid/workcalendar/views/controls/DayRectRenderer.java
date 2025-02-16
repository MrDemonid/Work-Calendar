package mr.demonid.workcalendar.views.controls;

import android.graphics.Canvas;
import android.graphics.Paint;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.views.PaintStore;

public class DayRectRenderer implements CellRenderer {

    @Override
    public void render(AbstractCell cell, Canvas canvas) {
        if (cell.type != DayType.EMPTY) {
            if (cell.type != DayType.FREE_DAY) {
                Paint p = PaintStore.getBorderPaint(cell.type);
                p.setStyle(Paint.Style.FILL);
                canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, p);
            } else {
                canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBackgroundPaint(DayType.FREE_DAY));
            }
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBorderPaint(DayType.FREE_DAY));
            cell.drawText(DayType.FREE_DAY, canvas, cell.text);
        } else {
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBackgroundPaint(DayType.FREE_DAY));
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBorderPaint(DayType.FREE_DAY));
        }
    }

}
