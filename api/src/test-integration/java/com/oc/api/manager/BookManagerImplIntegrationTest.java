package com.oc.api.manager;

import com.oc.api.manager.impl.BookManagerImpl;
import com.oc.api.model.beans.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
public class BookManagerImplIntegrationTest {


    @Autowired
    private BookManagerImpl classUnderTest;

    private Book bookToSave;

    /**
     *
     */
    @BeforeEach
    public void init() {
        bookToSave = new Book();
        bookToSave.setAuthorFirstName("Charles");
        bookToSave.setAuthorLastName("Cathala");
        bookToSave.setPictureURL("toto");
        bookToSave.setPublicationDate(LocalDate.of(2020,3,20));
        bookToSave.setSynopsis("toto");
        bookToSave.setTitle("toto");
    }

    /**
     *
     */
    @AfterEach
    public void undef() {
        bookToSave = null;
    }


    @Test
    public void Given_bookListSizeIs3_When_findAll_Then_shouldReturn3() {
        // GIVEN

        // WHEN
        final List<Book> result = classUnderTest.findAll();
        // THEN
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void Given_bookId_When_findById_Then_shouldReturnBeanWithSameId() {
        // GIVEN

        // WHEN
        final Optional<Book> result = classUnderTest.findById(1);
        // THEN
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    public void Given_bookBean_When_save_Then_shouldReturnGivenBookBean() {
        // GIVEN

        // WHEN
        final Book result = classUnderTest.save(bookToSave);
        classUnderTest.deleteById(bookToSave.getId());
        // THEN
        assertThat(result).isEqualTo(bookToSave);
    }

    @Test
    public void Given_titleAndParutionDateOfBookRecordedInDatabase_When_existsBookByTitleAndPublicationDate_Then_shouldReturnTrue() {
        // GIVEN

        // WHEN
        final Boolean result = classUnderTest.existsBookByTitleAndPublicationDate("Dune I", LocalDate.of(2005,6,9));
        // THEN
        assertThat(result).isTrue();
    }

    @Test
    public void Given_titleAndParutionDateOfBookWhichIsNotRecordedInDatabase_When_existsBookByTitleAndPublicationDate_Then_shouldReturnFalse() {
        // GIVEN

        // WHEN
        final Boolean result = classUnderTest.existsBookByTitleAndPublicationDate("Toto", LocalDate.of(2006,6,9));
        // THEN
        assertThat(result).isFalse();
    }

    @Test
    public void Given_bookBeanToDlete_When_deleteById_Then_shouldReturnBookCountLess1() {
        // GIVEN
        Book bookToDelete = classUnderTest.save(bookToSave);
        int bookCount = classUnderTest.findAll().size();
        // WHEN
        classUnderTest.deleteById(bookToDelete.getId());
        final int result = classUnderTest.findAll().size();
        // THEN
        assertThat(result).isEqualTo(bookCount - 1);
    }
}
