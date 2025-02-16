package mr.demonid.workcalendar;

import android.app.Application;
import android.content.Context;


/**
 * Предоставляет контекст приложения. Для управления конфигурацией.
 */
public class WorkApp extends Application {

    private static WorkApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance;
    }
}
