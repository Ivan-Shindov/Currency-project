# Currency-project
Application which get currencies from BNB when POST endpoint is hit, save/update in DB and send via websocket. Another service consume websocket message and do some operations in DB.

Before run two applications, should navigate to docker directory and execute docker-compose up -d. After that you can run applications, please let first be CurrencyApplication after that WebsocketApplication.
