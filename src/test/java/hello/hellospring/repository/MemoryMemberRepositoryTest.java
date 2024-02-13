package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @Test
    void save(){
        //given
        Member member = new Member();
        member.setName("터진입");
        //when
        repository.save(member);
        //then
        Member rst = repository.findById(member.getId()).get();
        assertThat(rst).isEqualTo(member);
    }

    @Test
    void findByName(){
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setName("황상익");
        member2.setName("황상익1");
        repository.save(member1);
        repository.save(member2);

        //when
        Member rst = repository.findByName("황상익").get();

        //then
        assertThat(rst).isEqualTo(member1);
    }

    @Test
    void findAll(){
        //given
        Member member1 = new Member();
        member1.setName("황상익");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("박주연");
        repository.save(member2);

        //when
        List<Member> rst = repository.findALl();

        //then
        assertThat(rst.size()).isEqualTo(2);
    }

}