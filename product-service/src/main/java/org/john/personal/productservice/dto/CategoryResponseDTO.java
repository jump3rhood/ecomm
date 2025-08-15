package org.john.personal.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryResponseDTO implements Serializable {
    private Integer id;
    private String title;
}
