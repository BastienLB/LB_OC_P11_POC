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

count_online_containers(){
  local counter=0;
    for i in $(kubectl get pods -o=jsonpath='{.items[*].status.containerStatuses[*].ready}')
    do
      if [ $i == 'true' ]
      then
        let "counter+=1";
      fi
    done
    echo "$counter"
}
offline_containers_initial_count="$(count_offline_containers)"

echo "Sending a request to shut down at least one container"
curl http://node28791-env-6815870.rag-cloud.hosteur.com/dev-poc-siu/stop-spring

echo "Check every second if a container has been shut down"
# Check every second for 30 seconds if a container has been stopped
restarted=false
for i in {1..30}
do
  # Check if there's more offline containers after sending the test request
  # meaning at least one container has been shut down
  actually_offline_containers="$(count_offline_containers)"
  if [ "$actually_offline_containers" -ge "$offline_containers_initial_count" ]
  then
      restarted=true
      online_containers_after_restart="$(count_online_containers)"
  fi
  sleep 1
done

# If no container has been shut down the test has fail
if [ $restarted == false ]
then
  echo "No container has restarted"
  exit 1
fi

echo "At least one container has shut down"
echo "Waiting 5 minutes to let time for pods to restart"
sleep 300

echo "Verifying container has restarted"
online_containers="$(count_online_containers)"
if [ "$online_containers" -lt "$online_containers_after_restart" ]
then
    exit 1
fi
echo "SUCCESS"