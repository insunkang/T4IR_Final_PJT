package miri.member;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="family")
public class familyVO {
	@Id
	private String Family_name;
	private String Family_password;
	private String Family_ce_state;
	private String Family_car_state;
	public familyVO() {
	}
	

	public String getFamily_password() {
		return Family_password;
	}


	public void setFamily_password(String family_password) {
		Family_password = family_password;
	}


	public String getFamily_name() {
		return Family_name;
	}

	public void setFamily_name(String family_name) {
		Family_name = family_name;
	}

	public String getFamily_ce_state() {
		return Family_ce_state;
	}

	public void setFamily_ce_state(String family_ce_state) {
		Family_ce_state = family_ce_state;
	}

	public String getFamily_car_state() {
		return Family_car_state;
	}

	public void setFamily_car_state(String family_car_state) {
		Family_car_state = family_car_state;
	}

}
