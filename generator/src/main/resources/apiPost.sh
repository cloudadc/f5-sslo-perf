#!/bin/bash
  
CURDIR="$(dirname $BASH_SOURCE)"


BIQ_TOKEN=$(curl -k "https://192.168.1.124/mgmt/shared/authn/login" -sH 'Content-Type: application/json; charset=utf-8' -d '{"username":"TOPOLOGY_GENERATOR_BASH_BIGIQ_USER","password":"TOPOLOGY_GENERATOR_BASH_BIGIQ_PASSWORD","needsToken":true}' | jq -r .token.token)
echo "Access Token: $BIQ_TOKEN"

counter=1
for i in $(ls TOPOLOGY_GENERATOR_BASH_NAME*)
do
  if [ $counter -eq TOPOLOGY_GENERATOR_BASH_TOKEN_REFRSH_TIMES ]
  then
    echo ""
    echo ""
    echo ""
    read -p "Press enter to continue..."
    echo "Refresh Token"
    BIQ_TOKEN=$(curl -k "https://TOPOLOGY_GENERATOR_BASH_BIGIQ_HOST/mgmt/shared/authn/login" -sH 'Content-Type: application/json; charset=utf-8' -d '{"username":"TOPOLOGY_GENERATOR_BASH_BIGIQ_USER","password":"TOPOLOGY_GENERATOR_BASH_BIGIQ_PASSWORD","needsToken":true}' | jq -r .token.token)
    echo "Access Token: $BIQ_TOKEN"
    counter=1
  fi

  echo curl -k -u 'TOPOLOGY_GENERATOR_BASH_SSLO_USER:TOPOLOGY_GENERATOR_BASH_SSLO_PASSWORD'  -H "Content-Type: application/json" https://TOPOLOGY_GENERATOR_BASH_SSLO_HOST/mgmt/tm/ltm/pool -X POST -d "$(cat SSL_POOL_$i)"
  curl -k -u 'TOPOLOGY_GENERATOR_BASH_SSLO_USER:TOPOLOGY_GENERATOR_BASH_SSLO_PASSWORD'  -H "Content-Type: application/json" https://TOPOLOGY_GENERATOR_BASH_SSLO_HOST/mgmt/tm/ltm/pool -X POST -d "$(cat SSL_POOL_$i)" | jq
  
  echo curl -k -u 'TOPOLOGY_GENERATOR_BASH_SSLO_USER:TOPOLOGY_GENERATOR_BASH_SSLO_PASSWORD' -H "Content-Type: application/json" https://TOPOLOGY_GENERATOR_BASH_SSLO_HOST/mgmt/tm/ltm/virtual -X POST -d "$(cat SSL_VS_$i)" 
  curl -k -u 'TOPOLOGY_GENERATOR_BASH_SSLO_USER:TOPOLOGY_GENERATOR_BASH_SSLO_PASSWORD' -H "Content-Type: application/json" https://TOPOLOGY_GENERATOR_BASH_SSLO_HOST/mgmt/tm/ltm/virtual -X POST -d "$(cat SSL_VS_$i)" | jq
  
  echo curl -k -u 'TOPOLOGY_GENERATOR_BASH_SSLO_USER:TOPOLOGY_GENERATOR_BASH_SSLO_PASSWORD'  -H "Content-Type: application/json" https://TOPOLOGY_GENERATOR_BASH_SSLO_HOST/mgmt/tm/ltm/pool -X POST -d "$(cat YEWU_POOL_$i)"
  curl -k -u 'TOPOLOGY_GENERATOR_BASH_SSLO_USER:TOPOLOGY_GENERATOR_BASH_SSLO_PASSWORD'  -H "Content-Type: application/json" https://TOPOLOGY_GENERATOR_BASH_SSLO_HOST/mgmt/tm/ltm/pool -X POST -d "$(cat YEWU_POOL_$i)" | jq
  
  echo curl -X POST -k -H "Content-Type: application/json; charset=utf-8" -H "X-F5-Auth-Token:$BIQ_TOKEN" https://TOPOLOGY_GENERATOR_BASH_BIGIQ_HOST/mgmt/cm/sslo/api/topology  -d "$(cat $i)" 
  curl -X POST -k -H "Content-Type: application/json; charset=utf-8" -H "X-F5-Auth-Token:$BIQ_TOKEN" https://TOPOLOGY_GENERATOR_BASH_BIGIQ_HOST/mgmt/cm/sslo/api/topology  -d "$(cat $i)" | jq
  sleep TOPOLOGY_GENERATOR_BASH_INTERVAL
  ((counter++))
  echo ""
  echo ""
done