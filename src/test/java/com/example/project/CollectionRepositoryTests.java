package com.example.project;

import com.example.project.entity.Collection;
import com.example.project.entity.User;
import com.example.project.repository.CollectionRepository;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CollectionRepositoryTests {

    @Autowired
    private CollectionRepository collectionrepo;

    @Autowired
    private UserRepository userrepo;

    @Test
    public void testCreateRoles() {

        User user = userrepo.findByEmail("admin@gmail.com");

        Collection mark = new Collection("marks");
        Collection book = new Collection("books");

        mark.setOwner(user);

        book.setOwner(user);


        collectionrepo.saveAll(List.of(mark, book));

        List<Collection> listCollections = collectionrepo.findAll();

        assertThat(listCollections.size()).isEqualTo(2);

    }

}
