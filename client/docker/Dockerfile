FROM ghcr.io/openconext/openconext-basecontainers/apache2:latest
COPY ./dist/ /var/www/
RUN rm -rf /etc/apache2/sites-enabled/*.conf
COPY ./docker/appconf.conf /etc/apache2/sites-enabled/

# --- rootless + readOnlyRootFilesystem ---
RUN set -eux; \
    # Unprivileged ports only: 80 -> 8080 and drop the ssl/gnutls 443 listener,
    # since capabilities are dropped (no NET_BIND_SERVICE).
    sed -i 's/^Listen 80/Listen 8080/' /etc/apache2/ports.conf; \
    sed -i '/Listen 443/d' /etc/apache2/ports.conf; \
    # /var/log/apache2 is root:adm and read-only at runtime; the main error/access
    # logs already go to /proc/self/fd via conf-enabled/openconext.conf.
    rm -f /etc/apache2/conf-enabled/other-vhosts-access-log.conf

# Pid file, mutexes and the ssl shm cache must live on a writable mount.
# Requires an emptyDir volume on /tmp (readOnlyRootFilesystem).
ENV APACHE_PID_FILE=/tmp/apache2.pid \
    APACHE_RUN_DIR=/tmp \
    APACHE_LOCK_DIR=/tmp \
    APACHE_LOG_DIR=/tmp \
    APACHE_RUN_USER=www-data \
    APACHE_RUN_GROUP=www-data \
    HTTPD_CSP=

# The base image ENTRYPOINT (/entrypoint.sh) does useradd/chown/chmod, and the
# Debian apachectl wrapper chowns $APACHE_RUN_DIR/socks -- both need root.
# So exec the httpd binary directly.
USER 65532:65532
EXPOSE 8080
ENTRYPOINT ["apache2","-D","FOREGROUND"]
