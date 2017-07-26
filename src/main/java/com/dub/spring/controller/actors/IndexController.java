package com.dub.spring.controller.actors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
	
	@RequestMapping({"/", "backHome"})
    public String greeting(Model model) {
       
        return "index";
    }

}
