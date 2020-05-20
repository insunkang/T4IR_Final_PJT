package fin.prj.miri.call;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class indexController {
	@RequestMapping("/index.do")
	public String adminMain(){
		System.out.println("어노테이션 기반");
		return "/index";
	}

}
