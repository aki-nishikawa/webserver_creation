# TCP Server and Client

TCP サーバとクライアントを C と Java で実装した．
クライアントは www.west.sd.keio.ac.jp に HTTP リクエストを送信し，
サーバは www.west.sd.keio.ac.jp が返す HTTP レスポンスを送信するようにデータを用意した．
サーバにブラウザでアクセスすると HTML が表示される上に，
新しいページへリダイレクトされる．

サーバが返すレスポンスのデータ部分（下2行）は gzip で圧縮されており，
回答すると response_data.html になる．