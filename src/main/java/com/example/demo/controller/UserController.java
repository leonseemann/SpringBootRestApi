package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

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

    //Create user with id
    @PostMapping("/api/user/{id}")
    public ResponseEntity<User> save(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(userRepo.save(new User(id)), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete user by id
    @DeleteMapping("/apt/user/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        try {
            userRepo.deleteById(id);
            return new ResponseEntity<>("Deleted user @" + id, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get all user
    @GetMapping("/api/user")
    public ResponseEntity<Collection<User>> get(){
        Collection<User> userCollection = userRepo.findAll();
        List<User> response = new ArrayList<>(userCollection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //Get user points with id
    @GetMapping("/api/user/{id}/points")
    public ResponseEntity<Integer> getPointsOfUser(@PathVariable("id") Long id){
        Optional<User> user = userRepo.findById(id);
        return user.map(value -> new ResponseEntity<>(value.getPoints(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    //Update user by id
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

    //Add points by id
    @PutMapping("/api/users/{id}/add/{points}/points")
    public ResponseEntity<User> addPoints(@PathVariable("id") Long id, @PathVariable("points") Integer points){
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            user.get().setPoints(user.get().getPoints() + points);
            return new ResponseEntity<>(userRepo.save(user.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
