package mr.demonid.workcalendar.types;

/**
 * Тип ячейки календаря
 */
public enum DayType {
    EMPTY,                              // пустая клетка календаря
    FREE_DAY,                           // выходной
    FIRST_HALF_OF_24_HOUR_SHIFT,        // сутки - 16 часов в день
    SECOND_HALF_OF_24_HOUR_SHIF,        // сутки - 8 часов остаток ночи до утра
    DAY_SHIFT,                          // работа в дневную смену
    NIGHT_SHIFT,                        // работа в ночную смену
    LABEL                               // разметка таблицы
}
