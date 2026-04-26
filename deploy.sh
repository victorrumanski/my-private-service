#!/bin/bash
set -e

echo "🚀 Deploying Private Service..."
docker build -t my-private-service:latest ./my-private-service/
k3d image import my-private-service:latest -c learnk8s
kubectl apply -f my-private-service/k8s.yaml
kubectl rollout restart deployment my-private-service -n apps
kubectl rollout status deployment my-private-service -n apps

echo "✅ Private Service updated!"
