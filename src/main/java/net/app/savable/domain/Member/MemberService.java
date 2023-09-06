package net.app.savable.domain.Member;

import net.app.savable.domain.Member.dto.MemberResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    @Autowired
    private MemberRepository memberRepository; // RequiredArgsConstructor 왜 안 먹을까?

    private List<Member> store = new ArrayList<>();

    public MemberService() {
        store.add(new Member("kdy", "e@naber.com", "1234", Role.USER, "010-1234-5245"));
        store.add(new Member("deb", "yjfd@naber.com", "134", Role.USER, "010-7548-7568"));
        store.add(new Member("arh", "f23sdg@naber.com", "54326", Role.USER, "010-4627-1835"));
    }

    public List<Member> getUsers() {
        return this.store;
    }

    public MemberResponseDto findUserInfoById(Long id){
        return memberRepository.findById(id)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("해당 로그인 유저가 없습니다."));
    }

    public MemberResponseDto findUserInfoByEmail(String email){
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
    }
}
