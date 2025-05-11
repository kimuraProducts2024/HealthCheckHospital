package com.healthcheck.hospital.entity;

import lombok.Data;

/**
 * m_userテーブルエンティティクラス
 * ユーザマスターテーブルに対応する実体クラス
 */
@Data
public class MUser {
	// ユーザID
	private Integer userId;
	
	// パスワード
	private String password;
	
	// 苗字
	private String firstName;
	
	// 名前
	private String lastName;
	
	// 年齢
	private int age;
	
	// 性別
	private int sex;
	
	// 電話番号
	private long tel;
	
	// 検査項目情報
	private MTestItem mTestItem;
}
