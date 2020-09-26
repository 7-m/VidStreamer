kubectl delete gateway all-connection-gateway

kubectl delete horizontalpodautoscalers.autoscaling vidindexer-deployment

kubectl delete svc viduploader-deployment
kubectl delete svc vidindexer-deployment
kubectl delete svc vidauth-deployment
kubectl delete svc vidweb-deployment

kubectl delete deployment viduploader-deployment
kubectl delete deployment vidindexer-deployment
kubectl delete deployment vidauth-deployment
kubectl delete deployment vidweb-deployment

kubectl apply -f vidindexer/k8s/deployment.yaml
kubectl apply -f viduploader/k8s/deployment.yaml
kubectl apply -f vidauth/k8s/deployment.yaml
kubectl apply -f vidweb/k8s/deployment.yaml

kubectl expose deployment viduploader-deployment --type=NodePort --port=8080
kubectl expose deployment vidindexer-deployment --type=NodePort --port=8080
kubectl expose deployment vidauth-deployment --type=NodePort --port=8080
kubectl expose deployment vidweb-deployment --type=NodePort --port=80

# patch to allow isitio to recognise the traffic as http
kubectl patch svc vidindexer-deployment --patch '{"spec":{"ports":[{"port":8080,"name":"http"}]}}'
kubectl patch svc viduploader-deployment --patch '{"spec":{"ports":[{"port":8080,"name":"http"}]}}'
kubectl patch svc vidauth-deployment --patch '{"spec":{"ports":[{"port":8080,"name":"http"}]}}'
kubectl patch svc vidweb-deployment --patch '{"spec":{"ports":[{"port":80,"name":"http"}]}}'

kubectl autoscale deployment vidindexer-deployment --cpu-percent=85 --min=1 --max=2


kubectl apply -f gatewat.yaml
