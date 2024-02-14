package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void welcome_Member(){
        Member member = new Member();
        member.setName("Hello");

        Long saveId = memberService.join(member);


        Member findMember = memberRepository.findById(saveId).get();
        Assertions.assertEquals(member.getName(), findMember.getName());
    }

    @Test
    void duplicate_Member(){
        Member member = new Member();
        member.setName("Hello");

        Member member1 = new Member();
        member1.setName("Hello");

        memberService.join(member);
        IllegalStateException e = Assertions.assertThrows(IllegalStateException.class,
                () -> memberService.join(member1));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원");
    }
}
