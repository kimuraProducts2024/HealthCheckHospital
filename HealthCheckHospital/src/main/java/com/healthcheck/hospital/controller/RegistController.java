package com.healthcheck.hospital.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.healthcheck.hospital.common.CommonFunc;
import com.healthcheck.hospital.form.UserRegistForm;
import com.healthcheck.hospital.repository.UserRegistMapper;
import com.healthcheck.hospital.service.UserRegistService;

import jakarta.servlet.http.HttpSession;

/**
 * 受診者登録コントローラ
 * 受診者登録画面、確認画面、完了画面の各処理を行う
 */
@Controller
public class RegistController {
	// 受診者登録マッパー定義
	@Autowired
	private UserRegistMapper userRegistMapper;
	
	// 受診者登録サービス
	@Autowired
	UserRegistService userRegistService;
	
	/**
	 * 受診者登録画面：戻るボタンクリック
	 * メインメニュー画面に遷移する
	 * @return 遷移先のHTMLファイル名
	 */
	@PostMapping(value = "/input", params = "returnBtn")
	public String returnMain() {
		// 遷移先のHTMLファイル名を返す
		return "MainMenu";
	}
	
	/**
	 * 受診者登録画面：確認ボタンクリック
	 * 入力チェックを行い、次画面に遷移する
	 * @param userRegistForm 受診者登録情報
	 * @param model モデル
	 * @return 遷移先のHTMLファイル名
	 */
	@PostMapping(value = "/input", params = "confirmBtn")
	public String showConfirm(UserRegistForm userRegistForm, Model model) {
		// 入力値チェックを行い、次画面への遷移先を返す
		return CommonFunc.getNextConfPageName(userRegistForm, model);
	}
	
	/**
	 * 確認画面：戻るボタンクリック
	 * @param userRegistForm 受診者登録情報
	 * @param model モデル
	 * @return 遷移先のHTMLファイル名
	 */
	@PostMapping(value = "/confirm", params = "returnBtn")
	public String returnUserRegist(UserRegistForm userRegistForm, Model model) {
		// 受診者登録情報をもとに、入力画面へ戻る
		return "UserRegist";
	}
	
	/**
	 * 確認画面：確定ボタンクリック
	 * 入力値から、M_Userテーブル、M_Test_Itemテーブルの情報
	 * を更新し、完了の場合、完了画面へ遷移する
	 * @param userRegistMapper 受診者登録マッパー
	 * @param userRegistForm 受診者登録情報
	 * @return 遷移先のHTMLファイル名
	 * @throws NoSuchAlgorithmException 使用不可、Exception 例外
	 */
	@PostMapping(value = "/confirm", params = "completeBtn")
	public String showComplete(UserRegistForm userRegistForm, Model model) 
				throws NoSuchAlgorithmException, Exception {
		try {
			// 入力値をもとに、テーブルへのInsert処理を行う
			return userRegistService.insertData(userRegistMapper, userRegistForm);
		} catch (Exception ex) {
			// エラーを出力
			System.out.println("テーブルInsert文実行時エラー：" + ex.getMessage());
			
			// エラーメッセージを設定
			userRegistForm.setErrorLabel("データ登録処理が失敗しました");
			
			// modelに登録
			model.addAttribute("userRegistForm", userRegistForm);
			
			// 遷移先のHTMLファイル名を返す
			return "UserRegist";
		}
	}
	
	/**
	 * 登録完了画面：メインメニュー画面に遷移する
	 * @return 遷移先のHTMLファイル名
	 */
	@GetMapping("/mainmenu")
	public String showMainMenu(HttpSession session) {
		// 遷移先のHTMLファイル名を返す
		return "MainMenu";
	}
}
