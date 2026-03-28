# Start with the smallest Linux possible
FROM alpine:3.20

# Set where Java will live
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Install only what is absolutely needed to run Java
RUN apk add --no-cache libstdc++ tzdata ca-certificates musl-locales \
    && wget -O /tmp/openjdk.tar.gz https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.12%2B7/OpenJDK17U-jre_x64_alpine-linux_hotspot_17.0.12_7.tar.gz \
    && mkdir -p "$JAVA_HOME" \
    && tar --extract --file /tmp/openjdk.tar.gz --directory "$JAVA_HOME" --strip-components 1 \
    && rm /tmp/openjdk.tar.gz

CMD ["java", "-version"]