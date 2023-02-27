package study.querydsl.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private int id;
}
