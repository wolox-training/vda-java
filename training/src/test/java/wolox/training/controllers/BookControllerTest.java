package wolox.training.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import wolox.training.models.Book;
import wolox.training.repositoies.BookRepository;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookController bookController;

    @MockBean
    private BookRepository mockUserRepository;
    private Book oneTestBook;
    private Book twoTestBook;
    private List<Book> books;
    private List<Book> booksByTitle;


    @BeforeEach
    void setUp() {
        oneTestBook = new Book();
        oneTestBook.setAuthor("Stephen King");
        oneTestBook.setGenre("Terror");
        oneTestBook.setImage("image.jpeg");
        oneTestBook.setIsbn("4578-8665");
        oneTestBook.setPages(1253);
        oneTestBook.setPublisher("Viking Publisher");
        oneTestBook.setSubtitle("Worst clown ever");
        oneTestBook.setTitle("It");
        oneTestBook.setYear("1986");
        twoTestBook = new Book();
        twoTestBook.setAuthor("J.K Rowling");
        twoTestBook.setGenre("Fantasy");
        twoTestBook.setImage("image-harry.png");
        twoTestBook.setIsbn("9780747532743");
        twoTestBook.setPages(226);
        twoTestBook.setPublisher("Bloomsbury Publishing");
        twoTestBook.setSubtitle("-");
        twoTestBook.setTitle("Harry Potter and the philosopher's Stone");
        twoTestBook.setYear("1997");
        books = new ArrayList<>();
        books.add(oneTestBook);
        books.add(twoTestBook);

    }

    @Test
    public void contextLoads() {
        assertThat(bookController).isNotNull();
    }
    @Test
    @DisplayName("WhenFindAll_thenStatusOk")
    void whenFindAll_thenStatusOk() throws Exception {
        Mockito.when(mockUserRepository.findAll()).thenReturn(books);
        String url =("/api/books");
        mvc.perform(get(url)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("WhenFindByTitle_thenStatusOk")
    void whenFindByTitle_thenStatusOk() throws Exception {
        Mockito.when(mockUserRepository.findByTitle("It"))
                .thenReturn(books.stream()
                        .filter(book -> book.getTitle().equals("It"))
                        .collect(Collectors.toList()));
        String url =("/api/books?title=It");
        mvc.perform(get(url)
                        .contentType(MediaType.ALL_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        String url =("/api/books/0");
        mvc.perform(get(url)
                        .contentType(MediaType.ALL_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void create() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void delete() {
    }
}
