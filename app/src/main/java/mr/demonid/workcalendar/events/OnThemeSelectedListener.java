package mr.demonid.workcalendar.events;

import mr.demonid.workcalendar.config.CalendarUnit;

/**
 * Интерфейс для извещения слушателей о выборе пользователем графика режима работы.
 */
public interface OnThemeSelectedListener {
    void onThemeSelected(CalendarUnit theme);
}
