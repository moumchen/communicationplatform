package com.jixiata.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api("页面跳转API")
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/toIndex")
    public String toIndex() {
        return "index";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "/login/login";
    }

    @RequestMapping("/toRegister")
    public String toRegister() {
        return "/login/register";
    }

    @RequestMapping("/toJoinClass")
    public String toJoinClass() {
        return "/login/joinClass";
    }

    @RequestMapping("/toCreateClass")
    public String toCreateClass() {
        return "/login/createClass";
    }

    @RequestMapping("/toCreateSuccess")
    public String toCreateSuccess() {
        return "/login/createSuccess";
    }

    @RequestMapping("/toTaskHome")
    public String toTaskHome() {
        return "/task/home";
    }

    @RequestMapping("/toAddTask")
    public String toAddTask() {
        return "/task/add";
    }
    @RequestMapping("/toTaskAddSuccess")
    public String toTaskAddSuccess() {
        return "/task/addSuccess";
    }

    @RequestMapping("/toTaskDetail")
    public String toTaskDetail() {
        return "/task/detail";
    }

    @RequestMapping("/toTaskDeleteSuccess")
    public String toTaskDeleteSuccess() {
        return "/task/deleteSuccess";
    }

    @RequestMapping("/toTaskResultDetail")
    public String toTaskResultDetail() {
        return "/task/resultDetail";
    }
    @RequestMapping("/toExamHome")
    public String toExamHome() {
        return "/exam/home";
    }
    @RequestMapping("/toExamAnalysis")
    public String toExamAnalysis() {
        return "/exam/examAnalysis";
    }

    @RequestMapping("/toExamDetail")
    public String toExamDetail() {
        return "/exam/detail";
    }
    @RequestMapping("/toAddExam")
    public String toAddExam() {
        return "/exam/add";
    }
    @RequestMapping("/toApplicationHome")
    public String toApplicationHome() {
        return "/application/home";
    }

    @RequestMapping("/toAddApplication")
    public String toAddApplication() {
        return "/application/add";
    }
    @RequestMapping("/toMineHome")
    public String toMineHome() {
        return "/mine/home";
    }
    @RequestMapping("/toMineInfo")
    public String toMineInfo() {
        return "/mine/mineInfo";
    }
    @RequestMapping("/toClassAdmin")
    public String toClassAdmin() {
        return "/mine/classAdmin";
    }
    @RequestMapping("/toAboutUs")
    public String toAboutUs() {
        return "/mine/aboutUs";
    }

    @RequestMapping("/toChatHome")
    public String toChatHome() {
        return "/chat/home";
    }
    @RequestMapping("/toChatRoom")
    public String toChatRoom() {
        return "/chat/chatRoom";
    }

    @RequestMapping("/toUserAdmin")
    public String toAdminHome() {
        return "admin/userAdmin";
    }

    @RequestMapping("/toAnnouncementAdmin")
    public String toAnnouncementAdmin() {
        return "admin/announcementAdmin";
    }

    @RequestMapping("/toAdminLogin")
    public String toAdminLogin() {
        return "/admin/login";
    }

    @RequestMapping("/toAddAnnounce")
    public String toAddAnnounce() {
        return "/admin/addAnnounce";
    }
}