package miri.member;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class familyController {
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public String  login(){
		return "/login";

	}
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public ModelAndView  login(familyVO vo, HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("/index");
		
		return mav;

	}
	@RequestMapping("/forgotPassword.do")
	public String forgotPassword(){
		return "/forgot-password";
	}
	@RequestMapping("/register.do")
	public String register(){
		return "/register";
	}
}
