package com.healthcheck.hospital.repository;

import org.apache.ibatis.annotations.Mapper;

import com.healthcheck.hospital.entity.MUser;

/**
 * MUserテーブルマッパーインタフェース
 */
@Mapper
public interface MUserMapper {
	// ユーザIDをキーとしてデータ検索
	MUser selectMUser(String userId);
}
