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

sleep 15

first_count="$(count_offline_containers)"

if [ "$first_count" -lt 1 ]
then
    exit 22
fi

sleep 300


second_count="$(count_offline_containers)"

echo "first_count" $first_count
echo "Second count" $second_count

if [ "$second_count" -lt "$first_count" ]
then
    exit 32
fi