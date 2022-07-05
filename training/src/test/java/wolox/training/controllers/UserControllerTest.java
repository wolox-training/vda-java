package wolox.training.controllers;

import static java.util.Arrays.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import wolox.training.models.User;
import wolox.training.repositoies.BookRepository;
import wolox.training.repositoies.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private BookRepository mockBookRepository;

    private User oneTestUser;
    private User twoTestUser;
    private Book oneTestBook;
    private List<User> userTestList;
    private ObjectMapper mapper;
    List<Integer> birthDate;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        birthDate = new ArrayList<>(asList(1980,6,25));
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
        oneTestUser= new User();
        oneTestUser.setName("Ramiro Meneces");
        oneTestUser.setUsername("rammeneces");
        oneTestUser.setBirthdate(LocalDate.of(1980,6,25));
        twoTestUser= new User();
        twoTestUser.setName("Tania Garces");
        twoTestUser.setUsername("tangarces");
        twoTestUser.setBirthdate(LocalDate.of(1992,8,30));
        twoTestUser.addBookToCollection(oneTestBook);
        userTestList=new ArrayList<>();
        userTestList.add(oneTestUser);
        userTestList.add(twoTestUser);
    }



    @Test
    @DisplayName("WhenFindAll_thenReturnListAllUserWhitStatusOK")
    void whenFindAll_thenReturnListAllUserWhitStatusOK() throws Exception {
        Mockito.when(mockUserRepository.findAll()).thenReturn(userTestList);
        String url =("/api/users");
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":0,\"username\":\"rammeneces\","
                        + "\"name\":\"Ramiro Meneces\",\"birthdate\":[1980,6,25],"
                        + "\"books\":[]},{\"id\":0,\"username\":\"tangarces\","
                        + "\"name\":\"Tania Garces\",\"birthdate\":[1992,8,30],"
                        + "\"books\":[{\"id\":0,\"genre\":\"Terror\",\"author\":\"Stephen King\","
                        + "\"image\":\"image.jpeg\",\"title\":\"It\",\"subtitle\":\"Worst clown ever\","
                        + "\"publisher\":\"Viking Publisher\",\"year\":\"1986\",\"pages\":1253,"
                        + "\"isbn\":\"4578-8665\"}]}]"));

        Mockito.verify(mockUserRepository).findAll();
    }

    @Test
    @DisplayName("WhenFindAllInEmptyBD_thenReturnNotFoundException")
    void whenFindAllInEmptyBD_thenReturnNotFoundException() throws Exception {
        userTestList.remove(0);
        userTestList.remove(0);
        Mockito.when(mockUserRepository.findAll()).thenReturn(userTestList);
        String url =("/api/users");
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(mockUserRepository).findAll();
    }

    @Test
    @DisplayName("WhenFindById_thenReturnFoundUserStatusOk")
    void whenFindById_thenReturnFoundUserStatusOk() throws Exception {
        Mockito.when(mockUserRepository.findById(0L))
                .thenReturn(Optional.of(oneTestUser));
        String url =("/api/users/0");
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.username").value("rammeneces"))
                .andExpect(jsonPath("$.name").value("Ramiro Meneces"))
                .andExpect(jsonPath("$.birthdate").value(birthDate));
        Mockito.verify(mockUserRepository).findById(0L);

    }

    @Test
    @DisplayName("WhenFindById_thenReturnFoundUserStatusOk")
    void whenFindNotById_thenReturnNotFoundException() throws Exception {
        Mockito.when(mockUserRepository.findById(0L))
                .thenReturn(Optional.of(oneTestUser));
        String url =("/api/users/1");
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        Mockito.verify(mockUserRepository).findById(1L);
    }

    @Test
    @DisplayName("WhenCreateUser_thenReturnUserStatusIsCreated")
    void whenCreateUser_thenReturnUserStatusIsCreated() throws Exception {
        Mockito.when(mockUserRepository.save(oneTestUser)).thenReturn(oneTestUser);
        String url =("/api/users");
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(oneTestUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.username").value("rammeneces"))
                .andExpect(jsonPath("$.name").value("Ramiro Meneces"))
                .andExpect(jsonPath("$.birthdate").value(birthDate));
        Mockito.verify(mockUserRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("whenUpdateUser_thenReturnUpdatedUserStatusOK")
    void whenUpdateUser_thenReturnUpdatedUserStatusOK() throws Exception {
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(Optional.of(oneTestUser));
        Mockito.when(mockUserRepository.save(Mockito.any())).thenReturn(oneTestUser);
        String url =("/api/users/0");
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.username").value("rammeneces"))
                .andExpect(jsonPath("$.name").value("Ramiro Meneces"))
                .andExpect(jsonPath("$.birthdate").value(birthDate));
        Mockito.verify(mockUserRepository).findById(0L);
        Mockito.verify(mockUserRepository).save(Mockito.any());

    }

    @Test
    @DisplayName("WhenUpdateNotExistUser_thenReturnNotFoundException")
    void whenUpdateNotExistUser_thenReturnNotFoundException() throws Exception {
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(userTestList.stream()
                                                    .filter(user -> user.getId()==1L)
                                                    .findAny());
        Mockito.when(mockUserRepository.save(Mockito.any())).thenReturn(oneTestUser);
        String url =("/api/users/0");
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestUser)))
                .andExpect(status().isNotFound());
        Mockito.verify(mockUserRepository).findById(0L);
    }

    @Test
    @DisplayName("WhenUpdateMismatchUser_thenReturnBadRequestException")
    void whenUpdateMismatchUser_thenReturnBadRequestException() throws Exception {
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(userTestList.stream()
                .filter(user -> user.getId()==0L)
                .findAny());
        Mockito.when(mockUserRepository.save(Mockito.any())).thenReturn(oneTestUser);
        String url =("/api/users/1");
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("WhenDelete_thenRespondIsNotContent")
    void whenDelete_thenRespondIsNotContent() throws Exception{
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(Optional.of(oneTestUser));
        String url =("/api/users/0");
        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestBook)))
                .andExpect(status().isNoContent());

        Mockito.verify(mockUserRepository).deleteById(0L);
    }

    @Test
    @DisplayName("WhenDeleteNotExistUser_thenRespondNotFoundException")
    void whenDeleteNotExistUser_thenRespondNotFoundException() throws Exception{
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(Optional.of(oneTestUser));
        String url =("/api/users/0");
        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(oneTestBook)))
                .andExpect(status().isNoContent());

        Mockito.verify(mockUserRepository).deleteById(0L);
    }

    @Test
    @DisplayName("WhenAddBookInUserCollection_thenRespondStatusOk")
    void whenAddBookInUserCollection_thenRespondStatusOk()throws Exception {
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(Optional.of(oneTestUser));
        Mockito.when(mockUserRepository.save(oneTestUser)).thenReturn(oneTestUser);

        String url =("/api/users/0/books");
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":0}"))
                .andExpect(status().isOk());

        Mockito.verify(mockUserRepository).findById(0L);
        Mockito.verify(mockBookRepository).findById(0L);
        Mockito.verify(mockUserRepository).save(Mockito.any());
    }
    @Test
    @DisplayName("whenTryAddNotExistBookInUserCollection_thenRespondNotFoundException")
    void whenTryAddNotExistBookInUserCollection_thenRespondNotFoundException()throws Exception {
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.empty());
        Mockito.when(mockUserRepository.save(oneTestUser)).thenReturn(oneTestUser);

        String url =("/api/users/0/books");
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0}"))
                .andExpect(status().isNotFound());
        Mockito.verify(mockBookRepository).findById(0L);
    }

    @Test
    @DisplayName("WhenTryAddBookNotExistUser_thenRespondNotFoundException")
    void whenTryAddBookNotExistUser_thenRespondNotFoundException()throws Exception {
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        String url =("/api/users/0/books");
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0}"))
                .andExpect(status().isNotFound());
        Mockito.verify(mockUserRepository).findById(0L);
    }

    @Test
    @DisplayName("WhenRemoveBookInUserCollection_thenRespondStatusIsNotContent")
    void whenRemoveBookInUserCollection_thenRespondStatusIsNotContent() throws Exception {
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        Mockito.when(mockUserRepository.findById(1L)).thenReturn(Optional.of(twoTestUser));
        Mockito.when(mockUserRepository.save(twoTestUser)).thenReturn(twoTestUser);

        String url =("/api/users/1/books");
        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0}"))
                .andExpect(status().isNoContent());

        Mockito.verify(mockUserRepository).findById(1L);
        Mockito.verify(mockBookRepository).findById(0L);
        Mockito.verify(mockUserRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("whenTryDeleteNotExistBookInUserCollection_thenRespondNotFoundException")
    void whenTryDeleteNotExistBookInUserCollection_thenRespondNotFoundException()throws Exception {
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.empty());
        Mockito.when(mockUserRepository.save(oneTestUser)).thenReturn(oneTestUser);

        String url =("/api/users/0/books");
        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0}"))
                .andExpect(status().isNotFound());
        Mockito.verify(mockBookRepository).findById(0L);
    }

    @Test
    @DisplayName("WhenTryDeleteBookNotExistUser_thenRespondNotFoundException")
    void whenTryDeleteBookNotExistUser_thenRespondNotFoundException()throws Exception {
        Mockito.when(mockBookRepository.findById(0L)).thenReturn(Optional.of(oneTestBook));
        Mockito.when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        String url =("/api/users/0/books");
        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0}"))
                .andExpect(status().isNotFound());
        Mockito.verify(mockUserRepository).findById(0L);
    }
}
