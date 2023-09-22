package com.microservices.product.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString(exclude = { "products" })
@Entity
@Table(name = "T_PRODUCT_CATEGORY")
public class ProductCategory {
	@Id 
	@SequenceGenerator(name = "t_product_category_seq", sequenceName = "t_product_category_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_product_category_seq")
	@Column(name = "ID")
    private Integer id;
	@Column(name = "NAME")
    private String name;
	@Column(name = "TOTAL_PRODUCT")
    private Integer totalProduct;
    @ManyToOne
    @JoinColumn(name="outlet_id", nullable=false)
    private Outlet outlet;
    @OneToMany(fetch = FetchType.LAZY, mappedBy="productCategory", cascade = CascadeType.ALL)
    private List<Product> products;
}
