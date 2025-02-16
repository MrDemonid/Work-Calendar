package mr.demonid.workcalendar.config;

import mr.demonid.workcalendar.types.DayType;

public class ColorUnit {

    private DayType type;
    private int foreground;
    private int background;
    private int border;

    public ColorUnit(DayType type, int foreground, int background, int border) {
        this.type = type;
        this.foreground = foreground;
        this.background = background;
        this.border = border;
    }

    public DayType getType() {
        return type;
    }

    public void setType(DayType type) {
        this.type = type;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public int getForeground() {
        return foreground;
    }

    public void setForeground(int foreground) {
        this.foreground = foreground;
    }


    @Override
    public String toString() {
        return "ColorSettings{" +
                "type=" + type +
                ", background=" + background +
                ", border=" + border +
                ", foreground=" + foreground +
                '}';
    }
}
