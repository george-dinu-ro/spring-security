package my.work.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import my.work.model.MyUser;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {

	Optional<MyUser> findByUsername(String username);
}
