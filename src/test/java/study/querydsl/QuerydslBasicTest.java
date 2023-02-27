package study.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member memberA = new Member("memberA", 11, teamA);
        Member memberB = new Member("memberB", 12, teamB);
        Member memberC = new Member("memberC", 13, teamA);
        Member memberD = new Member("memberD", 14, teamB);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
    }

    @Test
    public void startJPQL() {
        Member jpqlMember = em.createQuery("select m from Member m where username = :username", Member.class)
                .setParameter("username", "memberA")
                .getSingleResult();
        Assertions.assertThat(jpqlMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    public void startQuerydsl() {

        Member memberA = queryFactory.selectFrom(member)
                .where(member.username.eq("memberA"))
                .fetchOne();

        Assertions.assertThat(memberA.getUsername()).isEqualTo("memberA");
    }

    @Test
    public void search() {
        List<Member> memberList = queryFactory.selectFrom(member)
                .where(member.username.startsWith("member").and(member.age.between(11, 13)))
                .fetch();

        Assertions.assertThat(memberList).isNotEmpty();
        Assertions.assertThat(memberList.size()).isEqualTo(3);

    }
    @Test
    public void searchParam() {
        List<Member> memberList = queryFactory.selectFrom(member)
                .where(member.username.startsWith("member"),
                        member.age.between(11, 13))
                .fetch();

        Assertions.assertThat(memberList).isNotEmpty();
        Assertions.assertThat(memberList.size()).isEqualTo(3);

    }
    @Test
    public void aggeration() {
        List<Tuple> tupleList = queryFactory.select(
                        member.count(),
                        member.age.max(),
                        member.age.avg(),
                        member.age.min()
                ).from(member)
                .fetch();
        Tuple tuple = tupleList.get(0);
        Assertions.assertThat(tuple.get(member.count())).isEqualTo(4);
        Assertions.assertThat(tuple.get(member.age.max())).isEqualTo(14);
        Assertions.assertThat(tuple.get(member.age.avg())).isEqualTo(12.5);
        Assertions.assertThat(tuple.get(member.age.min())).isEqualTo(11);
    }
    
    @Test
    public void groupby() throws Exception {
        List<Tuple> tupleList = queryFactory.select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();


    }

    @PersistenceUnit
    EntityManagerFactory emf;
    
    @Test
    public void joinfetch() throws Exception {
        Member memberA1 = queryFactory.select(member)
                .from(member)
                .join(member.team, team)
                .where(member.username.eq("MemberA"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(memberA1.getTeam());
        Assertions.assertThat(loaded).as("로딩 체크").isFalse();

    }


}
