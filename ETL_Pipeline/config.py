MYSQL_CONFIG = {
    "host": "127.0.0.1",
    "port": 3306,
    "user": "etl_user",
    "password": "etl123",
    "database": "money_transfer_db",
    "use_pure": True,
    "ssl_disabled": True,
    "connection_timeout": 5
}




SNOWFLAKE_CONFIG = {
    "user": "<username>",
    "password": "<pw>",
    "account": "<account-identifier>",
    "warehouse": "MTS_WH",
    "database": "MONEY_TRANSFER_OLTP",
    "schema": "CORE"
}
