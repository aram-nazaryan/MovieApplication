FROM nginx

WORKDIR /usr/share/nginx/html

COPY . .

WORKDIR /etc/nginx/conf.d
COPY default.conf .

CMD [ "/usr/sbin/nginx", "-g", "daemon off;" ]
