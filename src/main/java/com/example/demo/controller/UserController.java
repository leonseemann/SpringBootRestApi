package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    IUserRepo iUserRepo;

    @PostMapping("/users")
    public ResponseEntity<User> save(@RequestBody User user) {
        try{
            return new ResponseEntity<>(iUserRepo.save(user), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
