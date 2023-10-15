package com.info5059.casestudy.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends JpaRepository<Product, String> {
    // extend so we can return the number of rows deleted
    @Modifying
    @Transactional
    @Query("delete from Product p where p.id = ?1")
    int deleteOne(String productid);

    List<Product> findByVendorid(Long vendorid);

}
