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

# 2. Configure MariaDB
RUN service mariadb start && \
    mysql -u root -e "CREATE DATABASE demo;" && \
    mysqladmin -u root password ''

# 3. VNC password (empty)
RUN mkdir -p /root/.vnc && x11vnc -storepasswd '' /root/.vnc/passwd

# 4. Copy entire Maven project
COPY pom.xml /root/pom.xml
COPY src /root/src

# 5. Build with Maven — downloads MQTT + SLF4J automatically
RUN mvn -f /root/pom.xml clean package

# 6. Copy startup script
COPY start.sh /root/start.sh
RUN chmod +x /root/start.sh

EXPOSE 6080 3306

# 7. The desktop + Java GUI start here
CMD ["/root/start.sh"]
