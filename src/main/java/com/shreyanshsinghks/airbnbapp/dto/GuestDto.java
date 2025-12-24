package com.shreyanshsinghks.airbnbapp.dto;

import com.shreyanshsinghks.airbnbapp.entity.User;
import com.shreyanshsinghks.airbnbapp.entity.enums.Gender;
import lombok.Data;


@Data
public class GuestDto {
    private Long Id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
