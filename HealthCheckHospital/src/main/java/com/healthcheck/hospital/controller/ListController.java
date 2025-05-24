package com.healthcheck.hospital.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthcheck.hospital.common.CommonFunc;
import com.healthcheck.hospital.service.AmbulantListService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 受診者一覧コントローラ
 */
@Controller
public class ListController {
	// 受診者一覧サービス
	@Autowired
	AmbulantListService ambulantListService;
	
	/**
	 * 受診者一覧画面：受診者一覧のIDリンククリックイベント
	 * 画面の受診者一覧表にあるID列のリンクをクリックした際、
	 * IDを元にして、受診者情報を取得し、個人結果画面に遷移する
	 * @param id ユーザID
	 * @param model モデル
	 * @param session セッション
	 * @return 遷移先のHTMLファイル名
	 */
	@GetMapping("/testresults/{id}")
	public String showAmbulantDetail(@PathVariable("id") Integer id, Model model, HttpSession session) {
		// IDを元に受診者情報を取得し、個人結果画面に遷移する
		return ambulantListService.sendTestResults(id, model, session);
	}
	
	/**
	 * 受診者一覧画面：ページングで、リンクをクリックした際、
	 * 次に表示する一覧情報を取得し、表示する
	 * @param pageNo
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/ambulantList/move")
	public String showNextAmbulantList(@RequestParam("p") int pageNo, Model model) {
		// 次に表示する受診者一覧情報を取得し、表示する
		return ambulantListService.sendNextAmbulantList(pageNo, model);
	}
	
	/**
	 * 受診者一覧情報をCSVファイル出力する
	 * @param response HttpServletResponse
	 * @param model モデル
	 * @param session セッション
	 * @return 遷移先のHTMLファイル名
	 * @throws IOException
	 */
	@PostMapping("/listdownload")
	public String csvListDownLoad(HttpServletResponse response, 
			Model model, HttpSession session) throws IOException {
		// 受診者一覧情報をcsvファイルでダウンロードする
		CommonFunc.csvDownload(true, response, ambulantListService, model, session);
		
		// 遷移先のHTMLファイル名を返す
		return "AmbulantList";
	}
	
	/**
	 * 個人結果画面：受診者情報をCSVファイル出力する
	 * @param response HttpServletResponse
	 * @param model モデル
	 * @param session セッション
	 * @return 遷移先のHTMLファイル名
	 * @throws IOException
	 */
	@PostMapping("/itemdownload")
	public String csvItemDownLoad(HttpServletResponse response, 
			Model model, HttpSession session) throws IOException {
		// 受診者情報をcsvファイルでダウンロードする
		CommonFunc.csvDownload(false, response, ambulantListService, model, session);
		
		// 遷移先のHTMLファイル名を返す
		return "AmbulantList";
	}
	
	/**
	 * 受診者一覧画面：メインメニュー画面に戻る
	 * @return 遷移先のHTMLファイル名
	 */
	@PostMapping("/returnmenu")
	public String returnMenu(HttpSession session) {
		// セッションをクリアする
		session.removeAttribute("AmbulantItem");
		
		// 遷移先のHTMLファイル名を返す
		return "MainMenu";
	}
}
