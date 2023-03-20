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

echo "Sending a request to shut down at least one container"
curl http://node28791-env-6815870.rag-cloud.hosteur.com/dev-poc-siu/stop-spring

echo "Check every second if a container has been shut down"
# Check every second for 30 seconds if a container has been stopped
restarted=false
for i in {1..50}
do
  count="$(count_offline_containers)"
  if [ "$count" -lt 1 ]
  then
      restarted=true
  fi
  sleep 1
done

if [ $restarted == false ]
then
  echo "No container has restarted"
  exit 1
fi
echo "At least one container has shut down"
echo "Waiting 4 minutes to let time for pods to restart"
sleep 240

echo "Verifying if all pods are up and running"
count="$(count_offline_containers)"
if [ "$count" -gt 0 ]
then
    exit 1
fi
echo "SUCCESS"