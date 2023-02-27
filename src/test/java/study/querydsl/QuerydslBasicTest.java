package study.querydsl;

import ch.qos.logback.core.CoreConstants;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(jpqlMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    public void startQuerydsl() {

        Member memberA = queryFactory.selectFrom(member)
                .where(member.username.eq("memberA"))
                .fetchOne();

        assertThat(memberA.getUsername()).isEqualTo("memberA");
    }

    @Test
    public void search() {
        List<Member> memberList = queryFactory.selectFrom(member)
                .where(member.username.startsWith("member").and(member.age.between(11, 13)))
                .fetch();

        assertThat(memberList).isNotEmpty();
        assertThat(memberList.size()).isEqualTo(3);

    }
    @Test
    public void searchParam() {
        List<Member> memberList = queryFactory.selectFrom(member)
                .where(member.username.startsWith("member"),
                        member.age.between(11, 13))
                .fetch();

        assertThat(memberList).isNotEmpty();
        assertThat(memberList.size()).isEqualTo(3);

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
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.max())).isEqualTo(14);
        assertThat(tuple.get(member.age.avg())).isEqualTo(12.5);
        assertThat(tuple.get(member.age.min())).isEqualTo(11);
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
        em.flush();
        em.clear();
        Member memberA1 = queryFactory
                    .selectFrom(member)
                .where(member.username.eq("memberA"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(memberA1.getTeam());
        assertThat(loaded).as("로딩 체크").isFalse();
    }

    @Test
    public void joinfetchUse() throws Exception {
        em.flush();
        em.clear();
        Member memberA1 = queryFactory
                    .selectFrom(member)
                .join(member.team, team)
                .fetchJoin()
                .where(member.username.eq("memberA"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(memberA1.getTeam());
        assertThat(loaded).as("로딩 체크").isTrue();
    }

    @Test
    public void subQueryTest() throws Exception {

        QMember subMember = new QMember("subMember");

        Member maxAgeMember = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions.select(subMember.age.max()).from(subMember)
                )).fetchOne();
        assertThat(maxAgeMember).extracting("age").isEqualTo(14);
    }
    
    @Test
    public void concat() throws Exception {
        List<Tuple> result = queryFactory.select(
                        member.username.concat("_").concat(member.age.stringValue()), member.username, member.age)
                .from(member)
                .fetch();

        for(Tuple s : result) {
            System.out.println("value : "  + s.toString());
        }


    }

    @Test
    public void dtoTestJPQL() throws Exception {
        // JPQL
        List<MemberDto> resultList = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();
        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto :" + memberDto);
        }

        /*queryFactory.select(member.username, member.age)
                .from(member)
                .fetch();*/
    }

    @Test
    public void dtoTestQueryDsl() throws Exception {
        List<MemberDto> resultList = queryFactory.select(
                        Projections.bean(MemberDto.class,
                                member.username,
                                member.age))
                .from(member)
                .fetch();
        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto :" + memberDto);
        }
    }

    @Test
    public void dtoTestQueryDslField() throws Exception {
        List<MemberDto> resultList = queryFactory.select(
                        Projections.fields(MemberDto.class,
                                member.username,
                                member.age))
                .from(member)
                .fetch();
        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto :" + memberDto);
        }
    }

    @Test
    public void dtoTestQueryDslConstruct() throws Exception {
        List<MemberDto> resultList = queryFactory.select(
                        Projections.fields(MemberDto.class,
                                member.username,
                                member.age))
                .from(member)
                .fetch();
        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto :" + memberDto);
        }
    }
    @Test
    public void dtoTestQueryDslQueryProduction() throws Exception {
        List<MemberDto> resultList = queryFactory.select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto :" + memberDto);
        }
    }
    
    @Test
    public void dynamicQuery_booleanBuilder() throws Exception {
        String userName = "MemberB";
        int age = 12;

        List<Member> userList = search1(userName, age);
        assertThat(userList.size()).isEqualTo(1);

    }

    private List<Member> search1(String userName, Integer age) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (StringUtils.isNotEmpty(userName)) {
            booleanBuilder.and(member.username.eq(userName));
        }
        if ( age != null) {
            booleanBuilder.and(member.age.eq(age));
        }

        return queryFactory
                .select(member)
                .from(member)
                .where(booleanBuilder)
                .fetch();
    }


    @Test
    public void dynamicQuery_where() throws Exception {
        String userName = "MemberB";
        int age = 12;

        List<Member> userList = search2(userName, age);
        assertThat(userList.size()).isEqualTo(1);

    }

    private List<Member> search2(String userName, Integer age) {
        return queryFactory.selectFrom(member).where(userNameEq(userName), ageEq(age)).fetch();
    }

    private BooleanExpression ageEq(Integer age) {
        return age != null ? member.age.eq(age) : null;
    }

    private BooleanExpression userNameEq(String userName) {
        return userName != null ? member.username.eq(userName):null;
    }
    
    @Test
    public void sqlFunction() throws Exception {
        List<MemberDto> memberDtoList = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto member1 : memberDtoList) {
            System.out.println("member1 : " + member1.getUsername() + ", age : " + member1.getAge());
        }
    }




}
