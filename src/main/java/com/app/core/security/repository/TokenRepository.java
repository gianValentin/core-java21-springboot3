package com.app.core.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.core.security.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	@Query(value = """
			select t from Token t inner join SecurityUser u\s
			on t.user.id = u.id\s
			where u.id = :id and (t.expired = false or t.revoked = false)\s
			""")
	List<Token> findAllValidTokenByUser(Long id);

	Optional<Token> findByToken(String token);
}
