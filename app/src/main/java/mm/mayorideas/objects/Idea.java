package mm.mayorideas.objects;

public class Idea {

    private String imageUrl = "http://themestudio.net/wp-content/uploads/2015/06/modern-psd-to-html-online-generator-idea.jpg";
    private String name;

    public Idea(String name) {
        this.name = name;
    }

    public String getImage() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
