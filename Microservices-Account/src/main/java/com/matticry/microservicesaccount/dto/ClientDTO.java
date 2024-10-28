package com.matticry.microservicesaccount.dto;

import com.matticry.microservicesaccount.enums.Genre;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Getter
@Setter
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    private String dni;
    private String name;
    private Genre genre;
    private Integer age;
    private String address;
    private String phone;
    private Character statusClient;

}
