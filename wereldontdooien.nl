server {
  server_name  www.wereldontdooien.nl;
  rewrite ^(.*) http://wereldontdooien.nl$1 permanent;
}

server {
  server_name wereldontdooien.nl;
  location / {
    include uwsgi_params;
    uwsgi_pass unix:/tmp/wereldontdooien.sock;
  }
  location /static {
    alias /home/www/wereldontdooien/static;
  }
  
  location /media {
    alias /srv/wereldontdooien;
  }
}
