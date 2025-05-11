package com.healthcheck.hospital.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 受診者一覧の各項目情報クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulantListForm {
	// ユーザID
	public String userId;
	
	// 氏名
	public String userName;
	
	// 身長
	public String height;
	
	// 体重
	public String weight;
	
	// 左視力
	public String visionLeft;
	
	// 右視力
	public String visionRight;
	
	// 聴力
	public String hearing;
	
	// 最大血圧
	public String systolicBloodPressure;
	
	// 最小血圧
	public String diastolicBloodPressure;
	
	// ページリンク中間セクション 開始位置
	public int pageFrom;
	
	// データ件数
	public int count;
}
