package com.healthcheck.hospital.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.healthcheck.hospital.entity.MUser;

/**
 * 受診者一覧情報マッパーインタフェース
 */
@Mapper
public interface AmbulantListMapper {
	// id=0：全受診者一覧情報をデータ検索
	// id<>0：idに一致する受診者情報をデータ検索
	List<MUser> selectAmbulantList(int id);
	
	// 表示分の受診者一覧をデータ検索
	List<MUser> selectAmbulantListPart(int pageFrom, int count);
	
	// 受診者の件数を取得
	int selectCount();
}
