package com.healthcheck.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

/**
 * メインメニューコントローラ
 * 初期表示、各入力画面への遷移、ログアウト時の処理を行う
 */
@Controller
public class MainMenuController {
	
	
	/**
	 * 登録完了画面：メインメニュー画面に遷移する
	 * @return 遷移先のHTMLファイル名
	 */
	@GetMapping("/mainmenu")
	public String showMainMenu(HttpSession session) {
		// 遷移先のHTMLファイル名を返す
		return "MainMenu";
	}
	
	/**
	 * メインメニュー画面：受診者一覧リンクをクリックしたとき、
	 * 受診者一覧画面に遷移する
	 * @return 遷移先のHTMLファイル名
	 */
	@GetMapping("/ambulantList")
	public String showAmbulantList() {
		return "AmbulantList";
	}
	
	/**
	 * メインメニュー画面：受診者登録リンクをクリックしたとき、
	 * 受診者登録画面に遷移する
	 * @return 遷移先のHTMLファイル名
	 */
	@GetMapping("/userRegist")
	public String showUserRegist() {
		return "UserRegist";
	}
	
	/**
	 * メインメニュー画面：ログアウトボタン押下時処理
	 * セッションを削除し、ログアウト
	 * @param session セッション
	 * @return ログイン画面のHTMLファイル名
	 */
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		// セッションをクリアする
		session = null;
		
		// ログイン画面に遷移する
		return "redirect:/";
	}
}
