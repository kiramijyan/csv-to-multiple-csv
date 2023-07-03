package com.dannyboodman.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCsv {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
}
