#Установщик для Debian 11
source /etc/profile
apt-get update
apt-get install sudo
sudo apt-get remove docker docker-engine docker.io containerd runc
sudo apt-get update
sudo apt-get install ca-certificates curl gnupg lsb-release
curl -fsSL https://download.docker.com/linux/debian/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
apt-get install samba samba-client samba-common -y
apt-get install git
mkdir /home/saturn
mkdir /home/saturn/CSV
chmod -R 0777 /home/saturn/CSV
useradd oper
groupadd oper_g
usermod -a -G oper_g oper
smbpasswd -a oper
echo "[public]
comment = public
path = /home/saturn/CSV
read only = No
create mask = 0777
directory mask = 0777
guest ok = Yes" > smb.conf
mv smb.conf /etc/samba/smb.conf
smbd restart
nmbd restart
git clone https://github.com/EvgeniyMsk/AccessControlSystem.git
echo 'Успешная установка АИС-Сатурн!\n'
while true;
do
    echo -n "Запускаем? <- "
    read -p "сделать [Y/N]? <-" ACT
    if [[ $ACT = "Y" || $ACT = "Yes" || $ACT = "y" || $ACT = "yes" ]]
    then
      docker-compose --env-file=./AccessControlSystem/AccessControlSystem.env -f ./AccessControlSystem/docker-compose.yml up
    else
      echo "Окей :)"
      break;
    fi
done