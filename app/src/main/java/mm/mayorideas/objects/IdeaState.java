package mm.mayorideas.objects;

import mm.mayorideas.R;

public enum IdeaState {

    OPEN(R.color.mayorideas_blue_dark),
    IN_PROGRESS(R.color.holo_orange_light),
    RESOLVED(R.color.holo_green_light);

    private final int color;

    IdeaState(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
