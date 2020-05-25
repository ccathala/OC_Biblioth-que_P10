package com.oc.api.manager;

import com.oc.api.manager.impl.BorrowManagerImpl;
import com.oc.api.model.beans.Book;
import com.oc.api.model.beans.Borrow;
import com.oc.api.model.beans.Library;
import com.oc.api.model.beans.RegisteredUser;
import com.oc.api.web.exceptions.FunctionnalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
public class BorrowManagerIntegrationTest {

    @Autowired
    private BorrowManagerImpl classUnderTest;

    private Borrow borrow;

    /**
     *
     */
    @BeforeEach
    public void init() {
        borrow = new Borrow();
        borrow.setBookReturned(false);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setExtendedDuration(false);
        borrow.setReturnDate(LocalDate.now().plusWeeks(4));
        Book book = new Book();
        book.setId(3);
        Library library = new Library();
        library.setId(2);
        RegisteredUser user = new RegisteredUser();
        user.setId(4);
        borrow.setBook(book);
        borrow.setLibrary(library);
        borrow.setRegistereduser(user);
    }

    /**
     *
     */
    @AfterEach
    public void undef() {
        borrow = null;
    }

    @Test
    public void Given_borrowListSizeIs5_When_getAllBorrows_Then_shouldReturn5() {
        // GIVEN

        // WHEN
        final List<Borrow> result = classUnderTest.getAllBorrows();
        // THEN
        assertThat(result.size()).isEqualTo(5);
    }

    @Test
    public void Given_borrowId_When_getById_Then_shouldReturnBorrowBeanWithTheSameId() {
        // GIVEN

        // WHEN
        final Borrow result = classUnderTest.getById(1).get();
        // THEN
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    public void Given_borrowBean_When_save_Then_shoudReturnSavedBorrowBean() throws FunctionnalException {
        // GIVEN

        // WHEN
        final Borrow result = classUnderTest.save(borrow, "out");
        classUnderTest.deleteById(result.getId());
        borrow.setId(result.getId());
        // THEN
        assertThat(result.toString()).isEqualTo(borrow.toString());
    }

    @Test
    public void Given_borrowBeanAndExtendOperationType_When_save_Then_shouldReturnReturnDatePlus4Weeks() throws FunctionnalException {
        // GIVEN

        // WHEN
        final Borrow result = classUnderTest.save(borrow, "extend");
        classUnderTest.deleteById(result.getId());
        // THEN
        assertThat(result.getReturnDate()).isEqualTo(borrow.getReturnDate().plusWeeks(4));
    }

    @Test
    public void Given_borrowBeanAndExtendOperationType_When_save_Then_shouldReturnExtendedDurationTrue() throws FunctionnalException {
        // GIVEN

        // WHEN
        final Borrow result = classUnderTest.save(borrow, "extend");
        classUnderTest.deleteById(result.getId());
        // THEN
        assertThat(result.getExtendedDuration()).isTrue();
    }

    @Test
    public void Given_borrowToDelete_When_deleteById_Then_shouldReturnBorrowListLess1() throws FunctionnalException {
        // GIVEN
        Borrow borrowToDelete = classUnderTest.save(borrow, "out");
        int borrowCount = classUnderTest.getAllBorrows().size();
        // WHEN
        classUnderTest.deleteById(borrowToDelete.getId());
        final int result = classUnderTest.getAllBorrows().size();
        // THEN
        assertThat(result).isEqualTo(borrowCount -1);
    }
}
