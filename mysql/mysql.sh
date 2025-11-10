docker run -d \
  --name mydb \
  -e MYSQL_ROOT_PASSWORD=1234 \
  -e MYSQL_DATABASE=mydb \
  -e MYSQL_USER=test \
  -e MYSQL_PASSWORD=1234 \
  -p 3309:3306 \
  -v mysql_data:/var/lib/mysql \
  mysql:8.0