# Official MySQL 8.0 image
FROM mysql:8.0

# Copy database initialization SQL script into Docker entrypoint init directory
# Any .sql file copied to /docker-entrypoint-initdb.d/ will be automatically executed on first boot
COPY bankmanagementdb.sql /docker-entrypoint-initdb.d/bankmanagementdb.sql

# Default environment variables (can be overridden by docker-compose)
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=bankmanagementdb

# Expose standard MySQL port
EXPOSE 3306
