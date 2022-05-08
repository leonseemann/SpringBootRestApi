package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    IUserRepo iUserRepo;

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> get(){
        Collection<User> userCollection = iUserRepo.findAll();
        List<User> response = new ArrayList<>(userCollection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<User> save(@PathVariable("id") Integer id) {
        try {
            return new ResponseEntity<>(iUserRepo.save(new User(id)), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Integer id, @RequestBody Map<Object, Object> fields) {
        try {
            Optional<User> user = iUserRepo.findById(id);
            if (user.isPresent()) {
                fields.forEach((key, value) -> {
                    System.out.println("[forEach] " + key + ": " + value);
                    Field field = ReflectionUtils.findField(User.class, (String) key);
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user.get(), value);
                });
                return new ResponseEntity<>(iUserRepo.save(user.get()), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
