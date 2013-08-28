exports.index = function(req,res){
	res.render('form.ejs', { title: 'Form' });
};

exports.post = function(req,res){
	var request = require('request');
	// Google APIs の API Accessで登録されているAPI Key
	var api_key = 'AIzaSyD9uN_ngz6VCQ5hyfB1mRwuTxErkPVnPO4';

	// メッセージの作成
	var msg = {
		// 端末で登録したregistrationIdを入れる
		registration_ids: ['APA91bGB6x9E3x_GnyH2e-USql1eDZF8SeGhLn6id2H2wg9ZXXlHAj33xEnXAw46yLCKHPaYw5doZm_oRUqKX7sMdOc3LzzNc3ieqZ3A_xCTFof9_4CucPx48IywYYW8QuGdt2C75CDnkPCPp-XxZAUsvx1I1d3yiWViFuz-CuN891ty8x5ewDs'],
		collapse_key: "update",
		time_to_live: 180,
		// 送信するデータ
		data: { 
			message: req.body.msg,
			url: 'http://www.yahoo.co.jp'
		}
	};
	request.post({
		uri: 'https://android.googleapis.com/gcm/send',
		json: msg,
		headers: {
			Authorization: 'key=' + api_key
		}
	},
	function(err, response, body) {
		res.redirect('/');
	})
};
