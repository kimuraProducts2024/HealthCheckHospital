package com.healthcheck.hospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.healthcheck.hospital.common.CommonFunc;
import com.healthcheck.hospital.common.SelectResult;
import com.healthcheck.hospital.entity.MUser;
import com.healthcheck.hospital.form.AmbulantListForm;
import com.healthcheck.hospital.repository.AmbulantListMapper;

import jakarta.servlet.http.HttpSession;

/**
 * 受診者一覧サービスクラス
 */
@Service
public class AmbulantListService {
	// 表示するデータ件数
	private static final int PAGE_LIMIT = 10;
	
	// 受診者一覧マッパー定義
	@Autowired
	private AmbulantListMapper ambulantListMapper;
	
	// 受診者一覧フォーム
	private AmbulantListForm form;
	
	// ページネーション関連情報
	SelectResult<AmbulantListForm> selectResult;
	
	/**
	 * 受診者一覧画面：初期表示処理
	 * DBから受診者一覧情報を取得し、
	 * 受診者一覧画面のHTMLファイル名を返す
	 * @param form 受診者一覧フォーム
	 * @param model モデル
	 * @return 遷移先のHTMLファイル名
	 */
	public String sendAmbulantListPage(AmbulantListForm form, Model model) {
		// 受診者一覧画面情報を設定
		this.form = form;
		
		// ページリンク中間セクションの開始位置を設定
		form.setPageFrom(0);
		
		// 受診者一覧に表示するデータ件数を設定
		form.setCount(PAGE_LIMIT);
		
		// DBから受診者一覧を取得する
		List<MUser> mUserList = ambulantListMapper.selectAmbulantListPart(form.getPageFrom(), form.getCount());
		
		// 受診者一覧画面表示情報リスト
		List<AmbulantListForm> ambulantList = null;
		
		// 受診者一覧が取得できた場合
		if (mUserList != null) {
			// 受診者一覧画面表示情報を取得
			ambulantList = CommonFunc.getListForm(mUserList);
			
			// 受診者一覧の全データ件数を取得
			int recordCount = ambulantListMapper.selectCount();
			
			// ページネーション関連情報の初期化
			selectResult = new SelectResult<>(recordCount, PAGE_LIMIT);
			
			// ページリンク中間セクションの開始位置、終了位置を設定
			selectResult.moveTo(1);
			
			// 表示する受診者一覧情報を設定
			selectResult.setEntities(ambulantList);
			
			// モデルに受診者一覧を追加する
			model.addAttribute("selectResult", selectResult);
		}
		
		// 遷移先のHTMLファイル名を返す
		return "AmbulantList";
	}
	
	/**
	 * 受診者一覧画面：ページネーションリンククリック
	 * ページ位置を元に、DBから受診者一覧情報を取得し、
	 * 受診者一覧画面のHTMLファイル名を返す
	 * @param pageNo 表示ページ位置
	 * @param model モデル
	 * @return 遷移先のHTMLファイル名
	 */
	public String sendNextAmbulantList(int pageNo, Model model) {
		// ページ位置が想定外の値の場合
		if(pageNo < 1 || pageNo > selectResult.getTotalPageCount()) {
			// 処理をせず、受診者一覧画面に戻る
			return "AmbulantList";
		}
		
		// ページリンク中間セクションの開始位置、終了位置を設定
		selectResult.moveTo(pageNo);
		
		// ページリンク中間セクションの開始位置を設定
		form.setPageFrom((pageNo - 1) * PAGE_LIMIT);
		
		// DBから受診者一覧を取得する
		List<MUser> mUserList = ambulantListMapper.selectAmbulantListPart(form.getPageFrom(), form.getCount());
		
		// 受診者一覧画面表示情報リスト
		List<AmbulantListForm> ambulantList = null;
		
		// 受診者一覧が取得できた場合
		if (mUserList != null) {
			// DBデータから画面表示用オブジェクトを取得
			ambulantList = CommonFunc.getListForm(mUserList);
			
			// 画面表示用オブジェクトを設定
			selectResult.setEntities(ambulantList);
		}
		
		// モデルに受診者一覧画面情報を追加する
		model.addAttribute("selectResult", selectResult);
		
		// 遷移先のHTMLファイル名を返す
		return "AmbulantList";
	}
	
	/**
	 * 受診者一覧画面：DBから全受診者情報を取得する
	 * @return 全受診者情報
	 */
	public List<MUser> selectAll() {
		// DBから全受診者情報を取得する
		return ambulantListMapper.selectAmbulantList(0);
	}
	
	/**
	 * 受診者一覧画面：受診者一覧のIDリンクボタンをクリック
	 * 個人結果画面へ遷移する
	 * @param id クリックしたIDリンクの値
	 * @param model モデル
	 * @param session セッション
	 * @return 遷移先のHTMLファイル名
	 */
	public String sendTestResults(Integer id, Model model, HttpSession session) {
		// IDを元にDBから受診者情報を取得する
		List<MUser> mUser = ambulantListMapper.selectAmbulantList(id);
		
		// 受診者情報から、画面表示用情報を取得
		List<AmbulantListForm> ambulantList = CommonFunc.getListForm(mUser);
		
		// モデルに個人結果画面情報を追加する
		model.addAttribute("ambulantItem", ambulantList.toArray()[0]);
		
		// セッションに個人結果画面表示情報を追加する
		session.setAttribute("AmbulantItem", ambulantList.toArray()[0]);
		
		// 遷移先のHTMLファイル名を返す
		return "TestResults";
	}
}
