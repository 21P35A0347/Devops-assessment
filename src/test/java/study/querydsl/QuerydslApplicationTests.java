package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import study.querydsl.entity.QUser;
import study.querydsl.entity.User;

import javax.persistence.EntityManager;


@SpringBootTest
@Transactional
@Commit
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
		Assertions.assertThat(result.getId()).isEqualTo(user.getId());

	}

}
