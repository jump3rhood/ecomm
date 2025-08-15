package org.john.personal.productservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Data
public class ProductResponseDTO implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private String price;
    private String image;
    private String categoryTitle;
}
