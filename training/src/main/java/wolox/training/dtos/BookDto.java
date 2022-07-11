package wolox.training.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BookDto implements Serializable {

    private static final long serialVersionUID = 3590094552089658621L;
    private String isbn;
    private String title;
    private String subtitle;
    private List<String> publishers = new ArrayList<>();
    private String publishDate;
    private int numeberOfPages =0;
    private String cover;
    List<String> authors = new ArrayList<>();

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getNumeberOfPages() {
        return numeberOfPages;
    }

    public void setNumeberOfPages(int numeberOfPages) {
        this.numeberOfPages = numeberOfPages;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", publishers=" + publishers +
                ", publishDate='" + publishDate + '\'' +
                ", numeberOfPages=" + numeberOfPages +
                ", cover='" + cover + '\'' +
                ", authors=" + authors +
                '}';
    }
}
