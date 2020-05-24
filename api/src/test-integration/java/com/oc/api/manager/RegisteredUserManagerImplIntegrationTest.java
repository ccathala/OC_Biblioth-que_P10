package com.oc.api.manager;

import com.oc.api.manager.impl.RegisteredUserManagerImpl;
import com.oc.api.model.beans.RegisteredUser;
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
public class RegisteredUserManagerImplIntegrationTest {

    @Autowired
    private RegisteredUserManagerImpl classUnderTest;

    private RegisteredUser user;

    /**
     *
     */
    @BeforeEach
    public void init() {
        user = new RegisteredUser();
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Titi");
        user.setPassword("azerty");
        user.setRoles("USER");
    }

    /**
     *
     */
    @AfterEach
    public void undef() {
        user = null;
    }

    @Test
    public void Given_registeredUserListSize_When_findAll_Then_shouldReturn4() {
        // GIVEN

        // WHEN
        List<RegisteredUser> result = classUnderTest.findAll();
        // THEN
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    public void Given_registeredUserBeanId_When_findById_Then_shouldReturn1() {
        // GIVEN

        // WHEN
        final RegisteredUser result = classUnderTest.findById(1).get();
        // THEN
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    public void Given_regiseteredUserBean_When_save_Then_shouldReturnTheSameBean() {
        // GIVEN

        // WHEN
        final RegisteredUser result = classUnderTest.save(user);
        classUnderTest.deleteById(result.getId());
        // THEN
        assertThat(result).isEqualTo(user);
    }

    @Test
    public void Given_registeredUserBeanToDelete_When_deleteById_Then_shouldReturnUserListSizeLess1() {
        // GIVEN
        RegisteredUser userToDelete = classUnderTest.save(user);
        int userCount = classUnderTest.findAll().size();
        // WHEN
        classUnderTest.deleteById(userToDelete.getId());
        int result = classUnderTest.findAll().size();
        // THEN
        assertThat(result).isEqualTo(userCount - 1);
    }

    @Test
    public void Given_userEmail_When_findByEmail_Then_shouldReturnRegisteredUserBeanWithIdIs1() {
        // GIVEN

        // WHEN
        final RegisteredUser result = classUnderTest.findByEmail("ccathala.dev@gmail.com");
        // THEN
        assertThat(result.getId()).isEqualTo(1);
    }
}
