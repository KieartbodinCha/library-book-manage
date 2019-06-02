package com.bookorder.management.repositories;

import com.bookorder.management.entities.BookOrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookOrderRepository extends JpaRepository<BookOrderModel, Long> {

    @Query(nativeQuery = true, value = "SELECT SUM(price) FROM books WHERE id IN (:ids)")
    BigDecimal selectSumPriceByIds(@Param("ids") List<Long> ids);

    @Query(nativeQuery = true , value = "DELETE FROM books_order WHERE user_id = ?1")
    Long deleteOrdersByUserId(Long userId);

}
