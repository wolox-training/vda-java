package wolox.training.models;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String  genre;
    @Column(nullable = false)
    private String  author;
    @Column(nullable = false)
    private String  image;
    @Column(nullable = false)
    private String  title;
    @Column(nullable = false)
    private String  subtitle;
    @Column(nullable = false)
    private String  publisher;
    @Column(nullable = false)
    private String  year;
    @Column(nullable = false)
    private int pages;
    @Column(nullable = false)
    private String isbn;

    @ManyToOne()
    @JoinColumn(name = "fk_user_id")
    private User user;

    public Book() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    @Override
    public boolean equals (Object obj){
        Book book =(Book) obj;
        if (this == book){
            return true;
        }
        if(book == null) {
            return false;
        }
        if(this.getClass() != book.getClass()){
            return false;
        }
        return Objects.equals(getId(), book.getId());
    }
}
