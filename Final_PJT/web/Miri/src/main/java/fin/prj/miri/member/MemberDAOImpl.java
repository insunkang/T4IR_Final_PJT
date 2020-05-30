package fin.prj.miri.member;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("famDao")
public class MemberDAOImpl implements MemberDAO{
	@Autowired
	SqlSession sqlSession;
	
	@Override
	public int insert(MemberVO user) {
		return sqlSession.insert("member.insert", user);
	}

	@Override
	public MemberVO login(MemberVO loginUser) {
		return sqlSession.selectOne("member.login", loginUser);
	}

	@Override
	public boolean idCheck(String member_id) {
		boolean result = false;
		MemberVO user=  sqlSession.selectOne("member.idcheck", member_id);
		if(user!=null) {
			result = true;
		}
		return result;
	}

	@Override
	public int memberInsert(String member_id, String member_pass, String member_family) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("member_pass", member_pass);
		map.put("member_family", member_family);
		return sqlSession.insert("member.memberInsert", map);
	}

	@Override
	public MemberVO memberSelect(String member_id, String member_pass) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("member_pass", member_pass);
		return sqlSession.selectOne("member.memberSelect", map);
	}
	
	@Override
	public MemberVO stateSelect(String member_id) {
		return sqlSession.selectOne("member.stateSelect", member_id);
	}

	@Override
	public int stateUpdate(String member_id, String member_car_state) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("member_car_state", member_car_state);
		System.out.println(map);
		int result = sqlSession.update("member.stateUpdate", map);
		return result;
	}
	
}
