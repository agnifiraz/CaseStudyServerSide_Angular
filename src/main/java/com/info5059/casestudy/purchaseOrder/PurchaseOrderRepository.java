package com.info5059.casestudy.purchaseOrder;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "purchaseorders", path = "purchaseorders")
public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long> {
    // extend so we can return the number of rows deleted
    @Modifying
    @Transactional
    @Query("delete from PurchaseOrder where id = ?1")
    int deleteOne(Long purchaseOrderid);

    List<PurchaseOrder> findByVendorid(long vendorid);

}
