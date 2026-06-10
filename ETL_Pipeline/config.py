import os

MYSQL_CONFIG = {
    "host": os.environ.get("MYSQL_HOST", "127.0.0.1"),
    "port": int(os.environ.get("MYSQL_PORT", 3306)),
    "user": os.environ.get("MYSQL_USER", "etl_user"),
    "password": os.environ.get("MYSQL_PASSWORD"),
    "database": os.environ.get("MYSQL_DATABASE", "money_transfer_db"),
    "use_pure": True,
    "ssl_disabled": True,
    "connection_timeout": 5
}


SNOWFLAKE_CONFIG = {
    "user": os.environ.get("SNOWFLAKE_USERNAME", "VISHALK"),
    "password": os.environ.get("SNOWFLAKE_PASSWORD"),
    "account": os.environ.get("SNOWFLAKE_ACCOUNT", "GZMBSPD-CF76283"),
    "warehouse": os.environ.get("SNOWFLAKE_WAREHOUSE", "MTS_WH"),
    "database": os.environ.get("SNOWFLAKE_DATABASE", "MONEY_TRANSFER_OLTP"),
    "schema": os.environ.get("SNOWFLAKE_SCHEMA", "CORE")
}
