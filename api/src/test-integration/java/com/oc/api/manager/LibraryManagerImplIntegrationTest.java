package com.oc.api.manager;

import com.oc.api.manager.impl.LibraryManagerImpl;
import com.oc.api.model.beans.Library;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LibraryManagerImplIntegrationTest {

    @Autowired
    private LibraryManagerImpl classUndertest;

    private Library library;

    /**
     *
     */
    @BeforeEach
    public void init() {
        library = new Library();
        library.setName("Tarnos");
    }

    /**
     *
     */
    @AfterEach
    public void undef() {
        library = null;
    }

    @Test
    public void Given_libraryListSizeIs3_When_findAll_Then_shouldReturn3() {
        // GIVEN

        // WHEN
        final List<Library> result = classUndertest.findAll();
        // THEN
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void Given_libraryIdIs1_When_findById_Then_shouldReturn1() {
        // GIVEN

        // WHEN
        final Library result = classUndertest.findById(1).get();
        // THEN
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    public void Given_libraryBean_When_save_Then_shouldReturnTheSameBean() {
        // GIVEN

        // WHEN
        final Library result = classUndertest.save(library);
        classUndertest.deleteById(result.getId());
        // THEN
        assertThat(result).isEqualTo(library);
    }

    @Test
    public void Given_libraryBeanToDelete_When_deleteById_Then_shouldReturnLibraryListSizeLess1() {
        // GIVEN
        Library libraryToDelete = classUndertest.save(library);
        int libraryCount = classUndertest.findAll().size();
        // WHEN
        classUndertest.deleteById(libraryToDelete.getId());
        final int result = classUndertest.findAll().size();
        // THEN
        assertThat(result).isEqualTo(libraryCount -1);
    }
}
