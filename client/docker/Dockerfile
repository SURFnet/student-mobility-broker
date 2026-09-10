FROM httpd:2.4.68-trixie AS builder

RUN apt-get update && apt-get install -y --no-install-recommends apache2-bin media-types

# This defines which apache modules are copied to the actual image.
# It needs to match what is imported in httpd.conf
ARG MODS="mpm_event unixd log_config authz_core mime dir rewrite proxy proxy_http headers"

RUN set -eux; \
    AP=/usr/local/apache2; \
    mkdir -p /out$AP/bin /out$AP/modules /out$AP/conf; \
    cp $AP/bin/httpd /out$AP/bin/; \
    cp $AP/conf/mime.types /out$AP/conf/; \
    files=$AP/bin/httpd; \
    for m in $MODS; do \
      cp $AP/modules/mod_$m.so /out$AP/modules/; \
      files="$files $AP/modules/mod_$m.so"; \
    done; \
    for f in $files; do ldd "$f"; done \
      | awk '/=>/ {print $3}' | sort -u \
      | grep -Ev '/(libc|libm)\.so' \
      | while read -r lib; do \
          dir="/out$(realpath "$(dirname "$lib")")"; \
          mkdir -p "$dir"; \
          cp -L "$lib" "$dir/"; \
        done

FROM gcr.io/distroless/cc-debian13:nonroot

COPY --from=builder /out/ /
COPY ./docker/httpd.conf /etc/apache2/httpd.conf
COPY ./docker/appconf.conf /etc/apache2/appconf.conf
COPY ./dist/ /var/www/

EXPOSE 8080
STOPSIGNAL SIGWINCH

ENTRYPOINT ["/usr/local/apache2/bin/httpd", "-f", "/etc/apache2/httpd.conf", "-DFOREGROUND"]
