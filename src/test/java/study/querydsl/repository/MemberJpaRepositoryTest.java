package study.querydsl.repository;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void basicTest() {
        Member member = new Member("MemberA", 11);
        memberJpaRepository.save(member);

        Optional<Member> memberOpt = memberJpaRepository.findById(member.getId());
        Member memberA = memberOpt.get();
        assertThat(member).isEqualTo(memberA);

        List<Member> memberList = memberJpaRepository.findAll();
        assertThat(memberList).containsExactly(memberA);

        List<Member> memberA1 = memberJpaRepository.findByUsername("MemberA");
        assertThat(memberList).containsExactly(memberA);


    }
}