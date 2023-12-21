#!/bin/bash

sleep 3

ldapadd -x -H ldap:// -D "cn=admin,dc=ddp,dc=com" -f /init/users.ldif -w ${LDAP_ADMIN_PASSWORD}

tail -f /dev/null
