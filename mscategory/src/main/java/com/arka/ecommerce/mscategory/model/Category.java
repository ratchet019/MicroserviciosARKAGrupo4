package com.arka.ecommerce.mscategory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    private Long id;
    private String name;
    private String description;

    
}
