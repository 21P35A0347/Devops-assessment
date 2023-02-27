package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    private EntityManager em;

    @Test
    public void testEntity() {
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

        em.flush();
        em.clear();

        List<Member> memberList = em.createQuery("Select m from Member m", Member.class)
                .getResultList();

        for (Member member : memberList) {
            System.out.println("Member : " + member);
            System.out.println("=> Team : " + member.getTeam());
        }
    }
}