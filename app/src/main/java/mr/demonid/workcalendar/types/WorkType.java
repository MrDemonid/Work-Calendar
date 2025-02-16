package mr.demonid.workcalendar.types;

public enum WorkType {
    DAY_TWO_FREE ("Сутки через двое"),
    THREE_DAY_THREE_NIGHT_THREE_FREE ("По три: день, ночь, отдых"),
    DAY_THREE_FREE ("Сутки через трое"),
    TWO_DAY_TWO_NIGHT_TWO_FREE ("По два: день, ночь, отдых"),
    WEEK_DAY_WEEK_NIGHT ("Неделя в день, неделя в ночь");

    private final String description;

    WorkType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
