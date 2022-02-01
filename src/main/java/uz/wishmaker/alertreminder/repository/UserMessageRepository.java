package uz.wishmaker.alertreminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.wishmaker.alertreminder.entity.Users;

import java.util.Optional;

@Repository
public interface UserMessageRepository extends JpaRepository<Users,Integer> {
    Optional<Users> findByPhoneNumber(String phoneNumber);
}
