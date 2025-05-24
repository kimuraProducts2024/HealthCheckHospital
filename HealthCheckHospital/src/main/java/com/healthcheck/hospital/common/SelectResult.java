package com.healthcheck.hospital.common;

import java.util.List;

import lombok.Data;

/**
 * ページネーション関連情報クラス
 * ページネーションの実装に必要な情報を取得・保持する
 * @param <E> フォームクラス： AmbulantListForm
 */
@Data
public class SelectResult<E> {
	private int pageFrom;				// ページリンク中間セクション 開始位置
	private int pageTo;					// ページリンク中間セクション 終了位置
	private int currentPage;			// 現在のページ位置
	private int recordPerPage;			// 表示ページ内データ件数
	private int totalRecordCount;		// 総データ件数
	private int totalPageCount;			// 総ページ数
	private List<E> entities;			// 表示ページ内エンティティ
	
	/**
	 * コンストラクタ
	 * 総データ件数、表示ページ内データ件数、
	 * 総ページ数を取得する
	 * @param totalRecordCount 総データ件数
	 * @param recordPerPage 表示ページ内データ件数
	 */
	public SelectResult(int totalRecordCount, int recordPerPage) {
		this.totalRecordCount = totalRecordCount;	// 総データ件数
		this.recordPerPage = recordPerPage;			// 表示ページ内データ件数
		
		// 総ページ数
		// (総データ件数 / 表示ページ内データ件数) + 
		// (総データ件数 % 表示ページ内データ件数)が1以上なら1、0なら0)
		this.totalPageCount = (this.totalRecordCount / this.recordPerPage) + 
									(this.totalRecordCount % this.recordPerPage > 0 ? 1 : 0);
	}
	
	/**
	 * ページの遷移処理
	 * ページリンクをクリックした際に実行される
	 * @param page 遷移先のページ位置
	 */
	public void moveTo(int page) {
		// 現在のページ位置を設定
		this.currentPage = page;
		
		// ページ位置 - 1 と 2 の大きい値を開始位置に設定
		this.pageFrom = Math.max(page - 1, 2);
		
		// 開始位置 + 2 と 総ページ数 - 1 の小さい値を終了位置に設定
		this.pageTo = Math.min(this.pageFrom + 2, totalPageCount - 1);
		
		// 終了位置 - 2 と 2 の大きい値を開始位置に設定
		this.pageFrom = Math.max(this.pageTo - 2, 2);
	}
}
