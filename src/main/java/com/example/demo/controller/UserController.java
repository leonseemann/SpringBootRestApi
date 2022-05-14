package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

    @PostMapping("/api/user/{id}")
    public ResponseEntity<User> save(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(userRepo.save(new User(id)), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/user")
    public ResponseEntity<Collection<User>> get(){
        Collection<User> userCollection = userRepo.findAll();
        List<User> response = new ArrayList<>(userCollection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/user/{id}/points")
    public ResponseEntity<String> getPointsOfUser(@PathVariable("id") Long id){
        Optional<User> user = userRepo.findById(id);
        return user.map(value -> new ResponseEntity<>(value.getPoints(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PatchMapping("/api/user/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody Map<Object, Object> fields) {
        try {
            Optional<User> user = userRepo.findById(id);
            if (user.isPresent()) {
                fields.forEach((key, value) -> {
                    System.out.println("[forEach] " + key + ": " + value);
                    Field field = ReflectionUtils.findField(User.class, (String) key);
                    if (field != null) {
                        field.setAccessible(true);
                    }
                    if (field != null) {
                        ReflectionUtils.setField(field, user.get(), value);
                    }
                });
                return new ResponseEntity<>(userRepo.save(user.get()), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
