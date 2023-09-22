package com.microservices.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "T_PRODUCT")
public class Product {
	@Id 
	@SequenceGenerator(name = "t_product_seq", sequenceName = "t_product_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_product_seq")
	@Column(name = "ID")
    private Integer id;
	@Column(name = "NAME")
    private String name;
	@Column(name = "DESCRIPTION")
    private String description;
	@Column(name = "PRICE")
    private Integer price;
    @ManyToOne
    @JoinColumn(name="product_category_id", nullable=false)
    private ProductCategory productCategory;
}
