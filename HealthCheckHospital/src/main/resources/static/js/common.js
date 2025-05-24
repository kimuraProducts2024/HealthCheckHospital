/**
 * DOMツリー読み込み後
 * 各要素の取得、各イベントリスナーの追加
 */
window.addEventListener('DOMContentLoaded', function() {
	// number要素すべてに対し、処理を実行する
	document.querySelectorAll("input[type='number']").forEach((elem) => {
		// inputイベントを追加
		elem.addEventListener("input", () => {
			// 入力時、数値以外の入力を不可にする
			inputValidation(elem);
		});
	});
})

// 入力時、数値以外の入力を不可にする
function inputValidation(elem) {
	elem.value = elem.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
}