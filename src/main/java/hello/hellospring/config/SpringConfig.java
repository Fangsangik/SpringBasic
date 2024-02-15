package hello.hellospring.config;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    //spring Jpa가 interface에 대한 구현체를 찾도록 만든다
    private final MemberRepository memberRepository;

    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository; //jpa 구현체 만들어 놓은거 등록
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository); //memberService에 의존관계 setting
    }

    /*
    interface를 보고 springBean 자동 등록
    -> proxt라는 기술로 객체를 생성 -> springBean 생성 -> inject
     */
}
