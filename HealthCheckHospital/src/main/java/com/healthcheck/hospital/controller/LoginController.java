package com.healthcheck.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.healthcheck.hospital.service.MUserService;

import jakarta.servlet.http.HttpSession;

/**
 * ログインコントローラ
 * 初期表示、ログイン時の処理を行う
 */
@Controller
public class LoginController {
	// ユーザサービス定義
	@Autowired
	MUserService mUserService;
	
	/*
	 * ログイン画面表示
	 * @return ログイン画面のファイル名
	 */
	@GetMapping("/")
	public String showLogin() {
		return "Login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String userId, @RequestParam String password,
							RedirectAttributes redirectAttributes, HttpSession session) throws Exception {
		// 入力値チェック後、次ページへ遷移する
		return mUserService.sendNextPage(userId, password, redirectAttributes, session);
	}
}
