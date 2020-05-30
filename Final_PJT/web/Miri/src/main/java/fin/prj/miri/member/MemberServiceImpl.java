package fin.prj.miri.member;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	@Qualifier("famDao")
	MemberDAO dao;

	@Override
	public int insert(MemberVO user) {
		return dao.insert(user);
	}

	@Override
	public MemberVO login(MemberVO loginUser) {
		return dao.login(loginUser);
	}

	@Override
	public boolean idCheck(String id) {
		return dao.idCheck(id);
	}


	@Override
	public int memberInsert(String member_id, String member_pass, String member_family) {
		return dao.memberInsert(member_id, member_pass, member_family);
	}

	@Override
	public MemberVO memberSelect(String member_id, String member_pass) {
		return dao.memberSelect(member_id, member_pass);
	}

	@Override
	public MemberVO stateSelect(String member_id) {
		return dao.stateSelect(member_id);
	}
	
	@Override
	public int stateUpdate(String member_id, String member_car_state) {
		return dao.stateUpdate(member_id, member_car_state);
	}
	
}
