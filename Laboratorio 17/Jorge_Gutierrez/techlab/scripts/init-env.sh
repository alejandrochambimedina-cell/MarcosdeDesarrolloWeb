#!/usr/bin/env bash
set -euo pipefail
umask 077
if [[ -e .env ]]; then echo '.env ya existe; no se modifica'; exit 1; fi
cat > .env <<EOF
DB_URL=jdbc:mysql://127.0.0.1:3307/techlab
DB_USER=techlab
DB_PASSWORD=$(openssl rand -hex 18)
MYSQL_ROOT_PASSWORD=$(openssl rand -hex 24)
TECHLAB_USER_PASSWORD=$(openssl rand -hex 16)
TECHLAB_ADMIN_PASSWORD=$(openssl rand -hex 16)
TECHLAB_JWT_SECRET=$(openssl rand -base64 32)
EOF
chmod 600 .env
printf '%s
' 'Archivo .env creado. No lo subas a Git.'
