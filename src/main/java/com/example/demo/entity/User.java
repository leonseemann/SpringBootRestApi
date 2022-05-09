package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tbl_user")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    private Integer id;
    private String points;

    public User(String points) {
        this.points = points;
    }

    @JsonCreator
    public User(@JsonProperty Integer id, @JsonProperty String points) {
        this.id = id;
        this.points = points;
    }

    public User(Integer id) {
        this.id = id;
    }
}
