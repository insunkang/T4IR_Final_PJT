package fin.prj.miri.member;

public class MemberVO {
	String member_id;
	String member_pass;
	String member_ceim_state;
	String member_car_state;
	String member_family;
	
	@Override
	public String toString() {
		return "MemberVO [member_id=" + member_id + ", member_pass=" + member_pass + ", member_ceim_state="
				+ member_ceim_state + ", member_car_state=" + member_car_state + ", member_family=" + member_family
				+ "]";
	}

	public MemberVO() {
		
	}
	
	public MemberVO(String member_id) {
		super();
		this.member_id = member_id;
	}
	public MemberVO(String member_id, String member_pass) {
		super();
		this.member_id = member_id;
		this.member_pass = member_pass;
	}
	
	public MemberVO(String member_id, String member_pass, String member_family) {
		super();
		this.member_id = member_id;
		this.member_pass = member_pass;
		this.member_family = member_family;
	}

	public MemberVO(String member_id, String member_pass, String member_ceim_state, String member_car_state) {
		super();
		this.member_id = member_id;
		this.member_pass = member_pass;
		this.member_ceim_state = member_ceim_state;
		this.member_car_state = member_car_state;
	}

	public MemberVO(String member_id, String member_pass, String member_ceim_state, String member_car_state,
			String member_family) {
		super();
		this.member_id = member_id;
		this.member_pass = member_pass;
		this.member_ceim_state = member_ceim_state;
		this.member_car_state = member_car_state;
		this.member_family = member_family;
	}


	public String getMember_family() {
		return member_family;
	}
	public void setMember_family(String member_family) {
		this.member_family = member_family;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_pass() {
		return member_pass;
	}
	public void setMember_pass(String member_pass) {
		this.member_pass = member_pass;
	}
	public String getMember_ceim_state() {
		return member_ceim_state;
	}
	public void setMember_ceim_state(String member_ceim_state) {
		this.member_ceim_state = member_ceim_state;
	}
	public String getMember_car_state() {
		return member_car_state;
	}
	public void setMember_car_state(String member_car_state) {
		this.member_car_state = member_car_state;
	}
	
	
	
	
	
}
