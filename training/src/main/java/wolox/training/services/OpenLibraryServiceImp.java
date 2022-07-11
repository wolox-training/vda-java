package wolox.training.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.dtos.BookDto;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositoies.BookRepository;

@Service
public class OpenLibraryServiceImp implements OpenLibraryService{

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookDto bookDto;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<BookDto> bookInfo(String isbn) {
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.ISO_8859_1));
        bookDto.setIsbn(isbn);
        String stringRequestLibrary = restTemplate.getForObject(url, String.class);
        try {
            JsonNode jsonNodeResponse = objectMapper.readTree(stringRequestLibrary);
            JsonNode jsonNodeBook = jsonNodeResponse.get("ISBN:" + isbn);
            if (jsonNodeBook.isEmpty()) {
                return Optional.ofNullable(bookDto);
            } else {
                parseToBookDto(jsonNodeBook);
            }
        }catch (JsonProcessingException | NullPointerException jpex) {
            throw new BookNotFoundException();
        }
        return Optional.of(bookDto);
    }

    private void parseToBookDto(JsonNode jsonNodeBook)  {
        List<String> publisherTmp =new ArrayList<>();
        List<String> authorTmp =new ArrayList<>();
        bookDto.setTitle(jsonNodeBook.get("title").asText());
        try {
            bookDto.setSubtitle(jsonNodeBook.get("subtitle").asText());
        }catch (NullPointerException npex){
            bookDto.setSubtitle("-");
        }
        try {
            ArrayNode publishersNode = (ArrayNode) jsonNodeBook.get("publishers");
            for (int i = 0; i < publishersNode.size(); i++) {
                JsonNode publisher = publishersNode.get(i);
                publisherTmp.add(publisher.get("name").asText());
            }
            bookDto.setPublishers(publisherTmp);
        }catch(NullPointerException npex) {
            bookDto.setPublishers(Collections.singletonList("-"));
        }
        try {
            bookDto.setPublishDate(jsonNodeBook.get("publish_date").asText()
                    .substring(jsonNodeBook.get("publish_date").asText().lastIndexOf(" ") + 1));
        }catch(NullPointerException npex) {
            bookDto.setPublishDate("-");
        }
        try {
            bookDto.setNumeberOfPages(jsonNodeBook.get("number_of_pages").asInt());
        }catch (NullPointerException npex) {
            bookDto.setNumeberOfPages(1);
        }
        try {
            ArrayNode authorsNode = (ArrayNode) jsonNodeBook.get("authors");
            for (int i = 0; i < authorsNode.size(); i++) {
                JsonNode author = authorsNode.get(i);
                authorTmp.add(author.get("name").asText());
            }
            bookDto.setAuthors(authorTmp);
        }catch (NullPointerException npex) {
            bookDto.setAuthors(Collections.singletonList("-"));
        }
        try {
            bookDto.setCover(jsonNodeBook.get("cover").get("medium").asText());
        }catch (NullPointerException npex) {
            bookDto.setCover("-");
        }
    }
    @Override
    public Optional<Book> saveBookDto(BookDto bookDto){
        Book book = new Book();
        book.setGenre("-");
        if(bookDto.getAuthors().isEmpty()){
            book.setAuthor("-");
        }else{
            book.setAuthor(bookDto.getAuthors().get(0));
        }
        book.setImage(bookDto.getCover());
        book.setTitle(bookDto.getTitle());
        book.setSubtitle(bookDto.getSubtitle());
        if(bookDto.getPublishers().isEmpty()){
            book.setPublisher("-");
        }else {
            book.setPublisher(bookDto.getPublishers().get(0));
        }
        book.setYear(bookDto.getPublishDate());
        book.setPages(bookDto.getNumeberOfPages());
        book.setIsbn(bookDto.getIsbn());
        book = bookRepository.save(book);
        return Optional.of(book);
    }
}
