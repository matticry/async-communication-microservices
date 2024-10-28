package com.matticry.microservicesproject.dto;

import com.matticry.microservicesproject.model.util.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {

    private Long id; // heredado de Person
    private String name;
    private Genre genre;
    private Integer age;
    private String dni;
    private String address;
    private String phone;
    private String passwordClient;
    private Character statusClient = 'A';
    private List<AccountDTO> accounts;
}
