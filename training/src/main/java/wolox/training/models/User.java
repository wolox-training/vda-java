package wolox.training.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity (name = "users")
@ApiModel(description = "Users from LibraryAPI")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @ApiModelProperty(notes = "user Id", required = false)
    private long id;

    @Column(nullable = false)
    @ApiModelProperty(notes = "username", required = true)
    private String username;

    @Column(nullable = false)
    @ApiModelProperty(notes = "name", required = true)
    private String name;

    @Column(nullable = false)
    @ApiModelProperty(notes = "birthday", required = true)
    private LocalDate birthdate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    @Column(nullable = false)
    @ApiModelProperty(notes = "User Book Collection", required = false)
    List<Book> books = new ArrayList<>();

    public long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBookToCollection(Book book){
        if(this.books.contains(book)){
            throw new BookAlreadyOwnedException();
        }else{
            this.books.add(book);
        }
    }

    public void removeBookToCollection(Book book){
        this.books.remove(book);
    }

}
