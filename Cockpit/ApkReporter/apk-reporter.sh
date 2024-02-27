#!/bin/bash

watch_path="/home/spooky/Downloads/apks/"
mkdir -p $watch_path
while [ ! -e /run/mysqld/mysqld.sock ]; do
  echo "waiting for mysql"
  sleep 5
done
# For every entry in apks, verify file exists
mysql -s -s -u board_database -pboard_database board -e "select * from apks" | while IFS=' ' read 
do  
  values=($REPLY); 
  id=${values[0]}; 
  file=${values[1]}; 
  directory=${values[2]}; 
  if [ ! -f "$directory$file" ]; then
    	 mysql -u board_database -pboard_database board -e "call deleteApk(\"$file\", @ignored)"
  fi
done

# For every .apk file found, call addApk
files=(`find $watch_path -type f -name "*.apk"`)
for file in "${files[@]}"; 
do 
  filename="${file#"$watch_path"}"
  mysql -u board_database -pboard_database board -e "call addApk(\"$filename\", \"$watch_path\", @ignored)"
done

inotifywait -mrq  -e CREATE -e DELETE $watch_path |
while IFS=" " read directory event file
do
  if [[ "$file" =~ .*apk ]]; then
     case $event in
       "DELETE")
	 mysql -u board_database -pboard_database board -e "call deleteApk(\"$file\", @ignored)"
       ;;
       "CREATE")
	 path="$directory$file"
         mysql -u board_database -pboard_database board -e "call addApk(\"$file\", \"$path\", @ignored)"
       ;;
     esac
  fi
done

