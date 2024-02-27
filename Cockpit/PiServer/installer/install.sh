sudo cp PiServer.jar /usr/local/bin/PiServer.jar
sudo cp INIT_FILE /usr/local/bin/INIT_FILE
sudo cp clientkeystore /usr/local/bin/clientkeystore
sudo cp pi-server /etc/init.d/pi-server
sudo chmod +x /etc/init.d/pi-server
sudo systemctl daemon-reload
sudo update-rc.d pi-server defaults
sudo systemctl stop pi-server
sudo systemctl start pi-server
