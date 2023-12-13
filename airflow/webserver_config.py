
import os
from flask_appbuilder.security.manager import AUTH_LDAP

AUTH_TYPE = AUTH_LDAP

AUTH_USER_REGISTRATION = True
AUTH_USER_REGISTRATION_ROLE = "Public"

AUTH_LDAP_SERVER = os.environ.get("LDAP_SERVER")
AUTH_LDAP_SEARCH = "ou=employee,dc=ddp,dc=com"
AUTH_LDAP_BIND_USER = "cn=admin,dc=ddp,dc=com"
AUTH_LDAP_BIND_PASSWORD = os.environ.get("LDAP_BIND_PASS")
AUTH_LDAP_UID_FIELD = "uid"

AUTH_LDAP_FIRSTNAME_FIELD = "givenName"
AUTH_LDAP_LASTNAME_FIELD = "sn"
AUTH_LDAP_EMAIL_FIELD = "mail"

AUTH_LDAP_USE_TLS = False
AUTH_LDAP_ALLOW_SELF_SIGNED = False
AUTH_LDAP_GROUP_FIELD = "memberOf"

AUTH_ROLES_SYNC_AT_LOGIN = True

AUTH_ROLES_MAPPING = {
    "cn=admin,ou=employee,dc=ddp,dc=com": ["Admin"],
    "cn=devs,ou=employee,dc=ddp,dc=com": ["Admin"],
    "cn=users,ou=employee,dc=ddp,dc=com": ["Viewer"],
}



