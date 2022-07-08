package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
@Entity(name = "books")
@ApiModel(description = "Book from LibraryAPI")
public class Book implements Serializable {

    static final String OBJECT_NULL_MESSAGE = "Please check Object supplied it's null %s ! ";
    private static final long serialVersionUID = -3673586493636539763L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @ApiModelProperty(notes = "book Id")
    private long id;
    @ApiModelProperty(notes = "genre", required = true)
    private String  genre;
    @ApiModelProperty(notes = "author", required = true)
    @Column(nullable = false)
    private String  author;
    @ApiModelProperty(notes = "image", required = true)
    @Column(nullable = false)
    private String  image;
    @ApiModelProperty(notes = "title", required = true)
    @Column(nullable = false)
    private String  title;
    @ApiModelProperty(notes = "subtitle", required = false)
    @Column(nullable = false)
    private String  subtitle;
    @ApiModelProperty(notes = "publisher", required = true)
    @Column(nullable = false)
    private String  publisher;
    @ApiModelProperty(notes = "year", required = true)
    @Column(nullable = false)
    private String  year;
    @ApiModelProperty(notes = "pages", required = true)
    @Column(nullable = false)
    private int pages;
    @ApiModelProperty(notes = "isbn", required = true)
    @Column(nullable = false,unique = true)
    private String isbn;

    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy = "books"
    )
    @JsonIgnore
    private List<User> users=new ArrayList<>();

    public Book() {
        //constructor necessary for JPA library
    }

    public long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        String nameParameter="genre";
        Preconditions.checkNotNull(genre,OBJECT_NULL_MESSAGE,nameParameter);
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        String nameParameter="author";
        Preconditions.checkNotNull(author,OBJECT_NULL_MESSAGE,nameParameter);
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        String nameParameter="image";
        Preconditions.checkNotNull(image,OBJECT_NULL_MESSAGE,nameParameter);
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String nameParameter="title";
        Preconditions.checkNotNull(title,OBJECT_NULL_MESSAGE,nameParameter);
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        String nameParameter="subtitle";
        Preconditions.checkNotNull(subtitle,OBJECT_NULL_MESSAGE,nameParameter);
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        String nameParameter="publisher";
        Preconditions.checkNotNull(publisher,OBJECT_NULL_MESSAGE,nameParameter);
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        String nameParameter="year";
        Preconditions.checkNotNull(year,OBJECT_NULL_MESSAGE,nameParameter);
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        Preconditions.checkArgument(pages>0,
                "The pages param must be mayor to 0");
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        String nameParameter="isbn";
        Preconditions.checkNotNull(isbn,OBJECT_NULL_MESSAGE,nameParameter);
        this.isbn = isbn;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        String nameParameter="users";
        Preconditions.checkNotNull(users,OBJECT_NULL_MESSAGE,nameParameter);
        this.users = users;
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
