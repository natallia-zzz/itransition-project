package com.example.project.service;
import java.util.List;

import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.entity.Role;
import com.example.project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired PasswordEncoder passwordEncoder;

    public void registerDefaultUser(User user) {
        Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);
        encode(user);
        userRepo.save(user);
    }

    public List<User> listAll() {
        return userRepo.findAll();
    }

    public User get(Long id) {
        return userRepo.findById(id).get();
    }

    public List<Role> listRoles() {
        return roleRepo.findAll();
    }

    public void save(User user) {
        encode(user);
        userRepo.save(user);
    }

    private void encode(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
