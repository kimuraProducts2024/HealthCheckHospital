package com.healthcheck.hospital.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.healthcheck.hospital.common.CommonFunc;
import com.healthcheck.hospital.entity.MUser;
import com.healthcheck.hospital.form.UserRegistForm;
import com.healthcheck.hospital.repository.UserRegistMapper;

/**
 * 受診者登録サービスクラス
 * m_userテーブルへInsertを実行
 * m_userテーブルのidの最大値を取得
 * 取得したidでm_test_itemテーブルにInsertを実行
 */
@Service
public class UserRegistService {
	/**
	 * 確認画面：確定ボタンクリック時の処理
	 * 入力した値をもとに、MUserエンティティを生成する
	 * その値でDBのm_user、m_test_itemテーブルへ
	 * それぞれInsertを実行する 
	 * @param userRegistForm 受診者登録情報
	 * @param model モデル
	 * @return 遷移先のHTMLファイル名
	 * @throws NoSuchAlgorithmException 使用不可Exception
	 */
	@Transactional(propagation = Propagation.NESTED)
	public String insertData(UserRegistMapper userRegistMapper, 
			UserRegistForm userRegistForm) throws NoSuchAlgorithmException, Exception {

			// 受診者登録入力値からMUserを生成する
			MUser mUser = CommonFunc.getMUser(userRegistForm);

			// m_userテーブルへInsertを実行
			userRegistMapper.insertMUser(mUser);
			
			// m_test_itemテーブルへInsertを実行
			userRegistMapper.insertMTestItem();
			
			// 遷移先のHTMLファイル名を返す
			return "Complete";
	}
}
