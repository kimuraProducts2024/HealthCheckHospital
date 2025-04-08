package com.healthcheck.hospital.entity;

import java.math.BigDecimal;

import lombok.Data;

/**
 * m_test_itemテーブルエンティティクラス
 * 検査項目テーブルに対応する実体クラス
 */
@Data
public class MTestItem {
	// ユーザID
	private Integer userId;
		
	// 身長
	private BigDecimal height;
	
	// 体重
	private BigDecimal weight;
	
	// 右目視力
	private BigDecimal visionRight;
	
	// 左目視力
	private BigDecimal visionLeft;
	
	// 聴力
	private Integer hearing;
	
	// 最大血圧
	private Integer systolicBloodPressure;
	
	// 最小血圧
	private Integer diastolicBloodPressure;
}
