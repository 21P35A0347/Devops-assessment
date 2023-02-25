package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import study.querydsl.entity.QUser;
import study.querydsl.entity.User;



@SpringBootTest
@Transactional
class QuerydslApplicationTests {

	@Autowired
	EntityManager em ;
	@Test
	void contextLoads() {
		User user = new User();

		em.persist(user);
		JPAQueryFactory query = new JPAQueryFactory(em);

		QUser qUser = new QUser("user");
		User result = query.selectFrom(qUser).fetchOne();

		Assertions.assertThat(result).isEqualTo(user);

	}

}
