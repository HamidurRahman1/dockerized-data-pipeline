#!/bin/sh

vault server -config=/v/init/config.hcl 2>&1 | tee /v/logs/vault.log &

sleep 2

export VAULT_ADDR='http://0.0.0.0:8200'
export VAULT_SKIP_VERIFY='true'

init_output=$(vault operator init -key-shares=5 -key-threshold=3)

unseal_keys=""
for i in $(seq 1 5); do
  key=$(echo "$init_output" | grep "Unseal Key $i:" | awk '{print $4}')
  unseal_keys="$unseal_keys$key "
done

printf "%s\n" $unseal_keys > /v/keys/unseal-keys.txt

root_token=$(echo "$init_output" | grep "Initial Root Token:" | awk '{print $4}' | tr -d '\n')

echo "VAULT_ADDR=$VAULT_ADDR" > /v/keys/vault-info.env
echo "VAULT_TOKEN=$root_token" >> /v/keys/vault-info.env

vault operator unseal $(echo $unseal_keys | cut -d ' ' -f 1)
vault operator unseal $(echo $unseal_keys | cut -d ' ' -f 2)
vault operator unseal $(echo $unseal_keys | cut -d ' ' -f 3)

export VAULT_TOKEN=$root_token

vault secrets enable -path=secret -version=1 kv

vault kv put secret/test \
  key1="value1" \
  key2="value2"

tail -f /dev/null
