#!/bin/bash

# =========================================
# Start XFCE + VNC + noVNC + MariaDB + Java
# =========================================

# Start the X server (virtual framebuffer)
Xvfb :0 -screen 0 1280x720x16 &

sleep 1

export DISPLAY=:0

# Start the XFCE desktop
startxfce4 &

# Start the VNC server
x11vnc -display :0 -rfbport 5900 -forever -shared -nopw &

# Start the noVNC server (web access)
websockify --web=/usr/share/novnc/ 6080 localhost:5900 &

# Start MariaDB
service mariadb start
echo "MariaDB started. Connect with: mysql -u root"

# Information for students
echo "========================================="
echo "Desktop: http://localhost:6080"
echo "MariaDB running on port 3306"
echo "DB: demo  user: root  (no password)"
echo "========================================="

nohup java -jar /root/target/Paho-1.0-SNAPSHOT-jar-with-dependencies.jar &

tail -f /dev/null
