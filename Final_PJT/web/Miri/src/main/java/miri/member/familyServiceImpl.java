package miri.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class familyServiceImpl implements familyService{
	@Autowired
	familyDAO dao;
	
	@Override
	public familyVO login(familyVO vo) {
		return dao.login(vo);
	}
}
