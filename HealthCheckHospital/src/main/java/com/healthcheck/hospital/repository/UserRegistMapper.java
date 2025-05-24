package com.healthcheck.hospital.repository;

import org.apache.ibatis.annotations.Mapper;

import com.healthcheck.hospital.entity.MUser;

/**
 * UserRegistマッパーインタフェース
 * m_userテーブル、m_test_itemテーブルそれぞれに、
 * 受診者登録画面で入力した値をもとにInsert文を実行する
 */
@Mapper
public interface UserRegistMapper {
	// m_userテーブルにInsert文を実行する
	int insertMUser(MUser mMuser);
	
	// m_userテーブルのidの最大値をキーとして
	// m_test_itemテーブルにInsert文を実行する
	int insertMTestItem();
	
}
