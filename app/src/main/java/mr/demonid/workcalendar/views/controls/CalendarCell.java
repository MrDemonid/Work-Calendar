package mr.demonid.workcalendar.views.controls;

import mr.demonid.workcalendar.types.DayType;


/**
 * Один день календаря.
 */
public class CalendarCell extends AbstractCell {

    //region Конструкторы
    public CalendarCell(float left, float top, float right, float bottom, DayType type, String text) {
        super(left, top, right, bottom, type, text, 2f, false);
    }

    public CalendarCell() {
        this(0f, 0f, 0f, 0f, DayType.FREE_DAY, "");
    }
    //endregion

}
