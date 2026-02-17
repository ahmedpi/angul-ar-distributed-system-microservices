#!/bin/bash
cd /home/youruser/project
git pull origin main         # Pull latest code (if repo is cloned)
docker-compose build         # Build images locally (or use 'pull' if using registry images)
docker-compose up -d         # Start/restart services