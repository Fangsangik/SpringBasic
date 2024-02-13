package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository repository;

    @Test
    void member_registration(){
        //given
        Member member = new Member();
        member.setName("황");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = repository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }

    @Test
    void duplicate_member(){
        //given
        Member member = new Member();
        member.setName("황");
        repository.save(member);

        Member member1 = new Member();
        member1.setName("황");
        repository.save(member);

        //when
        memberService.join(member);
        try {
            memberService.join(member1);
            fail();
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원 입니다");
        }
    }
}