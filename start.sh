#!/bin/bash

# Set display
export DISPLAY=:0

# Start X virtual framebuffer
Xvfb :0 -screen 0 1280x720x16 &

# Wait a bit to ensure Xvfb is ready
sleep 2

# Start XFCE desktop
startxfce4 &

# Wait for XFCE to initialize (adjust if needed)
sleep 5

# Start VNC server
x11vnc -display :0 -rfbport 5900 -forever -shared -nopw &

# Start noVNC
websockify --web=/usr/share/novnc/ 6080 localhost:5900 &

# Start MariaDB
service mariadb start

echo "========================================="
echo "Desktop at: http://localhost:6080"
echo "MariaDB running on port 3306"
echo "DB: demo  user: root  (no password)"
echo "========================================="

# Start Java GUI application
java -jar /root/target/BrokerVerse-1.0-jar-with-dependencies.jar
