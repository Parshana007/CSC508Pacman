FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
WORKDIR /root

# 1. Install desktop + VNC + noVNC + MariaDB + Java + Maven
RUN apt-get update && apt-get install -y \
    xfce4 xfce4-goodies \
    x11vnc xvfb novnc websockify \
    mariadb-server mariadb-client \
    wget curl net-tools \
    openjdk-17-jdk \
    maven \
    && rm -rf /var/lib/apt/lists/*

# 2. Configure MariaDB (create default DB, no password for demo)
RUN service mariadb start && \
    mysql -u root -e "CREATE DATABASE demo;" && \
    mysqladmin -u root password ''

# 3. Store VNC password (empty for demo)
RUN mkdir -p /root/.vnc && x11vnc -storepasswd "" /root/.vnc/passwd

COPY pom.xml /root/pom.xml
COPY src /root/src

RUN mvn -f /root/pom.xml clean package

# 4. Copy startup script
COPY start.sh /root/start.sh
RUN chmod +x /root/start.sh

# 5. Expose noVNC (web access) and MariaDB ports
EXPOSE 6080 3306

# 6. Launch everything with one script
CMD ["/root/start.sh"]