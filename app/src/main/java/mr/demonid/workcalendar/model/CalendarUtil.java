package mr.demonid.workcalendar.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.util.Calendar;

public class CalendarUtil {

    private static Calendar current = Calendar.getInstance();
    private static CalendarUtil instance = new CalendarUtil();


    public static CalendarUtil getInstance() {
        return instance;
    }

    /**
     * Возвращает день недели.
     * @return (0 - понедельник, 1 - вторник, .... 6 - воскресение)
     */
    public int getFirstDayOfWeek() {
        return getDayOfWeek(current.get(Calendar.YEAR), current.get(Calendar.MONTH), 1);
    }

    /**
     * Возвращает количество дней в текущем месяце.
     * @return
     */
    public int getDaysInCurrentMonth() {
        return getDaysInMonth(current.get(Calendar.YEAR), current.get(Calendar.MONTH));
    }

    /**
     * Возвращает порядковый номер первого числа текущего месяца,
     * начиная отсчет от 1 января 1970 года.
     */
    public long getDayFromEpoch() {
        return getDayFromEpoch(current.get(Calendar.YEAR), current.get(Calendar.MONTH), 1);
    }

    public int getCurrentMonth() {
        return current.get(Calendar.MONTH);
    }

    public int getCurrentYear() {
        return current.get(Calendar.YEAR);
    }

    /**
     * Переход на следующий месяц.
     */
    public void nextMonth() {
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        month++;
        if (month > Calendar.DECEMBER) {
            month = Calendar.JANUARY;
            year++;
        }
        current.set(year, month, 1);
        Log.d(TAG, String.format("-- next month: 01:%d:%d, days = %d\n", month, year, current.getActualMaximum(Calendar.DAY_OF_MONTH)));
    }

    /**
     * Переход на предыдущий месяц.
     */
    public void prevMonth() {
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        month--;
        if (month < 0) {
            month = Calendar.DECEMBER;
            year--;
        }
        current.set(year, month, 1);
        Log.d(TAG, String.format("-- prev month: 01:%d:%d\n", month, year));
    }

    /**
     * Возвращает количество дней в месяце.
     * @param year  Целевой год.
     * @param month Целевой месяц (0 - январь, 1 - февраль и тд.).
     * @return      Кол-во дней.
     */
    public int getDaysInMonth(int year, int month) {
        int numDays;

        switch (month) {
            case 1:
                if ((year % 4) == 0)
                    numDays = 29;
                else
                    numDays = 28;
                if ( ((year % 100) == 0) && ((year % 400) != 0) )
                    numDays = 28;
                break;
            case 10:
            case 3:
            case 5:
            case 8:
                numDays = 30;
                break;
            default:
                numDays = 31;
                break;
        }
        return numDays;
    }


    /**
     * Возвращает день недели.
     * @param year  Целевой год.
     * @param month Целевой месяц.
     * @param day   Целевой день.
     * @return      День недели (0 - понедельник, 1 - вторник, .... 6 - воскресение)
     */
    public int getDayOfWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int res = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 0 - воскресенье, 1 - понедельник и тд.
        return res > 0 ? --res : 6;
    }


    /**
     * Возвращает порядковый номер дня от 1 января 1970 года.
     * @param year  Целевой год.
     * @param month Целевой месяц.
     * @param day   Целевой день.
     */
    public static long getDayFromEpoch(int year, int month, int day) {
        Calendar source = Calendar.getInstance();
        source.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        source.set(Calendar.MILLISECOND, 0);

        Calendar destination = Calendar.getInstance();
        destination.set(year, month, day, 0, 0, 0); // целевая дата
        destination.set(Calendar.MILLISECOND, 0);

        long millSec = destination.getTimeInMillis() - source.getTimeInMillis();
        return millSec / (24 * 60 * 60 * 1000); // переводим миллисекунды в дни
    }

}
