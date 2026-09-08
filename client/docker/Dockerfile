FROM ghcr.io/openconext/openconext-basecontainers/apache2:latest
COPY ./dist/ /var/www/
RUN rm -rf /etc/apache2/sites-enabled/*.conf
COPY ./docker/appconf.conf /etc/apache2/sites-enabled/

# --- rootless (new layers) ---
# Unprivileged listen port + runtime dirs writable by the apache user
RUN sed -i 's/^Listen 80/Listen 8080/' /etc/apache2/ports.conf \
 && mkdir -p /var/lock/apache2 \
 && chown -R www-data:www-data /var/run/apache2 /var/lock/apache2 /var/log/apache2

USER www-data
EXPOSE 8080
ENTRYPOINT ["apachectl","-D","FOREGROUND"]
