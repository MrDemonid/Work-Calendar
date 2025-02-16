package mr.demonid.workcalendar.events;

/**
 * Интерфейс для извещения слушателей о нажатии пользователем на ячейку таблицы календаря.
 */
public interface OnWorkTableClickListener {
    void onCellClick(int day);
}

