package miri.member;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class familyDAOImpl implements familyDAO{
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public familyVO login(familyVO vo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("family_name").is(vo.getFamily_name()));
		query.addCriteria(Criteria.where("family_password").is(vo.getFamily_password()));
		
		return mongoTemplate.findOne(query, familyVO.class, "family");
	}
}
