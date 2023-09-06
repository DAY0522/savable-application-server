package net.app.savable.web;

import net.app.savable.config.auth.LoginMember;
import net.app.savable.config.auth.dto.SessionMember;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/auth/sign-up")
    public String index(Model model, @LoginMember SessionMember member){

        System.out.printf("member: %s\n", member);

        if (member != null) {
            model.addAttribute("MemberName", member.getName());
        }
        return "index";
    }
}
