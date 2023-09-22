package com.microservices.product.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@ToString(exclude = { "productCategories" })
@Entity
@Table(name = "T_OUTLET")
public class Outlet {
	@Id 
	@SequenceGenerator(name = "t_outlet_seq", sequenceName = "t_outlet_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_outlet_seq")
	@Column(name = "ID")
    private Integer id;
	@Column(name = "NAME")
    private String name;
	@Column(name = "TOTAL_CATEGORY")
    private Integer totalCategory;
    @OneToMany(fetch = FetchType.LAZY, mappedBy="outlet", cascade = CascadeType.ALL)
    private List<ProductCategory> productCategories;
}
