package com.healthcheck.hospital.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.healthcheck.hospital.entity.MUser;

/**
 * 受診者一覧情報マッパーインタフェース
 */
@Mapper
public interface AmbulantListMapper {
	// 受診者一覧をデータ検索
	List<MUser> selectAmbulantList(int id);
	
	// 表示分の受診者一覧をデータ検索
	List<MUser> selectAmbulantListPart(int pageFrom, int count);
	
	// 受診者の件数を取得
	int selectCount();
}
