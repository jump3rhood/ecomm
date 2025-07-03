package com.john.ecommerce.productservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class Product extends BaseModel {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;
}
