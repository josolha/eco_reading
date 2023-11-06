package com.checkcheck.ecoreading.domain.boards.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@Entity
@Table(name = "boards")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Boards extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long board_id;

    @Column(name = "giver_user_id")
    private Long giver_user_id;

    @Column(name = "message")
    private String message;

}
