package wolox.training.models;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;

@Entity(name = "users")
@ApiModel(description = "Users from LibraryAPI")
public class User implements Serializable {
    
    static final String OBJECT_NULL_MESSAGE = "Please check Object supplied it's null %s ! ";
    private static final long serialVersionUID = -8348234395026986132L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @ApiModelProperty(notes = "user Id")
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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })

    @JoinTable(name = "user_book",
            joinColumns=@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )

    @Column(nullable = false)
    @ApiModelProperty(notes = "User Book Collection")
    private List<Book> books = new ArrayList<>();
    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        String nameParameter="username";
        Preconditions.checkNotNull(username,OBJECT_NULL_MESSAGE,nameParameter);
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String nameParameter="name";
        Preconditions.checkNotNull(name,OBJECT_NULL_MESSAGE,nameParameter);
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        String nameParameter="birthdate";
        Preconditions.checkNotNull(birthdate,OBJECT_NULL_MESSAGE,nameParameter);
        this.birthdate = birthdate;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        String nameParameter="books";
        Preconditions.checkNotNull(books,OBJECT_NULL_MESSAGE,nameParameter);
        this.books = books;
    }

    public void addBookToCollection(Book book){
        String nameParameter="book";
        Preconditions.checkNotNull(book,OBJECT_NULL_MESSAGE,nameParameter);
        if(this.books.contains(book)){
            throw new BookAlreadyOwnedException();
        }else{
            this.books.add(book);
        }
    }

    public void removeBookToCollection(Book book){
        String nameParameter="book";
        Preconditions.checkNotNull(book,OBJECT_NULL_MESSAGE,nameParameter);
        if(this.books.contains(book)){
            this.books.remove(book);
        }else {
            throw new BookNotFoundException();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    @Override
    public boolean equals (Object obj){
        User user =(User) obj;
        if (this == user){
            return true;
        }
        if(user == null) {
            return false;
        }
        if(this.getClass() != user.getClass()){
            return false;
        }
        return Objects.equals(getId(), user.getId());
    }

}
