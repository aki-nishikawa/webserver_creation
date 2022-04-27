# webwerver creation

「Webサーバを作りながら学ぶ 基礎からのWebアプリケーション開発入門」
のコードを自分で書いたもの．

http://kmaebashi.com/webserver/index.html

## tcpserver
TCP サーバとクライアントを C と Java で実装した．

## webserver Modoki
### modoki-0.0
HTTP リクエストに対して指定されたリソースを返す
### modoki-0.1
modoki-0.0 をマルチスレッドにして何回でもレスポンスを返せるようにしたもの
### modoki-0.2
modoki-0.1 を不正なパスへのアクセスや存在しないファイルへのアクセスに対応させたもの