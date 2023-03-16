count_offline_containers(){
    local counter=0;
    for i in $(kubectl get pods -o=jsonpath='{.items[*].status.containerStatuses[*].ready}')
    do
        if [ $i == 'false' ]
        then
            let "counter+=1";
        fi
    done
    echo "$counter"
}

curl http://node28791-env-6815870.rag-cloud.hosteur.com/dev-poc-siu/stop-spring

restarted=false


# Check every second for 30 seconds if a container has been stopped
for i in {1..30}
do
  count="$(count_offline_containers)"
  if [ "$count" -lt 1 ]
  then
      restarted=true
  fi
done

if [ $restarted == false ]
then
  exit 1
fi

sleep 240

count="$(count_offline_containers)"


if [ "$count" -gt 0 ]
then
    exit 1
fi