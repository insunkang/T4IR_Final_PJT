package fin.prj.miri.member;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MemberController {
	@Autowired
	MemberService service;
	
	// 기본 index페이지
	@RequestMapping(value="/index.do", method=RequestMethod.GET)
	public ModelAndView Index() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("fail", false);
		mav.setViewName("index");	
		return mav;
	}
	
	// 1. user 로그인 페이지 보기
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public ModelAndView adminLoginGET() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("fail", false);
		mav.setViewName("login");	
		return mav;
	}
	
	// 1-1 user 로그인 처리 요청
	@RequestMapping(value="/index.do", method=RequestMethod.POST)
	public ModelAndView adminLogin(String member_id, String member_pass, HttpServletRequest request) {
		System.out.println("로그인 하기 위해서 Admin사용자가 입력한 값: "+member_id+", "+member_pass);
		ModelAndView mav = new ModelAndView();
		MemberVO loginUser = new MemberVO(member_id, member_pass);
		MemberVO user = service.login(loginUser);
		System.out.println("로그인 성공 후 조회된 레코드로 만들어진 값: "+user);
		String viewName ="";
		if(user != null) {
			// 로그인 성공시
			HttpSession ses = request.getSession();
			// 2. 세션에 데이터 공유
			ses.setAttribute("user", user);
			// redirect로 해야함
			mav.addObject("fail", false);
			mav.addObject("user", user);
			viewName = "index";
		}else {
			//로그인 실패
			mav.addObject("fail", true);
			viewName = "login";
		}
		mav.setViewName(viewName);
		return mav;
	}
	
	// 2. member user 등록 페이지 보이기
	@RequestMapping(value="/register.do", method=RequestMethod.GET)
	public ModelAndView regView() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("register");
		return mav;
	}
	
	// 2-1 member user 등록 요청 처리하기
	@RequestMapping(value="/register.do", method=RequestMethod.POST)
	public ModelAndView MemberInsert(String member_id, String member_pass, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession(false);
		MemberVO user = new MemberVO(member_id, member_pass);
		session.setAttribute("reg", user);
		int result = service.insert(user);
		System.out.println("insert 행 개수: "+result);
		
		mav.setViewName("login");
		return mav;
	}
	
	// 2-2 아이디 중복체크
	@RequestMapping(value="/idCheck.do", 
			method=RequestMethod.GET,
			produces="application/text; charset=utf-8")
	public @ResponseBody String idCheck(String member_id) {
		boolean state = service.idCheck(member_id);
		String result = "";
		if(state) { //이미 사용자가 입력한 아이디가  db에 저장되어 있다는 의미
			result = "이미 존재하는 아이디 입니다.";
		}else {
			result = "사용가능한 아이디";
		}
		return result;
	}
	
	//안드로이드에서 member user 등록하기
	@RequestMapping(value="/member/insert", method=RequestMethod.POST)
	public @ResponseBody String memberInsert(@RequestBody String json) {
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		String str="";
		try {
			MemberVO vo = mapper.readValue(json, MemberVO.class);
			System.out.println("====="+vo);
			int result = service.memberInsert(vo.getMember_id(), vo.getMember_pass(), vo.getMember_family());
			if(result==1) {
				str = "true";
			}else {
				str = "false";
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	@RequestMapping(value="/member/select", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody MemberVO childSelect(@RequestBody String json) {
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		MemberVO vo;
		try {
			vo = mapper.readValue(json, MemberVO.class);
			System.out.println("====="+vo);
			MemberVO user = service.memberSelect(vo.getMember_id(), vo.getMember_pass());
			
			if(user!=null) {
				return user;
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/state/select", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody MemberVO stateCarSelect(@RequestBody String json) {
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		MemberVO vo;
		String result="";
		MemberVO user;
		try {
			vo = mapper.readValue(json, MemberVO.class);
			System.out.println("====="+vo);
			user = service.stateSelect(vo.getMember_id());
			
			System.out.println("test============"+user);
			if(user!=null) {
				return user;
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// id를 통해서 member_car_state 수정하기
	@RequestMapping(value="/state/update", method=RequestMethod.POST)
	public @ResponseBody String stateUpdate(@RequestBody String json) {
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		String str="";
		try {
			MemberVO vo = mapper.readValue(json, MemberVO.class);
			System.out.println(vo.getMember_id());
			System.out.println(vo.getMember_car_state());
			int result = service.stateUpdate(vo.getMember_id(), vo.getMember_car_state());
			if(result==1) {
				str = "true";
			}else {
				str = "false";
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
}
