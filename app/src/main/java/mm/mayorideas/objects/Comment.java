package mm.mayorideas.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Comment {

    private final String authorName;
    private final String text;
    private final Date date;

    public Comment(String authorName, String text, Date date) {
        this.authorName = authorName;
        this.text = text;
        this.date = date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EEE, d MMM yyyy", Locale.getDefault());
        return formatter.format(date);
    }
}
