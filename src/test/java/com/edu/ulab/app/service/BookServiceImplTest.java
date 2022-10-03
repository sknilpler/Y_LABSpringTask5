package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Person person = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        person = userRepository.save(person);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(1L);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPerson(person);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление книги. Должно пройти успешно.")
    void updateBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        BookDto bookUpdateDto = new BookDto();
        bookUpdateDto.setUserId(1L);
        bookUpdateDto.setAuthor("new author");
        bookUpdateDto.setTitle("new title");
        bookUpdateDto.setPageCount(1000);

        Book updateBook = new Book();
        updateBook.setId(1L);
        updateBook.setPageCount(1000);
        updateBook.setTitle("new title");
        updateBook.setAuthor("new author");
        updateBook.setPerson(person);

        BookDto resultUpdateDto = new BookDto();
        resultUpdateDto.setUserId(1L);
        resultUpdateDto.setAuthor("new author");
        resultUpdateDto.setTitle("new title");
        resultUpdateDto.setPageCount(1000);

        //when
        when(bookMapper.bookDtoToBook(bookUpdateDto)).thenReturn(updateBook);
        when(bookRepository.save(updateBook)).thenReturn(updateBook);
        when(bookMapper.bookToBookDto(updateBook)).thenReturn(resultUpdateDto);

        //then
        BookDto result = bookService.updateBook(bookUpdateDto);
        assertEquals("new author", result.getAuthor());
        assertEquals("new title", result.getTitle());
    }

    @Test
    @DisplayName("Получение книги. Должно пройти успешно.")
    void getBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        Long bookId = 1L;

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(savedBook));
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto getBookDto = bookService.getBookById(bookId);
        assertEquals(1L, getBookDto.getId());
        assertEquals(1L, getBookDto.getUserId());
        assertEquals(1000, getBookDto.getPageCount());
        assertEquals("test title", getBookDto.getTitle());
        assertEquals("test author", getBookDto.getAuthor());
    }

    @Test
    @DisplayName("Получение всех книг по id юзера. Должно пройти успешно.")
    void getAllBook_Test() {
        //given
        Person person = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");
        person.setId(1L);


        BookDto result1 = new BookDto();
        result1.setId(1L);
        result1.setUserId(person.getId());
        result1.setAuthor("author1");
        result1.setTitle("title1");
        result1.setPageCount(1000);

        Book savedBook1 = new Book();
        savedBook1.setId(1L);
        savedBook1.setPageCount(1000);
        savedBook1.setTitle("title1");
        savedBook1.setAuthor("author1");
        savedBook1.setPerson(person);

        BookDto result2 = new BookDto();
        result2.setId(2L);
        result2.setUserId(1L);
        result2.setAuthor("author2");
        result2.setTitle("title2");
        result2.setPageCount(1000);

        Book savedBook2 = new Book();
        savedBook2.setId(2L);
        savedBook2.setPageCount(1000);
        savedBook2.setTitle("title2");
        savedBook2.setAuthor("author2");
        savedBook2.setPerson(person);

        List<Book> personBooks = new ArrayList<>();
        personBooks.add(savedBook1);
        personBooks.add(savedBook2);

        //when
        when(bookMapper.bookToBookDto(savedBook1)).thenReturn(result1);
        when(bookMapper.bookToBookDto(savedBook2)).thenReturn(result2);
        when(bookRepository.findAllByPersonId(person.getId())).thenReturn(personBooks);

        //then
        List<Book> books = bookService.getBooksByUserId(person.getId());
        assertEquals(books.size(), 2);
        assertEquals("title1", books.get(0).getTitle());
        assertEquals("title2", books.get(1).getTitle());

    }

    @Test
    @DisplayName("Удаление книги. Должно пройти успешно.")
    void deleteBook_Test() {
        //given
        Long id = 1L;

        //when
        doNothing().when(bookRepository).deleteById(id);

        //then
        bookService.deleteBookById(id);
    }


    // * failed
    @Test
    @DisplayName("Ошибка при выдачи книги. Должно пройти успешно.")
    void getFailedBook_Test() {
        //given
        Long bookId = 1L;

        Person person = new Person();
        person.setId(1L);

        BookDto result = new BookDto();
        result.setId(bookId);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book savedBook = new Book();
        savedBook.setId(bookId);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when
        when(bookRepository.findById(bookId)).thenThrow(new NotFoundException("Book with id: " + bookId + " not found"));
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        assertThatThrownBy(() -> bookService.getBookById(bookId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book with id: 1 not found");
    }

    @Test
    @DisplayName("Ошибка при выдачи книги. Должно пройти успешно.")
    void getFailedBook2_Test() {
        //given
        Long bookId = 1L;

        Person person = new Person();
        person.setId(1L);

        BookDto result = new BookDto();
        result.setId(bookId);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book savedBook = new Book();
        savedBook.setId(bookId);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        assertThatThrownBy(() -> bookService.getBookById(bookId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found!");
    }

}
