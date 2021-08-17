package com.example.project.service;
import java.util.ArrayList;
import java.util.List;

import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.entity.Role;
import com.example.project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired PasswordEncoder passwordEncoder;

    public void registerDefaultUser(User user) {
        Role roleUser = roleRepo.findByName("ROLE_USER");
        user.addRole(roleUser);
        user.setIsactive(true);
        encode(user);
        userRepo.save(user);
    }
    public boolean emailExist(String email) {
        return userRepo.findByEmail(email) != null;
    }

    public void changeStatus(User user)
    {
        if(user.getIsactive()) {user.setIsactive(false);}
        else user.setIsactive(true);
        userRepo.save(user);
    }
    public void addRole(User user)
    {
        Role roleUser = roleRepo.findByName("ROLE_ADMIN");
        user.addRole(roleUser);
        userRepo.save(user);
    }
    public void deleteRole(User user)
    {
        Role roleUser = roleRepo.findByName("ROLE_ADMIN");
        user.deleteRole(roleUser);
        userRepo.save(user);
    }

    public boolean checkRole(User user)
    {
        Role roleUser = roleRepo.findByName("ROLE_ADMIN");
        if(user.getRoles().contains(roleUser)) return true;
        else return false;
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

    public List<String> listEmails(List<Long> ids)
    {
        List<String> emails=new ArrayList<String>();
        for(Long id:ids)
            emails.add(userRepo.findById(id).get().getEmail());
        return emails;
    }

    public void save(User user) {
        encode(user);
        userRepo.save(user);
    }

    public void delete(Long id) {

        userRepo.deleteById(id);
    }

    private void encode(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
