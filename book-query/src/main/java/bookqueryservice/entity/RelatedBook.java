package bookqueryservice.entity;

public class RelatedBook {
    private String ISBN;
    private String title;
    private String Author;

    // Default constructor required for JSON deserialization
    public RelatedBook() {}

    public RelatedBook(String ISBN, String title, String Author) {
        this.ISBN = ISBN;
        this.title = title;
        this.Author = Author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String Author) {
        this.Author = Author;
    }
}