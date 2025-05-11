package com.healthcheck.hospital.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.ui.Model;

import com.healthcheck.hospital.entity.MUser;
import com.healthcheck.hospital.form.AmbulantListForm;
import com.healthcheck.hospital.service.AmbulantListService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 共通機能クラス
 * 画面初期化、画面遷移等の処理を行う
 */
public class CommonFunc {
	/**
	 * CSVダウンロード処理
	 * 受診者一覧情報に表示されているデータを
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
		ambulantListForm.userName = mUser.getFirstName() + " " + mUser.getLastName();				// 氏名
		
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
}
