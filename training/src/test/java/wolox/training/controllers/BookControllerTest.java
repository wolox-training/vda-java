package wolox.training.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private BookRepository mockBookRepository;
    private Book oneTestBook;
    private List<Book> books;

    private ObjectMapper mapper;
    BookControllerTest() {
    }

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
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
        Book twoTestBook = new Book();
        twoTestBook.setAuthor("J.K Rowling");
        twoTestBook.setGenre("Fantasy");
        twoTestBook.setImage("image-harry.png");
        twoTestBook.setIsbn("9780747532743");
        twoTestBook.setPages(226);
        twoTestBook.setPublisher("Bloomsbury Publishing");
        twoTestBook.setSubtitle("-");
        twoTestBook.setTitle("Harry Potter and the philosopher's Stone");
        twoTestBook.setYear("1997");
        Book threTestBook = new Book();
        threTestBook.setAuthor("J.K Rowling");
        threTestBook.setGenre("Fantasy");
        threTestBook.setImage("image-harry.png");
        threTestBook.setIsbn("9780747532111");
        threTestBook.setPages(226);
        threTestBook.setPublisher("Bloomsbury Publishing");
        threTestBook.setSubtitle("-");
        threTestBook.setTitle("Harry Potter and the philosopher's Stone - test");
        threTestBook.setYear("1997");
        books = new ArrayList<>();
        books.add(oneTestBook);
        books.add(twoTestBook);
        books.add(threTestBook);
    }

    @Test
    @DisplayName("WhenFindAll_thenReturnListAllBooksWhitStatusOK")
    void whenFindAll_thenReturnListAllBooksWhitStatusOK () throws Exception {
        Mockito.when(mockBookRepository.findBooksWithOptionalFilters(null,null,null,null,
                        null,null,null))
                .thenReturn(books);
        String url =("/api/books");
        mvc.perform(get(url)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":0,\"genre\":\"Terror\","
                        + "\"author\":\"Stephen King\",\"image\":\"image.jpeg\",\"title\":\"It\","
                        + "\"subtitle\":\"Worst clown ever\",\"publisher\":\"Viking Publisher\","
                        + "\"year\":\"1986\",\"pages\":1253,\"isbn\":\"4578-8665\"},{\"id\":0,"
                        + "\"genre\":\"Fantasy\",\"author\":\"J.K Rowling\","
                        + "\"image\":\"image-harry.png\",\"title\":\"Harry Potter and the philosopher's Stone\","
                        + "\"subtitle\":\"-\",\"publisher\":\"Bloomsbury Publishing\","
                        + "\"year\":\"1997\",\"pages\":226,\"isbn\":\"9780747532743\"},{\"id\":0,\"genre\":\"Fantasy\","
                        + "\"author\":\"J.K Rowling\",\"image\":\"image-harry.png\","
                        + "\"title\":\"Harry Potter and the philosopher's Stone - test\",\"subtitle\":\"-\","
                        + "\"publisher\":\"Bloomsbury Publishing\",\"year\":\"1997\",\"pages\":226,"
                        + "\"isbn\":\"9780747532111\"}]"))
                .andExpect(jsonPath("$",hasSize(3)));
        Mockito.verify(mockBookRepository).findBooksWithOptionalFilters(null,null,null,null,
                null,null,null);
    }
    @Test
    @DisplayName("WhenFindAllInEmptyCollection_thenReturnExceptionNotFoundException")
    void whenFindAllInEmptyCollection_thenReturnExceptionNotFoundException () throws Exception {
        books.remove(0);
        books.remove(0);
        books.remove(0);
        Mockito.when(mockBookRepository.findBooksWithOptionalFilters(null,null,null,null,
                null,null,null)).thenReturn(books);
        String url =("/api/books");
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        Mockito.verify(mockBookRepository).findBooksWithOptionalFilters(null,null,null,null,
                null,null,null);
    }

    @Test
    @DisplayName("WhenFindById_thenStatusOk")
    void whenFindById_thenReturnBookEntity() throws Exception {
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        String url =("/api/books/0");
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.genre").value("Terror"))
                .andExpect(jsonPath("$.author").value("Stephen King"))
                .andExpect(jsonPath("$.image").value("image.jpeg"))
                .andExpect(jsonPath("$.title").value("It"))
                .andExpect(jsonPath("$.subtitle").value("Worst clown ever"))
                .andExpect(jsonPath("$.publisher").value("Viking Publisher"))
                .andExpect(jsonPath("$.year").value("1986"))
                .andExpect(jsonPath("$.pages").value(1253))
                .andExpect(jsonPath("$.isbn").value("4578-8665"));

        Mockito.verify(mockBookRepository).findById(0L);
    }

    @Test
    @DisplayName("WhenCreateBook_thenReturnBookEntityCreatedStatusCreated")
    void whenCreateBook_thenReturnBookEntityCreatedStatusCreated() throws Exception {
        Mockito.when(mockBookRepository.save(Mockito.any())).thenReturn(oneTestBook);
        String url =("/api/books");
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(oneTestBook)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.genre").value("Terror"))
                .andExpect(jsonPath("$.author").value("Stephen King"))
                .andExpect(jsonPath("$.image").value("image.jpeg"))
                .andExpect(jsonPath("$.title").value("It"))
                .andExpect(jsonPath("$.subtitle").value("Worst clown ever"))
                .andExpect(jsonPath("$.publisher").value("Viking Publisher"))
                .andExpect(jsonPath("$.year").value("1986"))
                .andExpect(jsonPath("$.pages").value(1253))
                .andExpect(jsonPath("$.isbn").value("4578-8665"));

        Mockito.verify(mockBookRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("WhenUpdateBook_thenReturnBookEntityUpdateStatusOk")
    void whenUpdateBook_thenReturnBookEntityUpdateStatusOk() throws Exception {
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        Mockito.when(mockBookRepository.save(Mockito.any())).thenReturn(oneTestBook);
        String url =("/api/books/0");
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.genre").value("Terror"))
                .andExpect(jsonPath("$.author").value("Stephen King"))
                .andExpect(jsonPath("$.image").value("image.jpeg"))
                .andExpect(jsonPath("$.title").value("It"))
                .andExpect(jsonPath("$.subtitle").value("Worst clown ever"))
                .andExpect(jsonPath("$.publisher").value("Viking Publisher"))
                .andExpect(jsonPath("$.year").value("1986"))
                .andExpect(jsonPath("$.pages").value(1253))
                .andExpect(jsonPath("$.isbn").value("4578-8665"));

        Mockito.verify(mockBookRepository).findById(0L);
        Mockito.verify(mockBookRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("WhenUpdateNotExistBook_thenNotFoundException")
    void whenUpdateNotExistBook_thenNotFoundException() throws Exception {
        Mockito.when(mockBookRepository.findById(1L))
                .thenReturn(books.stream()
                        .filter(book -> book.getId()==1L)
                        .findFirst());
        Mockito.when(mockBookRepository.save(Mockito.any())).thenReturn(oneTestBook);
        String url =("/api/books/0");
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestBook)))
                .andExpect(status().isNotFound());
        Mockito.verify(mockBookRepository).findById(0L);
    }

    @Test
    @DisplayName("WhenUpdateMismatchBook_thenBadRequestException")
    void whenUpdateMismatchBook_thenBadRequestException() throws Exception {
        Mockito.when(mockBookRepository.findById(0L))
                .thenReturn(books.stream()
                        .filter(book -> book.getId()==0L)
                        .findFirst());
        Mockito.when(mockBookRepository.save(Mockito.any())).thenReturn(oneTestBook);
        String url =("/api/books/1");
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestBook)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("WhenDeleteBook_thenResponseNoContent")
    void whenDeleteBook_thenResponseNoContent() throws Exception{
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        String url =("/api/books/0");
        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestBook)))
                .andExpect(status().isNoContent());

        Mockito.verify(mockBookRepository).deleteById(0L);
    }

    @Test
    @DisplayName("WhenDeleteNotExistBook_thenResponseNotFoundException")
    void whenDeleteNotExistBook_thenResponseNotFoundException() throws Exception{
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        String url =("/api/books/1");
        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestBook)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("WhenFindByWhitParams_thenReturnFilterListBooksWhitStatusOK")
    void whenFindByWhitParams_thenReturnFilterListBooksWhitStatusOK () throws Exception {
        String publisher = "Bloomsbury Publishing";
        String genre="Fantasy";
        String year="1997";
        Mockito.when(mockBookRepository.findBooksWithOptionalFilters(
                        "Fantasy",
                        null,
                        null,
                        null,
                        "Bloomsbury Publishing",
                        "1997",
                        null))
                .thenReturn(books.stream().filter(book -> book.getPublisher().equals(publisher))
                        .filter(book -> book.getGenre().equals(genre))
                        .filter(book -> book.getYear().equals(year))
                        .collect(Collectors.toList()));
        String url =("/api/books");
        mvc.perform(get(url)
                        .param("publisher", publisher)
                        .param("genre", genre)
                        .param("year", year)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":0,\"genre\":\"Fantasy\",\"author\":\"J.K Rowling\","
                        + "\"image\":\"image-harry.png\",\"title\":\"Harry Potter and the philosopher's Stone\","
                        + "\"subtitle\":\"-\",\"publisher\":\"Bloomsbury Publishing\",\"year\":\"1997\","
                        + "\"pages\":226,\"isbn\":\"9780747532743\"},{\"id\":0,\"genre\":\"Fantasy\","
                        + "\"author\":\"J.K Rowling\",\"image\":\"image-harry.png\","
                        + "\"title\":\"Harry Potter and the philosopher's Stone - test\",\"subtitle\":\"-\","
                        + "\"publisher\":\"Bloomsbury Publishing\",\"year\":\"1997\",\"pages\":226,"
                        + "\"isbn\":\"9780747532111\"}]"))
                .andExpect(jsonPath("$",hasSize(2)));
        Mockito.verify(mockBookRepository).findBooksWithOptionalFilters(
                "Fantasy",
                null,
                null,
                null,
                "Bloomsbury Publishing",
                "1997",
                null);
    }
    @Test
    @DisplayName("WhenFindBooksWhitParams_thenReturnExceptionNotFoundException")
    void WhenFindBooksWhitParams_thenReturnExceptionNotFoundException () throws Exception {
        String publisher = "Bloomsbury";
        String genre="Fantasy Test";
        String year="1997";
        Mockito.when(mockBookRepository.findBooksWithOptionalFilters(
                        "Fantasy test",
                        null,
                        null,
                        null,
                        "Not publishing",
                        "1997",
                        null))
                .thenReturn(books.stream().filter(book -> book.getPublisher().equals(publisher))
                        .filter(book -> book.getGenre().equals(genre))
                        .filter(book -> book.getYear().equals(year))
                        .collect(Collectors.toList()));
        String url =("/api/books");
        mvc.perform(get(url)
                        .param("publisher", publisher)
                        .param("genre", genre)
                        .param("year", year)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        Mockito.verify(mockBookRepository).findBooksWithOptionalFilters(
                genre,
                null,
                null,
                null,
                publisher,
                year,
                null);
    }
}
