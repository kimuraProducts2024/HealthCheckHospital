package com.healthcheck.hospital.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.healthcheck.hospital.entity.MUser;
import com.healthcheck.hospital.form.AmbulantListForm;
import com.healthcheck.hospital.form.UserRegistForm;
import com.healthcheck.hospital.service.AmbulantListService;

import jakarta.servlet.http.HttpSession;

/**
 * メインメニューコントローラ
 * 初期表示、各入力画面への遷移、ログアウト時の処理を行う
 */
@Controller
public class MainMenuController {
	// 受診者一覧情報
	List<MUser> ambulantList;
	
	// 受診者一覧サービス定義
	@Autowired
	AmbulantListService ambulantListService;
	
	/**
	 * メインメニュー画面：受診者一覧リンクをクリックしたとき、
	 * 受診者一覧画面に遷移する
	 * @param model モデル
	 * @return 遷移先のHTMLファイル名
	 */
	@GetMapping("/ambulantList")
	public String showAmbulantList(@ModelAttribute AmbulantListForm form, Model model) {
		// DBから受診者一覧情報を取得し、受診者一覧画面へ遷移する
		return ambulantListService.sendAmbulantListPage(form, model);
	}
	
	/**
	 * メインメニュー画面：受診者登録リンクをクリックしたとき、
	 * 受診者登録画面に遷移する
	 * @return 遷移先のHTMLファイル名
	 */
	@GetMapping("/userRegist")
	public String showUserRegist(UserRegistForm userRegistForm, Model model) {
		
		userRegistForm.setAge(null);
		
		// 受診者登録情報を登録
		model.addAttribute("userRegistForm", userRegistForm);
		
		
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
