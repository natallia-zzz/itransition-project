package com.example.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.example.project.entity.Role;
import com.example.project.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

    @Autowired private RoleRepository repo;
    @Test
    public void testCreateRoles() {
        Role user = new Role("ROLE_USER");
        Role admin = new Role("ROLE_ADMIN");

        List<Role> roles=new ArrayList<>();
        roles.add(user);
        roles.add(admin);
        repo.saveAll(roles);

        List<Role> listRoles = repo.findAll();

        assertThat(listRoles.size()).isEqualTo(2);
    }

}