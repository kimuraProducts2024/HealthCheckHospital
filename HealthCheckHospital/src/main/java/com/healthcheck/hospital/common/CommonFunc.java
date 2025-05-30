package com.healthcheck.hospital.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.ui.Model;

import com.healthcheck.hospital.entity.MUser;
import com.healthcheck.hospital.form.AmbulantListForm;
import com.healthcheck.hospital.form.UserRegistForm;
import com.healthcheck.hospital.service.AmbulantListService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 共通機能クラス
 * 画面初期化、画面遷移、CSV出力等の処理を行う
 */
public class CommonFunc {	
	/**
	 * 受診者一覧・個人結果画面：CSVダウンロード処理
	 * 受診者一覧情報のデータを
	 * CSV形式で、所定のフォルダにダウンロードする
	 * @param listFlg 一覧の場合：true、個人結果の場合：false
	 * @param response HttpServletResponse
	 * @param ambulantListService 受診者一覧サービス
	 * @param model モデル
	 * @param session セッション
	 * @throws IOException IO例外
	 */
	public static void csvDownload(Boolean listFlg, HttpServletResponse response, 
					AmbulantListService ambulantListService, Model model, HttpSession session) 
						throws IOException {
		// セッションから個人結果情報を取得する
		AmbulantListForm ambulantItem = (AmbulantListForm)session.getAttribute("AmbulantItem");
		
		// 個人結果の場合、ファイル名にIDが付く
		String FileName = listFlg ? "medical_info.csv" : "medical_info_" + ambulantItem.userId + ".csv";
		
		// Fileオブジェクト
		File file = new File(FileName);
		
		// FileWriterオブジェクト
		FileWriter fileWriter = new FileWriter(file);
		
		// PrintWriterオブジェクト
		PrintWriter pw = new PrintWriter(new BufferedWriter(fileWriter));
		
		// Fileにヘッダ情報を書き込む
		pw.println("ユーザーID,氏名,身長,体重,左視力,右視力,聴力,最大血圧,最小血圧");
		
		// 受診者一覧画面情報
		List<AmbulantListForm> ambulantListForm = null;
		
		// 受診者一覧の場合
		if (listFlg) {
			// DBから受診者一覧を取得
			List<MUser> ambulantList = ambulantListService.selectAll();
			
			// 受診者一覧から、受診者一覧画面情報を取得
			ambulantListForm = getListForm(ambulantList);
		}
		
		// 個人結果の場合
		else {
			// 受診者一覧画面情報リスト
			ambulantListForm = new ArrayList<AmbulantListForm>();
			
			// 受診者一覧画面情報リストに個人結果を追加
			ambulantListForm.add(ambulantItem);
		}
		
		// 受診者一覧画面情報イテレータ
		Iterator<AmbulantListForm> it = ambulantListForm.iterator();
		
		// データが存在する場合
		while (it.hasNext()) {
			// 受診者情報
			AmbulantListForm tempAmbulantItem = (AmbulantListForm)it.next();
			String str = tempAmbulantItem.getUserId() + ",";					// ユーザID
			str += tempAmbulantItem.getUserName() + ",";						// 氏名
			str += tempAmbulantItem.getHeight() + ",";							// 身長
			str += tempAmbulantItem.getWeight() + ",";							// 体重
			str += tempAmbulantItem.getVisionLeft() + ",";						// 左視力
			str += tempAmbulantItem.getVisionRight() + ",";						// 右視力
			str += tempAmbulantItem.getHearing() + ",";							// 聴力
			str += tempAmbulantItem.getSystolicBloodPressure() + ",";			// 最高血圧
			str += tempAmbulantItem.getDiastolicBloodPressure() + ",";			// 最低血圧
			
			// Fileに受診者情報を追記する
			pw.println(str);
		}
		
		// PrintWriterオブジェクトを閉じる
		pw.close();
		
		// OutputStreamオブジェクト
		OutputStream os = response.getOutputStream();

		// Fileパスからバイト情報を取得する
		byte[] fb1 = Files.readAllBytes(file.toPath());
        response.setContentType("application/octet-stream");					// HTTP MIMEタイプ
        
        // Header Value
        String headerValue = listFlg ? 
        		"attachment; filename=medical_info.csv" : 
        		"attachment; filename=medical_info_" + ambulantItem.getUserId() + ".csv";
        response.setHeader("Content-Disposition", headerValue);					// CSVヘッダ情報
        response.setContentLength(fb1.length);									// CSVボディの長さ
        
        // OutputStreamからFileに書き込み
        os.write(fb1);
        
        // OutputStreamに書き込み
        os.flush();
        
        // プロジェクト内のtempファイルを削除
        file.delete();
	}
	
	/**
	 * DBから取得した受診者一覧情報を、
	 * 受診者一覧画面の各情報オブジェクトに変換する
	 * @param mUserList DBから取得した受診者一覧情報
	 * @return 受診者一覧画面の各情報リストオブジェクト
	 */
	public static List<AmbulantListForm> getListForm(List<MUser> mUserList) {
		// 戻り値
		List<AmbulantListForm> ambulantList = new ArrayList<AmbulantListForm>();
		
		// DBから取得した受診者一覧情報の分繰り返し
		for (MUser mUser : mUserList) {
			// 戻り値に受診者情報を追加
			ambulantList.add(getItemForm(mUser));
		}
		
		// 戻り値を返す
		return ambulantList;
	}
	
	/**
	 * DBから取得した受診者情報を、
	 * 受診者一覧画面の各情報オブジェクトに変換する
	 * @param mUser DBから取得した受診者情報
	 * @return 受診者一覧画面の各情報オブジェクト
	 */
	public static AmbulantListForm getItemForm(MUser mUser) {
		// 戻り値に追加する要素
		AmbulantListForm ambulantListForm = new AmbulantListForm();
		
		// 各値を設定
		ambulantListForm.userId = String.valueOf(mUser.getUserId());								// ユーザID
		ambulantListForm.userName = mUser.getLastName() + " " + mUser.getFirstName();				// 氏名
		
		// 検査項目情報がnullでない場合
		if (mUser.getMTestItem() != null) {
			ambulantListForm.height = String.valueOf(mUser.getMTestItem().getHeight());				// 身長
			ambulantListForm.weight = String.valueOf(mUser.getMTestItem().getWeight());				// 体重
			ambulantListForm.visionLeft = String.valueOf(mUser.getMTestItem().getVisionLeft());		// 左視力
			ambulantListForm.visionRight = String.valueOf(mUser.getMTestItem().getVisionRight());	// 右視力
			
			// 聴力
			switch (mUser.getMTestItem().getHearing()) {
				// 1の場合
				case 1: 
					ambulantListForm.hearing = "正常";
					break;
				
				// 2の場合
				case 2:
					ambulantListForm.hearing = "中等度難聴";
					break;
				
				// 3の場合
				case 3:
					ambulantListForm.hearing = "高度難聴";
					break;
				
				// 1～3以外の場合
				default:
					ambulantListForm.hearing = "";
					break;
			}
			ambulantListForm.systolicBloodPressure
				= String.valueOf(mUser.getMTestItem().getSystolicBloodPressure());					// 最大血圧
			ambulantListForm.diastolicBloodPressure
				= String.valueOf(mUser.getMTestItem().getDiastolicBloodPressure());					// 最小血圧
		}
		
		// 遷移先のHTMLファイル名を返す
		return ambulantListForm;
	}
	
	/**
	 * 受診者登録画面：確認ボタンクリック時の処理
	 * 入力チェックを行い、
	 * エラーの場合は、エラーメッセージを設定し、
	 * 入力画面の遷移先を返す
	 * エラーがない場合は、確認画面の遷移先を返す
	 * @param userRegistForm  入力画面の各項目情報
	 * @param model モデル
	 * @return 遷移先のHTMLファイル名
	 */
	public static String getNextConfPageName(UserRegistForm userRegistForm, Model model) {
		// 入力チェックがエラーの場合
		if (!inputCheck(userRegistForm, model)) {
			// 入力画面のHTMLファイル名を返す
			return "UserRegist";
		}
		
		// 表示項目の設定
		setTelPasswordItems(userRegistForm);
		
		// モデルに登録
		model.addAttribute("userRegistForm", userRegistForm);
		
		// 確認画面のHTMLファイル名を返す
		return "Confirm";
	}
	
	/**
	 * 入力チェックを行う
	 * エラーの場合は、エラーメッセージを設定し、falseを返す
	 * エラーがない場合は、trueを返す
	 * @param userRegistForm 入力画面の各項目情報
	 * @param session セッション
	 * @param model モデル
	 * @return
	 */
	private static Boolean inputCheck(UserRegistForm userRegistForm, Model model) {
		// 未入力・未選択チェック
		// 姓
		if (userRegistForm.getLastName().isBlank()) {
			// エラーメッセージを設定し、モデルに登録
			setErrorLabel("姓", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// 名
		if (userRegistForm.getFirstName().isBlank()) {
			// エラーメッセージを設定し、モデルに登録
			setErrorLabel("名", userRegistForm, model);
			
			// falseを返す
			return false;
		}

		// 年齢
		if (userRegistForm.getAge() == null) {
			// エラーメッセージを設定し、モデルに登録
			setErrorLabel("年齢", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// 性別
		if (userRegistForm.getSex() == 0) {
			// エラーメッセージを設定し、モデルに登録
			setErrorLabel("性別", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// 電話番号1ブロック目
		if (userRegistForm.getTelFirstBlock().isBlank()) {
			// エラーメッセージを設定し、モデルに登録
			setErrorLabel("電話番号の1ブロック目", userRegistForm, model);
			
			// falseを返す
			return false;
		}
 		
		// 電話番号2ブロック目
		if (userRegistForm.getTelSecondBlock().isBlank()) {
			// エラーメッセージを設定し、モデルに登録
			setErrorLabel("電話番号の2ブロック目", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// 電話番号3ブロック目
		if (userRegistForm.getTelThirdBlock().isBlank()) {
			// エラーメッセージを設定し、モデルに登録
			setErrorLabel("電話番号の3ブロック目", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// パスワード
		if (userRegistForm.getPassword().isBlank()) {
			// エラーメッセージを設定し、モデルに登録
			setErrorLabel("パスワード", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// 桁数チェック
		// 年齢
		if (userRegistForm.getAge() >= 1000) {
			// エラーメッセージを設定し、モデルに登録
			setDigitErrLabel(true, "年齢", "3", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// 電話番号1ブロック目
		if (Integer.valueOf(userRegistForm.getTelFirstBlock()) >= 1000) {
			// エラーメッセージを設定し、モデルに登録
			setDigitErrLabel(true, "電話番号の1ブロック目", "3", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// 電話番号2ブロック目
		if (Integer.valueOf(userRegistForm.getTelSecondBlock()) >= 10000) {
			// エラーメッセージを設定し、モデルに登録
			setDigitErrLabel(true, "電話番号の2ブロック目", "4", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// 電話番号3ブロック目
		if (Integer.valueOf(userRegistForm.getTelThirdBlock()) >= 10000) {
			// エラーメッセージを設定し、モデルに登録
			setDigitErrLabel(true, "電話番号の3ブロック目", "4", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// パスワード最小文字列
		if (userRegistForm.getPassword().length() <= 8) {
			// エラーメッセージを設定し、モデルに登録
			setDigitErrLabel(false, "パスワード", "8", userRegistForm, model);
			
			// falseを返す
			return false;
		}
		
		// trueを返す
		return true;
	}
	
	/**
	 * userRegistFormのエラーラベルに、
	 * 引数のエラーメッセージを設定し、モデルに登録する
	 * @param strMessage エラーメッセージ文字列
	 * @param userRegistForm 入力画面の各項目情報
	 * @param model モデル
	 */
	private static void setErrorLabel(String strMessage, UserRegistForm userRegistForm, Model model) {
		// エラーメッセージの設定
		userRegistForm.setErrorLabel(strMessage + "を入力してください");
		
		// モデルに登録
		model.addAttribute("userRegistForm", userRegistForm);
	}
	
	/**
	 * userRegistFormのエラーラベルに、
	 * 引数のエラーメッセージを設定し、モデルに登録する
	 * @param belowFog 以下フラグ true：下、false：上 
	 * @param strMessage エラーメッセージ文字列
	 * @param userRegistForm 入力画面の各項目情報
	 * @param model モデル
	 */
	private static void setDigitErrLabel(Boolean belowFlg, String strMessage, String strDigit, UserRegistForm userRegistForm, Model model) {
		// 追加する文字列
		String addMessagePart = belowFlg ? "下" : "上";
		
		// エラーメッセージの設定
		userRegistForm.setErrorLabel(
				strMessage + 
				"は" + 
				strDigit + 
				"桁以" + 
				addMessagePart + 
				"で入力してください");
		
		// モデルに登録
		model.addAttribute("userRegistForm", userRegistForm);
	}
	
	/**
	 * 電話番号の各入力項目を0埋めする
	 * パスワードのマスキング文字列を取得する
	 * @param userRegistForm 受診者登録画面入力情報
	 */
	private static void setTelPasswordItems(UserRegistForm userRegistForm) {
		// マスキングパスワード
		String maskPassword = "";
		
		// 電話番号1ブロック目 3桁0埋め
		userRegistForm.setTelFirstBlock(
				String.format("%03d", Integer.valueOf(userRegistForm.getTelFirstBlock())));
		
		// 電話番号2ブロック目 4桁0埋め
		userRegistForm.setTelSecondBlock(
				String.format("%04d", Integer.valueOf(userRegistForm.getTelSecondBlock())));
		
		// 電話番号3ブロック目 4桁0埋め
		userRegistForm.setTelThirdBlock(
				String.format("%04d", Integer.valueOf(userRegistForm.getTelThirdBlock())));
		
		// 入力されたパスワードの桁数分繰り返す
		for (int i = 0; i < userRegistForm.getPassword().length(); i++) {
			// マスキングパスワードの修正
			maskPassword += "･";
		}
		
		// マスキングパスワードの設定
		userRegistForm.setMaskPassword(maskPassword);
	}
	
	/**
	 * 受診者登録画面の入力値をもとに、
	 * m_userテーブルに登録する値に変換し返す
	 * @param userRegistForm
	 * @return
	 * @throws NoSuchAlgorithmException 使用不可Exception
	 */
	public static MUser getMUser(UserRegistForm userRegistForm) throws NoSuchAlgorithmException {
		// 戻り値
		MUser mUser = new MUser();
		
		// 受診者登録画面情報がnullでない場合
		if (userRegistForm != null) {
			// 暗号化したパスワード
			mUser.setPassword(getEncryptPassword(userRegistForm.getPassword()));
			
			// 姓
			mUser.setLastName(userRegistForm.getLastName());
			
			// 名
			mUser.setFirstName(userRegistForm.getFirstName());
			
			// 年齢
			mUser.setAge(userRegistForm.getAge());
			
			// 性別
			mUser.setSex(userRegistForm.getSex());
			
			// 電話番号
			mUser.setTel(
					Long.parseLong(
							userRegistForm.getTelFirstBlock() +
							userRegistForm.getTelSecondBlock() + 
							userRegistForm.getTelThirdBlock()));
		}
		
		// MUserを返す
		return mUser;
	}
	
	/**
	 * 受診者登録画面で入力したパスワードの値をもとに、
	 * 暗号化したパスワードの値を取得し返す
	 * @param password パスワード
	 * @return 暗号化したパスワード
	 * @throws NoSuchAlgorithmException 使用不可Exception
	 */
	private static String getEncryptPassword(String password) throws NoSuchAlgorithmException {
		// MessageDigest
		var md = MessageDigest.getInstance("SHA-512");
		
		// パスワードからbyte配列を取得
        byte[] cipherBytes = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        
        // byte配列を文字列に変換
        for (int i=0; i<cipherBytes.length; i++) {        
            sb.append(String.format("%02x", cipherBytes[i]&0xff));
        }
        
        // 暗号化したパスワードを返す
        return sb.toString();
	}
}
