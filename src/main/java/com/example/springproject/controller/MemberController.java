package com.example.springproject.controller;

import com.example.springproject.dto.ArticleForm;
import com.example.springproject.dto.MemberForm;
import com.example.springproject.entity.Article;
import com.example.springproject.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.springproject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;
    @GetMapping("/members/signup")
    public String singup(){return "members/signup";}

    @PostMapping("/join")
    public String signupMember(MemberForm form){
        log.info(form.toString());
        Member member = form.toEntity();
        log.info(member.toString());
        Member saved = memberRepository.save(member);
        log.info(saved.toString());
        return "redirect:/members/"+saved.getId();
    }
    @GetMapping("/members")
    public String indexMember(Model model){
        List<Member> memberEntityList = memberRepository.findAll();
        model.addAttribute("memberList",memberEntityList);
        return "/members/mbindex";
    }
    @GetMapping("/members/{id}")
    public String showMember(@PathVariable Long id, Model model){
        Member memberEntity = memberRepository.findById(id).orElse(null);
        model.addAttribute("member",memberEntity);
        return "/members/mbshow";
    }
    @GetMapping("/members/{id}/mbedit")
    public String mbedit(@PathVariable Long id, Model model){
        Member memberEntity = memberRepository.findById(id).orElse(null);
        model.addAttribute("member", memberEntity);
        return "/members/mbedit";
    }

    @PostMapping("/members/update")
    public String mbupdate(MemberForm form){
        log.info(form.toString());
        // dto > 엔티티 변환
        Member memberEntity = form.toEntity();
        // 엔티티 db 저장
        Member mbtarget = memberRepository.findById(memberEntity.getId()).orElse(null);
        if (mbtarget !=null){
            memberRepository.save(memberEntity);
        }
        // 수정결과 페이지로 리다이렉트
        return "redirect:/members/"+mbtarget.getId();
    }
    @GetMapping("/members/{id}/delete")
    public String mbdelete(@PathVariable Long id){
        Member mbtarget = memberRepository.findById(id).orElse(null);
        if(mbtarget != null){
            memberRepository.delete(mbtarget);
        }
        return "redirect:/members";
    }
}
