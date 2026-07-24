package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish,Long> {
    // Spring Data JPA automatically provides implementation for this method!
    boolean existsBySenderUserIdAndReceiverUserIdAndWishType(
            String senderUserId,
            String receiverUserId,
            String wishType
    );
}
