FROM ghcr.io/openconext/openconext-basecontainers/apache2:latest
COPY ./dist/ /var/www/
RUN rm -rf /etc/apache2/sites-enabled/*.conf
COPY ./docker/appconf.conf /etc/apache2/sites-enabled/
