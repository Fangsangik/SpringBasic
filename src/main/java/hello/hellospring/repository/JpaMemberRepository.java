package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em; //JPA 구동 원리

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member; //JPA가 자동으로 쿼리 짠다.
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id); //select문
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> rst = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name).getResultList();
        return rst.stream().findAny(); //findByName으로 하나만 찾아온다
    }

    @Override
    public List<Member> findALl() {
        return em.createQuery("select m from Member", Member.class) //이미 Mapping 되어 있다.
                .getResultList();
    }
}
