package com.healthcheck.hospital.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 受診者登録画面情報クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistForm {
	// 姓
	private String lastName;
	
	// 名
	private String firstName;
	
	// 年齢
	private Integer age;
	
	// 性別
	private Integer sex;
	
	// 電話番号1ブロック目
	private String telFirstBlock;
	
	// 電話番号2ブロック目
	private String telSecondBlock;
	
	// 電話番号3ブロック目
	private String telThirdBlock;
	
	// パスワード
	private String password;
	
	// マスキングパスワード（確認画面表示用）
	private String maskPassword;
	
	// エラーメッセージ
	private String errorLabel;
}
