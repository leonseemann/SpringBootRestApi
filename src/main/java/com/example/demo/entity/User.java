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
    private Long id;
    private Integer points;

    public User(Integer points) {
        this.points = points;
    }

    @JsonCreator
    public User(@JsonProperty Long id, @JsonProperty Integer points) {
        this.id = id;
        this.points = points;
    }

    public User(Long id) {
        this.id = id;
    }
}
